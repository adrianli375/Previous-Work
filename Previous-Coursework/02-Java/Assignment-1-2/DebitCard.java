
/**
 * @author adrianli375
 * Debit Card class includes additional attributes that is specific to credit card (password)
 * and has a function to get the details of the credit card
 */
public class DebitCard extends Card {
	
	/**
	 * @param type type of debit card
	 * @param owner owner of debit card
	 * @param cardNo card no of debit card
	 * @param password password of debit card
	 */
	DebitCard(String type, String owner, String cardNo, String password) {
		this.type = "Debit";
		this.owner = owner;
		this.cardNo = cardNo;
		this.password = password;
	}
	
	/**
	 * the getCardDetail function prints the details of the card when the show function is called
	 */
	public void getCardDetail() {
		System.out.println("Owner name: " + this.owner + ", Card No.: " + this.cardNo + 
				", type: Debit");
	}
	
}
