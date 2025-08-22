package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.database.Database;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * The controller for the script page.
 *
 * @author Kaleb Anagnostou
 * @version 2025 August
 */
public class ScriptsPageController {
    /** Script save name to search for in the database. */
    @FXML private TextField saveName;
    /** Kinetic weapon frame to search for in the database. */
    @FXML private TextField kineticFrame;
    /** Kinetic weapon type to search for in the database. */
    @FXML private TextField kineticType;
    /** Energy weapon frame to search for in the database. */
    @FXML private TextField energyFrame;
    /** Energy weapon type to search for in the database. */
    @FXML private TextField energyType;
    /** Power weapon frame to search for in the database. */
    @FXML private TextField powerFrame;
    /** Power weapon type to search for in the database. */
    @FXML private TextField powerType;
    /** Average DPS to search for in the database. */
    @FXML private TextField avgDPS;
    /** Cumulative damage to search for in the database. */
    @FXML private TextField totalDamage;
    /** The table displaying the query results. */
    @FXML private TableView<ScriptRow> scriptsTable;
    /** Column that will display the sim's script id. */
    @FXML private TableColumn<ScriptRow, Integer> colScriptID;
    /** Column that will display the sim's saved name. */
    @FXML private TableColumn<ScriptRow, String> colSaveName;
    /** Column that will display the sim's kinetic frame. */
    @FXML private TableColumn<ScriptRow, String> colKineticFrame;
    /** Column that will display the sim's kinetic type. */
    @FXML private TableColumn<ScriptRow, String> colKineticType;
    /** Column that will display the sim's energy frame. */
    @FXML private TableColumn<ScriptRow, String> colEnergyFrame;
    /** Column that will display the sim's energy type. */
    @FXML private TableColumn<ScriptRow, String> colEnergyType;
    /** Column that will display the sim's power frame. */
    @FXML private TableColumn<ScriptRow, String> colPowerFrame;
    /** Column that will display the sim's power type. */
    @FXML private TableColumn<ScriptRow, String> colPowerType;
    /** Column that will display the sim's average DPS. */
    @FXML private TableColumn<ScriptRow, Double> colAvgDPS;
    /** Column that will display the sim's cumulative damage. */
    @FXML private TableColumn<ScriptRow, Integer> colTotalDamage;
    /** Column that will display the sim's save date. */
    @FXML private TableColumn<ScriptRow, String> colDate;

