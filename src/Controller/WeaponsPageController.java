package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.database.Database;
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
    public void initialize() {
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
    public void onSearch() {
        try {
            ResultSet rs = queryWeapons();
            Connection conn = rs.getStatement().getConnection();

            try (rs; conn) {

                weaponsTable.getItems().clear();

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
                w.weapon_id, w.weapon_type, w.weapon_frame,
                wi.weapon_desc,
                ws.reserves, ws.fire_rate, ws.reload_speed, ws.magazine,
                ws.body_damage, ws.precision_damage,
                at.ammo,
                (ws.magazine * ws.precision_damage) AS one_mag_damage,
                (ws.reserves * ws.precision_damage) AS theoretical_total_damage,
                CASE WHEN ws.fire_rate IS NULL OR ws.fire_rate = 0 THEN NULL
                     ELSE ROUND ((ws.magazine * ws.precision_damage) / (((ws.magazine - 1) * (60.0 / ws.fire_rate)) + ws.reload_speed ),1)
                END AS sustained_dps,
                CASE WHEN ws.fire_rate IS NULL OR ws.fire_rate = 0 THEN NULL
                     ELSE ROUND ((ws.magazine * ws.precision_damage) / (ws.magazine * (60.0 / ws.fire_rate)), 1) 
                END AS true_dps 
            FROM weapons w
            LEFT JOIN weapon_stats ws ON ws.weapon_id = w.weapon_id
            LEFT JOIN weapon_ammo wa ON wa.weapon_id = w.weapon_id
            LEFT JOIN ammo_types at ON at.ammo_id = wa.ammo_id
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
                WHERE w.weapon_frame = ?
            """);
            params.add(frameField.getText().trim());
        }

        if (ammoType != 0){
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                LEFT JOIN weapon_ammo wa ON wa.weapon_id = w.weapon_id
                WHERE wa.ammo_id = ?
            """);
            params.add(ammoType);
        }

        if (magCapacityField.getText() != null && !magCapacityField.getText().trim().isEmpty()) {
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                LEFT JOIN weapon_stats ws ON ws.weapon_id = w.weapon_id
                WHERE ws.magazine > ?
            """);
            params.add(Integer.parseInt(magCapacityField.getText().trim()));
        }
        if (reservesMagazineField.getText() != null && !reservesMagazineField.getText().trim().isEmpty()) {
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                LEFT JOIN weapon_stats ws ON ws.weapon_id = w.weapon_id
                WHERE ws.reserves > ?
            """);
            params.add(Integer.parseInt(reservesMagazineField.getText().trim()));
        }
        if (fireRateField.getText() != null && !fireRateField.getText().trim().isEmpty()) {
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                LEFT JOIN weapon_stats ws ON ws.weapon_id = w.weapon_id
                WHERE ws.fire_rate > ?
            """);
            params.add(Integer.parseInt(fireRateField.getText().trim()));
        }
        if (reloadSpeedField.getText() != null && !reloadSpeedField.getText().trim().isEmpty()) {
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                LEFT JOIN weapon_stats ws ON ws.weapon_id = w.weapon_id
                WHERE ws.reload_speed < ?
            """);
            params.add(Double.parseDouble(reloadSpeedField.getText().trim()));
        }
        if (bodyDamageField.getText() != null && !bodyDamageField.getText().trim().isEmpty()) {
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                LEFT JOIN weapon_stats ws ON ws.weapon_id = w.weapon_id
                WHERE ws.body_damage > ?
            """);
            params.add(Integer.parseInt(bodyDamageField.getText().trim()));
        }
        if (precisionDamageField.getText() != null && !precisionDamageField.getText().trim().isEmpty()) {
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                LEFT JOIN weapon_stats ws ON ws.weapon_id = w.weapon_id
                WHERE ws.precision_damage > ?
            """);
            params.add(Integer.parseInt(precisionDamageField.getText().trim()));
        }

        if(trueDPSField.getText() != null && !trueDPSField.getText().trim().isEmpty()) {
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                LEFT JOIN weapon_stats ws ON ws.weapon_id = w.weapon_id
                WHERE ((ws.magazine * ws.precision_damage) / ( ws.magazine * (60.0 / ws.fire_rate) ) > ?
            """);
            params.add(Double.parseDouble(trueDPSField.getText().trim()));
        }

        if(sustainedDPSField.getText() != null && !sustainedDPSField.getText().trim().isEmpty()) {
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                LEFT JOIN weapon_stats ws ON ws.weapon_id = w.weapon_id
                WHERE (ws.magazine * ws.precision_damage) / (((ws.magazine - 1) * (60.0 / ws.fire_rate)) + ws.reload_speed)
            """);
            params.add(Double.parseDouble(sustainedDPSField.getText().trim()));
        }

        if(theoreticalTotalDamageField.getText() != null && !theoreticalTotalDamageField.getText().trim().isEmpty()) {
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                LEFT JOIN weapon_stats ws ON ws.weapon_id = w.weapon_id
                WHERE (ws.reserves * ws.precision_damage) > ?
            """);
            params.add(Double.parseDouble(theoreticalTotalDamageField.getText().trim()));
        }

        if(oneMagDamageField.getText() != null && !oneMagDamageField.getText().trim().isEmpty()) {
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                LEFT JOIN weapon_stats ws ON ws.weapon_id = w.weapon_id
                WHERE (ws.magazine * ws.precision_damage) > ?
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
