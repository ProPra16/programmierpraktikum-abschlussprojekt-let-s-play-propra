package de.hhu.propra.view;

import de.hhu.propra.CodeTester;
import de.hhu.propra.Main;
import de.hhu.propra.Tracker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
	private TextArea testTextArea;
	
	//@FXML
	//private Button testPruefen;
	
	//@FXML
	//private Button testPruefenUndWechsel;
	
	//@FXML
	//private Button testLeeren;
	
	@FXML
	private TextArea codeTextMainArea;

	@FXML
	private TextArea codeTextKlasseArea;

	@FXML
	private TabPane codeTab;

	//@FXML
	//private Button codePruefen;
	
	//@FXML
	//private Button codePruefenUndWechsel;
	
	//@FXML
	//private Button codeLeeren;
	
	@FXML
	private TextArea konsoleTextArea;
	
	@FXML
	private ListView<String> fehlgeschlageneTests;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		this.codeTester = new CodeTester();
		konsoleTextArea.textProperty().bind(codeTester);
		setWechselButtonText();
	}

	/*@FXML
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
        for(Tab tab : tabs){
            TextArea codeArea = (TextArea) tab.getContent();
            code += "\n//NEUE KLASSSE\n";
            code += codeArea.getText();
        }
        codeTester.testCode(code, tabs.get(0).getText());
	}
	
	@FXML
	protected void handleCodePruefenUndWechsel() throws Exception {

	}*/

	public void setWechselButtonText(){
		Label label = new Label("Phase wechseln");
		label.setRotate(-90);
		phaseWechseln.setGraphic(new Group(label));
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
		/*codePruefen.setDisable(true);
		codePruefenUndWechsel.setDisable(true);
		codeLeeren.setDisable(true);*/
	}

	public void disableTestArea() {
		testTextArea.setEditable(false);
		testTextArea.setStyle("-fx-control-inner-background: #555555");
		/*testPruefen.setDisable(true);
		testPruefenUndWechsel.setDisable(true);
		testLeeren.setDisable(true);*/
		
		enableCodeArea();
	}


    private void enableTextArea(){
        testTextArea.setEditable(true);
        testTextArea.setStyle("");
        /*testPruefen.setDisable(false);
        testPruefenUndWechsel.setDisable(false);
        testLeeren.setDisable(false);*/
    }

	private void enableCodeArea(){
        codeTab.setDisable(false);

		codeTextMainArea.setEditable(true);
        codeTextKlasseArea.setEditable(true);

		codeTextKlasseArea.setStyle("");
        codeTextMainArea.setStyle("");

		/*codePruefen.setDisable(false);
		codePruefenUndWechsel.setDisable(false);
		codeLeeren.setDisable(false);*/
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
			test = false;
			code = true;
		}
		else if (code) {
			code=false;
			//TODO: wechseln zu refactor wenn code okay
		}
		else {
			disableCodeArea();
			test=true;
		}
	}

	public void handleLeeren() {
		wechsel = false;
		if(test){
			// TODO auf Anfang zurücksetzen, e.g. testTextArea.setText(test)
			testTextArea.setText("");
		} else {
			// TODO auf Anfang zurücksetzen, e.g. codeTextArea.setText(code)
			for(Tab tab : codeTab.getTabs()){
				TextArea codeArea = (TextArea) tab.getContent();
				codeArea.setText("");
			}
		}
	}
}