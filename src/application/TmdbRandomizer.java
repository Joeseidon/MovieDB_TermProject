package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;

/**
 * @author Joseph Cutino
 * @version 1.0
 * @since 2017-07-07
 */
public class TmdbRandomizer {
	private List<MovieDb> movieWatchList;
	private List<MovieDb> movieFavorites;
	private ArrayList<MovieDb> moviePool;
	private MovieDBAccount user;
	private TmdbMovies movieObj;

	/**
	 * This constructor creates a movie database randomizer for the provided
	 * account.
	 * 
	 * @param user
	 *            The account from which this randomizer will draw data.
	 * @return None
	 */
	public TmdbRandomizer(MovieDBAccount usr) {
		this.user = usr;
		// store the users favorites and watchlist for later randomization
		this.update();
		// creates a movies object for later use
		createMoviesObj(user.getTmdbApi());
		// generates a pool of random movies that the user may like
		generatePool();
	}

	/**
	 * This function pulls the current users watchlist and favorites from the
	 * 	movie database and stores them locally.
	 * @param None
	 */
	public void update() {
		updateFavorites();
		updateWatchList();
	}

	/**
	 * This function provides the caller with a random movie from the generated
	 * movie pool as long as a movie pool has been generated.
	 * 
	 * @param None
	 * @return MovieDb Random movie
	 * @throws RandomNotFoundException
	 *             Thrown when a movie pool hasn't been generated.
	 */
	public MovieDb getRandomMovie() throws RandomNotFoundException {
		if (moviePool == null || moviePool.isEmpty()) {
			// if a movie pool doesn't exits, generate one
			try {
				// generate movie pool
				moviePool = createRandomPool();
			} catch (DataBaseConnectionException e) {
				e.printStackTrace();
			}
		}
		// These exceptions may be unneccesary now
		if (moviePool == null) {
			throw new RandomNotFoundException("moviePool is empty. Regenerate Pool");
		}
		if (moviePool.isEmpty()) {
			throw new RandomNotFoundException("moviePool is empty. Regenerate Pool");
		}
		

		// if there are movies in the pool generate a random movie from the pool
		Random rnd = new Random();
		int index = rnd.nextInt(moviePool.size() - 1);
		return moviePool.get(index);
	}

	/**
	 * Creates a randomized pool of movies from the users favorites and
	 * watchlist. If createRandomPool throws a dataBaseConnectionException the
	 * funtion waits for a time for a connection to potentially be esstablished.
	 * The pool is then generated from popular movies.
	 * 
	 * @param None
	 * @return None
	 */
	public void generatePool() {
		try {
			moviePool = createRandomPool();
		} catch (RandomNotFoundException rd) {
			// generate a pool from new releases and top movies
			moviePool = createPoolWithoutUserData();
		} catch (DataBaseConnectionException dbe) {
			// will need to wait and generate pool
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
	 * @return MoviePool Arraylist of random movies
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
	 * Generates a random movie pool from the current users favorites and
	 * 	watch list.
	 * 
	 * @param None
	 * @return MoviePool Arraylist of random movies
	 * @throws RandomNotFoundException
	 *             If the pool is empty
	 * @throws DataBaseConnectionException
	 *             If the users data cannot be referenced
	 */
	private ArrayList<MovieDb> createRandomPool() throws RandomNotFoundException, DataBaseConnectionException {
		// Containers used to store database returns and final movie pool
		List<MovieDb> tmp;
		ArrayList<MovieDb> pool = new ArrayList<MovieDb>();
		try {
			if (movieWatchList != null && !movieWatchList.isEmpty()) {
				for (MovieDb i : movieWatchList) {
					// retrieve similar movies for all in watchlist
					tmp = movieObj.getSimilarMovies(i.getId(), "English", 1).getResults();
					// if similar movies are found then they will be added to
					// the pool
					if (tmp != null && !tmp.isEmpty()) {
						for (MovieDb m : tmp) {
							pool.add(m);
						}
					}
				}
			}

			if (movieFavorites != null && !movieFavorites.isEmpty()) {
				for (MovieDb i : movieFavorites) {
					// retrieve similar movies for all in favorites
					tmp = movieObj.getSimilarMovies(i.getId(), "English", 1).getResults();
					// if similar movies are found then they will be added to
					// the pool
					if (tmp != null && !tmp.isEmpty()) {
						for (MovieDb m : tmp) {
							pool.add(m);
						}
					}
				}
			}
		} catch (NullPointerException e) {
			// Thrown when users database cannot be reached
			throw new DataBaseConnectionException();
		}

		if (pool == null || pool.isEmpty()) {
			// thrown when the final pool is empty
			throw new RandomNotFoundException("WatchList and Favorites must be empty.");
		}
		return pool;
	}

	/**
	 * Updates local favorites list with data retrieved from database for
	 * 	current user.
	 * 
	 * @param None
	 * @return None
	 */
	private void updateFavorites() {
		movieFavorites = user.getFavorites().getResults();
	}

	/**
	 * Updates local watch list with data retrieved from database for current
	 * 	user.
	 * 
	 * @param None
	 * @return None
	 */
	private void updateWatchList() {
		movieWatchList = user.getWatchList().getResults();
	}

	/**
	 * Generates a movieObj for the current users API key.
	 * 
	 * @param ApiKey
	 *            Provided to the java wrappers to link the movie object with
	 *            the api
	 * @return None
	 */
	private void createMoviesObj(TmdbApi apiKey) {
		movieObj = new TmdbMovies(apiKey);
	}
}
