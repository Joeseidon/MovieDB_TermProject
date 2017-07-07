package application;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

public class SearchModule extends TmdbSearch {

	public SearchModule(TmdbApi tmdbApi) {
		super(tmdbApi);
		
	}
	
	/*
	 * Function returns a page of results from the movie database pertaining to
	 * the input search string
	 * 
	 * @Parameters String search string
	 * 
	 * @Return MovieResultsPage
	 * 
	 * @author Joseph
	 */
	public MovieResultsPage searchByMovieTitle(String seachString, boolean adult,int pages) {
		return super.searchMovie(seachString, 0, "en", adult, pages);
	}

	/*
	 * This function will return a list of movies and shows that link with the
	 * input keyword search string. Search is only performed on a single
	 * keyword. Consider updating search to except and array or arraylist of
	 * keywords to extend search.
	 * 
	 * @Parameter String keywordSearchString
	 * 
	 * @Return MultiListResultsPage multi-media list of results
	 * 
	 * @author Joseph
	 */
	public MultiListResultsPage searchByKeyword(String keywordSearchString,int pages) {
		return super.searchMulti(keywordSearchString, "English", pages);
	}


}
