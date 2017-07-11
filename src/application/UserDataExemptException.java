package application;

/**
 * @author  Joseph Cutino
 * @version 1.0
 * @since   2016-07-07
 */
@SuppressWarnings("serial")
public class UserDataExemptException extends Exception {

	/**
	 * This constructor creates an exception with no message.
	 * 
	 * @param None
	 * @return None
	 */
	public UserDataExemptException() {
	}

	/**
	 * This constructor creates an exception with a message.
	 * 
	 * @param message
	 *            String that will be stored within the exception
	 * @return None
	 */
	public UserDataExemptException(String message) {
		super(message);
	}
}
