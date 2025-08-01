package model.database;

import java.util.List;

import model.script.Weapon;

/**
 * Class that handles basic database operations
 * @author Roman Bureacov
 * @version July 2025
 */
public final class Database {
    private Database() {
        throw new IllegalStateException("Illegal to construct static class");
    }

    /**
     * Builds the appropriate weapon from the database.
     * @param weaponType the type name of the weapon
     * @param weaponFrame the frame name of the weapon
     * @return the built weapon object
     */
    public static Weapon buildWeapon(final String weaponFrame, final String weaponType) {
        return null;
    }
}
