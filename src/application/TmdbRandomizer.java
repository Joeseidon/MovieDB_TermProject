package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;

/**
 * This class links with an existing MovieDBAccount
 * and is used to generate a random movie pool which
 * the user can draw from for suggestions.
 * 
 * @author Joseph Cutino
 * @version 1.0
 * @since 2017-07-07
 */
public class TmdbRandomizer {
	
	/**
	 * Current users movie watch list.
	 */
	private List<MovieDb> movieWatchList;
	
	/**
	 * Current users favorite movies.
	 */
	private List<MovieDb> movieFavorites;
	
	/**
	 * Random movie dictionary created from
	 * user data or top movies on database.
	 */
	private ArrayList<MovieDb> moviePool;
	
	/**
	 * Current user.
	 */
	private MovieDBAccount user;
	
	/**
	 * Movie database object used for generating movie specific data.
	 */
	private TmdbMovies movieObj;

	/**
	 * This constructor creates a movie database
	 * randomizer for the provided account.
	 * 
	 * @param user The account from which this randomizer will draw data.
	 */
	public TmdbRandomizer(final MovieDBAccount user) {
		
		this.user = user;
		
		// Store the users favorites and
		// watch list for later randomization
		this.update();
		
		// Creates a movies object for later use
		createMoviesObj(user.getTmdbApi());
		
		// Generates a pool of random movies that the user may like
		generatePool();
	}

	/**
	 * This function pulls the current users watch list and favorites
	 * from the movie database and stores them locally.
	 * 
	 * @param None
	 */
	public void update() {
		
		updateFavorites();
		updateWatchList();
	}

	/**
	 * This function provides the caller with a random movie from the
	 * generated movie pool as long as a movie pool has been generated.
	 * 
	 * @param None
	 * @return MovieDb Random movie
	 * @throws RandomNotFoundException Thrown when a movie pool
	 *                                 hasn't been generated.
	 */
	public MovieDb getRandomMovie() throws RandomNotFoundException {
		
		if (moviePool == null || moviePool.isEmpty()) {
			// If a movie pool doesn't exits, generate one
			try {
				// Generate movie pool
				moviePool = createRandomPool();
			} catch (DataBaseConnectionException e) {
				e.printStackTrace();
			}
		}
		
		// These exceptions may be unnecessary now
		if (moviePool == null) {
			throw new RandomNotFoundException(
					"moviePool is empty. Regenerate Pool");
		}
		
		if (moviePool.isEmpty()) {
			throw new RandomNotFoundException(
					"moviePool is empty. Regenerate Pool");
		}
		

		// If there are movies in the pool generate
		// a random movie from the pool
		Random rnd = new Random();
		int index = rnd.nextInt(moviePool.size() - 1);
		
		return moviePool.get(index);
	}

	/**
	 * Creates a randomized pool of movies from the users favorites and
	 * watch list. If createRandomPool throws a dataBaseConnectionException
	 * the function waits for a time for a connection to potentially be
	 * established. The pool is then generated from popular movies.
	 * 
	 * @param None
	 */
	public void generatePool() {
		
		try {
			moviePool = createRandomPool();
		} catch (RandomNotFoundException rd) {
			// Generate a pool from new releases and top movies
			moviePool = createPoolWithoutUserData();
		} catch (DataBaseConnectionException dbe) {
			// Will need to wait and generate pool
			try {
				Thread.sleep(400);
				moviePool = createPoolWithoutUserData();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * Generates a random movie pool from popular and top rated movies.
	 * 
	 * @param None
	 * @return MoviePool ArrayList of random movies
	 */
	private ArrayList<MovieDb> createPoolWithoutUserData() {
		
		List<MovieDb> tmp;
		
		ArrayList<MovieDb> pool = new ArrayList<MovieDb>();
		
		tmp = movieObj.getPopularMovies("English", 1).getResults();
		
		for (MovieDb m : tmp) {
			pool.add(m);
		}
		
		tmp = movieObj.getTopRatedMovies("English", 1).getResults();
		
		for (MovieDb m : tmp) {
			pool.add(m);
		}
		
		return pool;
	}

	/**
	 * Generates a random movie pool from the
	 * current users favorites and watch list.
	 * 
	 * @param None
	 * @return MoviePool ArrayList of random movies
	 * @throws RandomNotFoundException If the pool is empty
	 * @throws DataBaseConnectionException If the users data
	 *                                     cannot be referenced
	 */
	private ArrayList<MovieDb> createRandomPool() 
			throws RandomNotFoundException, DataBaseConnectionException {
		
		// Containers used to store database
		// returns and final movie pool
		List<MovieDb> tmp;
		ArrayList<MovieDb> pool = new ArrayList<MovieDb>();
		
		try {
			
			if (movieWatchList != null && !movieWatchList.isEmpty()) {
				
				for (MovieDb i : movieWatchList) {
					// Retrieve similar movies
					// for all in watch list
					tmp = movieObj.getSimilarMovies(
							i.getId(), "English", 1).getResults();
					
					// If similar movies are found then
					// they will be added to the pool
					if (tmp != null && !tmp.isEmpty()) {
						for (MovieDb m : tmp) {
							pool.add(m);
						}
					}
				}
			}

			if (movieFavorites != null && !movieFavorites.isEmpty()) {
				
				for (MovieDb i : movieFavorites) {
					// Retrieve similar movies
					// for all in favorites
					tmp = movieObj.getSimilarMovies(
							i.getId(), "English", 1).getResults();
					
					// If similar movies are found then
					// they will be added to the pool
					if (tmp != null && !tmp.isEmpty()) {
						for (MovieDb m : tmp) {
							pool.add(m);
						}
					}
				}
			}
			
		} catch (NullPointerException e) {
			// Thrown when users database cannot be reached
			throw new DataBaseConnectionException(
					"Failed to connect to the Movie Data Base");
		}

		if (pool == null || pool.isEmpty()) {
			// Thrown when the final pool is empty
			throw new RandomNotFoundException(
					"WatchList and Favorites can not be empty.");
		}
		
		return pool;
	}

	/**
	 * Updates local favorites list with data
	 * retrieved from database for current user.
	 * 
	 * @param None
	 */
	private void updateFavorites() {
		
		movieFavorites = user.getFavorites().getResults();
	}

	/**
	 * Updates local watch list with data retrieved
	 * from database for current user.
	 * 
	 * @param None
	 */
	private void updateWatchList() {
		
		movieWatchList = user.getWatchList().getResults();
	}

	/**
	 * Generates a movieObj for the current users API key.
	 * 
	 * @param apiKey Provided to the java wrappers to link
	 *               the movie object with the API
	 */
	private void createMoviesObj(final TmdbApi apiKey) {
		
		movieObj = new TmdbMovies(apiKey);
	}
}
