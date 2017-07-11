package application;

/**
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
	 */
	public DataBaseConnectionException() {
	}

	/**
	 * Public constructor that creates an exception with a message.
	 * 
	 * @param message
	 *            Exception message.
	 */
	public DataBaseConnectionException(final String message) {
		super(message);
	}
}
