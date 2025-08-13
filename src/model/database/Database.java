package model.database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;

import model.script.Weapon;

/**
 * Class that handles basic database operations
 * @author Roman Bureacov
 * @version July 2025
 */
public final class Database {
    private static final String DB_NAME = "datadamage.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_NAME;
    private static final String INIT_SCRIPT = "CreateMySQLDatabase.txt";

    private static Database instance;

    private Database() {
        createDatabaseIfNotExists();
    }

    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

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
