package model.database;

/**
 * Class that handles basic database operations
 * @author Roman Bureacov
 * @version July 2025
 */
public final class Database {
    private Database() {
        super();
        throw new IllegalStateException("Illegal to construct static class");
    }

    /**
     * Builds the appropriate weapon from the database.
     * @param weaponType the type name of the weapon
     * @param weaponFrame the frame name of the weapon
     * @return the built weapon object
     */
    public static Weapon buildWeapon(final String weaponFrame, final String weaponType) {
        return switch(weaponType) {
            case "Bow" -> buildBow(weaponFrame);
            case "FusionRifle" -> buildFusionRifle(weaponFrame);
            case "PulseRifle" -> buildPulseRifle(weaponFrame);
            case "Shotgun" -> buildShotgun(weaponFrame);
            case "Sword" -> buildSword(weaponFrame);
            default -> buildGenericWeapon(weaponFrame, weaponType);
        };
    }

    private static Weapon buildGenericWeapon(final String weaponFrame, final String weaponType) {
        return null;
    }

    private static Weapon buildSword(final String weaponFrame) {
        return null;
    }

    private static Weapon buildShotgun(final String weaponFrame) {
        return null;
    }

    private static Weapon buildPulseRifle(final String weaponFrame) {
        return null;
    }

    private static Weapon buildFusionRifle(final String weaponFrame) {
        return null;
    }

    private static Weapon buildBow(final String weaponFrame) {
        return null;
    }
}
