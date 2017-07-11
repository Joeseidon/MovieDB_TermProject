package application;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

/**
 * This class links with an existing MovieDBAccount object and is 
 * used to perform search queries from the database for the current
 * account.
 * @author Joseph Cutino
 * @version 1.0
 * @since 2016-07-07
 */
public class SearchModule extends TmdbSearch {

	/**
	 * Public constructor for creating a new searchModule.
	 * @param tmdbApi Used to connect to the movie database.
	 */
	public SearchModule(TmdbApi tmdbApi) {
		super(tmdbApi);
	}

	/**
	 * Function returns a page of results from the movie database pertaining to
	 * 	the input search string.
	 * 
	 * @param searchString
	 *            Title to search for related movies with.
	 * @param adult
	 *            Used to keep results within user preference.
	 * @param pages
	 *            The number of result pages returned.
	 * @return searchResults Results found based on the provided criteria.
	 */
	public MovieResultsPage searchByMovieTitle(String seachString, boolean adult, int pages) {
		return super.searchMovie(seachString, 0, "en", adult, pages);
	}

	/**
	 * This function will return a list of movies and shows that link with the
	 * input keyword search string. Search is only performed on a single
	 * keyword. Consider updating search to except and array or arraylist of
	 * keywords to extend search.
	 * 
	 * 
	 * This constructor creates an exception with a message.
	 * 
	 * @param keywordSearchString
	 *            User provided keyword to search for related titles.
	 * @param pages
	 *            The number of result pages returned.
	 * @return searchResults MultiListResultsPage which inlcudes movies and tv
	 *         shows.
	 */
	public MultiListResultsPage searchByKeyword(String keywordSearchString, int pages) {
		return super.searchMulti(keywordSearchString, "English", pages);
	}

}
