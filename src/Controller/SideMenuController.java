package Controller;

import javafx.fxml.FXML;

/**
 * The controller for the side menu.
 *
 * @author Kaleb Anagnostou
 * @version 2025 August
 */
public class SideMenuController {
    /** The controller for the main window that this element is a component of. */
    private MainWindowController myMainWindow;

    /**
     * Sets the main window controller.
     *
     * @param theMainWindow the Main window that whose view we are changing via the buttons in the side menu.
     */
    void setMainController(MainWindowController theMainWindow) {
        myMainWindow = theMainWindow;
    }

    /**
     * Sets the center of the main window to the weapons page.
     */
    @FXML private void onWeapons() {
        if (myMainWindow != null) {
            myMainWindow.showWeapons();
        }
    }

    /**
     * Sets the center of the main window to the scripts page.
     */
    @FXML private void onScripts() {
        if (myMainWindow != null) {
            myMainWindow.showScripts();
        }
    }

    /**
     * Sets the center of the main window to the simulator page.
     */
    @FXML private void onSimulator() {
        if (myMainWindow != null) {
            myMainWindow.showSimulator();
        }
    }
}
