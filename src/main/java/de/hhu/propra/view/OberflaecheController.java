package main.java.de.hhu.propra.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import main.java.de.hhu.propra.CodeTester;

public class OberflaecheController {
	@FXML
	private TextArea testTextArea;
	
	@FXML
	private Button testPruefen;
	
	@FXML
	private Button testLeeren;
	
	@FXML
	private TextArea codeTextArea;
	
	@FXML
	private Button codePruefen;
	
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
	protected void handleCodePruefen(){
		//testTextArea.setEditable(true);
		//testTextArea.setStyle("");
		//testPruefen.setDisable(false);
		//testLeeren.setDisable(false);
		
		//codeTextArea.setEditable(false);
		//codeTextArea.setStyle("-fx-control-inner-background: #555555");
		//codePruefen.setDisable(true);
		//codeLeeren.setDisable(true);
		
		logKonsole(CodeTester.testCode(codeTextArea.getText()));
		
	}
	
	public void logKonsole (String message){
		konsoleTextArea.setText(message);
	}
}
