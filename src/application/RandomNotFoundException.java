package application;

/*
 * @author Joseph
 */
@SuppressWarnings("serial")
public class RandomNotFoundException extends Exception{
	/*
	 * @author Joseph
	 */
	public RandomNotFoundException(){}
	
	/*
	 * @author Joseph
	 */
	public RandomNotFoundException(String message){
		super(message);
	}
}
