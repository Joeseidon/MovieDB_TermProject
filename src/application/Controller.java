package application;

import java.net.URL;
import java.util.*;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

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
	private ImageView ImageField;
	
	@FXML
	private WebView TrailerField;
	
	@FXML
	private CheckBox FavoriteCheckBox;

	@FXML
	private CheckBox WatchCheckBox;
	
	@FXML
	private TextField UsernameField;
	
	@FXML
	private TextField TitleField;
	
	@FXML
	private TextField SearchField;
	
	@FXML
	private TextArea DescriptionField;
	
	private Hashtable<String, MovieDb> watchList = new Hashtable<String, MovieDb>();
	private Hashtable<String, MovieDb> favoriteList = new Hashtable<String, MovieDb>();
	private String youTubeBaseURL = "https://www.youtube.com/watch?v=";
	private String baseImageURL = "https://image.tmdb.org/t/p/w500";
	private MovieDb cur;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MovieDBAccount user = new MovieDBAccount();
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
		// To do on initialization
		
	}
	
	public void SelectedMovieFromWatchList(MouseEvent event){
		cur = watchList.get(WatchList.getSelectionModel().getSelectedItem());
		Image img = new Image(baseImageURL+watchList.get(WatchList.getSelectionModel().getSelectedItem()).getPosterPath());
		ImageField.setImage(img);
		//System.out.println(WatchList.getSelectionModel().getSelectedItem());
	}
	
	public void SelectedMovieFromFavoriteList(MouseEvent event){
		cur = favoriteList.get(FavoriteList.getSelectionModel().getSelectedItem());
		Image img = new Image(baseImageURL+favoriteList.get(FavoriteList.getSelectionModel().getSelectedItem()).getPosterPath());
		ImageField.setImage(img);
		//System.out.println(FavoriteList.getSelectionModel().getSelectedItem());
	}
	
	public void TrailerSelected(ActionEvent event){
		//TrailerField.getEngine().load(user.getVideoURL);
		Stage stage = new Stage();
		AnchorPane root2 = new AnchorPane();
		AnchorPane.setBottomAnchor(TrailerField, 5.0);
		AnchorPane.setTopAnchor(TrailerField, 5.0);
		AnchorPane.setLeftAnchor(TrailerField, 5.0);
		AnchorPane.setRightAnchor(TrailerField, 5.0);
		root2.getChildren().add(TrailerField);
		
		stage.setTitle("Trailer");
		Scene Trailer = new Scene(root2, 1000, 700);
		stage.setScene(Trailer);
		stage.show();
	}
}
