package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.database.Database;
import model.database.build.DatabaseProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ScriptsPageController {
    @FXML public TextField saveName;
    @FXML public TextField kineticFrame;
    @FXML public TextField kineticType;
    @FXML public TextField energyFrame;
    @FXML public TextField energyType;
    @FXML public TextField powerFrame;
    @FXML public TextField powerType;
    @FXML public TextField avgDPS;
    @FXML public TextField totalDamage;
    @FXML public TextField saveDate;

    @FXML public TableView<ScriptRow> scriptsTable;
    @FXML public TableColumn<ScriptRow, String> colSaveName;
    @FXML public TableColumn<ScriptRow, String> colKineticFrame;
    @FXML public TableColumn<ScriptRow, String> colKineticType;
    @FXML public TableColumn<ScriptRow, String> colEnergyFrame;
    @FXML public TableColumn<ScriptRow, String> colEnergyType;
    @FXML public TableColumn<ScriptRow, String> colPowerFrame;
    @FXML public TableColumn<ScriptRow, String> colPowerType;
    @FXML public TableColumn<ScriptRow, Double> colAvgDPS;
    @FXML public TableColumn<ScriptRow, Integer> colTotalDamage;
    @FXML public TableColumn<ScriptRow, String> colDate;

    public void initialize() {
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

    public void onSearch() {
        try {
            scriptsTable.getItems().setAll(queryScripts());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<ScriptRow> queryScripts() throws SQLException {
        StringBuilder baseSql = new StringBuilder("""
            SELECT
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

        List<String> where = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        // Filters (all optional)
        if (!isBlank(saveName)) {
            where.add("s.sim_name = ?");
            params.add(saveName.getText().trim());
        }

        if (!isBlank(kineticFrame)) {
            where.add("k.weapon_frame = ?");
            params.add(kineticFrame.getText().trim());
        }

        if (!isBlank(kineticType)) {
            where.add("k.weapon_type = ?");
            params.add(kineticType.getText().trim());
        }

        if (!isBlank(energyFrame)) {
            where.add("e.weapon_frame = ?");
            params.add(energyFrame.getText().trim());
        }

        if (!isBlank(energyType)) {
            where.add("e.weapon_type = ?");
            params.add(energyType.getText().trim());
        }

        if (!isBlank(powerFrame)) {
            where.add("p.weapon_frame = ?");
            params.add(powerFrame.getText().trim());
        }

        if (!isBlank(powerType)) {
            where.add("p.weapon_type = ?");
            params.add(powerType.getText().trim());
        }

        if (!isBlank(avgDPS)) {
            // equality on doubles can be harsh; if you prefer a threshold use >=
            where.add("sm.average_dps >= ?");
            params.add(parseDouble(avgDPS));
        }

        if (!isBlank(totalDamage)) {
            where.add("sm.total_damage >= ?");
            params.add(parseInt(totalDamage));
        }

        if (!isBlank(saveDate)) {
            // exact date match (assuming save_date is ISO timestamp)
            where.add("DATE(s.save_date) = DATE(?)");
            params.add(saveDate.getText().trim());
        }

        if (!where.isEmpty()) {
            baseSql.append(" WHERE ").append(String.join(" AND ", where));
        }
        baseSql.append(" ORDER BY s.save_date DESC");

        try (Connection conn = DatabaseProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(baseSql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                List<ScriptRow> rows = new ArrayList<>();
                while (rs.next()) {
                    ScriptRow row = new ScriptRow();
                    row.setSaveName(rs.getString("sim_name"));
                    row.setKineticWeaponFrame(rs.getString("kinetic_frame"));
                    row.setKineticWeaponType(rs.getString("kinetic_type"));
                    row.setEnergyWeaponFrame(rs.getString("energy_frame"));
                    row.setEnergyWeaponType(rs.getString("energy_type"));
                    row.setPowerWeaponFrame(rs.getString("power_frame"));
                    row.setPowerWeaponType(rs.getString("power_type"));
                    row.setAvgDPS(rs.getDouble("average_dps"));
                    row.setTotalDPS(rs.getInt("total_damage"));
                    row.setSaveDate(rs.getString("save_date"));
                    rows.add(row);
                }
                return rows;
            }
        }
    }

    private static boolean isBlank(TextField tf) {
        return tf == null || tf.getText() == null || tf.getText().trim().isEmpty();
    }
    private static int parseInt(TextField tf)    { return Integer.parseInt(tf.getText().trim()); }
    private static double parseDouble(TextField tf) { return Double.parseDouble(tf.getText().trim()); }
}
