
/**
 * CreditCard is a subclass of Card and implements the Payment interface
 * @author adrianli375
 */
public class CreditCard extends Card implements Payment {
	
	double creditLimit;
	double currentCreditLimit = creditLimit;
	
	/**
	 * Constructor of a credit card object
	 * @param owner is the owner of the credit card
	 * @param cardNo is the card number of the credit card
	 * @param creditLimit is the credit limit of the credit card
	 */
	CreditCard(String owner, String cardNo, double creditLimit) {
		this.owner = owner;
		this.cardNo = cardNo;
		this.creditLimit = creditLimit;
		this.currentCreditLimit = creditLimit;
	}
	
	/**
	 * Pay function executes the payment and generates the transaction record of credit card in phase 2
	 */
	@Override
	public void pay() {
		System.out.print("Pay money $: ");
		String amount = Wallet.readInput();
		if (Double.parseDouble(amount)>currentCreditLimit) {
			System.out.println("current credit limit is $" + currentCreditLimit
					+ ", no enough credit limit in Credit Card");
		}
		else {
			System.out.println("Payment successful!");
			String record = "Payment record: pay $" + Double.parseDouble(amount) 
							+ ", time: " + Wallet.getCurrentTime();
			System.out.println(record);
			transactions.add(record);
			currentCreditLimit = currentCreditLimit - Double.parseDouble(amount);
		}
	}

	/**
	 * Show payment function enables the program to print out the particulars of the credit card
	 */
	@Override
	public void showPayment() {
		System.out.println(" - Credit - owner: " + owner + ", card no: " + cardNo + 
				", credit limit: " + creditLimit + ", current credit limit: " + currentCreditLimit);
	}

}
