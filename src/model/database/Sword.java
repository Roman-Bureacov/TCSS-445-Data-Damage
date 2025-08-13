package model.database;

import model.script.ScriptReader;
import model.script.TimeSheet;

/**
 * Class that models a sword.
 * @author Roman Bureacov
 * @version August 2025
 */
public class Sword extends GenericWeapon {
    private static final int FIRST_SWING = 460;
    private static final int SECOND_SWING = 460;
    private static final int THIRD_SWING = 490;

    Sword(final WeaponSkeleton skeleton) {
        super(skeleton);
    }

    @Override
    public void writeReloadEvent(final TimeSheet t) {
        // swords don't reload
    }

    @Override
    void fire(final int rounds) {
        setMagazineCurrent(getMagazineCurrent() - 1);
        setReservesCurrent(getReservesCurrent() - 1);
    }

    @Override
    public void writeFireEvent(final TimeSheet t, final ScriptReader.damageType d) {
        fire(1);
    }
}
