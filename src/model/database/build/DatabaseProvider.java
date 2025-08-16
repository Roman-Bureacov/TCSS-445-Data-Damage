package model.database.build;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that constructs the database, if it does not exist yet.
 * @author Roman Bureacov
 * @version August 2025
 */
public final class DatabaseProvider {
    private static final Logger LOGGER = Logger.getLogger("model.database.build.DatabaseProvider");

    private static final String WORKING_PATH = "src/model/database/build/";
    private static final String DB_NAME = "datadamage.db";
    private static final File INIT_SCRIPT = new File(getPath("CreateDatabase.sql"));
    private static final File DB_FILE = new File(getPath(DB_NAME));
    private static final String DB_URL = "jdbc:sqlite:" + DB_FILE.getPath();

    static {
        try {
            createDatabase();
            insertDatabase();
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to read/create file:\n" + e.getMessage());
            throw new RuntimeException(e);
        } catch (final SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to run SQL script:\n" + e.getMessage());
        }
    }

    private DatabaseProvider() {
        super();
    };

    /**
     * Gets the running database connection.
     * @return the connection to the database
     * @throws SQLException if the connection failed
     */
    public static Connection getConnection() throws SQLException {
        final Connection c;
        try {
             c = DriverManager.getConnection(DB_URL);
        } catch (final SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to establish connection:\n" + e.getMessage());
            throw new RuntimeException(e);
        }
        return c;
    }

    /**
     * Constructs the database, if it does not exist yet.
     * @throws IOException if it failed to read the init script
     * @throws SQLException if it failed to parse the init script
     */
    private static void createDatabase() throws IOException, SQLException {
        if (!DB_FILE.exists()) {
            final Connection c = getConnection();
            LOGGER.info("Creating database...");
            final String creationScript = Files.readString(INIT_SCRIPT.toPath());
            for (String statement : creationScript.split(";")) {
                final Statement s = c.createStatement();
                s.execute(statement);
            }
            LOGGER.info("Database creation complete!");
        }
    }

    /**
     * Inserts all the SQLite database entries
     * @throws IOException if it failed to read the scripts
     * @throws SQLException if it failed to parse the insertion script(s)
     */
    private static void insertDatabase() throws SQLException, IOException {
        final Connection c = getConnection();
        LOGGER.info("Inserting data");
        final Path[] insertionScriptPaths = {
                Path.of(getPath("InsertWeapons.sql")),
                Path.of(getPath("InsertFusionRifleSpecifics.sql")),
                Path.of(getPath("InsertPulseRifleSpecifics.sql")),
                Path.of(getPath("InsertWeaponAmmoTypes.sql")),
                Path.of(getPath("InsertWeaponInfo.sql")),
                Path.of(getPath("InsertWeaponSlotability.sql")),
                Path.of(getPath("InsertWeaponStats.sql")),
        };

        for (final Path p : insertionScriptPaths) {
            LOGGER.info("Running " + p);
            final String script = Files.readString(p);
            final Statement s = c.createStatement();
            s.execute(script);
        }

        LOGGER.info("Finished insertion");
    }

    /**
     * Translates the file name into a relative file path.
     * @param fileName the file name
     * @return the path to the file from the content source
     */
    private static String getPath(String fileName) {
        return Path.of(WORKING_PATH, fileName).toString();
    }
}
