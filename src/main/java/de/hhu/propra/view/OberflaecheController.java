package de.hhu.propra.view;

import de.hhu.propra.CodeTester;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class OberflaecheController implements OberflaecheControllerInterface{

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
	private TextArea codeTextArea;
	
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
		
		testTextArea.setEditable(false);
		testTextArea.setStyle("-fx-control-inner-background: #555555");
		testPruefen.setDisable(true);
		testLeeren.setDisable(true);
		
		codeTextArea.setEditable(true);
		codeTextArea.setStyle("");
		codePruefen.setDisable(false);
		codeLeeren.setDisable(false);

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
		codeTextArea.setText("");
		logKonsole("Code geleert");
	}
	
	@FXML
	protected void handleCodePruefen() throws Exception{
		wechsel = false;
		logKonsole(CodeTester.testCode(codeTextArea.getText()));
	}
	
	@FXML
	protected void handleCodePruefenUndWechsel() throws Exception {
		wechsel = true;
		logKonsole(CodeTester.testCode(codeTextArea.getText()));
	}

	public void disableCodeArea() {
		testTextArea.setEditable(true);
		testTextArea.setStyle("");
		testPruefen.setDisable(false);
		testPruefenUndWechsel.setDisable(false);
		testLeeren.setDisable(false);
		
		codeTextArea.setEditable(false);
		codeTextArea.setStyle("-fx-control-inner-background: #555555");
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
	
	private void enableCodeArea(){
		codeTextArea.setEditable(true);
		codeTextArea.setStyle("");
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
	
	
}