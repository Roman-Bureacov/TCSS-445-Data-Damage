package model.database;

import model.script.ScriptReader;
import model.script.TimeSheet;

/**
 * Interface that represents a weapon's ability to load individual shells rather than
 * and entire magazine.
 * @author Roman Bureacov
 * @version August 2025
 */
public abstract class ShellLoading extends GenericWeapon {

    /**
     * Constructs a weapon based on the skeleton.
     * @param skeleton the weapon skeleton specifying immutable parameters
     */
    ShellLoading(final WeaponSkeleton skeleton) {
        super(skeleton);
    }

    private boolean initialShell;

    @Override
    public void writeEquipEvent(final TimeSheet t) {
        initialShell = true;
        super.writeEquipEvent(t);
    }

    @Override
    public void writeFireEvent(final TimeSheet t, final ScriptReader.damageType d) {
        initialShell = true;
        super.writeFireEvent(t, d);
    }

    @Override
    public void writeReloadEvent(final TimeSheet t) {
        if (getMagazineCurrent() < getMagazineMax()) {
            t.writeEvent(getInitialShellTime(), 0, "loading initial shell");
            while (getMagazineCurrent() < getMagazineMax()) {
                t.writeEvent(getSubsequentShellTime(), 0, "loading shell");
            }
        }
    }

    /**
     * Loads a single shell rather than attempting to load all of them.
     * @param t the timesheet
     */
    public void writeLoadShellEvent(final TimeSheet t) {
        if (getMagazineCurrent() < getMagazineMax()) {
            if (initialShell) {
                initialShell = false;
                t.writeEvent(getInitialShellTime(), 0, "loading initial shell");
            }
            else t.writeEvent(getSubsequentShellTime(), 0, "loading shell");

            setMagazineCurrent(getMagazineCurrent() + 1);
        }
    }

    abstract int getInitialShellTime();
    abstract int getSubsequentShellTime();
}
