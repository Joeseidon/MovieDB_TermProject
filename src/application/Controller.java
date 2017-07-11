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
 * @author Josh Winfrey
 * @version 1.0
 * @since 2017-10-07
 */
public class Controller implements Initializable {
	
	/**
	 * GUI object declarations.
	 */

	@FXML
	private Button AddButton;

	@FXML
	private Button RandomButton;

	@FXML
	private Button RemoveButton;

	@FXML
	private Button SearchButton;

	@FXML
	private Button TrailerButton;

	@FXML
	private ListView<String> FavoriteList;

	@FXML
	private ListView<String> WatchList;

	@FXML
	private ListView<String> SearchList;

	@FXML
	private ImageView ImageField;

	@FXML
	private WebView TrailerField;

	@FXML
	private CheckBox FavoriteCheckBox;

	@FXML
	private CheckBox TitleCheck;

	@FXML
	private CheckBox KeyWordCheck;

	@FXML
	private CheckBox WatchCheckBox;

	@FXML
	private TextField UsernameField;

	@FXML
	private TextField SearchField;

	@FXML
	private TextArea DescriptionField;

	@FXML
	private Label MovieTitle;
	
	/**
	 * Objects for holding the user's account information, the 
	 * 	selected movie, the trailer search engine, and a random movie
	 * 	generator.
	 */
	private MovieDBAccount user;
	private MovieSelection selected;
	private SearchModule searchEngine;
	private TmdbRandomizer randMovieGen;

	/**
	 * Object to hold the movie trailer.
	 */
	private WebView webview;

	/**
	 * Objects to hold the user's favorite and watch list information
	 * 	as well as the search results.
	 */
	private Hashtable<String, MovieDb> watchList = new Hashtable<String, MovieDb>();
	private Hashtable<String, MovieDb> favoriteList = new Hashtable<String, MovieDb>();
	private Hashtable<String, MovieDb> searchList = new Hashtable<String, MovieDb>();

	/**
	 * Overrides the initialization function which 
	 * 	runs at the start of the project.
	 * 
	 * @param location
	 * 		location of FXML file
	 * @param resources
	 * 		resources of FXML file
	 * @return None
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			user = new MovieDBAccount();
		} catch (DataBaseConnectionException e) {
			// Connection Error
			JOptionPane.showMessageDialog(null,
					"Connection Error: " + e.getMessage() + "\nCheck internet connection and restart program.");
			// Close down the program
			Platform.exit();
			System.exit(0);
		}
		UsernameField.appendText(user.getUserName());
		selected = new MovieSelection(user);
		searchEngine = new SearchModule(user.getTmdbApi());
		randMovieGen = new TmdbRandomizer(user);

		webview = new WebView();
		generateFavandWatchlist();

	}

	/**
	 * Allows users to click on items in the watch list and 
	 * 	displays them in the window.
	 * 
	 * @param event
	 * 		mouse click
	 * @return None
	 */
	public void SelectedMovieFromWatchList(MouseEvent event) {
		String selectedTitle = WatchList.getSelectionModel().getSelectedItem();
		if (selectedTitle != null) {
			selected.setSelectedMovie(watchList.get(selectedTitle));
			displaySelectedMovie();
		}
	}

	/**
	 * Allows users to click on items in the favorite list and
	 * 	displays them in the window.
	 * 
	 * @param event
	 * 		mouse click
	 * @return None
	 */
	public void SelectedMovieFromFavoriteList(MouseEvent event) {
		String selectedTitle = FavoriteList.getSelectionModel().getSelectedItem();
		if (selectedTitle != null) {
			selected.setSelectedMovie(favoriteList.get(selectedTitle));
			displaySelectedMovie();
		}
	}

	/**
	 * Allows users to click on items in the search list and
	 * 	displays them in the window.
	 * 
	 * @param event
	 * 		mouse click
	 * @return None
	 */
	public void SelectedMovieFromSearchList(MouseEvent event) {
		String selectedTitle = SearchList.getSelectionModel().getSelectedItem();
		if (selectedTitle != null) {
			selected.setSelectedMovie(searchList.get(selectedTitle));
			displaySelectedMovie();
		}
	}

	/**
	 * Displays the selected movie in the window.
	 * 
	 * @param None
	 * @return None
	 */
	private void displaySelectedMovie() {
		try {
			Image img = new Image(selected.getImageURL());
			ImageField.setImage(img);
		} catch (Exception ex) {
			Image img = new Image("http://npsapps.com/wp-content/uploads/2015/09/slider1-bg.png");
			ImageField.setImage(img);
		}

		MovieTitle.setText("Title: " + selected.getSelectedMovie().getTitle());
		DescriptionField.clear();
		DescriptionField.appendText("Movie Description: " + selected.getSelectedMovie().getOverview());
	}

	/**
	 * Allows users to play the trailer for the selected movie with
	 * 	a button press.
	 * 
	 * @param event
	 * 		button press
	 * @return None
	 */
	public void TrailerSelected(ActionEvent event) {
		selected.generateTrailerWindow(webview);
	}

	/**
	 * Allows users to generate a random movie with a button press
	 * 	and displays it in the window.
	 * 
	 * @param event
	 * 		button press
	 * @return None
	 */
	public void RandomMovie(ActionEvent event) {

		try {
			MovieDb rand = randMovieGen.getRandomMovie();
			selected.setSelectedMovie(rand);
			Image img = new Image(selected.getImageURL());
			ImageField.setImage(img);

			DescriptionField.clear();
			DescriptionField.appendText(selected.getSelectedMovie().getOverview());

		} catch (RandomNotFoundException e) {
			JOptionPane.showInternalMessageDialog(null, "Error in Connection please wait and try to randomize again.");
			// e.printStackTrace();
		}

	}

