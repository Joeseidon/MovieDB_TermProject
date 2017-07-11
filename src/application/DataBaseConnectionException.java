package application;

/**
 * Exception thrown when a connection to the movie data was
 * interrupted or never made.
 * @author Joseph Cutino
 * @version 1.0
 * @since 2016-07-07
 */
@SuppressWarnings("serial")
public class DataBaseConnectionException extends Exception {
	/**
	 * Public constructor that creates an exception without a message.
	 * 
	 * @param None
	 * @return None
	 */
	public DataBaseConnectionException() {
	}

	/**
	 * Public constructor that creates an exception with a message.
	 * 
	 * @param message
	 *            Exception message.
	 * @return None
	 */
	public DataBaseConnectionException(String message) {
		super(message);
	}
}
