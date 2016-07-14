package de.hhu.propra.view;

import de.hhu.propra.Main;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;

/**
 * Created by David on 13.07.2016.
 */
public class StartbildschirmController {
    public static Main main;

    Stage stage;

    boolean katalogausgewaehlt;

    public boolean isKatalogausgewaehlt() {
        return katalogausgewaehlt;
    }

    public void setMain(Main main) {
        this.main = main;

    }

    public void setStage(Stage stage) {
        this.stage = stage;

    }

    @FXML
    public void handleSelect() {
        if (main.katalogLaden()) {
            katalogausgewaehlt = true;
            stage.hide();
        }

    }

    @FXML
    public void handleManual() throws Exception {
        Desktop.getDesktop().open(new File("TDDT-Handbuch.pdf"));
    }

    @FXML
    public void handleExit() {
        System.exit(20);
    }
}