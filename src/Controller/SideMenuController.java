package Controller;

import javafx.fxml.FXML;

public class SideMenuController {
    private MainWindowController main;

    void setMainController(MainWindowController main) {
        this.main = main;
    }

    @FXML
    private void onWeapons()   { if (main != null) main.showWeapons(); }
    @FXML private void onScripts()   { if (main != null) main.showScripts(); }
    @FXML private void onSimulator() { if (main != null) main.showSimulator(); }
}
