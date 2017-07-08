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
 * @author Joseph Cutino
 * @version 1.0
 * @since 2016-07-07
 */
public class MovieSelection {
	private MovieDb selectedMovie;
	private MovieDBAccount user;
	private TmdbMovies movieObj;
	private String youTubeBaseURL = "https://www.youtube.com/watch?v=";
	private String baseImageURL = "https://image.tmdb.org/t/p/w500";

	/**
	 * Public constructor used to create a MovieSelection instance. An instance
	 * of this class contains a user account and he currently selected movie.
	 * This constructor is overloaded to apply a selected movie on
	 * instantiation.
	 * 
	 * @param user
	 *            MovieDBAccount associated with this selection.
	 * @param selectedMovie
	 *            MovieDb currently selected by the user.
	 * @return None
	 */
	public MovieSelection(MovieDBAccount user, MovieDb selection) {
		this.setUser(user);
		selectedMovie = selection;
		createMoviesObject(this.user.getTmdbApi());
	}

	/**
	 * Public constructor used to create a MovieSelection instance. An instance
	 * of this class contains a user account and the currently selected movie.
	 * 
	 * @param user
	 *            MovieDBAccount associated with this selection.
	 * @param selectedMovie
	 *            MovieDb currently selected by the user.
	 * @return None
	 */
	public MovieSelection(MovieDBAccount user) {
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
	 * @param selectedMovie
	 *            Of type MovieDb.
	 * @return None
	 */
	public void setSelectedMovie(MovieDb selected) {
		this.selectedMovie = selected;
	}

	/**
	 * Returns a URL that links to a youtube trailer for the selected Movie.
	 * 
	 * @param None
	 * @return trailerURL Of type string
	 */
	public String getVidoeURL() {
		/*
		 * TODO: ADD protection (perferably an execption throw) for indexing a
		 * video that doesnt exist. As in there are no videos for the selected
		 * movie.
		 */
		return youTubeBaseURL + movieObj.getVideos(this.getSelectedMovie().getId(), "English").get(0).getKey();
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
	 * Generates a new window and plays the trailer associated with the users
	 * currently selected movie.
	 * 
	 * @param Webview
	 *            Media engine in which the trailer will be played.
	 * @return None
	 */
	public void generateTrailerWindow(WebView webview) {
		try {
			// media engine used to play trailer
			webview.getEngine().load(this.getVidoeURL());
			// new window
			Stage stage = new Stage();

			// Anchorpane used to structure video window
			AnchorPane root2 = new AnchorPane();
			AnchorPane.setBottomAnchor(webview, 0.0);
			AnchorPane.setTopAnchor(webview, 0.0);
			AnchorPane.setLeftAnchor(webview, 0.0);
			AnchorPane.setRightAnchor(webview, 0.0);
			root2.getChildren().add(webview);

			// place the trailer within a new scene then placed in the new stage
			stage.setTitle("TrailerWindow");
			Scene Trailer = new Scene(root2, 1000, 800);
			stage.setScene(Trailer);
			stage.centerOnScreen();
			stage.show();
			// event handler to cancel play on close
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					webview.getEngine().load(null);
				}
			});
		} catch (IndexOutOfBoundsException idexEx) {
			// video trailer not found
			JOptionPane.showMessageDialog(null, "There is not a trailer associated with this movie.");
		}
	}

	/**
	 * This function only provides the first keyword in the current selections
	 * list of keywords. This may not be enough for search. Consider overloading
	 * function to provide an arraylist of keywords to expand search.
	 *
	 * @param None
	 * @return keyword The first keyword provided in the list of keywords
	 *         provided by the API
	 */
	public String getSelectionKeyWord() {
		return movieObj.getKeywords(this.getSelectedMovie().getId()).get(0).getName().toString();
	}

	/**
	 * Creates a movie object for later use
	 * 
	 * @param APIKey
	 *            Used to connect the movie object with the user
	 * @return None
	 */
	private void createMoviesObject(TmdbApi Apikey) {
		movieObj = new TmdbMovies(Apikey);
	}

	/**
	 * Returns the user to which this movie selection belongs.
	 * 
	 * @param None
	 * @return currentUser The user to which the current movie selection
	 *         belongs.
	 */
	public MovieDBAccount getUser() {
		return user;
	}

	/**
	 * Sets the users currently selected movie.
	 * 
	 * @param user
	 *            Current user to which selected movies will apply.
	 * @return None
	 */
	public void setUser(MovieDBAccount user) {
		this.user = user;
	}
}
