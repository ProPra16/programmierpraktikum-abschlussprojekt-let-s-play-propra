/* TODO 
 * - Toolbar einbauen
 * - Echte Tests/Codes anzeigen
 * - Fehlgeschlagene Tests/Codes -> Auswirkung auf Buttons
 * - ListView leeren bei erneutem Testen
 */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class TDDTOberfläche extends Application{
	@Override
	public void start(Stage primaryStage) throws Exception {
		Pane root = (Pane)FXMLLoader.load(getClass().getResource("TDDTDesign.fxml"));
		Scene scene = new Scene(root);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("TDDT");
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
