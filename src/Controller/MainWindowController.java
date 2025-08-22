package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Main window controller that will be holding all other gui elements.
 *
 * @author Kaleb Anagnostou
 * @version 2025 August
 */
public class MainWindowController {
    /** The side menu for switching between pages. */
    @FXML public AnchorPane mySideMenu;
    /** The root window that will hold other elements. */
    @FXML private BorderPane myRootBorder;
    /** The contoller for the side menu. */
    @FXML private SideMenuController mySideMenuController;

    /**
     * A map from FXML file names to already loaded node objects.
     * Used for maintaining state without rebuilding pages.
     */
    private final Map<String, Node> myPageCache = new HashMap<>();

    /**
     * Initializes the side menu to display pages on start up.
     */
    @FXML private void initialize() {
        mySideMenuController.setMainController(this);
    }

    /**
     * Sets the view to be the weapons page.
     */
    public void showWeapons() {
        myRootBorder.setCenter(loadView("WeaponsPage.fxml"));
    }

    /**
     * Sets the view to be the scripts page.
     */
    public void showScripts() {
        myRootBorder.setCenter(loadView("ScriptsPage.fxml"));
    }

    /**
     * Sets the view to be the simulator page.
     */
    public void showSimulator() {
        myRootBorder.setCenter(loadView("SimulatorPage.fxml"));
    }

    /**
     * Loads a FXML element from the view package and stores it in a map so that it can maintain state while
     * switching pages.
     *
     * @param theFileName the name of the file in the view package to load.
     */
    private Node loadView(String theFileName) {
        return myPageCache.computeIfAbsent(theFileName, key -> {
            URL url = MainWindowController.class.getResource("/view/" + key);
            if (url == null) {
                throw new IllegalStateException("FXML not found on classpath: /view/" + key);
            }
            try {
                return FXMLLoader.load(url);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load " + key, e);
            }
        });
    }
}
