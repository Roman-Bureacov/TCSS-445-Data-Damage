package model.database;

import model.script.ScriptReader;
import model.script.TimeSheet;

/**
 * Class that models a pulse rifle
 * @author Roman Bureacov
 * @version August 2025
 */
public class PulseRifle extends PrimaryAmmoWeapon {
    private static final int BURST_DELAY = 33; // guesstimation

    private final int burstCount;
    private final int burstRecovery;

    PulseRifle(final WeaponSkeleton skeleton,
               final int theBurstCount, final int theBurstRecovery) {
        super(skeleton);
        burstCount = theBurstCount;
        burstRecovery = theBurstRecovery;
    }

    @Override
    public void writeFireEvent(final TimeSheet t, final ScriptReader.damageType d) {
        if (getMagazineCurrent() > 0) {
            final int damage = getDamageFromType(d);
            fire(burstCount);
            for (int i = 0; i < burstCount; i++)
                t.writeEvent(BURST_DELAY, damage, "Fire pulse rifle burst");
            t.advancePlayhead(-BURST_DELAY);
            t.writeEvent(burstRecovery, 0, "Pulse rifle burst recovery");
        }
    }
}
