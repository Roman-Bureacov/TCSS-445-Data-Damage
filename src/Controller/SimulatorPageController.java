package Controller;

import javafx.fxml.FXML;
import java.util.List;

public class SimulatorPageController {
    @FXML private ScriptEditorController scriptEditorController;
    @FXML private GraphDisplayController graphDisplayController;

    @FXML
    private void initialize() {
        scriptEditorController.setOnRun(timesheet -> graphDisplayController.renderDamage(timesheet));
    }
}
