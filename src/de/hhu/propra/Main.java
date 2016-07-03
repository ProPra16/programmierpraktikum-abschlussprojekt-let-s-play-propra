package de.hhu.propra;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
	private Stage primaryStage;
	private BorderPane hauptfenster;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("TDDT");
		
		hauptfenster = FXMLLoader.load(getClass().getResource("view/Hauptfenster.fxml"));
		Scene scene = new Scene(hauptfenster);
		
		primaryStage.setScene(scene);
		
		BorderPane oberflaeche = FXMLLoader.load(getClass().getResource("view/Oberflaeche.fxml"));
		hauptfenster.setCenter(oberflaeche);
		
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}