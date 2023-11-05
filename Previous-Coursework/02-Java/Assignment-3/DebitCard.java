
/**
 * DebitCard is a subclass of Card and implements the Payment interface
 * @author adrianli375
 */
public class DebitCard extends Card implements Payment {
	
	String password;
	double balance;
	
	/**
	 * Constructor of a debit card object
	 * @param owner is the owner of the debit card
	 * @param cardNo is the card number of the debit card
	 * @param password is the password of the debit card
	 * @param balance is the balance of the debit card
	 */
	DebitCard(String owner, String cardNo, String password, double balance) {
		this.owner = owner;
		this.cardNo = cardNo;
		this.password = password;
		this.balance = balance;
	}
	
	/**
	 * Pay function executes the payment and generates the transaction record of debit card in phase 2
	 */
	@Override
	public void pay() {
		System.out.print("Pay money $: ");
		String amount = Wallet.readInput();
		String pwd = "";
		while(!pwd.equals(password)) {
			System.out.print("Please input the password: ");
			pwd = Wallet.readInput();
		}
		if (Double.parseDouble(amount)>balance) {
			System.out.println("current balance is $" + balance 
					+ ", no enough balance in Debit Card");
		}
		else {
			System.out.println("Payment successful!");
			String record = "Payment record: pay $" + Double.parseDouble(amount) 
							+ ", time: " + Wallet.getCurrentTime();
			System.out.println(record);
			transactions.add(record);
			balance = balance - Double.parseDouble(amount);
		}
	}

	/**
	 * Show payment function enables the program to print out the particulars of the debit card
	 */
	@Override
	public void showPayment() {
		System.out.println(" - Debit - owner: " + owner + ", card no: " + cardNo 
				+ ", balance: " + balance);
	}

}
