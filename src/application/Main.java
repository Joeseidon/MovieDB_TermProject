package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

/**
 * Main file for the project.
 * @author Joshua
 * @version 1.0
 * @since 2017-11-7
 */
public class Main extends Application {
	@Override
	public void start(final Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root, 400, 400);
			scene.getStylesheets().add(getClass().
				getResource("application.css").
					toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Main function for project.
	 * @param args command line arguments
	 */
	public static void main(final String[] args) {
		launch(args);
	}
}
