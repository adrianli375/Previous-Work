import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * @author adrianli375
 * 
 * Order is a class storing the variables methods needed for an order object
 */
public class Order {
	int orderID;
	String action;
	String details;
	String receivedTime;
	double price;
	int volume;
	
	/**
	 * Constructor of an order object
	 * @param receivedTime the received time of the order
	 * @param orderID the ID of the order
	 * @param action the action of the order (BUY/SELL/DELETE/MODIFY/SHOWQUEUE/SHOWSTATUS)
	 * @param details
	 */
	Order(String receivedTime, int orderID, String action, String details) {
		this.receivedTime = receivedTime;
		this.orderID = orderID;
		this.action = action;
		this.details = details;
	}
	
	/**
	 * printOrderToQueue is a function to print the order details of the bid/ask queue
	 * @return the formatted message to be printed in the bid/ask queue when the SHOWQUEUE function 
	 * is called
	 */
	public String printOrdertoQueue() {
		String message = Integer.toString(orderID) + "," + action + "," + Double.toString(price)
		+ ";" + Integer.toString(volume);
		return message;
	}
	
	/**
	 * modifytXID is a function which can format the transaction ID and prints it in the log file
	 * @param txID the transaction ID to be printed in the log file
	 * @return the formatted transaction ID to be printed in the log file
	 */
	public String modifyTXID(int txID){
		String ID = Integer.toString(txID);
		String output = "";
		for(int i = 0; i<6-ID.chars().count(); i++) {
			output += "0";
		}
		output += ID;
		return output;
	}
	
	/**
	 * orderExecuted is a function which prints the transaction details of an executed order 
	 * in the log file
	 * @param mode the action of the order object, i.e. "BUY" or "SELL"
	 * @param oID the ID of the order
	 * @param price the executed price of the order
	 * @param volume the executed volume of the order
	 * @param txID the transaction ID to be printed in the log file
	 * @return a logMessage object storing the message to be printed and the ID of the order
	 */
	public logMessage orderExecuted(String mode, int oID, double price, int volume, int txID) {
		String outputStringToLog = "";
		switch(mode) {
		case "BUY":
			outputStringToLog = modifyTXID(txID) + "," + getCurrentTime() + 
			",ORDER-EXECUTED," + Integer.toString(oID) + ";" + Double.toString(price) + 
			";" + Integer.toString(volume) + ";BOUGHT\n";
			break;
		case "SELL":
			outputStringToLog = modifyTXID(txID) + "," + getCurrentTime() + 
			",ORDER-EXECUTED," + Integer.toString(oID) + ";" + Double.toString(price) + 
			";" + Integer.toString(volume) + ";SOLD\n";
			break;
		}
		logMessage logmsg = new logMessage(outputStringToLog, Integer.toString(oID));
		return logmsg;
	}
	
	/**
	 * orderDeleted is a function which prints the transaction details of a deleted order 
	 * in the log file
	 * @param details the order ID to be deleted
	 * @param txID the transaction ID to be printed in the log file
	 * @return a logMessage object storing the message to be printed and the ID of the order
	 */
	public logMessage orderDeleted(String details, int txID) {
		String outputStringToLog = modifyTXID(txID) + "," + getCurrentTime() + 
				",ORDER-DELETED," + details + "\n";
		logMessage logmsg = new logMessage(outputStringToLog, details);
		return logmsg;
	}
	
	/** orderRejected is a function which prints the transaction details of a rejected order 
	 * in the console and log file
	 * @param txID the transaction ID to be printed in the log file
	 * @return a logMessage object storing the message to be printed and the ID of the order
	 */
	public logMessage orderRejected(int txID) {
		String consoleString = "Order " + orderID + " REJECTED " + getCurrentTime();
		System.out.println(consoleString);
		String outputStringToLog = modifyTXID(txID) + "," + getCurrentTime() + 
				",ORDER-REJECTED," + orderID + "\n";
		logMessage logmsg = new logMessage(outputStringToLog, Integer.toString(orderID));
		return logmsg;
	}
	
	/**
	 * orderUpdated is a function which prints the transaction details of an updated order 
	 * in the log file
	 * @param details details of the order
	 * @param txID the transaction ID to be printed in the log file
	 * @return a logMessage object storing the message to be printed and the ID of the order
	 */
	public logMessage orderUpdated(String details, int txID) {
		String[] message = details.split(";");
		String outputStringToLog = modifyTXID(txID) + "," + getCurrentTime() + 
				",ORDER-UPDATED," + message[0] + "\n";
		logMessage logmsg = new logMessage(outputStringToLog, message[0]);
		return logmsg;
	}
	
	/**
	 * orderShow is a function which prints the transaction details of a show order in the log file
	 * @param details details of the order
	 * @param txID the transaction ID to be printed in the log file
	 * @return a logMessage object storing the message to be printed and the ID of the order
	 */
	public logMessage orderShow(String details, int txID) {
		String outputStringToLog = modifyTXID(txID) + "," + getCurrentTime() + 
				",ORDER-SHOW," + details + "\n";
		logMessage logmsg = new logMessage(outputStringToLog, details);
		return logmsg;
	}
	
	/**
	 * orderShowQueue is a function which prints the transaction details of a show queue order 
	 * in the log file
	 * @param txID the transaction ID to be printed in the log file
	 * @return a logMessage object storing the message to be printed and the ID of the order
	 */
	public logMessage orderShowQueue(int txID) {
		String outputStringToLog = modifyTXID(txID) + "," + getCurrentTime() + 
				",ORDER-SHOWQUEUE," + orderID + "\n";
		logMessage logmsg = new logMessage(outputStringToLog, Integer.toString(orderID));
		return logmsg;
	}
	
	/**
	 * orderCompleted is a function which prints the transaction details of a completed order in 
	 * the log file
	 * @param txID the transaction ID to be printed in the log file
	 * @return a logMessage object storing the message to be printed and the ID of the order
	 */
	public logMessage orderCompleted(int txID) {
		String outputStringToLog = modifyTXID(txID) + "," + getCurrentTime() + 
				",ORDER-COMPLETED," + orderID + "\n";
		logMessage logmsg = new logMessage(outputStringToLog, Integer.toString(orderID));
		return logmsg;
	}
	
	/**
	 * orderReceived is a function which prints the transaction details of a received order in the 
	 * console and log file
	 * @param txID the transaction ID to be printed in the log file
	 * @return a logMessage object storing the message to be printed and the ID of the order
	 */
	public logMessage orderReceived(int txID) {
		String consoleString1 = "Order " + orderID + " RECEIVED " + getCurrentTime();
		System.out.println(consoleString1);
		String consoleString2 = "order-ID:" + orderID + ",order-action:" + action + 
				",order-details:" + details;
		System.out.println(consoleString2);
		String outputStringToLog = modifyTXID(txID) + "," + getCurrentTime() + 
				",ORDER-RECEIVED," + orderID + "\n";
		logMessage logmsg = new logMessage(outputStringToLog, Integer.toString(orderID));
		return logmsg;
	}
	
	/**
	 * getCurrentTime is a method to obtain the current time recorded
	 * @return the formatted string of the current time
	 */
	public String getCurrentTime() {
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd '-' HH:mm:ss z");
		Date date = new Date(System.currentTimeMillis());
		return formatter.format(date);
	}
	
}
