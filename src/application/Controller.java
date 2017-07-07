package application;

import java.net.URL;
import java.util.*;

import javax.swing.JOptionPane;

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

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

/*
 * @author Josh Winfrey
 */
public class Controller implements Initializable {

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

	private MovieDBAccount user;
	private MovieSelection selected;
	private SearchModule searchEngine;
	private TmdbRandomizer randMovieGen;

	private WebView webview;

	private Hashtable<String, MovieDb> watchList = new Hashtable<String, MovieDb>();
	private Hashtable<String, MovieDb> favoriteList = new Hashtable<String, MovieDb>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		user = new MovieDBAccount();
		UsernameField.appendText(user.getUserName());
		selected = new MovieSelection(user);
		searchEngine = new SearchModule(user.getTmdbApi());
		randMovieGen = new TmdbRandomizer(user);

		webview = new WebView();
		generateFavandWatchlist();
				
	}
	
	public void SelectedMovieFromWatchList(MouseEvent event) {
		selected.setSelectedMovie(watchList.get(WatchList.getSelectionModel().getSelectedItem()));
		Image img = new Image(selected.getImageURL());
		ImageField.setImage(img);

		DescriptionField.clear();
		DescriptionField.appendText(selected.getSelectedMovie().getOverview());
	}

	public void SelectedMovieFromFavoriteList(MouseEvent event) {
		selected.setSelectedMovie(favoriteList.get(FavoriteList.getSelectionModel().getSelectedItem()));
		Image img = new Image(selected.getImageURL());
		ImageField.setImage(img);

		DescriptionField.clear();
		DescriptionField.appendText(selected.getSelectedMovie().getOverview());
	}

	public void TrailerSelected(ActionEvent event) {
		selected.generateTrailerWindow(webview);
	}

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

	public void SearchMovie(ActionEvent event) {
		//TODO: Checkboxs: keywords or title 
		//TODO: results listview on GUI
		
		
	}

	public void AddMovie(ActionEvent event) {
		if(FavoriteCheckBox.isSelected()){
			this.addMovieToFavorites();
		}else if(WatchCheckBox.isSelected()){
			this.addMovieToWatchlist();
		}else{
			JOptionPane.showMessageDialog(null, "Please select Favorites or Watchlist");
		}
	}

	public void RemoveMovie(ActionEvent event) {
		if(FavoriteCheckBox.isSelected()){
			this.removeItemFromFavorites();
		}else if(WatchCheckBox.isSelected()){
			this.removeItemFromWatchList();
		}else{
			JOptionPane.showMessageDialog(null, "Please select Favorites or Watchlist");
		}
	}
	private void addMovieToWatchlist(){
		user.addMovieToWatchlist(selected.getSelectedMovie());
		watchList.put(selected.getSelectedMovie().getTitle(), selected.getSelectedMovie());
		generateFavandWatchlist();
		//TODO: add to list view
	}
	private void addMovieToFavorites(){
		user.addMovieToFavorites(selected.getSelectedMovie());
		favoriteList.put(selected.getSelectedMovie().getTitle(), selected.getSelectedMovie());
		generateFavandWatchlist();
		//TODO: add to list view 
	}
	private void removeItemFromWatchList(){
		user.removeMovieFromWatchlist(selected.getSelectedMovie());
		watchList.remove(selected.getSelectedMovie());
		generateFavandWatchlist();
		//TODO: add functionality
	}
	private void removeItemFromFavorites(){
		user.removeMovieFromFavorites(selected.getSelectedMovie());
		favoriteList.remove(selected.getSelectedMovie());
		generateFavandWatchlist();
		//TODO add functionality 
	}

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
