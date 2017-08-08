package application;

/**
 * This exception is thrown when a random movie pool
 * was not created or the resulting pool was null.
 * 
 * @author Joseph Cutino
 * @version 1.0
 * @since 2017-07-07
 */
@SuppressWarnings("serial")
public class RandomNotFoundException extends Exception {
	
	/**
	 * This constructor creates an exception with a message.
	 * 
	 * @param message String that will be stored within the exception
	 */
	public RandomNotFoundException(final String message) {
		super(message);
	}
}
