package de.hhu.propra.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class OberflaecheController {
	@FXML
	private TextArea testTextArea;
	
	@FXML
	private Button testPrüfen;
	
	@FXML
	private Button testLeeren;
	
	@FXML
	private TextArea codeTextArea;
	
	@FXML
	private Button codePrüfen;
	
	@FXML
	private Button codeLeeren;
	
	@FXML
	private ListView<String> fehlgeschlageneTests;
	
	@FXML
	protected void handleTestPrüfen(){
		testTextArea.setEditable(false);
		testTextArea.setStyle("-fx-control-inner-background: #555555");
		testPrüfen.setDisable(true);
		testLeeren.setDisable(true);
		
		codeTextArea.setEditable(true);
		codeTextArea.setStyle("");
		codePrüfen.setDisable(false);
		codeLeeren.setDisable(false);
		for (int i=0;i<5;i++) {
			fehlgeschlageneTests.getItems().add(i+"");
		}
		System.out.println("Test prüfen");
	}
	
	@FXML
	protected void handleTestLeeren(){
		testTextArea.setText("");
		System.out.println("Test geleert");
	}
	
	@FXML
	protected void handleCodeLeeren(){
		codeTextArea.setText("");
		System.out.println("Code geleert");
	}
	
	@FXML
	protected void handleCodePrüfen(){
		testTextArea.setEditable(true);
		testTextArea.setStyle("");
		testPrüfen.setDisable(false);
		testLeeren.setDisable(false);
		
		codeTextArea.setEditable(false);
		codeTextArea.setStyle("-fx-control-inner-background: #555555");
		codePrüfen.setDisable(true);
		codeLeeren.setDisable(true);
		System.out.println("Code prüfen");
	}
}
