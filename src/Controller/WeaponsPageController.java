package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.database.Database;
import model.database.build.DatabaseProvider;

import java.sql.*;
import java.util.*;

public class WeaponsPageController {
    public TextField typeField;
    public TextField frameField;
    @FXML private ToggleGroup ammoGroup;
    public int ammoType;
    public RadioButton primaryRadio;
    public RadioButton specialRadio;
    public RadioButton heavyRadio;
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
    @FXML public TableColumn<WeaponRow, String> colDescription;
    @FXML private TableColumn<WeaponRow, Integer> colOneMag;
    @FXML private TableColumn<WeaponRow, Integer> colTheo;
    @FXML private TableColumn<WeaponRow, Double>  colSust;
    @FXML private TableColumn<WeaponRow, Double>  colTrue;
    @FXML private TableView<WeaponRow> weaponsTable;
    @FXML private TableColumn<WeaponRow,String>  colType, colFrame;
    @FXML private TableColumn<WeaponRow,Integer> colReserves, colMagazine, colFireRate, colBody, colPrecision;
    @FXML private TableColumn<WeaponRow,Double>  colReload;
    @FXML private TableColumn<WeaponRow,String> colAmmoType;

    @FXML
    public void initialize() throws SQLException {
        ammoType = 0;
        primaryRadio.setToggleGroup(ammoGroup);
        specialRadio.setToggleGroup(ammoGroup);
        heavyRadio.setToggleGroup(ammoGroup);
        colOneMag.setCellValueFactory(new PropertyValueFactory<>("oneMagDamage"));
        colTheo.setCellValueFactory(new PropertyValueFactory<>("theoreticalTotalDamage"));
        colSust.setCellValueFactory(new PropertyValueFactory<>("sustainedDps"));
        colTrue.setCellValueFactory(new PropertyValueFactory<>("trueDps"));
        colType.setCellValueFactory(new PropertyValueFactory<>("weaponType"));
        colFrame.setCellValueFactory(new PropertyValueFactory<>("frame"));
        colReserves.setCellValueFactory(new PropertyValueFactory<>("reserves"));
        colMagazine.setCellValueFactory(new PropertyValueFactory<>("magazine"));
        colFireRate.setCellValueFactory(new PropertyValueFactory<>("fireRate"));
        colReload.setCellValueFactory(new PropertyValueFactory<>("reload"));
        colBody.setCellValueFactory(new PropertyValueFactory<>("body"));
        colPrecision.setCellValueFactory(new PropertyValueFactory<>("precision"));
        colAmmoType.setCellValueFactory(new PropertyValueFactory<>("ammo"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("weapon_desc"));

        onSearch();
    }

    @FXML
    public void onSearch() throws SQLException {
        weaponsTable.getItems().setAll(this.queryWeapons());
    }

    private List<WeaponRow> queryWeapons() throws SQLException {
        StringBuilder sql = new StringBuilder("""
        SELECT
            w.weapon_id, w.weapon_type, w.weapon_frame,
            wi.weapon_desc,
            ws.reserves, ws.fire_rate, ws.reload_speed, ws.magazine,
            ws.body_damage, ws.precision_damage,
            at.ammo,
            (ws.magazine * ws.precision_damage) AS one_mag_damage,
            (ws.reserves * ws.precision_damage) AS theoretical_total_damage,
            CASE WHEN ws.fire_rate IS NULL OR ws.fire_rate = 0 THEN NULL
                 ELSE ROUND((ws.magazine * ws.precision_damage) /
                            (((ws.magazine - 1) * (60.0 / ws.fire_rate)) + ws.reload_speed), 1)
            END AS sustained_dps,
            CASE WHEN ws.fire_rate IS NULL OR ws.fire_rate = 0 THEN NULL
                 ELSE ROUND((ws.magazine * ws.precision_damage) /
                            (ws.magazine * (60.0 / ws.fire_rate)), 1)
            END AS true_dps
        FROM weapons w
        LEFT JOIN weapon_stats ws ON ws.weapon_id = w.weapon_id
        LEFT JOIN weapon_ammo  wa ON wa.weapon_id = w.weapon_id
        LEFT JOIN ammo_types   at ON at.ammo_id = wa.ammo_id
        LEFT JOIN weapon_info  wi ON wi.weapon_id = w.weapon_id
    """);

        List<String> where = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        // Simple filters
        if (!isBlank(typeField)) { where.add("w.weapon_type = ?"); params.add(typeField.getText().trim()); }
        if (!isBlank(frameField)) { where.add("w.weapon_frame = ?"); params.add(frameField.getText().trim()); }

        // Ammo filter (doesn't break LEFT JOINs)
        if (ammoType != 0) {
            where.add("""
            EXISTS (SELECT 1 FROM weapon_ammo wa2
                    WHERE wa2.weapon_id = w.weapon_id AND wa2.ammo_id = ?)
        """);
            params.add(ammoType);
        }

        // Stat filters (note: WHERE on ws.* effectively turns the LEFT JOIN into INNER for those rows)
        if (!isBlank(magCapacityField)) {
            where.add("ws.magazine > ?");
            params.add(parseInt(magCapacityField));
        }

        if (!isBlank(reservesMagazineField)) {
            where.add("ws.reserves  > ?");
            params.add(parseInt(reservesMagazineField));
        }

        if (!isBlank(fireRateField)) {
            where.add("ws.fire_rate > ?");
            params.add(parseInt(fireRateField));
        }

        if (!isBlank(reloadSpeedField)) {
            where.add("ws.reload_speed < ?");
            params.add(parseDouble(reloadSpeedField));
        }

        if (!isBlank(bodyDamageField)) {
            where.add("ws.body_damage > ?");
            params.add(parseInt(bodyDamageField));
        }

        if (!isBlank(precisionDamageField)) {
            where.add("ws.precision_damage > ?");
            params.add(parseInt(precisionDamageField));
        }

        // Derived metrics in WHERE (guard against NULL/0 fire_rate)
        if (!isBlank(trueDPSField)) {
            where.add("(ws.fire_rate > 0 AND (ws.magazine * ws.precision_damage) / (ws.magazine * (60.0 / ws.fire_rate)) > ?)");
            params.add(parseDouble(trueDPSField));
        }
        if (!isBlank(sustainedDPSField)) {
            where.add("(ws.fire_rate > 0 AND (ws.magazine * ws.precision_damage) / (((ws.magazine - 1) * (60.0 / ws.fire_rate)) + ws.reload_speed) > ?)");
            params.add(parseDouble(sustainedDPSField));
        }
        if (!isBlank(theoreticalTotalDamageField)) {
            where.add("(ws.reserves * ws.precision_damage) > ?");
            params.add(parseDouble(theoreticalTotalDamageField));
        }
        if (!isBlank(oneMagDamageField)) {
            where.add("(ws.magazine * ws.precision_damage) > ?");
            params.add(parseDouble(oneMagDamageField));
        }

        if (!where.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", where));
        }

        try (Connection conn = DatabaseProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                List<WeaponRow> rows = new ArrayList<>();
                while (rs.next()) {
                    WeaponRow row = new WeaponRow();
                    row.setWeaponType(rs.getString("weapon_type"));
                    row.setFrame(rs.getString("weapon_frame"));
                    row.setReserves(rs.getInt("reserves"));
                    row.setMagazine(rs.getInt("magazine"));
                    row.setFireRate(rs.getInt("fire_rate"));
                    row.setReload(rs.getDouble("reload_speed"));
                    row.setBody(rs.getInt("body_damage"));
                    row.setPrecision(rs.getInt("precision_damage"));
                    row.setAmmo(rs.getString("ammo"));
                    row.setOneMagDamage(rs.getInt("one_mag_damage"));
                    row.setTheoreticalTotalDamage(rs.getInt("theoretical_total_damage"));
                    row.setSustainedDps(rs.getDouble("sustained_dps"));
                    row.setTrueDps(rs.getDouble("true_dps"));
                    row.setDescription(rs.getString("weapon_desc"));
                    rows.add(row);
                }
                return rows;
            }
        }
    }

    private static boolean isBlank(TextField tf) {
        return tf.getText() == null || tf.getText().trim().isEmpty();
    }

    private static int parseInt(TextField tf) {
        return Integer.parseInt(tf.getText().trim());
    }

    private static double parseDouble(TextField tf) {
        return Double.parseDouble(tf.getText().trim());
    }

    public void primaryAmmo(ActionEvent actionEvent) {
        ammoType = 1;
    }

    public void specialAmmo(ActionEvent actionEvent) {
        ammoType = 2;
    }

    public void heavyAmmo(ActionEvent actionEvent) {
        ammoType = 3;
    }
}
