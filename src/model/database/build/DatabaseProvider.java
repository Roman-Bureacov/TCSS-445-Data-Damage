package model.database.build;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that constructs the database, if it does not exist yet.
 * @author Roman Bureacov
 * @version August 2025
 */
public class DatabaseProvider {
    private static final Logger LOGGER = Logger.getLogger("model.database.build.DatabaseProvider");

    private static final String DB_NAME = "datadamage.db";
    private static final File INIT_SCRIPT = new File("src/model/database/build/CreateDatabase.sql");
    private static final File DB_FILE = new File("src/model/database/build/" + DB_NAME);
    private static final String DB_URL = "jdbc:sqlite:" + DB_FILE.getPath();

    static {
        try {
            createDatabase();
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to read/create file:\n" + e.getMessage());
            throw new RuntimeException(e);
        } catch (final SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to run SQL script:\n" + e.getMessage());
        }
    }

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
}
