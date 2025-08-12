package model.script;

/**
 * Class that represents a weapon and will be responsible for advancing
 * the time marker based on its characteristics.
 * @author Roman Bureacov
 * @version July 2025
 */
public interface Weapon {

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
     * Returns the swap speed of this weapon.
     * @return the swap speed in milliseconds
     */
    int getSwapSpeed();

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
     * Marks on the timesheet the equip event.
     * @param t the timesheet to mark on
     */
    void equip(TimeSheet t);

    /**
     * Marks on the timesheet the shoot event.
     * @param t the timesheet to mark on
     * @param d the damage type
     */
    void shoot(TimeSheet t, ScriptReader.damageType d);

    /**
     * Marks on the timesheet the reload event
     * @param t the timesheet to mark on
     */
    void reload(TimeSheet t);
}
