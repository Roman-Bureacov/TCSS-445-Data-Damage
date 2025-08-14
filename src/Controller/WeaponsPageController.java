package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import model.database.Database;
import java.sql.*;
import java.util.*;

public class WeaponsPageController {
    public TextField typeField;
    public TextField frameField;
    public CheckBox energyField;
    public CheckBox kineticField;
    public CheckBox powerField;
    public TextField trueDPSField;
    public TextField sustainedDPSField;
    public TextField theoreticalTotalDamageField;
    public TextField oneMagDamageField;
    public TextField magCapacityField;
    public TextField reservesMagazineField;
    public TextField fireRateField;
    public TextField reloadSpeedField;
    public TextField timeToEmptyField;
    public TextField bodyDamageField;
    public TextField precisionDamageField;
    @FXML public TableColumn<WeaponRow, Image> colImage;
    @FXML public TableColumn<WeaponRow, String> colDescription;
    @FXML private TableColumn<WeaponRow, Integer> colOneMag;
    @FXML private TableColumn<WeaponRow, Integer> colTheo;
    @FXML private TableColumn<WeaponRow, Double>  colSust;
    @FXML private TableColumn<WeaponRow, Double>  colTrue;
    @FXML private TableView<WeaponRow> weaponsTable;
    @FXML private TableColumn<WeaponRow,String>  colType, colFrame;
    @FXML private TableColumn<WeaponRow,Integer> colReserve, colMagazine, colFireRate, colBody, colPrecision;
    @FXML private TableColumn<WeaponRow,Double>  colReload;
    @FXML private TableColumn<WeaponRow,Boolean> colKinetic, colEnergy, colPower;

