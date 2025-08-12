package model.database;

import model.script.ScriptReader;
import model.script.TimeSheet;
import model.script.Weapon;

/**
 * Class that represents a weapon and will be responsible for advancing
 * the time marker based on its characteristics.
 * @author Roman Bureacov
 * @version July 2025
 */
public class GenericWeapon implements Weapon {
    private final int precisionDamage;
    private final int bodyDamage;
    private final int rpm;
    private final int reloadSpeed;
    private final int swapSpeed;
    private final int magazineMax;
    private final int magazineCurrent;
    private final int reservesCurrent;
    private final int reservesMax;

    protected GenericWeapon(final String theWeaponType, final String theWeaponFrame) {
        precisionDamage = 0;
        bodyDamage = 0;
        reloadSpeed = 0;
        swapSpeed = 0;
        magazineMax = 0;
        magazineCurrent = 0;
        reservesCurrent = 0;
        reservesMax = 0;
    }

    @Override
    public int getPrecisionDamage() {
        return precisionDamage;
    }

    @Override
    public int getBodyDamage() {
        return bodyDamage;
    }

    @Override
    public int getReloadSpeed() {
        return reloadSpeed;
    }

    @Override
    public int getSwapSpeed() {
        return swapSpeed;
    }

    @Override
    public int getMagazineMax() {
        return magazineMax;
    }

    @Override
    public int getMagazineCurrent() {
        return magazineCurrent;
    }

    @Override
    public int getReservesCurrent() {
        return reservesCurrent;
    }

    @Override
    public int getReservesMax() {
        return reservesMax;
    }

    @Override
    public void equip(final TimeSheet t) {
        t.writeEvent(swapSpeed, 0, "swap weapons");
    }

    @Override
    public void shoot(final TimeSheet t, final ScriptReader.damageType d) {
        final int damage = switch(d) {
            case PRECISION -> precisionDamage;
            case BODY -> bodyDamage;
            default -> throw new IllegalArgumentException("Unknown damage type enum");
        };

        final int secPerMin = 60;
        t.writeEvent(1/rpm * secPerMin, damage, "fire");
    }

    @Override
    public void reload(final TimeSheet t) {
        t.writeEvent(reloadSpeed, 0, "reload");
    }
}
