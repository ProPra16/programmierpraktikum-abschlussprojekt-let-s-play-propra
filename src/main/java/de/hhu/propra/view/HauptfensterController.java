package de.hhu.propra.view;

import de.hhu.propra.CodeTester;
import de.hhu.propra.Main;
import de.hhu.propra.model.Aufgabe;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * Created by FreddyG on 06.07.16.
 */
public class HauptfensterController {

    private static Main main;
    private CodeTester codeTester;
    private Aufgabe aktuelleaufgabe;
    private static Stage stage;
    private String aktuelleraufgabenname="keine Aufgabe ausgewählt";
    private  String aktuelleaufgabenstellung="Es wurde noch keine Aufgabe ausgewählt";

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
        main.aenderungenSpeichern();
        main.katalogLaden();
        Aufgabe[] aufgaben = main.getAufgaben();
        for (int k=0; k < aufgaben.length; k++){
            addAufgabe(k,aufgaben[k].getName(),aufgaben[k].getValueBabysteps());
        }
    }

    @FXML
    private void handleMenueAufgabeAendern(ActionEvent event){
        MenuItem temp = (MenuItem)event.getSource();
        int k = (int)temp.getUserData();

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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Aufgabenstellung für "+aktuelleraufgabenname);
        alert.setHeaderText(null);
        //alert.setContentText(aktuelleaufgabenstellung);
        alert.setResizable(true);
        alert.getDialogPane().setContent(new TextArea(aktuelleaufgabenstellung));
        alert.showAndWait();
    }

    public void addAufgabe(int id, String nameAufgabe, boolean babystep) {
        Menu tempmenu;
        if (babystep==false) {
            tempmenu=aufgabenmenu;

        } else {
            tempmenu=babymenu;
        }

        MenuItem temp = new MenuItem(nameAufgabe);
        temp.setUserData(id);
        temp.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                //System.out.println("Hab auf Item " + id + "geklickt");
                main.aenderungenSpeichern();
                aktuelleaufgabe=main.aktualisiereAufgabe(id);
                aktuelleraufgabenname=aktuelleaufgabe.getName();
                aktuelleaufgabenstellung=aktuelleaufgabe.getBeschreibung();
            }
        });
        tempmenu.getItems().add(temp);
    }

    public void setMain(Main main){
        this.main = main;
    }

    public void setCodeTester(CodeTester codeTester){
        this.codeTester = codeTester;
    }

    public void setStage (Stage stage){
        this.stage = stage;
    }
}
