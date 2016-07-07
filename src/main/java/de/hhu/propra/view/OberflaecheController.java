package de.hhu.propra.view;

import de.hhu.propra.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;

public class OberflaecheController implements OberflaecheControllerInterface{

	private Main main;
	public static boolean wechsel = false;
	
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
		//logKonsole();
		//logKonsole(CodeTester.testCode(codeTextKlasseArea.getText()), );
		//logKonsole(CodeTester.testCode(codeTextMainArea.getText()));
	}
	
	@FXML
	protected void handleCodePruefenUndWechsel() throws Exception {
		wechsel = true;
		//logKonsole(CodeTester.testCode(codeTextArea.getText()));
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
//		logKonsole(CodeTester.testCode(codeTextArea.getText()));
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
		konsoleTextArea.appendText(message);
	}
	
	public void setMain(Main main){
		this.main = main;
	}

    public void beenden(){
        // CodeTester.writeExternalFile(codeTextMainArea.getText());
        System.exit(20);
        // TODO: Katalog und Aufgabe in die Konfigdatei schreiben!
    }
}