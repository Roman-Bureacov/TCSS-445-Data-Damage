package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import model.database.Database;
import model.script.ScriptReader;
import model.script.TimeSheet;
import java.io.*;
import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ScriptEditorController {
    @FXML
    private TextArea editor;

    private Consumer<TimeSheet> onRun;

    public void setOnRun(Consumer<TimeSheet> onRun) { this.onRun = onRun; }

    @FXML
    private void initialize() {
        editor.setOnKeyPressed(e -> {
            if (Objects.requireNonNull(e.getCode()) == KeyCode.S) {
                if (e.isControlDown() || e.isMetaDown()) saveToFile();
            }
        });
    }

    public String getText() {
        return editor.getText();
    }

    public void setText(String text) {
        editor.setText(text);
    }

    public void saveToFile() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files", "*.txt", "*.md", "*.cfg", "*.*"));
        File file = fc.showSaveDialog(editor.getScene().getWindow());
        if (file == null) return;
        try (Writer w = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            w.write(editor.getText());
        } catch (IOException ex) { ex.printStackTrace(); }
    }

    public void loadFromFile() {
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(editor.getScene().getWindow());
        if (file == null) return;
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            editor.setText(r.lines().reduce((a,b)->a+"\n"+b).orElse(""));
        } catch (IOException ex) { ex.printStackTrace(); }
    }

    public void runScript(ActionEvent ignored) {
        runScript();
    }

    private void runScript() {
        try {
            TimeSheet timesheet = ScriptReader.readData(getText());
            saveSimulation(timesheet);
            if (onRun != null) onRun.accept(timesheet);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void saveSimulation(TimeSheet timesheet) throws Exception {
        try {
            long scriptId = 0;
            String simsScriptsInsertQuery = "INSERT INTO sims_scripts (script) VALUES (?)";
            List<Object> params = new ArrayList<>();
            params.add(this.getText());
            scriptId = Database.getInstance().executeInsertReturningId(simsScriptsInsertQuery, params);
            params.clear();

            String simsInsertQuery = "INSERT INTO sims (script_id) VALUES (?)";
            params.add(scriptId);
            Database.getInstance().executeUpdate(simsInsertQuery, params);
            params.clear();

            int totalDamage = 0;
            int maxMs = 0;
            for (Object[] row : timesheet) {
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

            String simsMetaInsertStatement = "INSERT INTO sims_meta (script_id, average_dps, total_damage) " +
                    "VALUES (?, ?, ?)";
            params.add(scriptId);
            params.add(averageDps);
            params.add(totalDamage);
            Database.getInstance().executeUpdate(simsMetaInsertStatement, params.toArray());
            params.clear();

            for (Object[] row : timesheet) {
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