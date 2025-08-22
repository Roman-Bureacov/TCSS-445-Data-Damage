package model.script;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import model.database.Database;
import model.database.Weapon;

/**
 * Static class that takes in a script and produces a csv data set based on the
 * simulation results as per the script.
 * <br>
 * Data is organized in three columns: timestamp, damage instance, and event description.
 * <br>
 * The script is expected to follow the grammar, with line-separated statements
 * <br>
 * Not thread-safe.
 * @author Roman Bureacov
 * @version July 2025
 */
public final class ScriptReader {
    // see the GRAMMAR.txt for the grammar description of how scripts are to be structured

    /**
     * the different damage types that may be used.
     */
    public enum damageType {
        /** precision damage type. */
        PRECISION,
        /** body damage type. */
        BODY
    }

    /** String that represents the output data */
    private static TimeSheet data;
    private static List<String> in;
    private static List<Integer> lastBrace;
    private static int position;
    private static Weapon kinetic;
    private static Weapon energy;
    private static Weapon power;
    private static Weapon equipped;

    private ScriptReader() {
        super();
        throw new IllegalStateException("Illegal to instantiate this static class");
    }

    /**
     * Runs the script reader based on the input
     * @param args the script as arg 0, nothing else
     * @throws Exception if any exception occurs
     */
    public static void main(String... args) throws Exception {
        for(final var dataPoint : readData(args[0])) {
            System.out.printf("%20s %20s %s%n", dataPoint[0], dataPoint[1], dataPoint[2]);
        }
    }

    /**
     * Reads the weapons being used in the script and returns them as a three-element array.
     * @param script the script to be read
     * @return a three element containing first the kinetic slot, the energy slot, and then the power slot
     * @throws IllegalArgumentException if the script failed to read the weapons
     */
    public static Weapon[] getWeapons(final String script) throws IllegalArgumentException {
        setup(script);

        try {
            kinetic = readWeapon("kinetic");
            energy = readWeapon("energy");
            power = readWeapon("power");
        } catch (final SQLException e) {
            throw new IllegalArgumentException("Failed to find weapons from database");
        } catch (final IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Reached EOS but expected additional tokens");
        }

        return new Weapon[] {kinetic, energy, power};
    }


    /**
     * Reads in a script and returns a data set simulated by the script.
     * @param script the script to base the simulation upon.
     * @return the simulation data
     * @throws IllegalArgumentException if the script has syntax errors
     */
    public static TimeSheet readData(final String script) throws IllegalArgumentException {
        setup(script);

        try {
            readHeader(); // initiates the grammar
        } catch (final TimeSheet.NoMoreTimeException e) {
            return data;
        } catch (final SQLException e) {
            throw new IllegalArgumentException("Failed to find weapons from database");
        }
        return data;
    }

    private static void setup(final String script) {
        position = 0;
        data = new TimeSheet();
        final Scanner input = new Scanner(script);
        in = new ArrayList<>();
        lastBrace = new LinkedList<>();
        while (input.hasNext()) in.addLast(input.next());
    }

    private static void readHeader() throws IllegalArgumentException, SQLException, TimeSheet.NoMoreTimeException {
        String weaponType, weaponFrame;

        // build the three weapons from the database
        kinetic = readWeapon("kinetic");
        energy = readWeapon("energy");
        power = readWeapon("power");
        readStartWith();

        readScript();
    }

    /**
     * Method intended for reading the weapons located in the header and calling back upon the database
     * to build the weapons.
     * @param slotName the slot to construct a weapon for
     * @return the weapon specified in the script
     * @throws SQLException if the database fails to somehow fetch the weapon
     */
    private static Weapon readWeapon(final String slotName) throws SQLException {
        final String givenSlotName = in.get(position);
        position++;
        final String givenWeaponFrame = in.get(position);
        position++;
        final String givenWeaponType = in.get(position);
        position++;

        if (slotName.equals(givenSlotName)) {
            return Database.buildWeapon(givenWeaponFrame, givenWeaponType);
        } else throw new IllegalArgumentException("Unexpected slot name or invalid slot order in header.");
    }

