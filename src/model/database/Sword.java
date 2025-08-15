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
    private int swing = 0;

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
        t.writeEvent(getNextSwingTime(), getDamageFromType(d), "swing sword");
    }

    @Override
    public void writeEquipEvent(final TimeSheet t) {
        super.writeEquipEvent(t);
        swing = 0;
    }

    @Override
    public void writeStowEvent(final TimeSheet t) {
        super.writeStowEvent(t);
        swing = 0;
    }

    private int getNextSwingTime() {
        swing = (swing + 1) % 3;
        return switch(swing) {
            case 0 -> FIRST_SWING;
            case 1 -> SECOND_SWING;
            default -> THIRD_SWING;
        };
    }
}
