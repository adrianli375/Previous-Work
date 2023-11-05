import java.util.ArrayList;

/**
 * @author adrianli375
 * Credit card class represents the credit card with attributes like type, owner and cardNo
 * and have functions to access these data
 */
public abstract class Card {
	String type;
	String owner;
	String cardNo;
	String password;
	ArrayList<String> transaction = new ArrayList<String>();
	
	/**
	 * call this method to get access to the card no
	 * @return the cardNo
	 */
	public String getCardNo() {
		return cardNo;
	}
	
	/**
	 * call this method to get access to the first 4 digits of the card
	 * @return first 4 digit of card no.
	 */
	public String getCardNoFirst4() {
		String headNo = cardNo.substring(0, 4);
		return headNo;
	}
	
	/**
	 * call this method to show the details of the card when the show card function is called
	 * this method implements polymorphism
	 */
	public abstract void getCardDetail();
	
}
