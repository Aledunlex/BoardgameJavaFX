package boardgame;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {
	

	
	@Override
	public void start(Stage primaryStage) {
		try {
			System.out.println("# Beginning of eco main #");
			Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/Main.fxml"));
			primaryStage.setTitle("Boardgame - Eco Game - Random Strategy");

			Scene mainScreenScene = new Scene(root);
			
			primaryStage.setScene(mainScreenScene);
			primaryStage.setResizable(false);
			primaryStage.show();
			primaryStage.centerOnScreen();


			System.out.println("\n # End of eco main #");

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}