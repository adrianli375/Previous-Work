import java.util.ArrayList;

/**
 * Abstract class MobilePay contains the variables to be implemented in the subclass, 
 * and has a function called show transactions
 * @author adrianli375
 */
public abstract class MobilePay implements Payment { //abstract class
	double balance;
	ArrayList<String> transactions = new ArrayList<String>();
	
	/**
	 * Show transactions function enables the payment tool to show the transactions done in phase 2
	 * The function is executed in phase 3
	 */
	public void showTransactions() {
		for(int j=0; j<transactions.size(); j++) {
			System.out.println(transactions.get(j));
		}
	}
	
}
