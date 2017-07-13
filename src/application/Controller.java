package application;

import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import info.movito.themoviedbapi.TmdbSearch.MultiListResultsPage;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Multi;
import info.movito.themoviedbapi.model.Multi.MediaType;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

/**
 * This class is the action controller for the FXML file.
 * 
 * @author Josh Winfrey
 * @version 1.0
 * @since 2017-10-07
 */
public class Controller implements Initializable {

	/**
	 * Button for adding a movie to the favorite/watch list.
	 */
	@FXML
	private Button addButton;

	/**
	 * Button for getting a random movie.
	 */
	@FXML
	private Button randomButton;

	/**
	 * Button for removing movies from the favorite/watch list.
	 */
	@FXML
	private Button removeButton;

	/**
	 * Button for searching for movies.
	 */
	@FXML
	private Button searchButton;

	/**
	 * Button for watching selected movie's trailer.
	 */
	@FXML
	private Button trailerButton;

	/**
	 * ListView to hold the user's favorite list.
	 */
	@FXML
	private ListView<String> favoriteList;

	/**
	 * ListView to hold the user's watch list.
	 */
	@FXML
	private ListView<String> watchList;

	/**
	 * ListView to hold the search results.
	 */
	@FXML
	private ListView<String> searchList;

	/**
	 * ImageView to hold the movies image.
	 */
	@FXML
	private ImageView imageField;

	/**
	 * WebView for holding the trailer.
	 */
	@FXML
	private WebView trailerField;

	/**
	 * CheckBox for adding/removing movies from favorite list.
	 */
	@FXML
	private CheckBox favoriteCheckBox;

	/**
	 * CheckBox for doing title searches. 
	 */
	@FXML
	private CheckBox titleCheck;

	/**
	 * CheckBox for doing keyword searches.
	 */
	@FXML
	private CheckBox keywordCheck;

	/**
	 * CheckBox for adding/removing movies from watch list.
	 */
	@FXML
	private CheckBox watchCheckBox;

	/**
	 * TextField to hold the user's user-name.
	 */
	@FXML
	private TextField usernameField;

	/**
	 * TextField to hold search title/keyword.
	 */
	@FXML
	private TextField searchField;

	/**
	 * TextArea to hold movie description.
	 */
	@FXML
	private TextArea descriptionField;

	/**
	 * Label to hold movie title.
	 */
	@FXML
	private Label movieTitle;
	
	/**
	 * Object for the user's account information.
	 */
	private MovieDBAccount user;
	
	/**
	 * Object for selected movie.
	 */
	private MovieSelection selected;
	
	/**
	 * Object for the trailer search engine.
	 */
	private SearchModule searchEngine;
	
	/**
	 * Object for randomizing movies.
	 */
	private TmdbRandomizer randMovieGen;

	/**
	 * Object to hold the movie trailer.
	 */
	private WebView webview;

	/**
	 * Object to hold the user's watch list.
	 */
	private Hashtable<String, MovieDb> watchListDic = 
			new Hashtable<String, MovieDb>();
	
	/**
	 * Object to hold the user's favorite list.
	 */
	private Hashtable<String, MovieDb> favoriteListDic = 
			new Hashtable<String, MovieDb>();
	
	/**
	 * Object to hold the search results.
	 */
	private Hashtable<String, MovieDb> searchListDic = 
			new Hashtable<String, MovieDb>();

	/**
	 * Overrides the initialization function which 
	 * runs at the start of the project.
	 * 
	 * @param location Location of FXML file
	 * @param resources Resources of FXML file
	 */
	@Override
	public void initialize(final URL location, 
			final ResourceBundle resources) {

		try {
			user = new MovieDBAccount();
		} catch (DataBaseConnectionException e) {
			// Connection Error
			JOptionPane.showMessageDialog(null,
					"Connection Error: " + e.getMessage() 
					+ "\nCheck internet connection "
					+ "and restart program.");
			
			// Close down the program
			Platform.exit();
			System.exit(0);
		}
		
		usernameField.appendText(user.getUserName());
		selected = new MovieSelection(user);
		searchEngine = new SearchModule(user.getTmdbApi());
		randMovieGen = new TmdbRandomizer(user);

		webview = new WebView();
		
		generateFavandWatchlist();

	}

