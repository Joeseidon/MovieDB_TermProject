package application;

import info.movito.themoviedbapi.TmdbAccount.MediaType;
import info.movito.themoviedbapi.TmdbAccount;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbAuthentication;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.config.Account;
import info.movito.themoviedbapi.model.config.TokenSession;
import info.movito.themoviedbapi.model.core.AccountID;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.core.ResponseStatus;
import info.movito.themoviedbapi.model.core.SessionToken;

public class MovieDBAccount {
	private TmdbApi tmdbApi;
	private SessionToken sessionToken;
	private TmdbAccount tmdbAccount;
	private Account act;
	private AccountID actId;
	TmdbSearch tmdbSearch;

	/*
	 * Public constructor which creates a TmdbApi, session token, account, and
	 * account ID for use throughout this program.
	 * 
	 * @Parameters None
	 * 
	 * @author Joseph
	 */
	public MovieDBAccount() {
		tmdbApi = new TmdbApi("3c55a927fbd8c6990313cb6d5de43d62");
		sessionToken = getSessionToken();
		tmdbAccount = tmdbApi.getAccount();
		act = tmdbAccount.getAccount(sessionToken);
		actId = new AccountID(act.getId());
		//moved functionality to new class
		//tmdbSearch = tmdbApi.getSearch();
	}

	
	/*
	 * 
	 * @author Joseph
	 */
	public ResponseStatus addMovieToWatchlist(MovieDb mToAdd) {
		return tmdbAccount.addToWatchList(sessionToken, actId, mToAdd.getId(), MediaType.MOVIE);
	}

	/*
	 * @author Joseph
	 */
	public ResponseStatus removeMovieFromWatchlist(MovieDb mToRemove) {
		return tmdbAccount.removeFromWatchList(sessionToken, actId, mToRemove.getId(), MediaType.MOVIE);
	}

	/*
	 * @author Joseph
	 */
	public MovieResultsPage getWatchList() {
		return tmdbAccount.getWatchListMovies(sessionToken, actId, 1);
	}

	public MovieResultsPage getFavorites() {
		return tmdbAccount.getFavoriteMovies(sessionToken, actId);
	}

	/*
	 * @author Joseph
	 */
	private SessionToken getSessionToken() {
		TmdbAuthentication tmdbAuth = tmdbApi.getAuthentication();
		TokenSession tokenSession = tmdbAuth.getSessionLogin("cutinoj", "Gv$u2017Temp");
		System.out.println("Session ID: " + tokenSession.getSessionId());
		SessionToken sessionToken = new SessionToken(tokenSession.getSessionId());

		return sessionToken;
	}

	/*
	 * @author Joseph
	 */
	public TmdbApi getTmdbApi() {
		return tmdbApi;
	}
}
