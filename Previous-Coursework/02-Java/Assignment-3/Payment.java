
/**
 * Payment interface contains the functions that must be implemented in the subclasses
 * @author adrianli375
 */
public interface Payment { //interface
	
	/**
	 * Function to show the payment tools before payment in phase 2, 
	 * to be implemented in the respective subclasses
	 */
	public void showPayment();
	
	/**
	 *  Pay function to execute payments in phase 2, 
	 *  to be implemented in the respective subclasses
	 */
	public void pay();
	
	
	/**
	 * Show transactions function to show each transactions made by the payment tool in phase 3
	 */
	public void showTransactions();
}
