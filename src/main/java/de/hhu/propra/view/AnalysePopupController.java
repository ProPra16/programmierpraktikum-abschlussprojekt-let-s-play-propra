package de.hhu.propra.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by Freddy on 09.07.2016.
 */
public class AnalysePopupController {

    @FXML
    private TextArea changelog;

    public void fuelleTextArea(String path){
        changelog.setText("");
        try {
            File file = new File(path);
            if (!file.exists()) {
                changelog.setText("Keine Änderungen vorhanden.");
                return;
            }
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            while (line != null){
                changelog.setText(changelog.getText() + line + "\n");
                line = reader.readLine();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
