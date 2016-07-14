package de.hhu.propra;

import de.hhu.propra.model.Aufgabe;
import de.hhu.propra.view.AnalysePopupController;
import de.hhu.propra.view.HauptfensterController;
import de.hhu.propra.view.OberflaecheController;
import de.hhu.propra.view.StartbildschirmController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
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
    private Aufgabe aktAufgabe;
    private static Tracker tracker;
    private static CodeTester codeTester;
    private static TestTester testTester;
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
        this.codeTester = ofController.getCodeTester();
        this.testTester = ofController.getTestTester();
        ofController.setMain(this);
        hauptfenster.setCenter(oberflaeche);

        tracker = new Tracker(ofController, nameAufgabe);
        hfController.setMain(this);
        ofController.reicheTrackerWeiter(tracker);

        ofController.disableAll();
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
            System.err.println("Unable to load resources: " + e);
        }
    }

    private void initialStart() throws Exception {
        FXMLLoader SL = new FXMLLoader(getClass().getResource("/fxml/Startbildschirm.fxml"));
        BorderPane startbildschirm = SL.load();
        StartbildschirmController Controller = SL.getController();
        Controller.setMain(this);

        Stage stage = new Stage();

        Scene scene = new Scene(startbildschirm);

        stage.setTitle("Startbildschirm");
        stage.centerOnScreen();
        stage.setHeight(250.0);
        stage.setWidth(500.0);
        stage.setScene(scene);
        Controller.setStage(stage);
        stage.setAlwaysOnTop(false);
        stage.setOnCloseRequest(close ->{
            System.exit(20);
        });
        stage.showAndWait();
        if (Controller.isKatalogausgewaehlt()) return;
    }


    public boolean katalogLaden(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Katalogdatei auswählen");

        File tempFile = fileChooser.showOpenDialog(primaryStage);

        if (tempFile != null){
            setAktuellerKatalog(tempFile);
            return true;
        }
        return false;
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
        // String path = getCorrectPath() + "/aufgaben/aufgaben.xml";
        XMLParser parser= new XMLParser(katalog);
        aufgaben = parser.getAufgaben();
    }

    public Aufgabe aktualisiereAufgabe(int k){//Wenn eine Aufgabe ausgewählt wird, wird das hier alles aktualisert
        setNameAufgabe(aufgaben[k].getName());
        aktAufgabe = aufgaben[k];
        ofController.aktualisiereCodeTab(aufgaben[k]);
        ofController.aktualisiereTestTextArea(aufgaben[k]);
        codeTester.setDateiname(aufgaben[k].getKlassen()[0].getName());
        ofController.enableNecessary();
        tracker.setMillisBeiLetztemWechsel(System.currentTimeMillis());
        return aufgaben[k];
    }

    public void setNameAufgabe(String nameAufgabe){
        testTester.setNameAufgabe(nameAufgabe);
        tracker.setNameAufgabe(nameAufgabe);
        codeTester.setNameAufgabe(nameAufgabe);
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
            codeTester.writeExternalFile(ofController.getCode());
            testTester.writeTest(ofController.getTestCode());
            tracker.phasenWechselMerken(ofController.getAktuellePhase());
            tracker.aktuellerStandtoFile();

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
            System.err.println("Unable to save: " + e);
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
                        name = "";
                        code = "";
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

    public Aufgabe getAktAufgabe(){
        return this.aktAufgabe;
    }
}