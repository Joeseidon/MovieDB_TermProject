package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Application file.
 * 
 * @author Joshua
 * @version 1.0
 * @since 2017-11-7
 */
public class App extends Application {

	@Override
	public void start(final Stage primaryStage) {
		try {
			// Read file FXML and draw interface.
			Parent root = FXMLLoader.load(getClass().
				getResource("/application/MainScene.fxml"));
			
			primaryStage.setTitle("Movie Gen 7000");
			primaryStage.setScene(new Scene(root));
			//primaryStage.setFullScreen(true);
			primaryStage.show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Main function for the application.
	 * 
	 * @param args Command line arguments
	 */
	public static void main(final String[] args) {
		launch(args);
	}
}
