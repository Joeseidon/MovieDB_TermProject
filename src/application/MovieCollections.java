package application;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

/**
 * Collection of movies.
 * 
 * @author Unknown
 *
 */
public class MovieCollections {

	/**
	 * Movies stored in collection.
	 */
	private final TmdbMovies movies;
	
	/**
	 * Public constructor.
	 * 
	 * @param api API connection created for current account. 
	 */
	public MovieCollections(final TmdbApi api) {
		
		movies = new TmdbMovies(api);
	}
	
	/**
	 * Returns the current top rated movies.
	 * 
	 * @param page result page
	 * @return TopRatedMovies The current top rated movies.
	 */
	public MovieResultsPage getTopRated(final int page) {
		
		return movies.getTopRatedMovies("english", page);
	}
	
	/**
	 * Returns movies which are going to be released soon.
	 * 
	 * @param page result page
	 * @return Upcoming Movies that will be out shortly.
	 */
	public MovieResultsPage getUpcoming(final int page) {
		
		return movies.getUpcoming("english", page);
	}
	
	/**
	 * Returns movies which are currently being played.
	 * 
	 * @param page result page
	 * @return BeingPlayed Movies being played in theaters.
	 */
	public MovieResultsPage getNowPlaying(final int page) {
		
		return movies.getNowPlayingMovies("english", page);
	}
}
