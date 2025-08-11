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

public class MainWindowController {
    @FXML
    public AnchorPane sideMenu;
    @FXML
    private BorderPane rootBorder;

    @FXML private SideMenuController sideMenuController;

    private final Map<String, Node> cache = new HashMap<>();

    @FXML
    private void initialize() {
        sideMenuController.setMainController(this);
    }

    public void showWeapons()   { setCenter(loadView("WeaponsPage.fxml")); }
    public void showScripts()   { setCenter(loadView("ScriptsPage.fxml")); }
    public void showSimulator() { setCenter(loadView("SimulatorPage.fxml")); }

    private void setCenter(Node node) {
        rootBorder.setCenter(node);
    }

    private Node loadView(String fileName) {
        return cache.computeIfAbsent(fileName, key -> {
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
