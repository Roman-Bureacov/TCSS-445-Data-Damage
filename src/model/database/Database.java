package model.database;

import java.sql.*;

import model.database.build.DatabaseProvider;

//import model.script.Weapon;

/**
 * Class that handles basic database operations
 * @author Roman Bureacov
 * @author Kaleb Anagnostou
 * @version July 2025
 */
public final class Database {

    private static final Database instance;

    static {
        try {
            // call upon the almighty JVM to use the static initializer
            Class.forName("model.database.build.DatabaseProvider");
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        instance = new Database();
    }

    private Database() { super(); }

    /**
     * Returns the singleton database manager instance
     * @return the database singleton
     */
    public static synchronized Database getInstance() {
        return instance;
    }

    /**
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public ResultSet executeQuery(String sql, Object... params) throws SQLException {
        Connection conn = DatabaseProvider.getConnection();
        PreparedStatement preparedStatementstmt = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            preparedStatementstmt.setObject(i + 1, params[i]);
        }
        return preparedStatementstmt.executeQuery();
    }

    public int executeUpdate(String sql, Object... params) throws SQLException {
        Connection conn = DatabaseProvider.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++){
            ps.setObject(i + 1, params[i]);
        }
        return ps.executeUpdate();
    }

    public long executeInsertReturningId(String sql, Object... params) throws SQLException {
        Connection conn = DatabaseProvider.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        for (int i = 0; i < params.length; i++){
            ps.setObject(i + 1, params[i]);
        }
        ps.executeUpdate();
        try (ResultSet keys = ps.getGeneratedKeys()) {
            if (keys.next()){
                return keys.getLong(1);
            }
            throw new SQLException("No generated key returned.");
        }
    }

    /**
     * Builds the appropriate weapon from the database.
     * @param weaponType the type name of the weapon
     * @param weaponFrame the frame name of the weapon
     * @return the built weapon object
     * @throws SQLException if unexpected error occurs while trying to fetch data
     */
    public static Weapon buildWeapon(final String weaponFrame, final String weaponType) throws SQLException {
        final String sql =
                """
                SELECT * 
                FROM weapons NATURAL JOIN weapon_stats
                WHERE weapon_id = (
                    SELECT weapon_id
                    FROM weapons
                    WHERE weapon_type = '%s' AND weapon_frame = '%s'
                )
                """.formatted(weaponType, weaponFrame);

        final ResultSet r;

        final Connection c = DatabaseProvider.getConnection();
        final Statement s = c.createStatement();
        r = s.executeQuery(sql);

        final WeaponSkeleton skeleton = buildWeaponSkeleton(r);

        return switch(weaponType) {
            case "Bow" -> buildBow(weaponFrame, skeleton, s);
            case "FusionRifle" -> buildFusionRifle(weaponFrame, skeleton, s);
            case "PulseRifle" -> buildPulseRifle(weaponFrame, skeleton, s);
            case "Shotgun" -> buildShotgun(skeleton);
            case "Sword" -> buildSword(skeleton);
            case "ScoutRifle" -> {
                if ("Aggressive".equals(weaponFrame)) yield buildAggressiveScoutRifle(skeleton);
                else yield buildGenericWeapon(skeleton);
            }
            default -> buildGenericWeapon(skeleton);
        };
    }

    /**
     * Constructs the basic weapon skeleton to be used when building weapon models.
     * @param r the result set of the weapon stats
     * @return the weapon skeleton
     * @throws SQLException if there is a problem with the SQL result set
     */
    private static WeaponSkeleton buildWeaponSkeleton(final ResultSet r) throws SQLException {
        final WeaponSkeleton skeleton = new WeaponSkeleton();

        skeleton.theWeaponFrame = r.getString("weapon_frame");
        skeleton.theWeaponType = r.getString("weapon_type");
        skeleton.theReservesMax = r.getInt("reserves");
        skeleton.theRPM = r.getInt("fire_rate");
        skeleton.theReloadSpeed = r.getInt("reload_speed");
        skeleton.theMagazineMax = r.getInt("magazine");
        skeleton.theStowSpeed = r.getInt("stow_speed");
        skeleton.theReadySpeed = r.getInt("ready_speed");
        skeleton.theBodyDamage = r.getInt("body_damage");
        skeleton.thePrecisionDamage = r.getInt("precision_damage");

        // get ammo
        final String sql =
                """
                SELECT ammo
                FROM ammo_types
                WHERE ammo_id = (
                    SELECT ammo_id
                    FROM weapons NATURAL JOIN weapon_ammo
                    WHERE weapon_frame = ? AND weapon_type = ?
                );
                """;
        try {
            skeleton.theAmmo =
                Weapon.getAmmoFromString(
                    Database.getInstance()
                    .executeQuery(sql, skeleton.theWeaponFrame, skeleton.theWeaponType)
                    .getString( "ammo")
                );
        } catch (final Exception e) {
            throw new IllegalArgumentException("Failed to fetch ammo type from database");
        }

        return skeleton;
    }

    /**
     * Constructs a generic weapon. Most weapons can be modeled this way.
     * @return a generic weapon model
     */
    private static Weapon buildGenericWeapon(final WeaponSkeleton skeleton) {
        return new GenericWeapon(skeleton);
    }

    /**
     * Constructs a weapon model specifically as a shell-loading aggressive scout-rifle
     * @param skeleton the weapon skeleton
     * @return aggressive scout rifle weapon model
     */
    private static Weapon buildAggressiveScoutRifle(final WeaponSkeleton skeleton) {
        return new AggressiveScoutRifle(skeleton);
    }

    /**
     * Constructs a sword model
     * @return a sword weapon model
     */
    private static Weapon buildSword(final WeaponSkeleton skeleton) {
        return new Sword(skeleton);
    }

    /**
     * Constructs a shotgun model
     * @return a shotgun weapon model
     */
    private static Weapon buildShotgun(final WeaponSkeleton skeleton) {
        return new Shotgun(skeleton);
    }

    /**
     * Constructs a pulse rifle model
     * @param weaponFrame the pulse rifle frame name
     * @return a pulse rifle weapon model
     */
    private static Weapon buildPulseRifle(final String weaponFrame,
                                          final WeaponSkeleton skeleton, final Statement s) throws SQLException {

        final String sql =
                """
                SELECT * FROM PulseRifleSpecifics
                WHERE weapon_id = (
                    SELECT weapon_id
                    FROM weapons
                    WHERE weapon_type = 'PulseRifle' AND weaponFrame = '%s'
                )
                """.formatted(weaponFrame);
        s.execute(sql);
        final ResultSet r = s.getResultSet();

        return new PulseRifle(skeleton,
                r.getInt("burst_count"),
                r.getInt("burst_recovery")
        );
    }

    /**
     * Constructs a fusion rifle model
     * @param weaponFrame the fusion rifle frame name
     * @return a fusion rifle weapon model
     */
    private static Weapon buildFusionRifle(final String weaponFrame,
                                           final WeaponSkeleton skeleton, final Statement s) throws SQLException {
        final String sql =
                """
                SELECT * FROM FusionRifleSpecifics
                WHERE weapon_id = (
                    SELECT weapon_id
                    FROM weapons
                    WHERE weapon_type = 'FusionRifle' AND weapon_frame = '%s'
                )
                """.formatted(weaponFrame);
        s.execute(sql);
        final ResultSet r = s.getResultSet();

        return new FusionRifle(skeleton,
                r.getInt("charge_time"),
                r.getInt("bolt_count"),
                r.getInt("charge_recovery")
        );
    }

    /**
     * Constructs a bow model
     * @param weaponFrame the bow frame name
     * @return a bow weapon model
     */
    private static Weapon buildBow(final String weaponFrame,
                                   final WeaponSkeleton skeleton, final Statement s) {
        return new Bow(skeleton);
    }
}
