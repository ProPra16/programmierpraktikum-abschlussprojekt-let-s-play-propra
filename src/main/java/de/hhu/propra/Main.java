package de.hhu.propra;

import de.hhu.propra.model.Aufgabe;
import de.hhu.propra.view.AnalysePopupController;
import de.hhu.propra.view.HauptfensterController;
import de.hhu.propra.view.OberflaecheController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import java.io.*;
import java.net.URISyntaxException;

public class Main extends Application {
	private Stage primaryStage;
    private BorderPane hauptfenster;
    private OberflaecheController ofController;
    private HauptfensterController hfController;
    private String aktuellerKatalog=""; // TODO: Siehe nameAufgabe!!!
    private String nameAufgabe = "test";
    private static Tracker tracker;
    private static String[] startconfig;
    private String katalog;
	private static int KATALOG = 1;
    private Aufgabe[] aufgaben;

    @Override
	public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("TDDT");

        if (!startconfig[KATALOG].equals("")) {
            initialStart();
        } else {
            try {
                initHauptprogramm();
            } catch (IOException e) {
                System.err.println("Unable to load Resources: " + e);
            }
        }

        this.primaryStage.setOnCloseRequest(close -> {
            beenden();
        });
	}

	public static void main(String[] args) throws URISyntaxException {
        String config = "";

        try {
            String path = getCorrectPath() + "/libs/config/";

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

        //hfController.anderetext("lalilu");
        Scene scene = new Scene(hauptfenster);

        primaryStage.setScene(scene);
        katalog = startconfig[1];
        initialStart();
        for (int k=0; k<aufgaben.length;k++) {
            hfController.addAufgabe(k,aufgaben[k].getName(),aufgaben[k].getValueBabysteps());
        }

        FXMLLoader obL = new FXMLLoader(getClass().getResource("/fxml/Oberflaeche.fxml"));
        BorderPane oberflaeche = obL.load();
        ofController = obL.getController();
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

            apController.fuelleTextArea(getCorrectPath() + "/libs/aufgaben/" + nameAufgabe + "/log.txt");

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

    private void initialStart(){
        // TODO: Kerstin und David
        /* 1) Gebrauchsanweisung anzeigen
           2) Erstmalige Katalogauswahl
           3) Aufgabenauswahl
            Erstellt für 3 und 4 am besten einen public String oben in der Klasse, welcher dann vom OberflächenControllern abgefragt wird,
            damit dieser die richtigen Inhalte auswählen kann
           4) Mit entsprechenden Inhalten das Hauptprogramm wählen
          */
        //TODO: Die aufgaben.xml datei lässt sich nur aus dem hauptverzeichnis lesen (warum auch immer)
    	XMLParser parser= new XMLParser("aufgaben.xml");
		aufgaben =parser.getAufgaben();
    }

    public static String getCorrectPath() throws URISyntaxException {
        String path = CodeTester.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        path = path.substring(0,path.lastIndexOf("/"));
        path = path.substring(0,path.lastIndexOf("/"));
        path = path.substring(0,path.lastIndexOf("/"));

        return path;
    }
    public Aufgabe aktualisiereAufgabe(int k){//Wenn eine Aufgabe ausgewählt wird, wird das hier alles aktualisert
        setNameAufgabe(aufgaben[k].getName());
        return aufgaben[k];
    }

    public void setNameAufgabe(String nameAufgabe){
        this.nameAufgabe = nameAufgabe;
    }

    public void beenden() {
        startconfig[1] = aktuellerKatalog;
        startconfig[3] = nameAufgabe;

        try {
            FileWriter writer = new FileWriter(getCorrectPath() + "/libs/config/config.txt");
            for(String element : startconfig) {
                writer.write(element + "#");
            }
            writer.close();
        } catch (Exception e) {
            System.err.println("Unable to save.");
        }

        System.exit(20);
    }
}