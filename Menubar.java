package TDDT;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Menubar extends Application {

	public static void main(String[] args) {
    launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
	BorderPane pane = new BorderPane();
		
	VBox vbox = new VBox();
		
	MenuBar menubar = new MenuBar();
	
	Scene scene = new Scene(pane);
		
	vbox.getChildren().add(menubar);
		 
	pane.setTop(vbox);
 
	Menu menu = new Menu("Menü");
	MenuItem newChallenge = new MenuItem("Neue Übung");
	MenuItem exit = new MenuItem("Programm beenden");
	menu.getItems().addAll(newChallenge, exit);

	Menu catalog = new Menu("Katalog");
	MenuItem changeCatalog = new MenuItem("Katalog ändern");
	MenuItem changeTask = new MenuItem("Aufgabe ändern");
	catalog.getItems().addAll(changeCatalog, changeTask);
		 
	Menu help = new Menu("Hilfe");
	MenuItem task = new MenuItem("Aufgabenstellung anzeigen");
	help.getItems().add(task);
		 
	menubar.getMenus().addAll(menu, catalog, help);
	
	primaryStage.setTitle("TDDT");	
	primaryStage.centerOnScreen();
	primaryStage.setHeight(400.0);
	primaryStage.setWidth(400.0);	
	primaryStage.setScene(scene);
	primaryStage.show();
	}
}
