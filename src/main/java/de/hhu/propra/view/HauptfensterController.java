package de.hhu.propra.view;

import de.hhu.propra.CodeTester;
import de.hhu.propra.Main;
import de.hhu.propra.TestTester;
import de.hhu.propra.model.Aufgabe;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by FreddyG on 06.07.16.
 */
public class HauptfensterController {

    public static Main main;
    private CodeTester codeTester;
    private TestTester testTester;
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
        aufgabenmenu.getItems().clear();
        babymenu.getItems().clear();
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

    @FXML
    public void handleMenueEinstellungen() {
        TextField eingabe = new TextField("");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Einstellungen");
        alert.setHeaderText("Bitte legen Sie die Länge der Babysteps in Sekunden fest.");
        ButtonType speichern = new ButtonType("Speichern");
        ButtonType abbrechen = new ButtonType("Abbrechen");
        alert.getButtonTypes().setAll(speichern, abbrechen);
        alert.getDialogPane().setContent(eingabe);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == speichern){
            try {
                int dauer = Integer.parseInt(eingabe.getText());
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Achtung");
                confirmation.setHeaderText("Ihre Eingabe wurde gespeichert.");
                confirmation.showAndWait();
                OberflaecheController.start = dauer;
            }
            catch (NumberFormatException dauer){
                Alert warning = new Alert(Alert.AlertType.WARNING);
                warning.setTitle("Warnung");
                warning.setHeaderText("Ungültige Eingabe!");
                warning.setContentText("Ihre Eingabe ist ungültig oder leer.");
                warning.showAndWait();
                alert.close();
                handleMenueEinstellungen();
            }
        } else {}

    }

    @FXML
    public void handleMenueGebrauchsanweisung() throws IOException{
        Desktop.getDesktop().open(new File("TDDT-Handbuch.pdf"));
    }

    public void setMain(Main main){
        this.main = main;
    }

    public void setCodeTester(CodeTester codeTester){
        this.codeTester = codeTester;
    }
    public void setTestTester(TestTester testTester){
        this.testTester = testTester;
    }

    public void setStage (Stage stage){
        this.stage = stage;
    }
}
