package de.hhu.propra.view;

import de.hhu.propra.CodeTester;
import de.hhu.propra.Main;
import de.hhu.propra.Tracker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OberflaecheController implements OberflaecheControllerInterface, Initializable {

	private Main main;
	public static boolean wechsel = false;
	private static CodeTester codeTester;
	
	@FXML
	private TextArea testTextArea;
	
	@FXML
	private Button testPruefen;
	
	@FXML
	private Button testPruefenUndWechsel;
	
	@FXML
	private Button testLeeren;
	
	@FXML
	private TextArea codeTextMainArea;

	@FXML
	private TextArea codeTextKlasseArea;

	@FXML
	private TabPane codeTab;

	@FXML
	private Button codePruefen;
	
	@FXML
	private Button codePruefenUndWechsel;
	
	@FXML
	private Button codeLeeren;
	
	@FXML
	private TextArea konsoleTextArea;
	
	@FXML
	private ListView<String> fehlgeschlageneTests;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		this.codeTester = new CodeTester();
		konsoleTextArea.textProperty().bind(codeTester);
	}

	@FXML
	protected void handleTestPruefen(){

		disableTestArea();
        enableCodeArea();

		wechsel = false;
		for (int i=0;i<5;i++) {
			fehlgeschlageneTests.getItems().add(i+"");
		}
	}
	
	@FXML
	protected void handleTestPruefenUndWechsel(){
		disableTestArea();
		wechsel = true;

		for (int i=0;i<5;i++) {
			fehlgeschlageneTests.getItems().add(i+"");
		}
	}
	
	@FXML
	protected void handleTestLeeren(){
		testTextArea.setText("");
		logKonsole("Test geleert");
	}
	
	@FXML
	protected void handleCodeLeeren(){
		codeTextMainArea.setText("");
		logKonsole("Code geleert");
	}
	
	@FXML
	protected void handleCodePruefen() throws Exception{
		wechsel = false;
		List<Tab> tabs  = codeTab.getTabs();
        String code = "";
        int i = 0;
        TextArea codeArea;
        for(Tab tab : tabs){
            codeArea = (TextArea) tab.getContent();
            if (i < tabs.size()-1){
                code += "//Neue Klasse";
            }
            if (codeArea.getText().trim().length() > 0){
                code += "\n" + codeArea.getText() + "\n";
            }
            i++;
        }
        codeTester.testCode(code, tabs.get(0).getText());
	}
	
	@FXML
	protected void handleCodePruefenUndWechsel() throws Exception {

	}

	public void disableCodeArea() {
		enableTextArea();

		codeTab.setDisable(true);
		codeTab.setStyle("-fx-control-inner-background: #555555");
		codePruefen.setDisable(true);
		codePruefenUndWechsel.setDisable(true);
		codeLeeren.setDisable(true);
	}

	public void disableTestArea() {
		testTextArea.setEditable(false);
		testTextArea.setStyle("-fx-control-inner-background: #555555");
		testPruefen.setDisable(true);
		testPruefenUndWechsel.setDisable(true);
		testLeeren.setDisable(true);
		
		enableCodeArea();
	}


    private void enableTextArea(){
        testTextArea.setEditable(true);
        testTextArea.setStyle("");
        testPruefen.setDisable(false);
        testPruefenUndWechsel.setDisable(false);
        testLeeren.setDisable(false);
    }

	private void enableCodeArea(){
        codeTab.setDisable(false);

		codeTextMainArea.setEditable(true);
        codeTextKlasseArea.setEditable(true);

		codeTextKlasseArea.setStyle("");
        codeTextMainArea.setStyle("");

		codePruefen.setDisable(false);
		codePruefenUndWechsel.setDisable(false);
		codeLeeren.setDisable(false);
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

	public void reicheTrackerWeiter (Tracker tracker){
		codeTester.setTracker(tracker);
	}
}