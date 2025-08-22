package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.database.Database;
import java.sql.*;
import java.util.*;

/**
 * The controller for the weapons page.
 *
 * @author Kaleb Anagnostou
 * @version 2025 August
 */
public class WeaponsPageController {
    /** Weapon type to search for in the database. */
    @FXML private TextField typeField;
    /** Weapon frame to search for in the database. */
    @FXML private TextField frameField;
    /** Toggle group for the radio buttons. */
    @FXML private ToggleGroup ammoGroup;
    /** Weapon ammo type to search for in the database. */
    private int ammoType;
    /** Radio button to set ammo type to search for in the database to kinetic. */
    @FXML private RadioButton primaryRadio;
    /** Radio button to set ammo type to search for in the database to energy. */
    @FXML private RadioButton specialRadio;
    /** Radio button to set ammo type to search for in the database to power. */
    @FXML private RadioButton heavyRadio;
    /** Weapon true dps to search for in the database. */
    @FXML private TextField trueDPSField;
    /** Weapon sustained dps to search for in the database. */
    @FXML private TextField sustainedDPSField;
    /** Weapon theoretical total damage to search for in the database. */
    @FXML private TextField theoreticalTotalDamageField;
    /** Weapon one magazine damage to search for in the database. */
    @FXML private TextField oneMagDamageField;
    /** Weapon magazine cpacity to search for in the database. */
    @FXML private TextField magCapacityField;
    /** Weapon reserve magazines to search for in the database. */
    @FXML private TextField reservesMagazineField;
    /** Weapon fire rate to search for in the database. */
    @FXML private TextField fireRateField;
    /** Weapon reload speed to search for in the database. */
    @FXML private TextField reloadSpeedField;
    /** Weapon time to empty to search for in the database. */
    @FXML private TextField timeToEmptyField;
    /** Weapon body damage to search for in the database. */
    @FXML private TextField bodyDamageField;
    /** Weapon precision dmage to search for in the database. */
    @FXML private TextField precisionDamageField;
    /** The table displaying the query results. */
    @FXML private TableView<WeaponRow> weaponsTable;
    /** Column that will display the weapon's type. */
    @FXML private TableColumn<WeaponRow,String> colType;
    /** Column that will display the weapon's frame. */
    @FXML private TableColumn<WeaponRow,String> colFrame;
    /** Column that will display the weapon's description. */
    @FXML public TableColumn<WeaponRow, String> colDescription;
    /** Column that will display the weapon's one mag damage. */
    @FXML private TableColumn<WeaponRow, Integer> colOneMag;
    /** Column that will display the weapon's theoretical total damage. */
    @FXML private TableColumn<WeaponRow, Integer> colTheo;
    /** Column that will display the weapon's sustainable dps. */
    @FXML private TableColumn<WeaponRow, Double>  colSust;
    /** Column that will display the weapon's true damage. */
    @FXML private TableColumn<WeaponRow, Double>  colTrue;
    /** Column that will display the weapon's precision damage. */
    @FXML private TableColumn<WeaponRow,Integer> colPrecision;
    /** Column that will display the weapon's body damage. */
    @FXML private TableColumn<WeaponRow,Integer> colBody;
    /** Column that will display the weapon's fire rate. */
    @FXML private TableColumn<WeaponRow,Integer> colFireRate;
    /** Column that will display the weapon's mazine capacity. */
    @FXML private TableColumn<WeaponRow,Integer> colMagazine;
    /** Column that will display the weapon's reserves. */
    @FXML private TableColumn<WeaponRow,Integer> colReserves;
    /** Column that will display the weapon's reload speed. */
    @FXML private TableColumn<WeaponRow,Double>  colReload;
    /** Column that will display the weapon's ammo type. */
    @FXML private TableColumn<WeaponRow,String> colAmmoType;

    /**
     * Sets up the property value factories so that the table will update when it gets a new result set. Calls the
     * search with no parameters to populate the table without any filter queries.
     */
    @FXML public void initialize() {
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

    /**
     * Opens a DB connection and queries the DB. Sets the results set entries to be the column data in the table.
     */
    @FXML public void onSearch() {
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

    /**
     * Constructs a query and gets back the necessary data to construct the table.
     */
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
                     ELSE ROUND ((ws.magazine * ws.precision_damage) / (((ws.magazine - 1) * (60.0 / ws.fire_rate)) + ws.reload_speed), 1)
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
                WHERE ((ws.magazine * ws.precision_damage) / (ws.magazine * (60.0 / ws.fire_rate))) > ?
            """);
            params.add(Double.parseDouble(trueDPSField.getText().trim()));
        }

        if(sustainedDPSField.getText() != null && !sustainedDPSField.getText().trim().isEmpty()) {
            queries.add("""
                SELECT w.weapon_id
                FROM weapons w
                LEFT JOIN weapon_stats ws ON ws.weapon_id = w.weapon_id
                WHERE (ws.magazine * ws.precision_damage) / (((ws.magazine - 1) * (60.0 / ws.fire_rate)) + ws.reload_speed) > ?
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
            ResultSet rs = Database.getInstance().executeQuery(queries.get(i), params.get(i));
            Set<Integer> s = new HashSet<>();
            while (rs.next()){
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
        finalSql.append(" WHERE w.weapon_id IN (");
        int n = finalIds.size();
        for (int i = 0; i < n; i++) {
            finalSql.append("?");
            if (i < n - 1) finalSql.append(", ");
        }
        finalSql.append(")");

        Object[] paramlist = finalIds.toArray();
        return Database.getInstance().executeQuery(finalSql.toString(), paramlist);
    }

    /**
     * Sets the ammo type to primary.
     *
     * @param theActionEvent the action event that triggers this method. Necessary for JavaFx.
     */
    public void primaryAmmo(ActionEvent theActionEvent) {
        ammoType = 1;
    }

    /**
     * Sets the ammo type to special.
     *
     * @param theActionEvent the action event that triggers this method. Necessary for JavaFx.
     */
    public void specialAmmo(ActionEvent theActionEvent) {
        ammoType = 2;
    }

    /**
     * Sets the ammo type to heavy.
     *
     * @param theActionEvent the action event that triggers this method. Necessary for JavaFx.
     */
    public void heavyAmmo(ActionEvent theActionEvent) {
        ammoType = 3;
    }

}