    /**
     * Sets up the property value factories so that the table will update when it gets a new result set. Calls the
     * search with no parameters to populate the table without any filter queries.
     */
    public void initialize() {
        colScriptID.setCellValueFactory(new PropertyValueFactory<>("scriptId"));
        colSaveName.setCellValueFactory(new PropertyValueFactory<>("saveName"));
        colKineticFrame.setCellValueFactory(new PropertyValueFactory<>("kineticWeaponFrame"));
        colKineticType.setCellValueFactory(new PropertyValueFactory<>("kineticWeaponType"));
        colEnergyFrame.setCellValueFactory(new PropertyValueFactory<>("energyWeaponFrame"));
        colEnergyType.setCellValueFactory(new PropertyValueFactory<>("energyWeaponType"));
        colPowerFrame.setCellValueFactory(new PropertyValueFactory<>("powerWeaponFrame"));
        colPowerType.setCellValueFactory(new PropertyValueFactory<>("powerWeaponType"));
        colAvgDPS.setCellValueFactory(new PropertyValueFactory<>("avgDPS"));
        colTotalDamage.setCellValueFactory(new PropertyValueFactory<>("totalDPS"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("saveDate"));

        onSearch();
    }

    /**
     * Opens a DB connection and queries the DB. Sets the results set entries to be the column data in the table.
     */
    public void onSearch() {
        try {
            ResultSet rs = queryScripts();
            Connection conn = rs.getStatement().getConnection();

            try (rs; conn) {
                scriptsTable.getItems().clear();

                while (rs.next()) {
                    ScriptRow row = new ScriptRow();

                    row.setScriptId(rs.getInt("sim_id"));
                    row.setSaveName(rs.getString("sim_name"));
                    row.setKineticWeaponFrame(rs.getString("kinetic_frame"));
                    row.setKineticWeaponType(rs.getString("kinetic_type"));
                    row.setMyEnergyWeaponFrame(rs.getString("energy_frame"));
                    row.setEnergyWeaponType(rs.getString("energy_type"));
                    row.setPowerWeaponFrame(rs.getString("power_frame"));
                    row.setPowerWeaponType(rs.getString("power_type"));
                    row.setAvgDPS(rs.getDouble("average_dps"));
                    row.setMyTotalDPS(rs.getInt("total_damage"));
                    row.setSaveDate(rs.getString("save_date"));

                    scriptsTable.getItems().add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs a query and gets back the necessary data to contstruct the table.
     */
    private ResultSet queryScripts() throws SQLException {
        StringBuilder baseSql = new StringBuilder("""
            SELECT
                s.sim_id,
                s.sim_name,
                k.weapon_frame AS kinetic_frame,
                k.weapon_type AS kinetic_type,
                e.weapon_frame AS energy_frame,
                e.weapon_type AS energy_type,
                p.weapon_frame AS power_frame,
                p.weapon_type AS power_type,
                ROUND(sm.average_dps, 2) AS average_dps,
                sm.total_damage,
                s.save_date
            FROM sims s
            Left JOIN sims_meta sm ON sm.script_id = s.sim_id
            LEFT JOIN weapons k ON k.weapon_id = sm.kinetic
            LEFT JOIN weapons e ON e.weapon_id = sm.energy
            LEFT JOIN weapons p ON p.weapon_id = sm.power
        """);

        List<String> queries = new ArrayList<>();
        List<Object> params  = new ArrayList<>();

        if (saveName.getText() != null && !saveName.getText().trim().isEmpty()) {
            queries.add("""
                SELECT s.sim_id
                FROM sims s
                WHERE s.sim_name = ?
            """);
            params.add(saveName.getText().trim());
        }

        if (kineticFrame.getText() != null && !kineticFrame.getText().trim().isEmpty()) {
            queries.add("""
                SELECT sm.script_id
                FROM sims_meta sm
                LEFT JOIN weapons w ON sm.kinetic = w.weapon_id
                WHERE w.weapon_frame = ?
            """);
            params.add(kineticFrame.getText().trim());
        }

        if (kineticType.getText() != null && !kineticType.getText().trim().isEmpty()) {
            queries.add("""
                SELECT sm.script_id
                FROM sims_meta sm
                LEFT JOIN weapons w ON sm.kinetic = w.weapon_id
                WHERE w.weapon_type = ?
            """);
            params.add(kineticType.getText().trim());
        }

        if (energyFrame.getText() != null && !energyFrame.getText().trim().isEmpty()) {
            queries.add("""
                SELECT sm.script_id
                FROM sims_meta sm
                LEFT JOIN weapons w ON sm.energy = w.weapon_id
                WHERE w.weapon_frame = ?
            """);
            params.add(energyFrame.getText().trim());
        }

        if (energyType.getText() != null && !energyType.getText().trim().isEmpty()) {
            queries.add("""
                SELECT sm.script_id
                FROM sims_meta sm
                LEFT JOIN weapons w ON sm.energy = w.weapon_id
                WHERE w.weapon_type = ?
            """);
            params.add(energyType.getText().trim());
        }

        if (powerFrame.getText() != null && !powerFrame.getText().trim().isEmpty()) {
            queries.add("""
                SELECT sm.script_id
                FROM sims_meta sm
                LEFT JOIN weapons w ON sm.power = w.weapon_id
                WHERE w.weapon_frame = ?
            """);
            params.add(powerFrame.getText().trim());
        }

        if (powerType.getText() != null && !powerType.getText().trim().isEmpty()) {
            queries.add("""
                SELECT sm.script_id
                FROM sims_meta sm
                LEFT JOIN weapons w ON sm.power = w.weapon_id
                WHERE w.weapon_type = ?
            """);
            params.add(powerType.getText().trim());
        }

        if(avgDPS.getText() != null && !avgDPS.getText().trim().isEmpty()) {
            queries.add("""
                SELECT sm.script_id
                FROM sims_meta sm
                WHERE average_dps > ?
            """);
            params.add(Double.parseDouble(avgDPS.getText().trim()));
        }

        if(totalDamage.getText() != null && !totalDamage.getText().trim().isEmpty()) {
            queries.add("""
                SELECT sm.script_id
                FROM sims_meta sm
                WHERE total_damage > ?
            """);
            params.add(Integer.parseInt(totalDamage.getText().trim()));
        }

        if (queries.isEmpty()) {
            return Database.getInstance().executeQuery(baseSql.toString());
        }

        List<Set<Integer>> idSets = new ArrayList<>();
        for (int i = 0; i < queries.size(); i++) {
            ResultSet rs = Database.getInstance().executeQuery(queries.get(i), params.get(i));
            Set<Integer> s = new HashSet<>();
            while (rs.next()) {
                s.add(rs.getInt(1));
            }
            idSets.add(s);
        }

        for (Set<Integer> s : idSets) {
            if (s.isEmpty()) {
                return Database.getInstance().executeQuery(baseSql + " WHERE 1=0");
            }
        }

        idSets.sort(Comparator.comparingInt(Set::size));
        Set<Integer> finalIds = new LinkedHashSet<>(idSets.getFirst());
        for (int i = 1; i < idSets.size(); i++) {
            finalIds.retainAll(idSets.get(i));
            if (finalIds.isEmpty()) {
                return Database.getInstance().executeQuery(baseSql + " WHERE 1=0");
            }
        }

        StringBuilder finalSql = new StringBuilder(baseSql);
        finalSql.append(" WHERE s.sim_id IN (");
        int n = finalIds.size();
        for (int i = 0; i < n; i++) {
            finalSql.append("?");
            if (i < n - 1) finalSql.append(", ");
        }
        finalSql.append(")");

        Object[] paramlist = finalIds.toArray();
        return Database.getInstance().executeQuery(finalSql.toString(), paramlist);
    }
}