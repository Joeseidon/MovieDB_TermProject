package application;

/**
 * @author Joseph Cutino
 * @version 1.0
 * @since 2016-07-07
 */
@SuppressWarnings("serial")
public class RandomNotFoundException extends Exception {
	/**
	 * This constructor creates an exception without a message.
	 * 
	 * @param None
	 * @return None
	 */
	public RandomNotFoundException() {
	}

	/**
	 * This constructor creates an exception with a message.
	 * 
	 * @param message
	 *            String that will be stored within the exception
	 * @return None
	 */
	public RandomNotFoundException(String message) {
		super(message);
	}
}