	/**
	 * Allows users to click on items in the watch
	 * list and displays them in the window.
	 * 
	 * @param event Mouse click
	 */
	public void selectedMovieFromWatchList(final MouseEvent event) {
		String selectedTitle = watchList.getSelectionModel()
				.getSelectedItem();
		
		if (selectedTitle != null) {
			selected.setSelectedMovie(
					watchListDic.get(selectedTitle));
			
			displaySelectedMovie();
		}
	}

	/**
	 * Allows users to click on items in the favorite
	 * list and displays them in the window.
	 * 
	 * @param event Mouse click
	 */
	public void selectedMovieFromFavoriteList(final MouseEvent event) {
		String selectedTitle = favoriteList.getSelectionModel()
				.getSelectedItem();
		
		if (selectedTitle != null) {
			selected.setSelectedMovie(
					favoriteListDic.get(selectedTitle));
			
			displaySelectedMovie();
		}
	}

	/**
	 * Allows users to click on items in the search
	 * list and displays them in the window.
	 * 
	 * @param event Mouse click
	 */
	public void selectedMovieFromSearchList(final MouseEvent event) {
		String selectedTitle = searchList.getSelectionModel()
				.getSelectedItem();
		
		if (selectedTitle != null) {
			selected.setSelectedMovie(
					searchListDic.get(selectedTitle));
			
			displaySelectedMovie();
		}
	}

	/**
	 * Displays the selected movie in the window.
	 * 
	 * @param None
	 */
	private void displaySelectedMovie() {
		try {
			Image img = new Image(selected.getImageURL());
			imageField.setImage(img);
		} catch (Exception ex) {
			Image img = new Image("http://npsapps.com/wp-content/"
					+ "uploads/2015/09/slider1-bg.png");
			imageField.setImage(img);
		}

		movieTitle.setText("Title: " 
				+ selected.getSelectedMovie().getTitle());
		
		descriptionField.clear();
		descriptionField.appendText("Movie Description: " 
				+ selected.getSelectedMovie().getOverview());
	}

	/**
	 * Allows users to play the trailer for the
	 * selected movie with a button press.
	 * 
	 * @param event Button press
	 */
	public void trailerSelected(final ActionEvent event) {
		selected.generateTrailerWindow(webview);
	}

	/**
	 * Allows users to generate a random movie with
	 * a button press and displays it in the window.
	 * 
	 * @param event Button press
	 */
	public void randomMovie(final ActionEvent event) {
		try {
			MovieDb rand = randMovieGen.getRandomMovie();
			selected.setSelectedMovie(rand);
			
			Image img = new Image(selected.getImageURL());
			imageField.setImage(img);

			descriptionField.clear();
			descriptionField.appendText(selected.getSelectedMovie()
					.getOverview());

		} catch (RandomNotFoundException e) {
			JOptionPane.showInternalMessageDialog(null, 
				"Error in Connection please wait"
				+ "and try to randomize again.");
		}

	}

	/**
	 * Allows user to search for a movie based on keyword
	 * or title and displays results in the search list.
	 * 
	 * @param event Button press
	 */
	public void searchMovie(final ActionEvent event) {
		if (titleCheck.isSelected() && !keywordCheck.isSelected()) {
			searchResultsToDisplay(searchEngine.searchByMovieTitle(
				searchField.getText(), false, 2));
		} else if (keywordCheck.isSelected() 
				&& !titleCheck.isSelected()) {
			searchResultsToDisplay(searchEngine.searchByKeyword(
				searchField.getText(), 2));
		} else {
			JOptionPane.showMessageDialog(null, 
				"Please select either Title or Keyword");
		}
	}

	/**
	 * Adds movie(s) to the search list.
	 * 
	 * @param mr Movie(s) found using title search
	 */
	private void searchResultsToDisplay(final MovieResultsPage mr) {
		ObservableList<String> searchData = FXCollections
				.observableArrayList();

		Iterator<MovieDb> iteratorW = mr.iterator();
		
		boolean results = false;
		
		while (iteratorW.hasNext()) {
			results = true;
			MovieDb movie = iteratorW.next();
			searchData.add(movie.toString());
			searchListDic.put(movie.toString(), movie);
		}
		
		if (!results) {
			searchData.add(
				"Sorry, your search returned no results.");
		}
		
		searchList.setItems(searchData);
	}

