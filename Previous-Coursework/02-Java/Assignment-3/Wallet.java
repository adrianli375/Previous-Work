import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Wallet class contains the main method and controls the work flow
 * @author adrianli375
 */
public class Wallet {
	
	ArrayList<Payment> paymentTools = new ArrayList<Payment>();
	
	/**
	 * Initialize function prompts the user to initialize their payment tools
	 */
	public void initialize() {
		System.out.println("------------Phase 1: Initialization------------");
		String input = "";
		while(!input.equals("pay")) {
			System.out.print("Initialize payment tool (Debit/Credit/FPS/Alipay): ");
			input = readInput();
			if(input.equals("Debit")) {
				System.out.print("Initialize one debit card, Please input the owner name: ");
				String owner = readInput();
				System.out.print("Please input the Card No: ");
				String cardNo = readInput();
				System.out.print("Please input the password: ");
				String password = readInput();
				System.out.print("Please input balance: ");
				String balance = readInput();
				paymentTools.add(new DebitCard(owner, cardNo, password, Double.parseDouble(balance)));
			}
			else if (input.equals("Credit")) {
				System.out.print("Initialize one credit card, Please input the owner name: ");
				String owner = readInput();
				System.out.print("Please input the Card No: ");
				String cardNo = readInput();
				System.out.print("Please input the creditLimit: ");
				String creditLimit = readInput();
				paymentTools.add(new CreditCard(owner, cardNo, Double.parseDouble(creditLimit)));
			}
			else if (input.equals("FPS")) {
				System.out.print("Initialize FPS, Please input your phone number: ");
				String phoneNo = readInput();
				System.out.print("Please input balance: ");
				String balance = readInput();
				paymentTools.add(new FPS(phoneNo, Double.parseDouble(balance)));
			}
			else if (input.equals("Alipay")) {
				System.out.print("Initialize Alipay, Please input your account name: ");
				String accountName = readInput();
				System.out.print("Please input balance: ");
				String balance = readInput();
				paymentTools.add(new Alipay(accountName, Double.parseDouble(balance)));
			}
		}
	}
	
	/**
	 * checkInteger function checks whether a string is an integer
	 * @param s a string
	 * @return the integer if the string is an integer, otherwise return a random integer
	 * that is an invalid input in phase 2
	 */
	public int checkInteger(String s) {
		try {
			int i = Integer.parseInt(s);
			return i;
		} catch (NumberFormatException e) {
			return paymentTools.size()+1;
		}
	}
	
	/**
	 * Pay function prompts the user to payment using their payment tools in phase 2
	 */
	public void pay() {
		System.out.println("------------Phase 2: Pay------------");
		int i = 1;
		for (Payment p : paymentTools) {
			System.out.print(i);
			i++;
			p.showPayment(); //polymorphism
		}
		String input = "";
		while(!input.equals("show")) {
			System.out.print("Please input the index to choose the payment method: ");
			input = Wallet.readInput();
			int index = checkInteger(input);
			if (index<=paymentTools.size() && index>0) {
				paymentTools.get(index-1).pay();
			}
		}
	}
	
	/**
	 * Show function prompts the user to show the particulars and transaction records 
	 * of their payment tools
	 */
	public void show() {
		System.out.println("------------Phase 3: Show payment record------------");
		int i = 1;
		for (Payment p : paymentTools) {
			System.out.print(i);
			i++;
			p.showPayment(); //polymorphism
			p.showTransactions();
		}
	}
	
	public static void main(String[] args) {
		Wallet w = new Wallet();
		w.initialize();
		w.pay();
		w.show();
	}
	
	/**
	 * Read the user input from the console
	 * @return the user input as a String
	 */	
	public static String readInput() {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		String inputLine = null;
		try {
			inputLine = input.readLine();
			//System.out.println(inputLine);
			return inputLine;
		} catch (IOException e) {
			System.err.println("Input ERROR.");
		}
		// return empty string if error occurs
		return inputLine;

	}
	
	/**
	 * Get current system time as a String
	 * @return a String for the system time in specific format.
	 */
	public static String getCurrentTime() {
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd '-' HH:mm:ss z");
		Date date = new Date(System.currentTimeMillis());
		//System.out.println(formatter.format(date));
		return formatter.format(date);
	}
	
}
