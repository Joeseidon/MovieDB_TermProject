package application;

/**
 * Exception thrown when a connection to the
 * movie data was interrupted or never made.
 * 
 * @author Joseph Cutino
 * @version 1.0
 * @since 2017-07-07
 */
@SuppressWarnings("serial")
public class DataBaseConnectionException extends Exception {
	
	/**
	 * Public constructor that creates an exception with a message.
	 * 
	 * @param message Exception message.
	 */
	public DataBaseConnectionException(final String message) {
		super(message);
	}
}