	/**
	 * Adds movie(s) to search list.
	 * 
	 * @param tvandMv Movie(s) found by keyword search
	 */
	private void searchResultsToDisplay(
			final MultiListResultsPage tvandMv) {
		
		ObservableList<String> searchData = FXCollections.
			observableArrayList();

		Iterator<Multi> iteratorW = tvandMv.iterator();

		while (iteratorW.hasNext()) {
			Multi m = iteratorW.next();
			
			if (m.getMediaType() == MediaType.MOVIE) {
				searchData.add(((MovieDb) m).toString());
				searchListDic.put(((MovieDb) m).toString(), 
					(MovieDb) m);
			}
		}

		searchList.setItems(searchData);
	}

	/**
	 * Allows users to add the selected movie to
	 * their favorite list or their watch list.
	 * 
	 * @param event Button press
	 */
	public void addMovie(final ActionEvent event) {
		if (favoriteCheckBox.isSelected()) {
			this.addMovieToFavorites();
		} else if (watchCheckBox.isSelected()) {
			this.addMovieToWatchlist();
		} else {
			JOptionPane.showMessageDialog(null, 
				"Please select either Favorites or Watchlist");
		}
	}

	/**
	 * Allows users to remove the selected movie
	 * from their favorite list or watch list.
	 * 
	 * @param event Button press
	 */
	public void removeMovie(final ActionEvent event) {
		if (favoriteCheckBox.isSelected()) {
			this.removeItemFromFavorites();
		} else if (watchCheckBox.isSelected()) {
			this.removeItemFromWatchList();
		} else {
			JOptionPane.showMessageDialog(null, 
				"Please select either Favorites or Watchlist");
		}
	}

	/**
	 * Adds selected movie to watch list.
	 * 
	 * @param None
	 */
	private void addMovieToWatchlist() {
		user.addMovieToWatchlist(selected.getSelectedMovie());
		
		watchListDic.put(selected.getSelectedMovie().getTitle(), 
				selected.getSelectedMovie());
		
		generateFavandWatchlist();
	}

	/**
	 * Adds selected movie to favorite list.
	 * 
	 * @param None
	 */
	private void addMovieToFavorites() {
		user.addMovieToFavorites(selected.getSelectedMovie());
		
		favoriteListDic.put(selected.getSelectedMovie().getTitle(), 
				selected.getSelectedMovie());
		
		generateFavandWatchlist();
	}

	/**
	 * Removes selected movie from watch list.
	 * 
	 * @param None
	 */
	private void removeItemFromWatchList() {
		user.removeMovieFromWatchlist(selected.getSelectedMovie());
		
		watchListDic.remove(selected.getSelectedMovie().getTitle());
		
		generateFavandWatchlist();
	}

	/**
	 * Removes selected movie from favorite list.
	 * 
	 * @param None
	 */
	private void removeItemFromFavorites() {
		user.removeMovieFromFavorites(selected.getSelectedMovie());
		
		favoriteListDic.remove(selected.getSelectedMovie().getTitle());
		
		generateFavandWatchlist();
	}

	/**
	 * Fills the favorite and watch lists with
	 * information from the users account.
	 * 
	 * @param None
	 */
	private void generateFavandWatchlist() {
		ObservableList<String> watchdata = FXCollections.
				observableArrayList();
		
		ObservableList<String> favoritedata = FXCollections.
				observableArrayList();

		MovieResultsPage watchlist = user.getWatchList();
		Iterator<MovieDb> iteratorW = watchlist.iterator();
		
		while (iteratorW.hasNext()) {
			MovieDb movie = iteratorW.next();
			watchdata.add(movie.toString());
			watchListDic.put(movie.toString(), movie);
		}

		watchList.setItems(watchdata);

		MovieResultsPage favoritelist = user.getFavorites();
		Iterator<MovieDb> iteratorF = favoritelist.iterator();
		
		while (iteratorF.hasNext()) {
			MovieDb movie = iteratorF.next();
			favoritedata.add(movie.toString());
			favoriteListDic.put(movie.toString(), movie);
		}

		favoriteList.setItems(favoritedata);
	}
}
