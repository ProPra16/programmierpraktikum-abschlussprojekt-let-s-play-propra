package de.hhu.propra.view;

import de.hhu.propra.CodeTester;
import de.hhu.propra.Main;
import de.hhu.propra.Tracker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OberflaecheController implements OberflaecheControllerInterface, Initializable {

	private Main main;
	public static boolean wechsel = false;

	private boolean test = true;
	private boolean code = false;
	private CodeTester codeTester;

	@FXML
	private Button phaseWechseln;

	@FXML
	private Button pruefen;

	@FXML
	private Button leeren;

	@FXML
	private TextArea testTextArea;
	
	/*@FXML
	private TextArea codeTextMainArea;

	@FXML
	private TextArea codeTextKlasseArea;*/

	@FXML
	private TabPane codeTab;
	
	@FXML
	private TextArea konsoleTextArea;
	
	@FXML
	private ListView<String> fehlgeschlageneTests;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		this.codeTester = new CodeTester();
		konsoleTextArea.textProperty().bind(codeTester);
		setButtonTextTest();
		fuelleCodeTab();
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

		//codeTab.setDisable(true);
		//codeTab.setStyle("-fx-control-inner-background: #555555");
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
		/*codeTextMainArea.setEditable(true);
        codeTextKlasseArea.setEditable(true);

		codeTextKlasseArea.setStyle("");
        codeTextMainArea.setStyle("");*/
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

    public void beenden(){
        // CodeTester.writeExternalFile(codeTextMainArea.getText());
		// codeTester.speichern();
        System.exit(20);
        // TODO: Katalog und Aufgabe in die Konfigdatei schreiben!
    }

	public void handlePruefen() {
		wechsel = false;
		if (test){
			// TODO fehlgeschlagene Tests in die Liste einfügen
			for (int i=0;i<5;i++) {
				fehlgeschlageneTests.getItems().add(i+"");
			}
		} else {
			List<Tab> tabs  = codeTab.getTabs();
			String code = "";
			for(Tab tab : tabs){
				TextArea codeArea = (TextArea) tab.getContent();
				code += "\n//NEUE KLASSSE\n";
				code += codeArea.getText();
			}
			codeTester.testCode(code, tabs.get(0).getText());
		}
	}

	public void handleWechseln() {
		// TODO wechsel nur dann true wenn es okay ist zu wechseln
		wechsel=true;
		if (test) {
			disableTestArea();
			setButtonTextCode();
			test = false;
			code = true;
		}
		else if (code) {
			code=false;
			//TODO: wechseln zu refactor wenn code okay
		}
		else {
			disableCodeArea();
			setButtonTextTest();
			test=true;
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
		codeTester.setTracker(tracker);
	}
}