package de.hhu.propra.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
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
