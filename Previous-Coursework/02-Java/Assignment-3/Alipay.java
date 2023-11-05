
/**
 * Alipay is a subclass of MobilePay and implements the Payment interface
 * @author adrianli375
 */
public class Alipay extends MobilePay implements Payment {

	String accountName;
	
	/** Constructor for creating an Alipay object
	 * @param accountName is the name of the Alipay account
	 * @param balance is the balance stored in the Alipay account
	 */
	Alipay(String accountName, double balance) {
		this.accountName = accountName;
		this.balance = balance;
	}
	
	/**
	 * Pay function executes the payment and generates the transaction record of Alipay in phase 2
	 */
	@Override
	public void pay() {
		System.out.print("Pay money $: ");
		String amount = Wallet.readInput();
		System.out.print("Receiver Alipay account name: ");
		String receiver = Wallet.readInput();
		if (Double.parseDouble(amount)>balance) {
			System.out.println("current balance is $" + balance + ", no enough balance");
		}
		else {
			System.out.println("Payment successful!");
			String record = "Payment record: pay $" + Double.parseDouble(amount) + ", receiver: " 
							+ receiver + ", time: " + Wallet.getCurrentTime();
			System.out.println(record);
			transactions.add(record);
			balance = balance - Double.parseDouble(amount);
		}
	}

	/**
	 * Show payment function enables the program to print out the particulars of Alipay
	 */
	@Override
	public void showPayment() {
		System.out.println(" - Alipay - account name: " + accountName + ", balance: " + balance);
	}

}
