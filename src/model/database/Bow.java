package model.database;

import model.script.ScriptReader;
import model.script.TimeSheet;

/**
 * Weapon class that represents the unique characteristics of a bow.
 * The bow will not fire unless it is manually reloaded. This allows for
 * the ability to stow it before it reloads, to be able to fire and stow
 * rather than waiting for the reload animation to occur.
 * @author Roman Bureacov
 * @version 2025 August
 */
public class Bow extends PrimaryAmmoWeapon {
    private final int drawTime;
    private boolean isLoaded;

    /**
     * Constructs a bow model.
     * @param skeleton the weapon skeleton specifying immutable parameters
     */
    Bow(final WeaponSkeleton skeleton) {
        super(skeleton);
        this.drawTime = 1/skeleton.theRPM * 60 - skeleton.theReloadSpeed; // draw time may be inferred
    }

    @Override
    public void writeReadyEvent(final TimeSheet t) throws TimeSheet.NoMoreTimeException {
        super.writeReadyEvent(t);
        isLoaded = true;
    }

    @Override
    public void writeFireEvent(final TimeSheet t, final ScriptReader.damageType d)
            throws TimeSheet.NoMoreTimeException {
        if (isLoaded) {
            final int damage = getDamageFromType(d);
            t.writeEvent(drawTime, 0, "draw bow");
            t.writeEvent(0, damage, "release arrow");
            isLoaded = false;
        }
    }

    @Override
    public void writeReloadEvent(final TimeSheet t) throws TimeSheet.NoMoreTimeException {
        super.writeReloadEvent(t);
        isLoaded = true;
    }
}
