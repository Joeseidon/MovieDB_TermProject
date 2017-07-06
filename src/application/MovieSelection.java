package application;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/*
 * @author Joseph
 */
public class MovieSelection {
	private MovieDb selectedMovie;
	private MovieDBAccount user;
	private TmdbMovies movieObj;
	private String youTubeBaseURL = "https://www.youtube.com/watch?v=";
	private String baseImageURL = "https://image.tmdb.org/t/p/w500";

	/*
	 * Public constructor used to create a MovieSelection instance.
	 * An instance of this class contains a user account and he currently 
	 * selected movie.
	 * @Parameters MovieDBAccount user, MovieDb selection 
	 * @author Joseph
	 */
	public MovieSelection(MovieDBAccount user, MovieDb selection) {
		this.setUser(user);
		selectedMovie = selection;
		createMoviesObject(this.user.getTmdbApi());
	}
	
	/*
	 * @author Joseph
	 */
	public MovieSelection(MovieDBAccount user){
		this.setUser(user);
		this.selectedMovie = null;
		createMoviesObject(this.user.getTmdbApi());
	}

	/*
	 * @author Joseph
	 */
	public MovieDb getSelectedMovie() {
		return selectedMovie;
	}

	/*
	 * @author Joseph
	 */
	public void setSelectedMovie(MovieDb selected) {
		this.selectedMovie = selected;
	}

	/*
	 * TODO: ADD protection (perferably an execption throw) for indexing a video that doesnt exist. As in there 
	 * are no videos for the selected movie.
	 * @author Joseph
	 */
	public String getVidoeURL() {
		return youTubeBaseURL + movieObj.getVideos(this.getSelectedMovie().getId(), "English").get(0).getKey();
	}

	/*
	 * @author Joseph
	 */
	public String getImageURL() {
		return baseImageURL + this.getSelectedMovie().getPosterPath();
	}

	/*
	 * @author Joseph
	 */
	public void generateTrailerWindow(WebView webview) {

		webview.getEngine().load(this.getVidoeURL());

		Stage stage = new Stage();

		AnchorPane root2 = new AnchorPane();
		root2.setBottomAnchor(webview, 0.0);
		root2.setTopAnchor(webview, 0.0);
		root2.setLeftAnchor(webview, 0.0);
		root2.setRightAnchor(webview, 0.0);
		root2.getChildren().add(webview);

		stage.setTitle("TrailerWindow");
		Scene Trailer = new Scene(root2, 1000, 800);
		stage.setScene(Trailer);
		stage.centerOnScreen();
		stage.show();
	}
	
	/*
	 * This function only provides the first keyword in the current selections list of keywords.
	 * This may not be enough for search. Consider overloading function to provide an arraylist of 
	 * keywords to expand search.
	 * @author Joseph
	 */
	public String getSelectionKeyWord(){
		return movieObj.getKeywords(this.getSelectedMovie().getId()).get(0).getName().toString();
	}

	/*
	 * @author Joseph
	 */
	private void createMoviesObject(TmdbApi Apikey) {
		movieObj = new TmdbMovies(Apikey);
	}

	/*
	 * @author Joseph
	 */
	public MovieDBAccount getUser() {
		return user;
	}

	/*
	 * @author Joseph
	 */
	public void setUser(MovieDBAccount user) {
		this.user = user;
	}
}
