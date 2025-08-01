package model.script;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import model.database.Database;

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

    /** String that represents the output data */
    private static List<Integer[]> data;
    private static Scanner in;
    private static Weapon kinetic;
    private static Weapon energy;
    private static Weapon power;

    private ScriptReader() {
        super();
        throw new IllegalStateException("Illegal to instantiate this static class");
    }

    /**
     * Reads in a script and returns a data set simulated by the script.
     * @param script the script to base the simulation upon.
     * @return the simulation data
     */
    public static List<Integer[]> readData(final String script) throws IllegalArgumentException {
        data = new LinkedList<>();
        in = new Scanner(script);


        return null;
    }

    private static void readFile() throws IllegalArgumentException {
        String weaponType, weaponFrame;

        // build the three weapons from the database
        kinetic = build("kinetic");
        energy = build("energy");
        power = build("power");

        script();
    }

    private static Weapon build(final String slotName) {
        final String givenSlotName = in.next();
        final String givenWeaponFrame = in.next();
        final String givenWeaponType = in.next();

        if (slotName.equals(givenSlotName)) {
            return Database.buildWeapon(givenWeaponFrame, givenWeaponType);
        } else throw new IllegalArgumentException("Unexpected slot name or invalid slot order in header.");
    }

    private static void script() {
        while (in.hasNext()) statement();
    }
    
    private static void statement() {
        final String token = in.next();
        switch (token) {
            case "if" -> conditional();
            case "loop" -> loop();
            default -> action();
        }
    }
    
    private static void conditional() {
        final boolean result = boolExpr();
        if (!"then".equals(in.next())) throw new IllegalArgumentException("Expected 'then' after condition");
        if (result) script();
    }
    
    private static boolean boolExpr() { // TODO: pick up from here, might want to tokenize instead of scan
        final String token = in.next();
        switch(token) {
            case "NOT" -> ;
            case "then" -> ;
            default throw new IllegalArgumentException("Unexpected argument " + token);
        }
        if ("NOT".equals(token)) return !statCompareStat();
        else
    }
    
    private static void loop() {
        
    }
    
    private static void action() {
        
    }

}
