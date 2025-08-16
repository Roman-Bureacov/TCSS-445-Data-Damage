package model.database;

import model.script.ScriptReader;
import model.script.TimeSheet;

/**
 * Class that represents a weapon and will be responsible for advancing
 * the time marker based on its characteristics.
 * @author Roman Bureacov
 * @version July 2025
 */
public interface Weapon {

    /**
     * Represents the ammo type of weapon.
     */
    enum Ammo {
        /** primary ammo. */
        PRIMARY,
        /** special ammo. */
        SPECIAL,
        /** heavy ammo, for power weapons exclusively. */
        HEAVY
    }

    /**
     * Returns the precision damage of this weapon.
     * @return the precision damage
     */
   int getPrecisionDamage();

    /**
     * Returns the body damage of this weapon.
     * @return the body damage
     */
    int getBodyDamage();

    /**
     * Returns the reload speed of this weapon.
     * @return the reload speed in milliseconds
     */
    int getReloadSpeed();

    /**
     * Returns the ready speed of this weapon, after stowing the other weapon.
     * @return the ready speed in milliseconds
     */
    int getReadySpeed();

    /**
     * Returns the stow speed of this weapon.
     * @return the stow speed in milliseconds
     */
    int getStowSpeed();

    /**
     * Returns the maximum magazine size of this weapon.
     * @return the maximum magazine size
     */
    int getMagazineMax();

    /**
     * Returns the current magazine size of this weapon.
     * @return the current magazine size
     */
    int getMagazineCurrent();

    /**
     * Returns the current reserves of this weapon.
     * @return positiveinteger if this weapon has reserves to work with, -1 otherwise
     */
    int getReservesCurrent();

    /**
     * Returns the maximum reserves of ths weapon.
     * @return positiveinteger or zero if this weapon has reserves to work with, -1 otherwise
     */
    int getReservesMax();

    /**
     * Returns the ammo type of this weapon.
     * @return the ammo type of this weapon
     */
    Ammo getAmmoType();

    /**
     * Returns the frame of this weapon.
     * @return the weapon frame, camel-case and no-space
     */
    String getWeaponFrame();

    /**
     * Returns the type of this weapon.
     * @return the weapon type, camel-case and no-space
     */
    String getWeaponType();

    /**
     * Markso n the timesheet the stow event.
     * @param t the timesheet to mark on
     */
    void writeStowEvent(TimeSheet t);

    /**
     * Marks on the timesheet the ready event.
     * @param t the timesheet to mark on
     */
    void writeReadyEvent(TimeSheet t);

    /**
     * Marks on the timesheet the shoot event.
     * Does nothing if the weapon magazine has no more ammo.
     * @param t the timesheet to mark on
     * @param d the damage type
     */
    void writeFireEvent(TimeSheet t, ScriptReader.damageType d);

    /**
     * Marks on the timesheet the reload event.
     * Does nothing if the weapon is at magazine capacity.
     * @param t the timesheet to mark on
     */
    void writeReloadEvent(TimeSheet t);

    /**
     * Convenience method that write the swapping event by first writing the
     * stow for the first weapon and then the ready for the other weapon
     * @param t the timesheet to mark on
     * @param w1 the weapon being swapped from, the weapon that is stowed
     * @param w2 the weapon being swapped to, the weapon that is readied
     */
    static void writeSwapEvent(final TimeSheet t, final Weapon w1, final Weapon w2) {
        w1.writeStowEvent(t);
        w2.writeReadyEvent(t);
    }
}
