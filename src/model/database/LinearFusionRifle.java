package model.database;

import model.script.ScriptReader;
import model.script.TimeSheet;

/**
 * A class modeling a linear fusion rifle.
 * @author Roman Bureacov
 * @version 2025 August
 */
public class LinearFusionRifle extends GenericWeapon {
    private final static int ADAPTIVE_BOLT_DELAY = 200;

    private final int chargeTime;
    private final int chargeRecovery;

    /**
     * Constructs a model of a linear fusion rifle
     * @param skeleton the weapon skeleton specifying immutable parameters
     * @param theChargeTime the charge time in milliseconds
     * @param theChargeRecovery the delay between firing and the next charge in milliseconds
     */
    LinearFusionRifle(final WeaponSkeleton skeleton,
                      final int theChargeTime, final int theChargeRecovery) {
        super(skeleton);
        chargeTime = theChargeTime;
        chargeRecovery = theChargeRecovery;
    }

    @Override
    public void writeFireEvent(final TimeSheet t, final ScriptReader.damageType d)
            throws TimeSheet.NoMoreTimeException {
        if (getMagazineCurrent() > 0) {
            fire(1);
            final int damage = getDamageFromType(d);
            if ("AdaptiveBurst".equals(getWeaponFrame())) {
                // three-round burst linear fusion
                final int boltDamage = damage / 3;
                t.writeEvent(chargeTime, 0, "charge linear fusion rifle");
                t.writeEvent(ADAPTIVE_BOLT_DELAY, boltDamage, "fire linear fusion rifle bolt");
                t.writeEvent(ADAPTIVE_BOLT_DELAY, boltDamage, "fire linear fusion rifle bolt");
                t.writeEvent(chargeRecovery, boltDamage, "fire linear fusion rifle bolt");
            } else {
                // single-bolt linear fusion
                t.writeEvent(chargeTime, 0, "charge linear fusion rifle");
                t.writeEvent(chargeRecovery, damage, "fire linear fusion rifle bolt");
            }
        }
    }
}
