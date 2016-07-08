package de.hhu.propra.view;

import de.hhu.propra.CodeTester;
import de.hhu.propra.Main;
import javafx.fxml.FXML;

/**
 * Created by FreddyG on 06.07.16.
 */
public class HauptfensterController {

    private static Main main;
    private CodeTester codeTester;

    @FXML
    private void handleMenueNeueUebung(){

    }

    @FXML
    private void handleMenueClose(){
        // codeTester.speichern();
        System.exit(20);
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

    public void setMain(Main main){
        this.main = main;
    }

    public void setCodeTester(CodeTester codeTester){
        this.codeTester = codeTester;
    }
}
