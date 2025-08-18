package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import model.script.ScriptReader;
import model.script.TimeSheet;
import java.io.*;
import java.nio.charset.StandardCharsets;
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

    public String getText() { return editor.getText(); }
    public void setText(String text) { editor.setText(text); }

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
            if (onRun != null) onRun.accept(timesheet);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}