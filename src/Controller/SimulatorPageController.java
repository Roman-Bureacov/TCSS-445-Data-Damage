package Controller;

import javafx.fxml.FXML;

/**
 * The controller for the simulator page.
 *
 * @author Kaleb Anagnostou
 * @version 2025 August
 */
public class SimulatorPageController {
    /** The controller for the script editor. */
    @FXML private ScriptEditorController scriptEditorController;
    /** The controller for the graph display. */
    @FXML private GraphDisplayController graphDisplayController;

    /**
     * Sets the onRun function in the script editor to take the returned timesheet and pass it to the graph display
     * controller for rendering.
     */
    @FXML private void initialize() {
        scriptEditorController.setOnRun(timesheet -> {
            graphDisplayController.renderDamage(timesheet);
            graphDisplayController.renderDpsChart(timesheet);
        });
    }
}
