package model.database;

import model.script.ScriptReader;
import model.script.TimeSheet;

/**
 * Class that models a fusion rifle.
 * @author Roman Bureacov
 * @version August 2025
 */
public class FusionRifle extends GenericWeapon {

    /** The time per bolt in milliseconds. */
    private static final int TIME_PER_BOLT = 33;

    private final int chargeTime;
    private final int boltCount;
    private final int recovery;

    /**
     * Constructs a fusion rifle model weapon.
     * @param skeleton the weapon skeleton
     * @param theChargeTime the time to charge in milliseconds
     * @param theBoltCount the bolt count
     * @param theRecovery the time in milliseconds after the final bolt until you can charge again
     */
    FusionRifle(final WeaponSkeleton skeleton,
                          final int theChargeTime, final int theBoltCount, final int theRecovery) {
        super(skeleton);
        chargeTime = theChargeTime;
        boltCount = theBoltCount;
        recovery = theRecovery;
    }

    @Override
    public void writeFireEvent(final TimeSheet t, final ScriptReader.damageType d) {
        if (getMagazineCurrent() > 0) {
            fire(1);
            final int damage = getDamageFromType(d);

            t.writeEvent(chargeTime, 0, "charge fusion rifle");
            for (int i = 0; i < boltCount; i++)
                t.writeEvent(TIME_PER_BOLT, damage, "fusion rifle bolt hits");
            t.advancePlayhead(-TIME_PER_BOLT); // final bolt doesn't contribute to the recovery
            t.writeEvent(recovery, 0, "fusion rifle charge recovery");
        }
    }
}