	/**
	 * Allows user to search for a movie based on keyword or title
	 * 	and displays results in the search list.
	 * 
	 * @param event
	 * 		button press
	 * @return None
	 */
	public void SearchMovie(ActionEvent event) {
		if (TitleCheck.isSelected() && !KeyWordCheck.isSelected()) {
			searchResultsToDisplay(searchEngine.searchByMovieTitle(SearchField.getText(), false, 2));
		} else if (KeyWordCheck.isSelected() && !TitleCheck.isSelected()) {
			searchResultsToDisplay(searchEngine.searchByKeyword(SearchField.getText(), 2));
		} else {
			JOptionPane.showMessageDialog(null, "Please select either Title or Keyword");
		}
	}

	/**
	 * Adds movie(s) to the search list.
	 * 
	 * @param mr
	 * 		movie(s) found using title search
	 * @return None
	 */
	private void searchResultsToDisplay(MovieResultsPage mr) {
		ObservableList<String> searchData = FXCollections.observableArrayList();

		Iterator<MovieDb> iteratorW = mr.iterator();
		boolean results = false;
		while (iteratorW.hasNext()) {
			results = true;
			MovieDb movie = iteratorW.next();
			searchData.add(movie.toString());
			searchList.put(movie.toString(), movie);
		}
		if (!results) {
			searchData.add("Sorry, your search resulted in no results.");
		}
		SearchList.setItems(searchData);
	}

	/**
	 * Adds movie(s) to search list.
	 * 
	 * @param TvandMv
	 * 		movie(s) found by keyword search
	 * @return None
	 */
	private void searchResultsToDisplay(MultiListResultsPage TvandMv) {
		ObservableList<String> searchData = FXCollections.observableArrayList();

		Iterator<Multi> iteratorW = TvandMv.iterator();

		while (iteratorW.hasNext()) {
			Multi m = iteratorW.next();
			if (m.getMediaType() == MediaType.MOVIE) {
				searchData.add(((MovieDb) m).toString());
				searchList.put(((MovieDb) m).toString(), (MovieDb) m);
			}
		}

		SearchList.setItems(searchData);
	}

	/**
	 * Allows users to add the selected movie to their favorite list
	 * 	or their watch list.
	 * 
	 * @param event
	 * 		button press
	 * @return None
	 */
	public void AddMovie(ActionEvent event) {
		if (FavoriteCheckBox.isSelected()) {
			this.addMovieToFavorites();
		} else if (WatchCheckBox.isSelected()) {
			this.addMovieToWatchlist();
		} else {
			JOptionPane.showMessageDialog(null, "Please select either Favorites or Watchlist");
		}
	}

	/**
	 * Allows users to remove the selected movie from their favorite
	 * 	list or watch list.
	 * 
	 * @param event
	 * 		button press
	 * @return None
	 */
	public void RemoveMovie(ActionEvent event) {
		if (FavoriteCheckBox.isSelected()) {
			this.removeItemFromFavorites();
		} else if (WatchCheckBox.isSelected()) {
			this.removeItemFromWatchList();
		} else {
			JOptionPane.showMessageDialog(null, "Please select either Favorites or Watchlist");
		}
	}

	/**
	 * Adds selected movie to watch list.
	 * 
	 * @param None
	 * @return None
	 */
	private void addMovieToWatchlist() {
		user.addMovieToWatchlist(selected.getSelectedMovie());
		watchList.put(selected.getSelectedMovie().getTitle(), selected.getSelectedMovie());
		generateFavandWatchlist();
	}

	/**
	 * Adds selected movie to favorite list.
	 * 
	 * @param None
	 * @return None
	 */
	private void addMovieToFavorites() {
		user.addMovieToFavorites(selected.getSelectedMovie());
		favoriteList.put(selected.getSelectedMovie().getTitle(), selected.getSelectedMovie());
		generateFavandWatchlist();
	}

	/**
	 * Removes selected movie from watch list.
	 * 
	 * @param None
	 * @return None
	 */
	private void removeItemFromWatchList() {
		user.removeMovieFromWatchlist(selected.getSelectedMovie());
		watchList.remove(selected.getSelectedMovie());
		generateFavandWatchlist();
	}

	/**
	 * 	Removes selected movie from favorite list.
	 * 
	 * @param None
	 * @return None
	 */
	private void removeItemFromFavorites() {
		user.removeMovieFromFavorites(selected.getSelectedMovie());
		favoriteList.remove(selected.getSelectedMovie());
		generateFavandWatchlist();
		// TODO add functionality
	}

	/**
	 * Fills the favorite and watch lists with information from the
	 * 	users account.
	 * 
	 * @param None
	 * @return None
	 */
	private void generateFavandWatchlist() {
		ObservableList<String> watchdata = FXCollections.observableArrayList();
		ObservableList<String> favoritedata = FXCollections.observableArrayList();

		MovieResultsPage watchlist = user.getWatchList();
		Iterator<MovieDb> iteratorW = watchlist.iterator();
		while (iteratorW.hasNext()) {
			MovieDb movie = iteratorW.next();
			watchdata.add(movie.toString());
			watchList.put(movie.toString(), movie);
		}

		WatchList.setItems(watchdata);

		MovieResultsPage favoritelist = user.getFavorites();
		Iterator<MovieDb> iteratorF = favoritelist.iterator();
		while (iteratorF.hasNext()) {
			MovieDb movie = iteratorF.next();
			favoritedata.add(movie.toString());
			favoriteList.put(movie.toString(), movie);
		}

		FavoriteList.setItems(favoritedata);
	}
}