    private static void readStartWith() {
        if (!"startswith".equals(in.get(position)))
            throw new IllegalArgumentException("Expected 'startswith' argument, instead found " + in.get(position));

        position++;
        equipped = readWeaponSlot(in.get(position));
        position++;
    }

    private static void readScript() throws TimeSheet.NoMoreTimeException {
        while (position < in.size()) readStatement();
    }

    private static void readScriptBlock() throws TimeSheet.NoMoreTimeException {
        if (!"{".equals(in.get(position))) throw new IllegalArgumentException("Expected opening brace '{'");
        position++;

        try {
            while (!"}".equals(in.get(position))) {
                readStatement();
            }
        } catch (final IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Missing closing brace");
        }
        position++;

    }
    
    private static void readStatement() throws TimeSheet.NoMoreTimeException {
        final String token = in.get(position);
        position++;

        switch (token) {
            case "if" -> readConditional();
            case "loop" -> readLoop();
            default -> {
                position--;
                readAction();
            }
        }
    }
    
    private static void readConditional() throws TimeSheet.NoMoreTimeException {
        final boolean result = readBoolExpr();
        if (!"then".equals(in.get(position))) throw new IllegalArgumentException("Expected 'then' after condition");
        position++;

        if (result) {
            readScriptBlock();
        } // otherwise skip this conditional
        else skipToClosingBrace();

        readScript();
    }
    
    private static boolean readBoolExpr() {
        return readDisjunction();
    }

    private static boolean readDisjunction() {
        final boolean b = readConjunction();

        if ("OR".equals(in.get(position))) {
                position++;
                return b || readDisjunction();
        } else return b;
    }

    private static boolean readConjunction() {
        final boolean b = readNegation();

        if ("AND".equals(in.get(position))) {
            position++;
            return b && readNegation();
        } else return b;
    }

    private static boolean readNegation() {
        if ("NOT".equals(in.get(position))) {
            position++;
            return !readNegation();
        } else {
            return readCondition();
        }
    }

    private static boolean readCondition() {
        final String token = in.get(position);

        if (token.endsWith("?")) return readBooleanStat();
        else return readNumericStatComparison();
    }

    private static boolean readBooleanStat() {
        final String token = in.get(position);
        // split on non-word to get the word tokens
        final String weapon = token.split("[.?]")[0];
        final String booleanStat = token.split("[.?]")[1];
        position++;

        return switch (weapon) {
            case "kinetic" -> readBooleanMember(kinetic, booleanStat);
            case "energy" -> readBooleanMember(energy, booleanStat);
            case "power" -> readBooleanMember(power, booleanStat);
            case "equipped" -> readBooleanMember(equipped, booleanStat);
            default -> throw new IllegalArgumentException("Unknown slot in token " + token);
        };

    }

    private static boolean readBooleanMember(final Weapon w, final String booleanStat) {
        return switch (booleanStat) {
            case "isEmpty", "isReservesEmpty" -> w.getReservesCurrent() == 0;
            case "isMagazineEmpty" -> w.getMagazineCurrent() == 0;
            default -> throw new IllegalArgumentException("Unknown boolean stat " + booleanStat);
        };
    }

    private static boolean readNumericStatComparison() {
        final int statL = readStat();

        final String operator = in.get(position);
        position++;

        final int statR = readStat();

        return switch(operator) {
            case ">" -> statL > statR;
            case ">=" -> statL >= statR;
            case "<" -> statL < statR;
            case "<=" -> statL <= statR;
            case "=" -> statL == statR;
            case "<>" -> statL != statR;
            default -> throw new IllegalArgumentException("Unexpected inequality operator: " + operator);
        };
    }

