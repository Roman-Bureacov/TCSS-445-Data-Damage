package model.script;

import java.sql.SQLException;

public class sampletest {
    public static void main(String[] args) throws SQLException {
        final String script = """
                kinetic Precision AutoRifle
                energy RapidFire SniperRifle
                power Precision RocketLauncher
                
                startswith energy
                
                kinetic.equip!
                loop 4 {
                    equipped.shootAtPrecision!
                }
                """;
        for (var thing :  ScriptReader.readData(script))
            System.out.format("%d %d %s\n", (Integer)thing[0], (Integer)thing[1], thing[2]);
    }
}
