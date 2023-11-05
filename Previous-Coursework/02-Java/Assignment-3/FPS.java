
/**
 * FPS is a subclass of MobilePay and implements the Payment interface
 * @author adrianli375
 */
public class FPS extends MobilePay implements Payment {

	String phoneNo;
	
	/** Constructor for creating an FPS object
	 * @param phoneNo is the phone number of the FPS account
	 * @param balance is the balance stored in the FPS account
	 */
	FPS(String phoneNo, double balance) {
		this.phoneNo = phoneNo;
		this.balance = balance;
	}
	
	/**
	 * Pay function executes the payment and generates the transaction record of FPS in phase 2
	 */
	@Override
	public void pay() {
		System.out.print("Pay money $: ");
		String amount = Wallet.readInput();
		System.out.print("Receiver FPS phone number: ");
		String receiverPhoneNo = Wallet.readInput();
		if (Double.parseDouble(amount)>balance) {
			System.out.println("current balance is $" + balance + ", no enough balance");
		}
		else {
			System.out.println("Payment successful!");
			String record = "Payment record: pay $" + Double.parseDouble(amount) + ", receiver: " 
							+ receiverPhoneNo + ", time: " + Wallet.getCurrentTime();
			System.out.println(record);
			transactions.add(record);
			balance = balance - Double.parseDouble(amount);
		}
	}

	/**
	 * Show payment function enables the program to print out the particulars of FPS
	 */
	@Override
	public void showPayment() {
		System.out.println(" - FPS - phone number: " + phoneNo + ", balance: " + balance);
	}

}
