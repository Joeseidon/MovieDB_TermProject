package application;

import info.movito.themoviedbapi.TmdbAccount.MediaType;
import info.movito.themoviedbapi.TmdbAccount;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbAuthentication;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.config.Account;
import info.movito.themoviedbapi.model.config.TokenSession;
import info.movito.themoviedbapi.model.core.AccountID;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.core.ResponseStatus;
import info.movito.themoviedbapi.model.core.SessionToken;

/**
 * Creates an account which links with an existing movie database 
 * account. This class manages account preferences (ex. favorites 
 * and watch list).
 * @author Joseph Cutino
 * @version 1.0
 * @since 2016-07-07
 */
public class MovieDBAccount {
	private TmdbApi tmdbApi;
	private SessionToken sessionToken;
	private TmdbAccount tmdbAccount;
	private Account act;
	private AccountID actId;

	/**
	 * Public constructor which creates a TmdbApi, session token, account, 
	 * and account ID for use throughout this program.
	 * 
	 * @param None
	 * @exception DataBaseConnectionException
	 * 		exception for when database can't connect
	 */
	public MovieDBAccount() throws DataBaseConnectionException {
		try {
		tmdbApi = new TmdbApi("3c55a927fbd8c6990313cb6d5de43d62");
		sessionToken = getSessionToken();
		tmdbAccount = tmdbApi.getAccount();
		act = tmdbAccount.getAccount(sessionToken);
		actId = new AccountID(act.getId());
		} catch (Exception e) {
			throw new DataBaseConnectionException(
				"Failed to connect to the Movie Data Base");
		}
	}

	/**
	 * Returns the current users name.
	 * 
	 * @param None
	 * @return userName Name of the current user
	 */
	public String getUserName() {
		return act.getName();
	}

	/**
	 * Adds the provided movie to the current users watchlist.
	 * 
	 * @param mToAdd
	 *            The movie chosen to be added.
	 * @return responseStatus The status of the requested action.
	 */
	public ResponseStatus addMovieToWatchlist(MovieDb mToAdd) {
		return tmdbAccount.addToWatchList(sessionToken, actId, mToAdd.getId(), MediaType.MOVIE);
	}

	/**
	 * Removes the provided movie from the current users watchlist.
	 * 
	 * @param mToRemove
	 *            The movie chosen to be removed.
	 * @return responseStatus The status of the requested action.
	 */
	public ResponseStatus removeMovieFromWatchlist(MovieDb mToRemove) {
		return tmdbAccount.removeFromWatchList(sessionToken, actId, mToRemove.getId(), MediaType.MOVIE);
	}

	/**
	 * Adds the provided movie to the current users favorites.
	 * 
	 * @param mToAdd
	 *            The movie chosen to be added.
	 * @return responseStatus The status of the requested action.
	 */
	public ResponseStatus addMovieToFavorites(MovieDb mToAdd) {
		return tmdbAccount.addFavorite(sessionToken, actId, mToAdd.getId(), MediaType.MOVIE);
	}

	/**
	 * Removes the provided movie from the current users favorites.
	 * 
	 * @param mToRemove
	 *            The movie chosen to be removed.
	 * @return responseStatus The status of the requested action.
	 */
	public ResponseStatus removeMovieFromFavorites(MovieDb mToRemove) {
		return tmdbAccount.removeFavorite(sessionToken, actId, mToRemove.getId(), MediaType.MOVIE);
	}

	/**
	 * Returns the current users watchlist.
	 * 
	 * @param None
	 * @return watchList The watchlist of the current user.
	 */
	public MovieResultsPage getWatchList() {
		return tmdbAccount.getWatchListMovies(sessionToken, actId, 1);
	}

	/**
	 * Returns the current users favorites.
	 * 
	 * @param None
	 * @return favorites The favorites of the current user.
	 */
	public MovieResultsPage getFavorites() {
		return tmdbAccount.getFavoriteMovies(sessionToken, actId);
	}

	/**
	 * Provides the caller with a sessionToken generated for the current user.
	 * 
	 * @param None
	 * @return SessionToken session token for the current users session with the
	 *         movie manager
	 */
	private SessionToken getSessionToken() {
		TmdbAuthentication tmdbAuth = tmdbApi.getAuthentication();
		TokenSession tokenSession = tmdbAuth.getSessionLogin("cutinoj", "Gv$u2017Temp");
		SessionToken sessionToken = new SessionToken(tokenSession.getSessionId());

		return sessionToken;
	}

	/**
	 * Provides the caller with the current users api.
	 * 
	 * @param None
	 * @return TmdbApi api for the current user
	 */
	public TmdbApi getTmdbApi() {
		return tmdbApi;
	}
}
