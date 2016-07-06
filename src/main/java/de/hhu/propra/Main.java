package de.hhu.propra;

import de.hhu.propra.view.OberflaecheController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import main.xmlparser.Aufgabe;
import main.xmlparser.XMLParser;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import java.io.*;
import java.net.URISyntaxException;

public class Main extends Application {
	private Stage primaryStage;
    private BorderPane hauptfenster;
    private OberflaecheController ofController;
    private static String[] startconfig;
    private String katalog;
	private static int KATALOG = 1;

	@Override
	public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("TDDT");

        if (!startconfig[KATALOG].equals("")) {
            // initialStart();
        } else {
            try {
                initHauptprogramm();
            } catch (IOException e) {
                System.err.println("Unable to load Resources: " + e);
            }
        }

        this.primaryStage.setOnCloseRequest(close -> {
            ofController.beenden();
        });
	}

	public static void main(String[] args) throws URISyntaxException {
        String config = "";
        try {
            InputStream is = Main.class.getResourceAsStream("/config/config.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();

            while (line != null){
                config += line;
                line = reader.readLine();
            }
            startconfig = config.split("#");
            for (String element : startconfig){
                System.out.println(element);
            }
        } catch (IOException e){
            System.err.println("Unable to find config.txt: " + e);
        }
		launch(args);
	}

    private void initHauptprogramm() throws IOException {
        hauptfenster = FXMLLoader.load(getClass().getResource("/fxml/Hauptfenster.fxml"));
        Scene scene = new Scene(hauptfenster);

        primaryStage.setScene(scene);
        katalog = startconfig[1];
        BorderPane oberflaeche = FXMLLoader.load(getClass().getResource("/fxml/Oberflaeche.fxml"));
        ofController = new OberflaecheController();
        hauptfenster.setCenter(oberflaeche);

        primaryStage.show();
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
    	XMLParser parser= new XMLParser("aufgaben.xml");
		Aufgabe[] aufgaben =parser.getAufgaben();
    }
}