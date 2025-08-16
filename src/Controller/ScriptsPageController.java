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
            ResultSet rs = queryScripts();
            Connection conn = rs.getStatement().getConnection();

            try (rs; conn) {
                scriptsTable.getItems().clear();

                while (rs.next()) {
                    ScriptRow row = new ScriptRow();

                    row.setSaveName(rs.getString("save_name"));
                    row.setKineticWeaponFrame(rs.getString("kinetic_frame"));
                    row.setKineticWeaponType(rs.getString("kinetic_type"));
                    row.setEnergyWeaponFrame(rs.getString("energy_frame"));
                    row.setEnergyWeaponType(rs.getString("energy_type"));
                    row.setPowerWeaponFrame(rs.getString("power_frame"));
                    row.setPowerWeaponType(rs.getString("power_type"));
                    row.setAvgDPS(rs.getDouble("average_dps"));
                    row.setTotalDPS(rs.getInt("total_damage"));
                    row.setSaveDate(rs.getString("save_date"));

                    scriptsTable.getItems().add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ResultSet queryScripts() throws SQLException {
        StringBuilder baseSql = new StringBuilder("""
            SELECT
                s.save_name,
                k.frame AS kinetic_frame,
                k.weapon_type AS kinetic_type,
                e.frame AS energy_frame,
                e.weapon_type AS energy_type,
                p.frame AS power_frame,
                p.weapon_type AS power_type,
                s.average_dps,
                s.total_damage,
                s.save_date
            FROM sims s
            LEFT JOIN weapons k ON k.weapon_id = s.kinetic
            LEFT JOIN weapons e ON e.weapon_id = s.energy
            LEFT JOIN weapons p ON p.weapon_id = s.power;
        """);

        List<String> queries = new ArrayList<>();
        List<Object> params  = new ArrayList<>();

        if (saveName.getText() != null && !saveName.getText().trim().isEmpty()) {
            queries.add("""
                SELECT s.id
                FROM sims s
                WHERE s.save_name = ?
            """);
            params.add(saveName.getText().trim());
        }

        if (kineticFrame.getText() != null && !kineticFrame.getText().trim().isEmpty()) {
            queries.add("""
                SELECT s.id
                FROM sims s
                LEFT JOIN weapons w ON s.kinetic = w.weapons_id
                WHERE w.frame = ?
            """);
            params.add(kineticFrame.getText().trim());
        }

        if (kineticType.getText() != null && !kineticType.getText().trim().isEmpty()) {
            queries.add("""
                SELECT s.id
                FROM sims s
                LEFT JOIN weapons w ON s.kinetic = w.weapons_id
                WHERE w.weapon_type = ?
            """);
            params.add(kineticType.getText().trim());
        }

        if (energyFrame.getText() != null && !energyFrame.getText().trim().isEmpty()) {
            queries.add("""
                SELECT s.id
                FROM sims s
                LEFT JOIN weapons w ON s.energy = w.weapons_id
                WHERE w.frame = ?
            """);
            params.add(energyFrame.getText().trim());
        }

        if (energyType.getText() != null && !energyType.getText().trim().isEmpty()) {
            queries.add("""
                SELECT s.id
                FROM sims s
                LEFT JOIN weapons w ON s.energy = w.weapons_id
                WHERE w.weapon_type = ?
            """);
            params.add(energyType.getText().trim());
        }

        if (powerFrame.getText() != null && !powerFrame.getText().trim().isEmpty()) {
            queries.add("""
                SELECT s.id
                FROM sims s
                LEFT JOIN weapons w ON s.power = w.weapons_id
                WHERE w.frame = ?
            """);
            params.add(powerFrame.getText().trim());
        }

        if (powerType.getText() != null && !powerType.getText().trim().isEmpty()) {
            queries.add("""
                SELECT s.id
                FROM sims s
                LEFT JOIN weapons w ON s.power = w.weapons_id
                WHERE w.weapon_type = ?
            """);
            params.add(powerType.getText().trim());
        }

        if(avgDPS.getText() != null && !avgDPS.getText().trim().isEmpty()) {
            queries.add("""
                SELECT s.id
                FROM sims s
                WHERE average_dps = ?
            """);
            params.add(Double.parseDouble(avgDPS.getText().trim()));
        }

        if(totalDamage.getText() != null && !totalDamage.getText().trim().isEmpty()) {
            queries.add("""
                SELECT s.id
                FROM sims s
                WHERE total_damage = ?
            """);
            params.add(Integer.parseInt(totalDamage.getText().trim()));
        }

//        if(saveDate.getText() != null && !saveDate.getText().trim().isEmpty()) {
//            queries.add("""
//                SELECT s.id
//                FROM sims s
//                WHERE save_date = ?
//            """);
//            params.add(saveDate.getText().trim());
//        }

        if (queries.isEmpty()) {
            return Database.getInstance().executeQuery(baseSql.toString());
        }

        List<Set<Integer>> idSets = new ArrayList<>();
        for (int i = 0; i < queries.size(); i++) {
            try (ResultSet rs = Database.getInstance().executeQuery(queries.get(i), params.get(i))) {
                Connection c = rs.getStatement().getConnection();
                Set<Integer> s = new HashSet<>();
                while (rs.next()) s.add(rs.getInt(1));
                c.close();
                idSets.add(s);
            }
        }

        for (Set<Integer> s : idSets) {
            if (s.isEmpty()) {
                return Database.getInstance().executeQuery(baseSql + " WHERE 1=0");
            }
        }

        idSets.sort((a, b) -> Integer.compare(a.size(), b.size()));
        Set<Integer> finalIds = new LinkedHashSet<>(idSets.get(0));
        for (int i = 1; i < idSets.size(); i++) {
            finalIds.retainAll(idSets.get(i));
            if (finalIds.isEmpty()) {
                return Database.getInstance().executeQuery(baseSql + " WHERE 1=0");
            }
        }

        StringBuilder finalSql = new StringBuilder(baseSql);
        finalSql.append(" WHERE s.id IN (");
        int n = finalIds.size();
        for (int i = 0; i < n; i++) {
            finalSql.append("?");
            if (i < n - 1) finalSql.append(", ");
        }
        finalSql.append(")");

        Object[] bind = finalIds.stream().map(Integer::valueOf).toArray();
        return Database.getInstance().executeQuery(finalSql.toString(), bind);
    }
}
