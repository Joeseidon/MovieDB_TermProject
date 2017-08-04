package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbSearch.MultiListResultsPage;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Multi;
import info.movito.themoviedbapi.model.Multi.MediaType;
import info.movito.themoviedbapi.model.Reviews;
import info.movito.themoviedbapi.model.people.PersonCast;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

/**
 * This class is the action controller for the FXML file.
 * 
 * @author Josh Winfrey
 * @author Joseph Cutino
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
	 * Button for logging in.
	 */
	@FXML
	private Button loginButton;
	
	/**
	 * Button for creating a new API account.
	 */
	@FXML
	private Button createAccountButton;

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
	 * ListView to hold the user's watch list.
	 */
	@FXML
	private ListView<String> inTheatersNowList;
	
	/**
	 * ListView to hold the user's watch list.
	 */
	@FXML
	private ListView<String> topRatedList;
	
	/**
	 * ListView to hold the user's watch list.
	 */
	@FXML
	private ListView<String> upcomingList;
	
	/**
	 * ListView to hold movie reviews.
	 */
	@FXML
	private ListView<String> reviewList;

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
	 * TextField to hold the login user-name.
	 */
	@FXML
	private TextField usernameField1;
	
	/**
	 * TextField to hold the login password.
	 */
	@FXML
	private TextField passwordField;

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
	 * Label to hold the error message on login failure.
	 */
	@FXML
	private Label errorLabel;
	
	/**
	 * ComboBox to hold filter options.
	 */
	@FXML
	private ComboBox<String> filterList;
	
	/**
	 * TextField to hold the filter.
	 */
	@FXML
	private TextField filterField;
	
	/**
	 * MenuButton to hold the active filters.
	 */
	@FXML
	private MenuButton activeFilters;
	
	/**
	 * Button to add filter.
	 */
	@FXML
	private Button addFilterButton;
	
	/**
	 * Button to remove filter(s).
	 */
	@FXML
	private Button removeFilterButton;
	
	/**
	 * Button to change search page up one.
	 */
	@FXML
	private Button searchPageUpButton;
	
	/**
	 * Button to change search page down one.
	 */
	@FXML
	private Button searchPageDownButton;
	
	/**
	 * Button to get reviews for the selected movie.
	 */
	@FXML
	private Button reviewsButton;
	
	/**
	 * Label for movie rating.
	 */
	@FXML
	private Label movieRating;
	
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
	 * Object to hold the top rated results.
	 */
	private Hashtable<String, MovieDb> topRatedListDic = 
			new Hashtable<String, MovieDb>();
	
	/**
	 * Object to hold the upcoming results.
	 */
	private Hashtable<String, MovieDb> upcomingListDic = 
			new Hashtable<String, MovieDb>();
	
	/**
	 * Object to hold the now playing results.
	 */
	private Hashtable<String, MovieDb> nowPlayingListDic = 
			new Hashtable<String, MovieDb>();
	
	/**
	 * Object used to pull movie collections.
	 */
	private MovieCollections movieCollection;
	
	/**
	 * Default page == first page of results list.
	 */
	private final int defaultPage = 1;
	
	/**
	 * Current page of the search.
	 */
	private static int currentPage = 1;
	
	/**
	 * Collections.
	 */
	enum Collections {
		/**
		 * Top rated movies.
		 */
		topRated,
		
		/**
		 * Movies playing now.
		 */
		nowPlaying,
		
		/**
		 * Upcoming movies.
		 */
		upcoming
	};
	/**
	 * Url for new API account.
	 */
	private final String newAccountPage = "https://www.themoviedb.org/"
			+ "account/signup?language=en";
	
	/**
	 * Object to hold the current count of controller objects created.
	 */	
	private static int i = 0;
	
	/**
	 * String to hold the user's user-name.
	 */
	private static String username;
	
	/**
	 * String to hold the user's password.
	 */
	private static String password;

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
		webview = new WebView();
		if (i == 1) {
			try {
				user = new MovieDBAccount(username, password);
			} catch (DataBaseConnectionException e) {
				// Connection Error
				JOptionPane.showMessageDialog(null,
					"Connection Error: " 
					+ e.getMessage() + "\nCheck "
					+ "internet connection "
					+ "and restart program.");
			
				// Close down the program
				Platform.exit();
				System.exit(0);
			}

			usernameField.appendText(user.getUserName());
			selected = new MovieSelection(user);
			searchEngine = new SearchModule(user.getTmdbApi());
			randMovieGen = new TmdbRandomizer(user);
			movieCollection = 
					new MovieCollections(user.getTmdbApi());

			webview = new WebView();

			generateFavandWatchlist();
			
			generateFilterList();

			displayCollection(Collections.nowPlaying, defaultPage);
			displayCollection(Collections.topRated, defaultPage);
			displayCollection(Collections.upcoming, defaultPage);
		}
		//i += 1;
	}
	
	
	/**
	 * Displays desired collection of movies.
	 * 
	 * @param current Current collection to display
	 * @param page Page number to display
	 */
	public void displayCollection(
			final Collections current, final int page) {
		if (current == Collections.nowPlaying) {
			nowPlayingListDic = 
					generateDictionaryandDisplay(
							movieCollection
							.getNowPlaying(page), 
							inTheatersNowList);
		} else if (current == Collections.topRated) {
			topRatedListDic = 
					generateDictionaryandDisplay(
							movieCollection
							.getTopRated(page),
							topRatedList);
		} else if (current == Collections.upcoming) {
			upcomingListDic = 
					generateDictionaryandDisplay(
							movieCollection
							.getUpcoming(page), 
							upcomingList);
		}
	}
	
	/**
	 * 
	 * 
	 * @param results Movies to be added
	 * @param frame Frame to fill
	 * @return Returns Hashtable of movies
	 */
	private Hashtable<String, MovieDb> generateDictionaryandDisplay(
			final MovieResultsPage results,
			final ListView<String> frame) {
		ObservableList<String> data = FXCollections.
				observableArrayList();
		Hashtable<String, MovieDb> tmp = 
				new Hashtable<String, MovieDb>();
		
		Iterator<MovieDb> itr = results.iterator();
		while (itr.hasNext()) {
			MovieDb movie = itr.next();
			data.add(movie.toString());
			tmp.put(movie.toString(), movie);
		}
		frame.setItems(data);
		return tmp;
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
	 * Allows users to click on items in the top rated
	 * list and displays them in the window.
	 * 
	 * @param event Mouse click
	 */
	public void selectedMovieFromTopRatedList(final MouseEvent event) {
		String selectedTitle = topRatedList.getSelectionModel()
				.getSelectedItem();
		
		if (selectedTitle != null) {
			selected.setSelectedMovie(
					topRatedListDic.get(selectedTitle));
			
			displaySelectedMovie();
		}
	}
	
	/**
	 * Allows users to click on items in the in theaters now
	 * list and displays them in the window.
	 * 
	 * @param event Mouse click
	 */
	public void selectedMovieFromInTheatersNowList(final MouseEvent event) {
		String selectedTitle = inTheatersNowList.getSelectionModel()
				.getSelectedItem();
		
		if (selectedTitle != null) {
			selected.setSelectedMovie(
					nowPlayingListDic.get(selectedTitle));
			
			displaySelectedMovie();
		}
	}
	
	/**
	 * Allows users to click on items in the upcoming
	 * list and displays them in the window.
	 * 
	 * @param event Mouse click
	 */
	public void selectedMovieFromUpcomingList(final MouseEvent event) {
		String selectedTitle = upcomingList.getSelectionModel()
				.getSelectedItem();
		
		if (selectedTitle != null) {
			selected.setSelectedMovie(
					upcomingListDic.get(selectedTitle));
			
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
		
		descriptionField.clear();
		descriptionField.appendText("Movie Title: " 
				+ selected.getSelectedMovie().getTitle() 
				+ "\n");
		descriptionField.appendText("Movie Description: " 
				+ selected.getSelectedMovie().getOverview());
		
		if (selected.getSelectedMovie().getVoteAverage() < 5.5) {
			movieRating.setTextFill(Color.web("#ff0000"));
		} else if (selected.getSelectedMovie().getVoteAverage() < 7.5) {
			movieRating.setTextFill(Color.web("#ffff00"));
		} else {
			movieRating.setTextFill(Color.web("#43f707"));
		}
		movieRating.setText("" 
				+ selected.getSelectedMovie().getVoteAverage());
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
			
			displaySelectedMovie();

		} catch (RandomNotFoundException e) {
			JOptionPane.showInternalMessageDialog(null, 
				"Error in Connection please wait"
				+ " and try to randomize again.");
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
				searchField.getText(), false, 1));
		} else if (keywordCheck.isSelected() 
				&& !titleCheck.isSelected()) {
			searchResultsToDisplay(searchEngine.searchByKeyword(
				searchField.getText(), 1));
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
			if (meetsFilters(movie)) {
				searchData.add(movie.toString());
				searchListDic.put(movie.toString(), movie);
			}
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
				MovieDb movie = (MovieDb) m;
				
				if (meetsFilters(movie)) {
					searchData.add(movie.toString());
					searchListDic.put(movie.toString(), 
							movie);
				}
			}
		}

		searchList.setItems(searchData);
	}
	
	/**
	 * Returns true if movie passes all applied filters.
	 * 
	 * @param movie Movie to be tested
	 * @return Boolean value
	 */
	private boolean meetsFilters(final MovieDb movie) {
		boolean genrePass;
		boolean actorPass;
		
		TmdbMovies movieT = new TmdbMovies(user.getTmdbApi());
		
		if (activeFilters.getItems().isEmpty()) {
			return true;
		} else {
			String text;
			
			for (int i = 0; i < activeFilters.getItems().size(); ++i) {
				
				text = activeFilters.getItems().get(i).getText();
				
				if (text.contains("Year")) {
					String yearText = text.substring(6);
					
					if (movie.getReleaseDate() == null) {
						return false;
					}
					
					if (!movie.getReleaseDate().contains(yearText)) {
						return false;
					}
				} else if (text.contains("Genre")) {
					genrePass = false;
					String genreText = text.substring(7).toLowerCase();
					
					ArrayList<Genre> listG = (ArrayList<Genre>) movieT.getMovie(
							movie.getId(), "english",
							TmdbMovies.MovieMethod
							.valueOf("lists")).getGenres();
					Iterator<Genre> iteratorG = listG.iterator();
										
					while (iteratorG.hasNext()) {
						Genre g = iteratorG.next();
						
						if (g.toString().toLowerCase().contains(genreText)) {
							genrePass = true;
						}
					}
					
					if (!genrePass) {
						return false;
					}
				} else if (text.contains("Actor")) {
					actorPass = false;
					String actorText = text.substring(7).toLowerCase();
					
					ArrayList<PersonCast> listC = 
							(ArrayList<PersonCast>) movieT.getMovie(
									movie.getId(), "english", 
									TmdbMovies.MovieMethod.valueOf(
											"lists")).getCast();
					if (listC != null) {
						Iterator<PersonCast> iteratorC = listC.iterator();
											
						while (iteratorC.hasNext()) {
							PersonCast p = iteratorC.next();
							
							if (p.toString().toLowerCase().contains(
									actorText)) {
								actorPass = true;
							}
						}
					}
					
					if (!actorPass) {
						return false;
					}
				}
			}
		}
		
		return true;
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
		/*ObservableList<String> watchdata = FXCollections.
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

		favoriteList.setItems(favoritedata);*/
		
		watchListDic = generateDictionaryandDisplay(
				user.getWatchList(), watchList);
		favoriteListDic = generateDictionaryandDisplay(
				user.getFavorites(), favoriteList);
		
	}
	
	/**
	 * Function for logging in to user's API account.
	 * 
	 * @param event Button press
	 */
	//@SuppressWarnings("unused")
	public void checkLogin(final ActionEvent event) {
		username = usernameField1.getText();
		password = passwordField.getText();
		MovieDBAccount temp = null;
		
		try {
			temp = new MovieDBAccount(username, password);
		} catch (DataBaseConnectionException e) {
			// Do nothing here
		}
		if (temp != null) {
			Controller.i = 1;
			try {
				Parent parent = FXMLLoader.load(getClass()
						.getResource("MainScene.fxml"));
				Scene scene = new Scene(parent);
				Stage stage = (Stage) ((Node) event.getSource())
						.getScene().getWindow();
				stage.setScene(scene);
				stage.centerOnScreen();			
				stage.show();
			} catch (Exception e) {
				e.printStackTrace();
			}	
		} else {
			if (usernameField1.getText().isEmpty() 
					&& passwordField.getText().isEmpty()) {
				errorLabel.setText(
						"No username and no password.");
			} else if (usernameField1.getText().isEmpty()
					&& !(passwordField.getText()
							.isEmpty())) {
				errorLabel.setText("No username.");
			} else if (!(usernameField1.getText().isEmpty())
					&& !(passwordField.getText()
							.isEmpty())) {
				errorLabel.setText("No password.");
			} else {
				errorLabel.setText("Invalid login.");
			}
		}
	}
	
	/**
	 * Function for creating a new API account.
	 * 
	 * @param event Button press
	 */
	//@SuppressWarnings("unused")
	public void newAPIAccount(final ActionEvent event) {
		try {
			webview.getEngine().load(newAccountPage);

			Stage stage = new Stage();

			AnchorPane root = new AnchorPane();
			AnchorPane.setBottomAnchor(webview, 0.0);
			AnchorPane.setTopAnchor(webview, 0.0);
			AnchorPane.setLeftAnchor(webview, 0.0);
			AnchorPane.setRightAnchor(webview, 0.0);

			root.getChildren().add(webview);

			stage.setTitle("New Account");
			Scene account = new Scene(root, 1400, 800);
			stage.setScene(account);
			stage.centerOnScreen();
			stage.show();

			stage.setOnCloseRequest(new 
					EventHandler<WindowEvent>() {
				@Override
				public void handle(final WindowEvent event) {
					webview.getEngine().load(null);
				}
			});
		} catch (IndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(null, 
					"Could not load page.");
		}
	}
	
	/**
	 * 
	 */
	private void generateFilterList() {
		filterList.getItems().addAll("Year", "Genre", "Actor");
	}
	
	/**
	 * Function for adding a filter to the search.
	 * 
	 * @param event Button press
	 */
	public void addFilter(final ActionEvent event) {
		if (!filterField.getText().isEmpty()
				&& filterList.getValue() != null) {
			CheckMenuItem filter = 
					new CheckMenuItem(
							filterList.getValue()
							+ ": "
							+ filterField
								.getText());
			activeFilters.getItems().add(filter);
		} else if (filterList.getValue() == null) {
			JOptionPane.showMessageDialog(
					null, "Please select a filter type "
							+ "from the list.");
		} else {
			JOptionPane.showMessageDialog(
					null, "Please enter somthing "
							+ "for the filter.");
		}
	}
	
	/**
	 * Function for removing filters from the search.
	 * 
	 * @param event Button press
	 */
	public void removeFilter(final ActionEvent event) {
		ObservableList<MenuItem> tempList = activeFilters.getItems();
		ObservableList<CheckMenuItem> addList = 
				FXCollections.observableArrayList();
		for (int j = 0; j < tempList.size(); j++) {
			CheckMenuItem tempItem = 
					(CheckMenuItem) tempList.get(j);
			if (!tempItem.isSelected()) {
				addList.add(tempItem);
			}
		}
		activeFilters.getItems().clear();
		while (!addList.isEmpty()) {
			activeFilters.getItems().addAll(addList.get(0));
			addList.remove(0);
		}
	}
	
	/**
	 * Function to change search page up one.
	 * 
	 * @param event Button press
	 */
	public void searchPageUp(final ActionEvent event) {
		if (!searchList.getItems().isEmpty()) {
			Controller.currentPage += 1;
			if (titleCheck.isSelected()) {
				searchResultsToDisplay(
						searchEngine.searchByMovieTitle(
								searchField
								.getText(), 
								false, 
								currentPage));
			} else {
				searchResultsToDisplay(
						searchEngine.searchByKeyword(
								searchField
								.getText(), 
								currentPage));
			}
		} else {
			JOptionPane.showMessageDialog(
					null, "No search has been done.");
		}
	}
	
	/**
	 * Function to change search page down one.
	 * 
	 * @param event Button press
	 */
	public void searchPageDown(final ActionEvent event) {
		if (!searchList.getItems().isEmpty()) {
			currentPage -= 1;
			if (currentPage < defaultPage) {
				currentPage = defaultPage;
			}
			
			if (titleCheck.isSelected()) {
				searchResultsToDisplay(
						searchEngine.searchByMovieTitle(
								searchField
								.getText(), 
								false, 
								currentPage));
			} else {
				searchResultsToDisplay(
						searchEngine.searchByKeyword(
								searchField
								.getText(), 
								currentPage));
			}
		} else {
			JOptionPane.showMessageDialog(
					null, "No search has been done.");
		}
	}	
	
	public void getReviews(ActionEvent event){
		TextArea reviewField = new TextArea();
		
		Stage stage = new Stage();

		AnchorPane root = new AnchorPane();
		AnchorPane.setBottomAnchor(reviewField, 0.0);
		AnchorPane.setTopAnchor(reviewField, 0.0);
		AnchorPane.setLeftAnchor(reviewField, 0.0);
		AnchorPane.setRightAnchor(reviewField, 0.0);

		root.getChildren().add(reviewField);

		stage.setTitle("Reviews");
		Scene account = new Scene(root, 1000, 600);
		stage.setScene(account);
		stage.centerOnScreen();
		stage.show();
		
		reviewField.setWrapText(true);
		reviewField.setStyle("-fx-background-color:  #c3c4c4,        "
				+ "linear-gradient(#d6d6d6 50%, white 100%),        "
				+ "radial-gradient(center 50% -40%, radius 200%, "
				+ "#e6e6e6 45%, rgba(230,230,230,0) 50%)");
		
		TmdbMovies temp = new TmdbMovies(user.getTmdbApi());
		ArrayList<Reviews> list = (ArrayList<Reviews>) temp.getMovie(
				selected.getSelectedMovie().getId(), "english", 
				TmdbMovies.MovieMethod.valueOf("reviews")).getReviews();
		Iterator<Reviews> iteratorR = list.iterator();
		
		while(iteratorR.hasNext()){
			Reviews review = iteratorR.next();
			reviewField.appendText(review.getAuthor() + "\n");
			reviewField.appendText(review.getContent() + "\n");
			reviewField.appendText("\n");
		}

//		stage.setOnCloseRequest(new 
//				EventHandler<WindowEvent>() {
//			@Override
//			public void handle(final WindowEvent event) {
//				reviewField.clear();
//			}
//		});
	}
}
