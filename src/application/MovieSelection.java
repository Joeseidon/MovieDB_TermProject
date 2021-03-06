package application;

import javax.swing.JOptionPane;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

/**
 * This class is linked with a current MovieDBAccount and
 * holds that accounts currently selected movie. This class
 * will provide access to specific aspects of the currently
 * selected movie such as video and image information.
 * 
 * @author Joseph Cutino
 * @version 1.0
 * @since 2017-07-07
 */
public class MovieSelection {
	
	/**
	 * Accounts currently selected movie. 
	 */
	private MovieDb selectedMovie;
	
	/**
	 * Current application user.
	 */
	private MovieDBAccount user;
	
	/**
	 * Movie database object used for acquiring movie specific data. 
	 */
	private TmdbMovies movieObj;
	
	/**
	 * Base URL used for trailer queries from movie database.
	 */
	private final String youTubeBaseURL = "https://www.youtube.com/watch?v=";
	
	/**
	 * Base URL used for image queries from movie database.
	 */
	private final String baseImageURL = "https://image.tmdb.org/t/p/w500";

	/**
	 * Public constructor used to create a MovieSelection instance.
	 * An instance of this class contains a user account and the
	 * currently selected movie.
	 * 
	 * @param user MovieDBAccount associated with this selection.
	 */
	public MovieSelection(final MovieDBAccount user) {
		
		this.setUser(user);
		
		this.selectedMovie = null;
		
		createMoviesObject(this.user.getTmdbApi());
	}

	/**
	 * Returns the users currently selected movie.
	 * 
	 * @param None
	 * @return selectedMovie Movie of type MovieDb.
	 */
	public MovieDb getSelectedMovie() {
		
		return selectedMovie;
	}

	/**
	 * Sets the users currently selected movie.
	 * 
	 * @param selected Of type MovieDb.
	 */
	public void setSelectedMovie(final MovieDb selected) {
		
		this.selectedMovie = selected;
	}

	/**
	 * Returns a URL that links to a YouTube trailer for the selected Movie.
	 * 
	 * @param None
	 * @return trailerURL Of type string
	 */
	public String getVidoeURL() {
		
		return youTubeBaseURL 
				+ movieObj.getVideos(this.getSelectedMovie().getId(), "English")
				.get(0).getKey();
	}

	/**
	 * Returns an image URL for the selected movie.
	 * 
	 * @param None
	 * @return imageURL Of type string
	 * 
	 */
	public String getImageURL() {
		
		return baseImageURL + this.getSelectedMovie().getPosterPath();
	}

	/**
	 * Generates a new window and plays the trailer associated
	 * with the users currently selected movie.
	 * 
	 * @param webview Media engine in which the trailer will be played.
	 */
	public void generateTrailerWindow(final WebView webview) {
		
		try {
			// Media engine used to play trailer
			webview.getEngine().load(this.getVidoeURL());
			
			// New window
			Stage stage = new Stage();

			// AnchorPane used to structure video window
			AnchorPane root2 = new AnchorPane();
			AnchorPane.setBottomAnchor(webview, 0.0);
			AnchorPane.setTopAnchor(webview, 0.0);
			AnchorPane.setLeftAnchor(webview, 0.0);
			AnchorPane.setRightAnchor(webview, 0.0);
			root2.getChildren().add(webview);

			// Place the trailer within a new
			// scene then placed in the new stage
			stage.setTitle("TrailerWindow");
			Scene trailer = new Scene(root2, 1000, 800);
			stage.setScene(trailer);
			stage.centerOnScreen();
			stage.show();
			
			// Event handler to cancel play on close
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				
				@Override
				public void handle(final WindowEvent event) {
					webview.getEngine().load(null);
				}
			});
			
		} catch (IndexOutOfBoundsException idexEx) {
			// Video trailer not found
			JOptionPane.showMessageDialog(
					null, "There is not a trailer associated with this movie.");
		}
	}

	/**
	 * Creates a movie object for later use.
	 * 
	 * @param apiKey Used to connect the movie object with the user
	 */
	private void createMoviesObject(final TmdbApi apiKey) {
		
		movieObj = new TmdbMovies(apiKey);
	}

	/**
	 * Sets the users currently selected movie.
	 * 
	 * @param user Current user to which selected movies will apply.
	 */
	public void setUser(final MovieDBAccount user) {
		
		this.user = user;
	}
}