    private static int readStat() {
        final String token = in.get(position);
        position++;

        if (token.endsWith("#")) {
            final String[] query = token.split("[.#]");

            final Weapon w = readWeaponSlot(query[0]);

            return switch (query[1]) {
                case "magazine" -> w.getMagazineCurrent();
                case "magazineMax" -> w.getMagazineMax();
                case "reloadSpeed" -> w.getReloadSpeed();
                case "reserves" -> w.getReservesCurrent();
                case "reservesMax" -> w.getReservesMax();
                default -> throw new IllegalArgumentException("Unknown numeric stat " + query[1]);
            };
        } else {
            return Integer.parseInt(token);
        }

    }

    private static void readLoop() throws TimeSheet.NoMoreTimeException {
        final String token = in.get(position);
        position++;
        
        if ("while".equals(token)) { // loop while loop
            final int boolExprStart = position;
            boolean condition = readBoolExpr();

            lastBrace.add(position);

            while (condition) {
                position = lastBrace.getLast();
                readScriptBlock();
                position = boolExprStart;
                condition = readBoolExpr();
            }

            skipToClosingBrace();

            lastBrace.removeLast();

        } else { // loop count loop

            int loopCount;
            if (token.matches("\\d+")) loopCount = Integer.parseInt(token);
            else throw new IllegalArgumentException("Expected loop count integer but instead found " + token);

            lastBrace.add(position); // right now we are on a brace

            while (loopCount > 0) {
                loopCount--;
                position = lastBrace.getLast();
                readScriptBlock();
            }

            lastBrace.removeLast();
        }
    }
    
    private static void readAction() throws TimeSheet.NoMoreTimeException {
        final String token = in.get(position);
        position++;

        if (!token.endsWith("!")) throw new IllegalArgumentException("Unknown action " + token);
        final String[] splitToken = token.split("[.!]");
        readFunction(splitToken[0], splitToken[1]);
    }

    private static void readFunction(final String slot, final String function) throws TimeSheet.NoMoreTimeException {
        final Weapon w = readWeaponSlot(slot);
        if ("equipped".equals(slot)) {
            switch(function) {
                case "shootAtPrecision" -> shoot(damageType.PRECISION);
                case "shootAtBody" -> shoot(damageType.BODY);
                case "reload" -> reload();
                default -> throw new IllegalArgumentException(
                        "Unknown weapon function %s for slot %s".formatted(function, slot)
                );
            }
        } else {
            switch(function) {
                case "equip" -> equip(w);
                default -> throw new IllegalArgumentException(
                        "Unknown weapon function %s for slot %s".formatted(function, slot)
                );
            }
        }
    }

    private static void equip(final Weapon w) throws TimeSheet.NoMoreTimeException {
        Weapon.writeSwapEvent(data, equipped, w);
        equipped = w;
    }

    private static void shoot(final damageType t) throws TimeSheet.NoMoreTimeException {
        equipped.writeFireEvent(data, t);
    }

    private static void reload() throws TimeSheet.NoMoreTimeException {
        equipped.writeReloadEvent(data);
    }

    /**
     * Convenience method that gives the weapon slot based on the provided string.
     * @param slot the slot as a string
     * @return the Weapon object
     */
    private static Weapon readWeaponSlot(final String slot) {
        return switch(slot) {
            case "kinetic" -> kinetic;
            case "energy" -> energy;
            case "power" -> power;
            case "equipped" -> equipped;
            default -> throw new IllegalArgumentException("Unknown weapon slot " + slot);
        };
    }

    /**
     * Convenience method that moves the position to the next closing brace.
     * The position variable is modified to be on the token ahead of the closing brace
     */
    private static void skipToClosingBrace() {
        int depth = 1;
        boolean endBraceFound = false;
        while (!(depth == 0 && endBraceFound)) {
            final String token = in.get(position);
            position++;
            if ("{".equals(token)) depth++;
            else if ("}".equals(token)) {
                depth--;
                endBraceFound = true;
            }
        }
    }
}
