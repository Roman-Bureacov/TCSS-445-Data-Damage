package model.database;

import model.script.Weapon;

/**
 * Class that represents a weapon and will be responsible for advancing
 * the time marker based on its characteristics.
 * @author Roman Bureacov
 * @version July 2025
 */
public class ConcreteWeapon implements Weapon {
    private final int precisionDamage;
    private final int bodyDamage;
    private final int reloadSpeed;
    private final int swapSpeed;
    private final int magazineMax;
    private final int magazineCurrent;
    private final int reservesCurrent;
    private final int reservesMax;

    protected ConcreteWeapon(final String theWeaponType, final String theWeaponFrame) {

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
}
