
/**
 * @author adrianli375
 * Credit Card class includes additional attributes that is specific to credit card (CVV and expiryDate)
 * has a constructor to create a credit card object
 * and has a function to get the details of the credit card
 */
public class CreditCard extends Card {
	String expiryDate;
	String cvv;

	/**
	 * @param type type of credit card
	 * @param owner owner of credit card
	 * @param cardNo card no of the credit card (16-digit)
	 * @param expiryDate expiry date of the credit card
	 * @param cvv cvv number of the credit card (3-digit)
	 */
	CreditCard(String type, String owner, String cardNo, String expiryDate, String cvv){
		this.type = "Credit";
		this.owner = owner;
		this.cardNo = cardNo;
		this.expiryDate = expiryDate;
		this.cvv = cvv;
	}
	
	/**
	 * the getCardDetail function prints the details of the card when the show function is called
	 */
	public void getCardDetail() {
		System.out.println("Owner name: " + this.owner + ", Card No.: " + this.cardNo + 
				", type: Credit, expiryDate: " + this.expiryDate + ", cvv: " + this.cvv);
	}
	
}
