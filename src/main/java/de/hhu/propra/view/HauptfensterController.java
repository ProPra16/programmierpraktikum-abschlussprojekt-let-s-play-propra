package de.hhu.propra.view;

import de.hhu.propra.CodeTester;
import de.hhu.propra.Main;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;

import static javafx.scene.input.KeyCode.F;

/**
 * Created by FreddyG on 06.07.16.
 */
public class HauptfensterController {

    private static Main main;
    private CodeTester codeTester;

    @FXML
    private Menu aufgabenmenu;

    @FXML
    private Menu babymenu;

    @FXML
    private void handleMenueNeueUebung(){

    }

    @FXML
    private void handleMenueClose(){
        main.beenden();
    }

    @FXML
    private void handleMenueKatalogAendern(){

    }

    @FXML
    private void handleMenueAufgabeAendern(){

    }

    @FXML
    private void handleAnalyse(){
        try {
            main.showAnalysePopup();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @FXML
    private void handleMenueAufgabenstellung(){

    }

    public void addAufgabe(String nameAufgabe, boolean babystep) {
        if (babystep==false) {
            aufgabenmenu.getItems().add(new MenuItem(nameAufgabe));
        } else {
            babymenu.getItems().add(new MenuItem(nameAufgabe));
        }
    }

    public void setMain(Main main){
        this.main = main;
    }

    public void setCodeTester(CodeTester codeTester){
        this.codeTester = codeTester;
    }
}
