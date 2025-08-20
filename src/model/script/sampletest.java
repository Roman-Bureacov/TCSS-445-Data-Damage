package model.script;

import java.sql.SQLException;

public class sampletest {
    public static void main(String[] args) throws SQLException {
        final String script1 =
                """
                kinetic Precision AutoRifle
                energy RapidFire SniperRifle
                power Precision RocketLauncher
                
                startswith energy
                
                kinetic.equip!
                loop 2 {
                    equipped.shootAtPrecision!
                }
                
                energy.equip!
                equipped.shootAtPrecision!
                """;

        final String script2 =
                """
                kinetic Aggressive ScoutRifle
                energy Aggressive Shotgun
                power Precision LinearFusionRifle
                
                startswith kinetic
                
                loop 200 {
                    equipped.shootAtPrecision!
                    if equipped.magazine# < 0 {
                        equipped.reload
                    }
                }
                equipped.reload!
                """;

        final String script3 =
                """
                kinetic Aggressive HandCannon
                energy RapidFire Shotgun
                power RapidFire MachineGun
                
                startswith kinetic
                
                loop while equipped.magazine# > 0 {
                    equipped.shootAtPrecision!
                }
                
                energy.equip!
                
                loop while NOT equipped.isMagazineEmpty? {
                    equipped.shootAtBody!
                }
                """;

        final String script4 =
                """
                kinetic Precision AutoRifle
                energy RapidFire SniperRifle
                power Precision RocketLauncher
                
                startswith energy
                
                loop 20 {
                    kinetic.equip!
                    loop 5 {
                        equipped.shootAtPrecision!
                    }
                
                    energy.equip!
                    equipped.shootAtPrecision!
                
                    power.equip!
                    loop 5 {
                        equipped.shootAtPrecision!
                    }
                }
                
                energy.equip!
                equipped.shootAtPrecision!
                """;

        final String script5 =
                """
                kinetic Precision AutoRifle
                energy RapidFire SniperRifle
                power Precision RocketLauncher
                
                startswith energy
                
                loop 2 {
                    kinetic.equip!
                    loop 2 {
                        equipped.shootAtPrecision!
                    }

                    energy.equip!
                    equipped.shootAtPrecision!

                    power.equip!
                    loop 2 {
                        equipped.shootAtPrecision!
                    }
                }
                
                energy.equip!
                equipped.shootAtPrecision!
                """;


        final var data = ScriptReader.readData(script4);

        for (final var thing : data)
            System.out.format("%10d %20d %s\n", (Integer)thing[0], (Integer)thing[1], thing[2]);

        for (final var thing : data.getDPSTimesheet(5000))
            System.out.format("%10d %20f %10s\n", thing[0], thing[1], thing[2]);
    }
}
