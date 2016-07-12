package de.hhu.propra;

import de.hhu.propra.model.Aufgabe;
import de.hhu.propra.view.AnalysePopupController;
import de.hhu.propra.view.HauptfensterController;
import de.hhu.propra.view.OberflaecheController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Optional;

public class Main extends Application {
	private Stage primaryStage;
    private BorderPane hauptfenster;
    private OberflaecheController ofController;
    private HauptfensterController hfController;
    private File aktuellerKatalog;
    private String nameAufgabe = " ";
    private static Tracker tracker;
    private static String[] startconfig;
    private String katalog;
	private static int KATALOG = 1;
    private Aufgabe[] aufgaben;

    @Override
	public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("TDDT");
        this.primaryStage.setFullScreen(false);

        if (startconfig[KATALOG].equals("")) {
            initialStart();
        } else{
            aktuellerKatalog = new File(getCorrectPath()+"/aufgaben/aufgaben.xml");
        }
        try {
            initHauptprogramm();
        } catch (IOException e) {
            System.err.println("Unable to load Resources: " + e);
        }

        this.primaryStage.setOnCloseRequest(close -> {
            beenden();
        });
	}

	public static void main(String[] args) throws URISyntaxException {
        String config = "";
        try {
            String path = getCorrectPath() + "/config/";

            BufferedReader reader = new BufferedReader(new FileReader(path + "config.txt"));
            String line = reader.readLine();

            while (line != null){
                config += line;
                line = reader.readLine();
            }
            startconfig = config.split("#");
        } catch (IOException e){
            System.err.println("Unable to find config.txt: " + e);
        }
		launch(args);
	}

    private void initHauptprogramm() throws IOException {
        FXMLLoader hfL = new FXMLLoader(getClass().getResource("/fxml/Hauptfenster.fxml"));
        hauptfenster = hfL.load();
        hfController = hfL.getController();
        hfController.setStage(primaryStage);

        Scene scene = new Scene(hauptfenster);

        primaryStage.setScene(scene);
        katalog = getCorrectPath() + "/aufgaben/aufgaben.xml";
        ladeAufgaben(aktuellerKatalog);
        for (int k=0; k < aufgaben.length;k++) {
            hfController.addAufgabe(k,aufgaben[k].getName(),aufgaben[k].getValueBabysteps());
        }

        FXMLLoader obL = new FXMLLoader(getClass().getResource("/fxml/Oberflaeche.fxml"));
        BorderPane oberflaeche = obL.load();
        ofController = obL.getController();
        ofController.reicheMainWeiter(this);
        hauptfenster.setCenter(oberflaeche);

        tracker = new Tracker(ofController, nameAufgabe);
        hfController.setMain(this);
        ofController.reicheTrackerWeiter(tracker);

        primaryStage.show();

    }

    public void showAnalysePopup() {
        try {
            FXMLLoader apL = new FXMLLoader(getClass().getResource("/fxml/AnalysePopup.fxml"));
            BorderPane analysePopup = apL.load();
            AnalysePopupController apController = apL.getController();
            tracker.analyseErstellen(ofController.getAktuellePhase());
            analysePopup.setCenter(tracker.getAnalyse().getChart());

            apController.fuelleTextArea(getCorrectPath() + "/aufgaben/" + nameAufgabe + "/log.txt");

            Stage popupStage = new Stage();
            popupStage.setTitle("Phasenanalyse");
            popupStage.initOwner(primaryStage);
            popupStage.initModality(Modality.NONE);

            Scene scene = new Scene(analysePopup);
            popupStage.setScene(scene);
            popupStage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initialStart() throws Exception {
        katalogLaden();
        //Stage stage = new Stage();

        //Label label = new Label("Willkommen!");
        //label.setFont(Font.font("Verdana", 50));

        //Button select = new Button("Katalog auswählen");
        //Button manual = new Button("Gebrauchsanweisung");
        //Button exit = new Button("Programm beenden");

        //select.setOnAction(new EventHandler<ActionEvent>(){

        //public void handle(ActionEvent AE) {
        //katalogLaden();
        //stage.close();
        //}
        //});

        //manual.setOnAction(new EventHandler<ActionEvent>(){

        //public void handle(ActionEvent AE) {
        //Alert alert = new Alert(Alert.AlertType.INFORMATION);
        //alert.setTitle("Gebrauchsanweisung");
        //alert.setHeaderText(null);
        //alert.setResizable(true);
        //alert.getDialogPane().setContent(new TextArea("Hier wird später die Gebrauchsanweisung angezeigt."));
        //alert.showAndWait();
                //Gebrauchsanweisung anzeigen
        //}
        //});

        //exit.setOnAction(new EventHandler<ActionEvent>(){

        //public void handle(ActionEvent AE) {
        //HauptfensterController.main.beenden();
        //}
        //});

        //HBox hbox = new HBox();
        //VBox vbox = new VBox();
        //hbox.getChildren().addAll(select, manual, exit);
        //hbox.setSpacing(10);
        //hbox.setPadding(new Insets(0, 0, 0, 50));
        //vbox.getChildren().add(label);
        //vbox.setPadding(new Insets(30, 0, 30, 75));

        //BorderPane pane = new BorderPane();
        //pane.setCenter(hbox);
        //pane.setLeft(button);
        //pane.setTop(vbox);

        //Scene scene = new Scene(pane);

        //stage.setTitle("Startbildschirm");
        //stage.centerOnScreen();
        //stage.setHeight(250.0);
        //stage.setWidth(500.0);
        //stage.setScene(scene);
        //stage.setAlwaysOnTop(false);
        //stage.show();

        // TODO: Kerstin und David
        /* 1) Gebrauchsanweisung anzeigen
           2) Erstmalige Katalogauswahl: Erledigt von Freddy

           4) Mit entsprechenden Inhalten das Hauptprogramm wählen: Ebenfalls von Freddy erledigt...
          */
        //Ich habe das Willkommensfenster fertig, allerdings stürzt das Programm noch ab, wenn die Config leer ist.
        //Daher habe ich den Code erstmal auskommentiert.
    }


    public void katalogLaden(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Katalogdatei auswählen");

        File tempFile = fileChooser.showOpenDialog(primaryStage);

        if (tempFile != null){
            setAktuellerKatalog(tempFile);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Keine Datei ausgewaehlt");
            alert.setHeaderText("Bitte Datei auswaehlen!");
            alert.showAndWait();
            katalogLaden();
        }
    }

    public static String getCorrectPath() {

        try {
            String path = CodeTester.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            path = path.substring(0, path.lastIndexOf("/"));
            path = path.substring(0, path.lastIndexOf("/"));
            path = path.substring(0, path.lastIndexOf("/"));
            if (path.endsWith("build")){
                path+="/libs";
            }
            else {
                path += "/build/libs";
            }
            return path;
        } catch (Exception e) {
            return "Fehler beim Pfad ermitteln: " + e;
        }
    }

    private void ladeAufgaben(File katalog){
        try {
            // String path = getCorrectPath() + "/aufgaben/aufgaben.xml";
            XMLParser parser= new XMLParser(katalog);
            aufgaben = parser.getAufgaben();
        } catch (Exception e){
            System.out.print("Fehler");
        }
    }

    public Aufgabe aktualisiereAufgabe(int k){//Wenn eine Aufgabe ausgewählt wird, wird das hier alles aktualisert
        setNameAufgabe(aufgaben[k].getName());
        ofController.aktualisiereCodeTab(aufgaben[k]);
        ofController.aktualisieretestTextArea(aufgaben[k]);
        return aufgaben[k];
    }

    public void setNameAufgabe(String nameAufgabe){
        tracker.setNameAufgabe(nameAufgabe);
        this.nameAufgabe = nameAufgabe;
    }

    public void setAktuellerKatalog(File katalog){
        this.aktuellerKatalog = katalog;
    }

    public void aenderungenSpeichern(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Zwischenergebnisse speichern?");
        alert.setHeaderText("Möchtest du deine Änderungen speichern?");

        ButtonType bTJa = new ButtonType("Ja");
        ButtonType bTNein = new ButtonType("Nein");

        alert.getButtonTypes().setAll(bTJa, bTNein);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == bTJa){
            ofController.getCodeTester().writeExternalFile();
            // TODO: Viktor, hier müssten dann noch alle Tests geschrieben werden!
        }
    }

    public void beenden() {
        startconfig[1] = aktuellerKatalog.toString();
        startconfig[3] = nameAufgabe;
        aenderungenSpeichern();
        try {
            FileWriter writer = new FileWriter(getCorrectPath() + "/config/config.txt");
            for(String element : startconfig) {
                writer.write(element + "#");
            }
            writer.close();
        } catch (Exception e) {
            System.err.println("Unable to save.");
        }
        System.exit(20);
    }

    public String getNameAufgabe(){
        return this.nameAufgabe;
    }

    public Aufgabe[] getAufgaben(){ return this.aufgaben; }

    public boolean schonBearbeitet(String nameAufgabe){
        File verzeichnis = new File(getCorrectPath() + "/aufgaben/" + nameAufgabe);
        return verzeichnis.exists();

    }

    public HashMap<String, String> getCode(String nameAufgabe, String nameHauptklasse){
        HashMap<String, String> klassen = new HashMap<>();
        String name = "";
        String code = "";
        int anzahlKlassen = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(getCorrectPath() + "/aufgaben/" + nameAufgabe + "/" + nameHauptklasse + ".java"));
            String line = reader.readLine();

            while (line != null){
                if (line.startsWith("//Neue Klasse")){
                    if(anzahlKlassen > 0){
                        klassen.put(name, code);
                    }
                    name = line.substring(14);
                    anzahlKlassen++;
                    line = reader.readLine();
                    continue;
                }
                code += line + "\n";
                line = reader.readLine();
            }
            klassen.put(name, code);
        } catch (Exception e) {
            System.err.println("Fehler beim Quellcodeladen: " + e);
        }
        return klassen;
    }
}