
/**
 * @author adrianli375
 * 
 * LogMessage is a class storing the variables need for a logMessage object and the constructor
 */
public class logMessage {
	String message;
	String orderID;
	
	/**
	 * constructor of a LogMessage object
	 * @param message the message to be printed on the log file
	 * @param orderID the ID of the order associated with the message
	 */
	logMessage(String message, String orderID) {
		this.message = message;
		this.orderID = orderID;
	}
	
}