    @FXML
    public void initialize() {
        colOneMag.setCellValueFactory(new PropertyValueFactory<>("oneMagDamage"));
        colTheo.setCellValueFactory(new PropertyValueFactory<>("theoreticalTotalDamage"));
        colSust.setCellValueFactory(new PropertyValueFactory<>("sustainedDps"));
        colTrue.setCellValueFactory(new PropertyValueFactory<>("trueDps"));
        colType.setCellValueFactory(new PropertyValueFactory<>("weaponType"));
        colFrame.setCellValueFactory(new PropertyValueFactory<>("frame"));
        colReserve.setCellValueFactory(new PropertyValueFactory<>("reserve"));
        colMagazine.setCellValueFactory(new PropertyValueFactory<>("magazine"));
        colFireRate.setCellValueFactory(new PropertyValueFactory<>("fireRate"));
        colReload.setCellValueFactory(new PropertyValueFactory<>("reload"));
        colBody.setCellValueFactory(new PropertyValueFactory<>("body"));
        colPrecision.setCellValueFactory(new PropertyValueFactory<>("precision"));
        colKinetic.setCellValueFactory(new PropertyValueFactory<>("kinetic"));
        colEnergy.setCellValueFactory(new PropertyValueFactory<>("energy"));
        colPower.setCellValueFactory(new PropertyValueFactory<>("power"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("weapon_disc"));
        colImage.setCellValueFactory(new PropertyValueFactory<>("image"));

        onSearch();
    }

    @FXML
    public void onSearch() {
        try {
            ResultSet rs = queryWeapons();
            Connection conn = rs.getStatement().getConnection();

            try (rs; conn) {

                weaponsTable.getItems().clear();

                while (rs.next()) {
                    WeaponRow row = new WeaponRow();

                    row.setWeaponType(rs.getString("weapon_type"));
                    row.setFrame(rs.getString("frame"));
                    row.setReserve(rs.getInt("reserve"));
                    row.setMagazine(rs.getInt("magazine"));
                    row.setFireRate(rs.getInt("fire_rate"));
                    row.setReload(rs.getDouble("reload_speed"));
                    row.setBody(rs.getInt("body_damage"));
                    row.setPrecision(rs.getInt("precision_damage"));
                    row.setKinetic(rs.getInt("in_kinetic") == 1);
                    row.setEnergy(rs.getInt("in_energy")  == 1);
                    row.setPower(rs.getInt("in_power")   == 1);
                    row.setOneMagDamage(rs.getInt("one_mag_damage"));
                    row.setTheoreticalTotalDamage(rs.getInt("theoretical_total_damage"));
                    row.setSustainedDps(rs.getDouble("sustained_dps"));
                    row.setTrueDps(rs.getDouble("true_dps"));
                    row.setDescription(rs.getString("weapon_disc"));

                    weaponsTable.getItems().add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ResultSet queryWeapons() throws SQLException {
        StringBuilder baseSql = new StringBuilder("""
            SELECT
                w.weapon_id, w.weapon_type, w.frame,
                wi.weapon_disc, wi.image,
                d.reserve, d.fire_rate, d.reload_speed, d.magazine,
                d.body_damage, d.precision_damage,
                s.in_kinetic, s.in_energy, s.in_power,
                (d.magazine * d.precision_damage) AS one_mag_damage,
                (d.reserve * d.precision_damage) AS theoretical_total_damage,
                CASE WHEN d.fire_rate IS NULL OR d.fire_rate = 0 THEN NULL
                     ELSE ROUND ((d.magazine * d.precision_damage) / (((d.magazine - 1) * (60.0 / d.fire_rate)) + d.reload_speed ),1)
                END AS sustained_dps,
                CASE WHEN d.fire_rate IS NULL OR d.fire_rate = 0 THEN NULL
                     ELSE ROUND ((d.magazine * d.precision_damage) / (d.magazine * (60.0 / d.fire_rate)), 1) 
                END AS true_dps 
            FROM weapons w
            LEFT JOIN damage d ON d.weapon_id = w.weapon_id
            LEFT JOIN slotability s ON s.weapon_id = w.weapon_id
            LEFT JOIN weapon_info wi ON wi.weapon_id = w.weapon_id
        """);

        List<String> queries = new ArrayList<>();
        List<Object> params  = new ArrayList<>();

        if (typeField.getText() != null && !typeField.getText().trim().isEmpty()) {
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                WHERE w.weapon_type = ?
            """);
            params.add(typeField.getText().trim());
        }

        if (frameField.getText() != null && !frameField.getText().trim().isEmpty()) {
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                WHERE w.frame = ?
            """);
            params.add(frameField.getText().trim());
        }

        if (energyField.isSelected()) {
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                LEFT JOIN slotability s ON s.weapon_id = w.weapon_id
                WHERE s.in_energy = ?
            """);
            params.add(1);
        }
        if (kineticField.isSelected()) {
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                LEFT JOIN slotability s ON s.weapon_id = w.weapon_id
                WHERE s.in_kinetic = ?
            """);
            params.add(1);
        }
        if (powerField.isSelected()) {
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                LEFT JOIN slotability s ON s.weapon_id = w.weapon_id
                WHERE s.in_power = ?
            """);
            params.add(1);
        }

        if (magCapacityField.getText() != null && !magCapacityField.getText().trim().isEmpty()) {
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                LEFT JOIN damage d ON d.weapon_id = w.weapon_id
                WHERE d.magazine > ?
            """);
            params.add(Integer.parseInt(magCapacityField.getText().trim()));
        }
        if (reservesMagazineField.getText() != null && !reservesMagazineField.getText().trim().isEmpty()) {
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                LEFT JOIN damage d ON d.weapon_id = w.weapon_id
                WHERE d.reserve > ?
            """);
            params.add(Integer.parseInt(reservesMagazineField.getText().trim()));
        }
        if (fireRateField.getText() != null && !fireRateField.getText().trim().isEmpty()) {
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                LEFT JOIN damage d ON d.weapon_id = w.weapon_id
                WHERE d.fire_rate > ?
            """);
            params.add(Integer.parseInt(fireRateField.getText().trim()));
        }
        if (reloadSpeedField.getText() != null && !reloadSpeedField.getText().trim().isEmpty()) {
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                LEFT JOIN damage d ON d.weapon_id = w.weapon_id
                WHERE d.reload_speed < ?
            """);
            params.add(Double.parseDouble(reloadSpeedField.getText().trim()));
        }
        if (bodyDamageField.getText() != null && !bodyDamageField.getText().trim().isEmpty()) {
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                LEFT JOIN damage d ON d.weapon_id = w.weapon_id
                WHERE d.body_damage > ?
            """);
            params.add(Integer.parseInt(bodyDamageField.getText().trim()));
        }
        if (precisionDamageField.getText() != null && !precisionDamageField.getText().trim().isEmpty()) {
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                LEFT JOIN damage d ON d.weapon_id = w.weapon_id
                WHERE d.precision_damage > ?
            """);
            params.add(Integer.parseInt(precisionDamageField.getText().trim()));
        }

        if(trueDPSField.getText() != null && !trueDPSField.getText().trim().isEmpty()) {
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                LEFT JOIN damage d ON d.weapon_id = w.weapon_id
                WHERE ((d.magazine * d.precision_damage) / ( d.magazine * (60.0 / d.fire_rate) ) > ?
            """);
            params.add(Double.parseDouble(trueDPSField.getText().trim()));
        }

        if(sustainedDPSField.getText() != null && !sustainedDPSField.getText().trim().isEmpty()) {
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                LEFT JOIN damage d ON d.weapon_id = w.weapon_id
                WHERE (d.magazine * d.precision_damage) / (((d.magazine - 1) * (60.0 / d.fire_rate)) + d.reload_speed)
            """);
            params.add(Double.parseDouble(sustainedDPSField.getText().trim()));
        }

        if(theoreticalTotalDamageField.getText() != null && !theoreticalTotalDamageField.getText().trim().isEmpty()) {
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                LEFT JOIN damage d ON d.weapon_id = w.weapon_id
                WHERE (d.reserve * d.precision_damage) > ?
            """);
            params.add(Double.parseDouble(theoreticalTotalDamageField.getText().trim()));
        }

        if(oneMagDamageField.getText() != null && !oneMagDamageField.getText().trim().isEmpty()) {
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                LEFT JOIN damage d ON d.weapon_id = w.weapon_id
                WHERE (d.magazine * d.precision_damage) > ?
            """);
            params.add(Double.parseDouble(oneMagDamageField.getText().trim()));
        }

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
        finalSql.append(" WHERE w.weapon_id IN (");
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
