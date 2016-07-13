package de.hhu.propra.view;

import de.hhu.propra.CodeTester;
import de.hhu.propra.Main;
import de.hhu.propra.TestTester;
import de.hhu.propra.Tracker;
import de.hhu.propra.model.Aufgabe;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class OberflaecheController implements OberflaecheControllerInterface, Initializable {

	private Main main;
	public static boolean wechsel = false;

	private boolean test = true;
	private boolean code = false;
    private String letzterStandCode="";
    private String letzterStandCodeBS ="";
	private String letzterStandTestCode="";
    private boolean refactor = false;
	private static CodeTester codeTester;
	private static TestTester testTester;
    private static Tracker tracker;

	private boolean babysteps = true;
	private boolean babystepsFail = false;
	public static int start = 10;
	private IntegerProperty babytime = new SimpleIntegerProperty(start);
	private Timeline babystepsAnimation = new Timeline();

	@FXML
	private Button phaseWechseln;

	@FXML
	private Button pruefen;

	@FXML
	private Button leeren;

	@FXML
	private ImageView phasenIcon;

	@FXML
	private TextArea testTextArea;

	@FXML
	private TabPane codeTab;
	
	@FXML
	private TextArea konsoleTextArea;
	
	@FXML
	private ListView<String> fehlgeschlageneTests;

	@FXML
	private Label timerLabel;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		this.codeTester = new CodeTester();
		this.testTester = new TestTester();
		konsoleTextArea.textProperty().bind(codeTester);
		setButtonTextTest();
		fuelleCodeTab();
		Image image = new Image("test.png");
		phasenIcon.setImage(image);
		starteTimer();
	}

	public void starteTimer(){
		if (babysteps) {
			if (babystepsAnimation != null) {
				babystepsAnimation.stop();
			}
			timerLabel.textProperty().bind(babytime.asString());
			babytime.set(start);
			babystepsAnimation = new Timeline();
			babystepsAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(start+1), new KeyValue(babytime, 0)));
			babystepsAnimation.playFromStart();
			if (babytime.getValue()==0){
				babystepsFail = true;
			}
		}
	}

	public void fuelleCodeTab(){
		Tab testMain = new Tab("TestMain");
		TextArea codeTextMainArea = new TextArea("code");
		testMain.setContent(codeTextMainArea);

		Tab testClass = new Tab("TestKlasse");
		TextArea codeTextKlasseArea = new TextArea("code");
		testClass.setContent(codeTextKlasseArea);

		codeTab.getTabs().add(testMain);
		codeTab.getTabs().add(testClass);
	}

	public void aktualisiereCodeTab(Aufgabe aktaufgabe){
		codeTab.getTabs().clear();
		letzterStandCode = "";
        if (main.schonBearbeitet(aktaufgabe.getName())){
            HashMap<String, String> klassen = main.getCode(aktaufgabe.getName(), aktaufgabe.getKlassen()[0].getName());
            for (String key : klassen.keySet()){
                Tab temp = new Tab(key);
                temp.setContent(new TextArea(klassen.get(key)));
				letzterStandCode += "//Neue Klasse " + key + "\n" + klassen.get(key);
                codeTab.getTabs().add(temp);
            }
            try {
                BufferedReader reader = new BufferedReader(new FileReader(main.getCorrectPath() + "/aufgaben/" + aktaufgabe.getName() + "/trackerstand.txt"));
                tracker.setMillisInRed(Long.valueOf(reader.readLine()));
                tracker.setMillisInGreen(Long.valueOf(reader.readLine()));
                tracker.setMillisInRefactor(Long.valueOf(reader.readLine()));
                tracker.setMillisBeiLetztemWechsel(System.currentTimeMillis());
            } catch (Exception e){
                codeTester.setKonsolenText("Trackerstand konnte nicht geladen werden: " + e);
            }
        } else {
            for (int i = 0; i < aktaufgabe.getKlassen().length; i++) {
                Tab temp = new Tab(aktaufgabe.getKlassen()[i].getName());
                temp.setContent(new TextArea(aktaufgabe.getKlassen()[i].getText()));
				letzterStandCode += "//Neue Klasse " + aktaufgabe.getKlassen()[i].getName() + "\n" + aktaufgabe.getKlassen()[i].getText();
                codeTab.getTabs().add(temp);
            }
			for (int i = 0; i < aktaufgabe.getInterfaace().length; i++) {
				Tab temp = new Tab(aktaufgabe.getInterfaace()[i].getName());
				temp.setContent(new TextArea(aktaufgabe.getInterfaace()[i].getText()));
				letzterStandCode += "//Neues Interface " + aktaufgabe.getInterfaace()[i].getName() + "\n" + aktaufgabe.getInterfaace()[i].getText();
				codeTab.getTabs().add(temp);
			}
        }
		codeTester.setLetzterStandCode(letzterStandCode);
	}
	public void aktualisieretestTextArea(Aufgabe aktaufgabe){
		testTextArea.setText(aktaufgabe.getTest().getText());
    }

	public void setButtonTextTest(){
		Label phaseLabel = new Label("Phase wechseln");
		phaseLabel.setRotate(-90);
		phaseWechseln.setGraphic(new Group(phaseLabel));

		Label pruefenLabel = new Label("Test prüfen");
		pruefenLabel.setRotate(-90);
		pruefen.setGraphic(new Group(pruefenLabel));

		Label leerenLabel = new Label("Test leeren");
		leerenLabel.setRotate(-90);
		leeren.setGraphic(new Group(leerenLabel));
	}

	public void setButtonTextCode(){
		Label phaseLabel = new Label("Phase wechseln");
		phaseLabel.setRotate(90);
		phaseWechseln.setGraphic(new Group(phaseLabel));

		Label pruefenLabel = new Label("Code prüfen");
		pruefenLabel.setRotate(90);
		pruefen.setGraphic(new Group(pruefenLabel));

		Label leerenLabel = new Label("Code leeren");
		leerenLabel.setRotate(90);
		leeren.setGraphic(new Group(leerenLabel));
	}

	public void disableCodeArea() {
		enableTextArea();
		for(Tab tab : codeTab.getTabs()){
			TextArea codeArea = (TextArea) tab.getContent();
			codeArea.setStyle("-fx-control-inner-background: #555555");
			codeArea.setEditable(false);
		}
	}

	public void disableTestArea() {
		testTextArea.setEditable(false);
		testTextArea.setStyle("-fx-control-inner-background: #555555");
		enableCodeArea();
	}


    private void enableTextArea(){
        testTextArea.setEditable(true);
        testTextArea.setStyle("");
    }

	private void enableCodeArea(){
        codeTab.setDisable(false);
		for (Tab tab : codeTab.getTabs()) {
			TextArea codeArea = (TextArea) tab.getContent();
			codeArea.setEditable(true);
			codeArea.setStyle("");
		}
	}
	
	public void logKonsole (String message){
		konsoleTextArea.setText(message);
	}

	public void appendKonsoleText(String message) {
		konsoleTextArea.setText(konsoleTextArea.getText() + "\n" + message);
	}
	
	public void setMain(Main main){
		this.main = main;
	}

    public void handlePruefen() {
        wechsel = false;
		List<Tab> tabs  = codeTab.getTabs();
        if (test){
            // TODO fehlgeschlagene Tests in die Liste einfügen
			// leert die Liste damit neu befüllt werden kann
			fehlgeschlageneTests.getItems().clear();
			try{testTester.testeTests(testTextArea.getText(),tabs.get(0).getText());

			}
			catch (Exception e){

			}

        } else {

            String code = "";
            int i = 0;
            TextArea codeArea;
            for(Tab tab : tabs){
                codeArea = (TextArea) tab.getContent();
                if (i > 0){
                    String codeOhnePublicSubklassen = "";
                    if(codeArea.getText().contains("public class")) {
                        codeOhnePublicSubklassen = codeArea.getText().replace("public class", "class");
                        codeArea.setText(codeOhnePublicSubklassen);
                    } else if(codeArea.getText().contains("public abstract class")) {
                        codeOhnePublicSubklassen = codeArea.getText().replace("public abstract class", "abstract class");
                        codeArea.setText(codeOhnePublicSubklassen);
                    } else if(codeArea.getText().contains("public interface")){
                        codeOhnePublicSubklassen = codeArea.getText().replace("public interface", "interface");
                        codeArea.setText(codeOhnePublicSubklassen);
                    }
                }
                if (i < tabs.size()){
                    code += "//Neue Klasse " + tab.getText();
                }
                if (codeArea.getText().trim().length() > 0){
                    code += "\n" + codeArea.getText() + "\n";
                }
                i++;
            }
            codeTester.testCode(code, tabs.get(0).getText());
        }
    }

    public void handleWechseln() {
        // TODO wechsel nur dann true wenn es okay ist zu wechseln
        wechsel=true;
        if (test) {
			babystepsFail = false;
			Image image = new Image("code.png");
			phasenIcon.setImage(image);
            setButtonTextCode();
            disableTestArea();
            test = false;
            code = true;
            tracker.phasenWechselMerken("red");
            tracker.logPhasenWechsel("red");
			starteTimer();

            letzterStandCodeBS = "";
            letzterStandCodeBS = testTextArea.getText();
        }
        else if (code) {
			Image image = new Image("refactor.png");
			phasenIcon.setImage(image);
            code = false;
            refactor = true;
			if (babystepsAnimation != null) {
				babystepsAnimation.stop();
			}
			babystepsAnimation = null;
			babytime.set(0);
            tracker.phasenWechselMerken("green");
            tracker.logPhasenWechsel("green");
            codeTester.setGetestetUndFehlerfrei(false);
            letzterStandCodeBS = "";
            for (Tab tab : codeTab.getTabs()){
                TextArea inhalt = (TextArea) tab.getContent();
                letzterStandCodeBS += inhalt.getText();
            }
            //TODO: wechseln zu refactor wenn code okay (Bem von Freddy: Wechseln, wenn code okay, oder wenn Test nicht mehr fehlschlagen?!)
        }
        else {
			babystepsFail = false;
			Image image = new Image("test.png");
			phasenIcon.setImage(image);
            disableCodeArea();
            setButtonTextTest();
            test=true;
            refactor = false;
            tracker.phasenWechselMerken("refactor");
            tracker.logPhasenWechsel("refactor");
			starteTimer();
            letzterStandCodeBS = "";
            for (Tab tab : codeTab.getTabs()){
                TextArea inhalt = (TextArea) tab.getContent();
                letzterStandCodeBS += inhalt.getText();
            }
        }
    }

	public void handleLeeren() {
		wechsel = false;
		if (test) {
			// TODO auf Anfang zurücksetzen, e.g. testTextArea.setText(test)
			testTextArea.setText("");
		} else {
			// TODO auf Anfang zurücksetzen, e.g. codeTextArea.setText(code)
			for (Tab tab : codeTab.getTabs()) {
				TextArea codeArea = (TextArea) tab.getContent();
				codeArea.setText("");
			}
		}
	}

    public void reicheTrackerWeiter (Tracker tracker){
        this.tracker = tracker;
        codeTester.setTracker(tracker);
		testTester.setTracker(tracker);
    }

    public String getAktuellePhase(){
        if(test){
            return "red";
        } else if(code){
            return "green";
        } else if (refactor){
            return "refactor";
        }
        return "";
    }

    public void reicheMainWeiter(Main main){
        this.main = main;
		codeTester.setMain(main);
    }

	public CodeTester getCodeTester(){
		return codeTester;
	}
}