package model.database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

import org.sqlite.SQLiteDataSource;

//import model.script.Weapon;

/**
 * Class that handles basic database operations
 * @author Roman Bureacov
 * @version July 2025
 */
public final class Database {
    private static final String DB_NAME = "datadamage.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_NAME;
    private static final String INIT_SCRIPT = "CreateMySQLDatabase.txt";
    private static final SQLiteDataSource SOURCE;

    static {
        SOURCE = new SQLiteDataSource();
        SOURCE.setUrl(DB_URL);
    }

    private static Database instance;

    private Database() {
        createDatabaseIfNotExists();
    }

    /**
     * Returns the singleton database manager instance
     * @return the database singleton
     */
    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    /**
     * Constructs the database if it does not exist
     */
    private void createDatabaseIfNotExists() {
        try {
            File dbFile = new File(DB_NAME);
            if (!dbFile.exists()) {
                try (Connection conn = DriverManager.getConnection(DB_URL)) {
                    if (conn != null) {
                        System.out.println("New database created: " + DB_NAME);
                        runInitScript(conn);
                    }
                }
            } else {
                System.out.println("Database already exists: " + DB_NAME);
            }
        } catch (SQLException e) {
            System.out.println("Error creating database: " + e.getMessage());
        }
    }

    /**
     * 
     * @param conn
     */
    private void runInitScript(Connection conn) {
        try {
            String sql = Files.readString(Paths.get(INIT_SCRIPT));

            String[] statements = sql.split(";");
            try (Statement stmt = conn.createStatement()) {
                for (String s : statements) {
                    String trimmed = s.trim();
                    if (!trimmed.isEmpty()) {
                        stmt.execute(trimmed);
                    }
                }
            }
            System.out.println("Initialization script executed.");
        } catch (IOException e) {
            System.out.println("Error reading init script: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error executing init script: " + e.getMessage());
        }
    }

    /**
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public ResultSet executeQuery(String sql, Object... params) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        PreparedStatement preparedStatementstmt = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            preparedStatementstmt.setObject(i + 1, params[i]);
        }
        return preparedStatementstmt.executeQuery();
    }

    /**
     * Builds the appropriate weapon from the database.
     * @param weaponType the type name of the weapon
     * @param weaponFrame the frame name of the weapon
     * @return the built weapon object
     */
    public static Weapon buildWeapon(final String weaponFrame, final String weaponType) throws SQLException {
        final String sql =
                """
                SELECT * FROM weapons NATURAL JOIN weapon_stats USING weapon_id
                """;

        ResultSet r;

        final Connection c = SOURCE.getConnection();
        final Statement s = c.createStatement();
        r = s.executeQuery(sql);

        final WeaponSkeleton skeleton = buildWeaponSkeleton(r);

        return switch(weaponType) {
            case "Bow" -> buildBow(weaponFrame, skeleton, s);
            case "FusionRifle" -> buildFusionRifle(weaponFrame, skeleton, s);
            case "PulseRifle" -> buildPulseRifle(weaponFrame, skeleton, s);
            case "Shotgun" -> buildShotgun(skeleton);
            case "Sword" -> buildSword(skeleton);
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

        skeleton.theReservesMax = r.getInt("reserves");
        skeleton.theRPM = r.getInt("fire_rate");
        skeleton.theReloadSpeed = r.getInt("reload_speed");
        skeleton.theMagazineMax = r.getInt("magazine");
        skeleton.theStowSpeed = r.getInt("stow_speed");
        skeleton.theReadySpeed = r.getInt("ready_speed");
        skeleton.theBodyDamage = r.getInt("body_damage");
        skeleton.thePrecisionDamage = r.getInt("precision_damage");

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
