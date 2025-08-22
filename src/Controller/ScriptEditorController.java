package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import model.database.Database;
import model.database.Weapon;
import model.script.ScriptReader;
import model.script.TimeSheet;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Controller for the script editor. Responsible for getting user's scripts, calling the script reader and then getting
 * the scripts to the graph display.
 *
 * @author Kaleb Anagnostou
 * @version 2025 August
 */
public class ScriptEditorController {
    /** The text field that sets the script name when saved. */
    @FXML public TextField myScriptName;
    /** The text field that sets the script id when loading. */
    @FXML public TextField myScriptID;
    /** The text area where the user inputs script. */
    @FXML private TextArea myEditor;
    /** The simulation timesheet. */
    private TimeSheet myTimeSheet;
    /** The simulation weapons. */
    private Weapon[] myWeapons;
    /** Consumer that passes the timesheet from the script editor to the graph controller to update when run. */
    private Consumer<TimeSheet> onRun;

    /**
     * Sets onRun to be the function passed in.
     */
    public void setOnRun(Consumer<TimeSheet> onRun) {
        this.onRun = onRun;
    }

    /**
     * Getter for the text in the editor textfield.
     */
    public String getText() {
        return myEditor.getText();
    }

    /**
     * Setter for the text in the editor textfield.
     */
    public void setText(String theText) {
        myEditor.setText(theText);
    }

    /**
     * Saves the text in the editor to a txt file.
     */
    public void saveToDB() {
        try {
            myTimeSheet = ScriptReader.readData(getText());
            myWeapons = ScriptReader.getWeapons(getText());
            saveSimulation(myTimeSheet, myWeapons);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Load text from a choose file into the editor.
     */
    public void loadFromDB() {
        try{
            String scriptTextFromIDQuery = """
                SELECT script FROM sims_scripts WHERE script_id = ?
                """;
            ResultSet rs = Database.getInstance().executeQuery(scriptTextFromIDQuery, Integer.parseInt(myScriptID.getText()));
            if(rs.next()) {
                String script = rs.getString("script").substring(1, rs.getString("script").length() - 1);
                setText(script);
            } else {
                setText("");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Cannot find script id: " + myScriptID.getText());
                alert.showAndWait();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Runs the script through the simulator and script reader. Passes the time sheet to on run to display in the graph.
     *
     * @param theActionEvent clicking the run button is the action. Needed because javafx expects this method signature.
     */
    public void runScript(ActionEvent theActionEvent) {
        try {
            myTimeSheet = ScriptReader.readData(getText());
            myWeapons = ScriptReader.getWeapons(getText());
            if (onRun != null) {
                onRun.accept(myTimeSheet);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Takes the time sheet and weapons array and inserts them into the database in the proper tables.
     *
     * @param theTimesheet the time sheets with sim time events.
     * @param theWeapons the weapons used in the simulation.
     */
    private void saveSimulation(TimeSheet theTimesheet, Weapon[] theWeapons) {
        try {
            long scriptId = 0;
            String simsScriptsInsertQuery = "INSERT INTO sims_scripts (script) VALUES (?)";
            List<Object> params = new ArrayList<>();
            params.add(this.getText());
            scriptId = Database.getInstance().executeInsertReturningId(simsScriptsInsertQuery, params);
            params.clear();

            String simsInsertQuery = "INSERT INTO sims (script_id, sim_name) VALUES (?, ?)";
            params.add(scriptId);
            params.add(myScriptName.getText());
            Database.getInstance().executeUpdate(simsInsertQuery, params.toArray());
            params.clear();

            int totalDamage = 0;
            int maxMs = 0;
            for (Object[] row : theTimesheet) {
                Integer tMs = (Integer) row[0];
                Integer dmg = (Integer) row[1];
                if (tMs != null && tMs > maxMs) {
                    maxMs = tMs;
                }
                if (dmg != null && dmg > 0) {
                    totalDamage += dmg;
                }
            }

            double secondsPerSim = 60.0;
            double averageDps = totalDamage / secondsPerSim;

            String insertScriptMeta = """
                WITH
                    kinetic AS (SELECT weapon_id FROM weapons WHERE weapon_frame = ? AND weapon_type = ?),
                    energy AS (SELECT weapon_id FROM weapons WHERE weapon_frame = ? AND weapon_type = ?),
                    power AS (SELECT weapon_id FROM weapons WHERE weapon_frame = ? AND weapon_type = ?)
                INSERT INTO sims_meta (script_id, kinetic, energy, power, average_dps, total_damage)
                SELECT ?, kinetic.weapon_id, energy.weapon_id, power.weapon_id, ?, ?
                FROM kinetic, energy, power;
                """;

            Database.getInstance().executeUpdate(
                    insertScriptMeta,
                    theWeapons[0].getWeaponFrame(), theWeapons[0].getWeaponType(),
                    theWeapons[1].getWeaponFrame(), theWeapons[1].getWeaponType(),
                    theWeapons[2].getWeaponFrame(), theWeapons[2].getWeaponType(),
                    scriptId,
                    averageDps,
                    totalDamage
            );
            params.clear();

            for (Object[] row : theTimesheet) {
                Integer tMs = (Integer) row[0];
                Integer dmg = (Integer) row[1];
                String event = (String) row[2];

                String simsEventsInsertQuery = "INSERT INTO sim_events (script_id, timestamps, damage_instance, event_desc) " +
                        "VALUES (?, ?, ?, ?)";
                params.add(scriptId);
                params.add(tMs);
                params.add(dmg);
                params.add(event);

                Database.getInstance().executeUpdate(simsEventsInsertQuery, params.toArray());
                params.clear();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}