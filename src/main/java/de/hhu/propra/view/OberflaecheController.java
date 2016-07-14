package de.hhu.propra.view;

import de.hhu.propra.*;
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
	private Label timerLabel;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		this.codeTester = new CodeTester();
		this.testTester = new TestTester();
		konsoleTextArea.textProperty().bind(testTester);
		setButtonTextTest();
		fuelleCodeTab();
		Image image = new Image("test.png");
		phasenIcon.setImage(image);
		starteTimer();
		enableCodeArea();
		disableCodeArea();
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

		codeTab.getTabs().add(testMain);
	}

	public void aktualisiereCodeTab(Aufgabe aktaufgabe){
		codeTab.getTabs().clear();
		letzterStandCode = "";
        if (main.schonBearbeitet(aktaufgabe.getName())) {
            File hauptklasse = new File(main.getCorrectPath() + "/aufgaben/" + aktaufgabe.getName() + "/" + aktaufgabe.getKlassen()[0].getName() + ".java");
            if (hauptklasse.exists()) {
                HashMap<String, String> klassen = main.getCode(aktaufgabe.getName(), aktaufgabe.getKlassen()[0].getName());
                for (String key : klassen.keySet()) {
                    Tab temp = new Tab(key);
                    temp.setContent(new TextArea(klassen.get(key)));
                    letzterStandCode += "//Neue Klasse " + key + "\n" + klassen.get(key);
                    codeTab.getTabs().add(temp);
                }
            } else {
                ladeAufgabeGrundzustand(aktaufgabe);
            }
            try {
                BufferedReader reader = new BufferedReader(new FileReader(main.getCorrectPath() + "/aufgaben/" + aktaufgabe.getName() + "/trackerstand.txt"));
                tracker.setMillisInRed(Long.valueOf(reader.readLine()));
                tracker.setMillisInGreen(Long.valueOf(reader.readLine()));
                tracker.setMillisInRefactor(Long.valueOf(reader.readLine()));
                tracker.setMillisBeiLetztemWechsel(System.currentTimeMillis());
            } catch (Exception e) {
                codeTester.setKonsolenText("Trackerstand konnte nicht geladen werden: " + e);
            }
            return;
        } else {
            ladeAufgabeGrundzustand(aktaufgabe);
        }

		disableCodeArea();
        code = false;
        refactor = false;
        test = true;
		codeTester.setLetzterStandCode(letzterStandCode);
	}

    private void ladeAufgabeGrundzustand(Aufgabe aktaufgabe){
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

	public void aktualisiereTestTextArea(Aufgabe aktaufgabe){

		testTextArea.clear();
		letzterStandTestCode = "";
		if (main.schonBearbeitet(aktaufgabe.getName())) {
			letzterStandCode += aktaufgabe.getTest().getText();
		} else{
			testTextArea.setText(aktaufgabe.getTest().getText());
		}
		disableCodeArea();
		code = false;
		refactor = false;
		test = true;
		testTester.setLetzterStandTestCode(letzterStandTestCode);
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

        if (test){
			try{
				testTester.testeTests(testTextArea.getText(),main.getNameAufgabe());
			}
			catch (Exception e){
				System.out.println(e.toString());
			}
        } else {
            String code = getCode();
            codeTester.testCode(code, codeTab.getTabs().get(0).getText());
        }
    }

    public String getCode(){
        List<Tab> tabs  = codeTab.getTabs();
        String code = "";
        int i = 0;
        TextArea codeArea;
        for(Tab tab : tabs) {
            codeArea = (TextArea) tab.getContent();
            if (i > 0) {
                String codeOhnePublicSubklassen = "";
                if (codeArea.getText().contains("public class")) {
                    codeOhnePublicSubklassen = codeArea.getText().replace("public class", "class");
                    codeArea.setText(codeOhnePublicSubklassen);
                } else if (codeArea.getText().contains("public abstract class")) {
                    codeOhnePublicSubklassen = codeArea.getText().replace("public abstract class", "abstract class");
                    codeArea.setText(codeOhnePublicSubklassen);
                } else if (codeArea.getText().contains("public interface")) {
                    codeOhnePublicSubklassen = codeArea.getText().replace("public interface", "interface");
                    codeArea.setText(codeOhnePublicSubklassen);
                }
            }
            if (i < tabs.size()) {
                code += "//Neue Klasse " + tab.getText();
            }
            if (codeArea.getText().trim().length() > 0) {
                code += "\n" + codeArea.getText() + "\n";
            }
            i++;
        }
        return code;
    }

    public void handleWechseln() {
        // TODO wechsel nur dann true wenn es okay ist zu wechseln
        wechsel=true;
        if (test) {
			if (TestTester.getAnzahlFehlerhaft()==1) {


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
				konsoleTextArea.textProperty().bind(codeTester);
			}
			else {
				testTester.setKonsolenText("Es darf nur genau ein Test fehlschlagen!");
			}
        }
        else if (code) {
			if (codeTester.isGetestetUndFehlerfrei()) {
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
				for (Tab tab : codeTab.getTabs()) {
					TextArea inhalt = (TextArea) tab.getContent();
					letzterStandCodeBS += inhalt.getText();
				}
			}
			else if (testTester.getAnzahlFehlerhaft()==0){
				codeTester.setKonsolenText("Der Code muss compiliert und fehlerfrei sein.");
			}
            //TODO: wechseln zu refactor wenn code okay (Bem von Freddy: Wechseln, wenn code okay, oder wenn Test nicht mehr fehlschlagen?!)
        } else {
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
			konsoleTextArea.textProperty().bind(testTester);
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

	public CodeTester getCodeTester(){
		return codeTester;
	}

	public void disableAll(){
		codeTab.setDisable(true);
		testTextArea.setDisable(true);
		pruefen.setDisable(true);
		phaseWechseln.setDisable(true);
		leeren.setDisable(true);
	}

    public void enableNecessary(){
        pruefen.setDisable(false);
        phaseWechseln.setDisable(false);
        leeren.setDisable(false);
        testTextArea.setDisable(false);
        codeTab.setDisable(false);
        disableCodeArea();
    }
}