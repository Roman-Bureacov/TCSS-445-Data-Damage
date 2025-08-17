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
     * Reads in a script and returns a data set simulated by the script.
     * @param script the script to base the simulation upon.
     * @return the simulation data
     * @throws IllegalArgumentException if the script has syntax errors
     * @throws SQLException if the script failed to find data from the database
     */
    public static TimeSheet readData(final String script) throws IllegalArgumentException, SQLException {
        data = new TimeSheet();
        final Scanner input = new Scanner(script);
        in = new ArrayList<>();
        lastBrace = new LinkedList<>();
        while (input.hasNext()) in.addLast(input.next());

        readHeader(); // initiates the grammar

        return data;
    }

    private static void readHeader() throws IllegalArgumentException, SQLException {
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

    private static void readScript() {
        while (position < in.size()) readStatement();
    }

    private static void readScriptBlock() {
        if (!"{".equals(in.get(position))) throw new IllegalArgumentException("Expected opening brace '{'");
        position++;

        try {
            while (!"}".equals(in.get(position))) {
                readStatement();
            }
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Missing closing brace");
        }
        position++;

    }
    
    private static void readStatement() {
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
    
    private static void readConditional() {
        final boolean result = boolExpr();
        if (!"then".equals(in.get(position))) throw new IllegalArgumentException("Expected 'then' after condition");
        position++;

        if (result) {
            readScriptBlock();
        } // otherwise skip this conditional
        else while(!"}".equals(in.get(position))) position++;
        position++;

        readScript();
    }
    
    private static boolean boolExpr() {
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
            position--;
            return readCondition();
        }

    }

    private static boolean readCondition() {
        final String token = in.get(position);

        if (token.endsWith("?")) return readBooleanStat();
        else if (token.endsWith("#")) return readNumericStatComparison();
        else throw new IllegalArgumentException("Unknown stat query " + token);
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
        final String[] queryL = in.get(position).split("[.#]");
        final String[] queryR = in.get(position + 2).split("[.#]");
        // split on non-word to get the word tokens
        final int statL = readNumericStat(queryL[0], queryL[1]);
        final int statR = readNumericStat(queryR[0], queryR[1]);

        position++;
        final boolean result = switch(in.get(position)) {
            case ">" -> statL > statR;
            case ">=" -> statL >= statR;
            case "<" -> statL < statR;
            case "<=" -> statL <= statR;
            case "=" -> statL == statR;
            case "<>" -> statL != statR;
            default -> throw new IllegalArgumentException("Unexpected value: " + in.get(position));
        };

        position += 2; // advance to next word
        return result;
    }

    private static int readNumericStat(final String slot, final String numericStat) {
        final Weapon w = readWeaponSlot(slot);

        return switch(numericStat) {
            case "magazine" -> w.getMagazineCurrent();
            case "magazineMax" -> w.getMagazineMax();
            case "reloadSpeed" -> w.getReloadSpeed();
            case "reserves" -> w.getReservesCurrent();
            case "reservesMax" -> w.getReservesMax();
            default -> throw new IllegalArgumentException("Unknown numeric stat " + numericStat);
        };

    }

    private static void readLoop() {
        final String loopCountToken = in.get(position);
        position++;

        int loopCount;
        if (loopCountToken.matches("\\d+")) loopCount = Integer.parseInt(loopCountToken);
        else throw new IllegalArgumentException("Expected loop count integer but instead found " + loopCountToken);

        lastBrace.add(position); // right now we are on a brace

        while (loopCount > 0) {
            loopCount--;
            position = lastBrace.getLast();
            readScriptBlock();
        }

        lastBrace.removeLast();
        position++;
    }
    
    private static void readAction() {
        final String token = in.get(position);
        position++;

        if (!token.endsWith("!")) throw new IllegalArgumentException("Unknown action " + token);
        final String[] splitToken = token.split("[.!]");
        readFunction(splitToken[0], splitToken[1]);
    }

    private static void readFunction(final String slot, final String function) {
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

    private static void equip(final Weapon w) {
        Weapon.writeSwapEvent(data, equipped, w);
        equipped = w;
    }

    private static void shoot(final damageType t) {
        equipped.writeFireEvent(data, t);
    }

    private static void reload() {
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

}
