package model.script;

public class sampletest {
    public static void main(String[] args) {
        final String script = """
                kinetic Aggressive AutoRifle
                energy RapidFire SniperRifle
                power Precision RocketLauncher
                
                kinetic.equip!
                loop 4 {
                    kinetic.shootAtPrecision!
                }
                """;
        for (var thing :  ScriptReader.readData(script))
            System.out.format("%d %d %s\n", (Integer)thing[0], (Integer)thing[1], thing[2]);
    }
}
