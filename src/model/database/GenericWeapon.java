package model.database;

import model.script.ScriptReader;
import model.script.TimeSheet;

/**
 * Class that represents a weapon and will be responsible for advancing
 * the time marker based on its characteristics.
 * @author Roman Bureacov
 * @version July 2025
 */
public class GenericWeapon implements Weapon {
    private final String weaponType;
    private final String weaponFrame;
    private final Ammo ammo;
    private final int precisionDamage;
    private final int bodyDamage;
    private final int rpm;
    private final int reloadSpeed;
    private final int swapSpeed;
    private final int magazineMax;
    private int magazineCurrent;
    private final int reservesMax;
    private int reservesCurrent;

    /**
     * Constructs a weapon based on the skeleton
     * @param skeleton the weapon skeleton specifying immutable parameters
     */
    GenericWeapon(final WeaponSkeleton skeleton) {
        super();
        weaponType = skeleton.theWeaponType;
        weaponFrame = skeleton.theWeaponFrame;
        ammo = skeleton.theAmmo;
        precisionDamage = skeleton.thePrecisionDamage;
        bodyDamage = skeleton.theBodyDamage;
        rpm = skeleton.theRPM;
        reloadSpeed = skeleton.theReloadSpeed;
        swapSpeed = skeleton.theEquipSpeed;
        magazineMax = skeleton.theMagazineMax;
        magazineCurrent = magazineMax;
        reservesMax = skeleton.theReservesMax;
        reservesCurrent = reservesMax;
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
    public Ammo getAmmoType() {
        return ammo;
    }

    @Override
    public String getWeaponFrame() {
        return weaponFrame;
    }

    @Override
    public String getWeaponType() {
        return weaponType;
    }

    @Override
    public void writeEquipEvent(final TimeSheet t) {
        t.writeEvent(swapSpeed, 0, "swap weapons");
    }

    @Override
    public void writeFireEvent(final TimeSheet t, final ScriptReader.damageType d) {
        if (magazineCurrent > 0) {
            fire(1);
            final int damage = getDamageFromType(d);

            final int secPerMin = 60;
            t.writeEvent(1 / rpm * secPerMin, damage, "fire");
        }
    }

    @Override
    public void writeReloadEvent(final TimeSheet t) {
        if (magazineCurrent != magazineMax) {
            reload();
            if (magazineCurrent != magazineMax)
                t.writeEvent(reloadSpeed, 0, "reload");
        }
    }

    /**
     * Reduces the magazine by a fixed delta, down to zero.
     * @param delta the amount to change the current magazine capacity by
     */
    void decrementMagazine(final int delta) {
        if (magazineCurrent != 0) magazineCurrent = Integer.max(0, magazineCurrent - delta);
    }

    /**
     * Reduces the reserves by a fixed delta.
     * @param delta the amount to change the current reserves by
     */
    void changeMagazine(final int delta) {
        if (reservesCurrent != 0) reservesCurrent = Integer.max(0, reservesCurrent - delta);
    }

    /**
     * Reloads the weapon based on the current magazine capacity, only
     * drawing as much from the reserves as necessary.
     */
    void reload() {
        reservesCurrent -= (magazineMax - magazineCurrent);
        magazineCurrent = magazineMax;
    }

    /**
     * Fire the necessary amount of rounds to decrement the magazine.
     * @param rounds the number of rounds to fire.
     */
    void fire(final int rounds) {
        if (magazineCurrent > 0) magazineCurrent -= rounds;
    }

    /**
     * Returns the respective damage based on the type provided
     * @param t the damage type
     * @return the damage based on the type provided
     */
    int getDamageFromType(ScriptReader.damageType t) {
        return switch (t) {
            case BODY -> bodyDamage;
            case PRECISION -> precisionDamage;
            default -> throw new IllegalArgumentException("Unknown enum");
        };
    }

    /**
     * Sets the current magazine.
     * @param theNewMagazine the new magazine size
     */
    void setMagazineCurrent(final int theNewMagazine) {
        magazineCurrent = Integer.max(0, Integer.min(magazineMax, theNewMagazine));
    }

    void setReservesCurrent(final int theNewReserves) {
        reservesCurrent = Integer.max(0, theNewReserves);
    }
}
