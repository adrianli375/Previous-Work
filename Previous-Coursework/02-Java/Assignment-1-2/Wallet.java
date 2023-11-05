import java.io.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * @author adrianli375
 * Wallet class contains the main method and other functions
 * including add, show, remove, use
 * Also has three given functions: readInput,  getCurrentTime and getCardByNo
 *
 */
public class Wallet {
	ArrayList<Card> cardList = new ArrayList<Card>();
	
	/**
	 * will be called when the user wants to add card into the function
	 * will ask user to input the information about credit or debit card
	 */
	public void addCard() {
		while (true) {
			boolean cardAdded = false;
			while (!cardAdded) {
				System.out.print("Please input type of Card (Debit or Credit): ");
				String cardType = readInput();
				if (cardType.equals("Credit")) {
					System.out.print("Add one credit card. Please input the card owner name: ");
					String cardOwner = readInput();
					System.out.print("Please input the Card No.: "); //16-digit
					String cardNo = readInput();
					System.out.print("Please input expiry date of Credit Card: ");
					String expiryDate = readInput();
					System.out.print("Please input cvv number: ");
					String cvv = readInput();
					cardList.add(new CreditCard("Credit", cardOwner, cardNo, expiryDate, cvv));
					cardAdded = true;
				}
				else if (cardType.equals("Debit")) {
					System.out.print("Add one debit card. Please input the owner name: ");
					String cardOwner = readInput();
					System.out.print("Please input the Card No.: "); //16-digit
					String cardNo = readInput();
					System.out.print("Please input the password: ");
					String cardPassword = readInput();
					cardList.add(new DebitCard("Debit", cardOwner, cardNo, cardPassword));
					cardAdded = true;
				}
			}
			System.out.print("Card is added successfully. Do you want to add another card (Yes or No): ");
			String add = readInput();
			if(add.equals("Yes")) {
				continue;
			}
			else
				break;
		}
	}
	
	/**
	 * show card function will be called when the user wants to check the card information
	 * input 'all' will show the card number with the first 4 digits and with other digits masked
	 * input cardNo, will show the details of the card info
	 * if the cardNo is not in the wallet it will inform the user that the card does not exist
	 */
	public void showCard() {
		
		while(true) {
			System.out.print("Please input instruction to check card info (all, card no., or c): ");
			String input = readInput();
			if (input.equals("c") ) {
				System.out.println("Show function finished");
				break;
			}
			else if (input.equals("all") ){
				//System.out.println("The number of credit cards in the wallet: " + w.cardNum);
				for (Card card : cardList) {
					System.out.println("Credit Card No.: " + card.getCardNoFirst4() + " **** **** **** ");
				}
			}
			else {
				Card c = getCardByNo(input);
				if(c == null) {
					System.out.println("No such card in the wallet.");
				}
				else {
					c.getCardDetail(); //polymorphism implemented here
					for (String record : c.transaction) {
						System.out.println(record);
					}
				}
			}
		}
	}
	
	/**
	 * remove card function will be called when the user want to remove the card
	 * input the card no which the user wants to remove
	 * find the card and set it to be removed 
	 * if the card no is not in the wallet, the program will inform that the card does not exist
	 */
	public void removeCard() {
		//boolean flag = true;
		System.out.print("Please input the card no. to be removed: ");
		String input = readInput();
		
		Card c = getCardByNo(input);
		if(c == null) {
			System.out.println("Remove fail, no such credit card.");
		}
		else {
			cardList.remove(c);
			System.out.println("Remove successfully");
		}

	}

	/**
	 * use card function will be called when the user want to use the card
	 * the user will input the card no and the cost amount
	 * the use card function will record the amount and the transaction time
	 * if the card used is a debit card, password input will be required for validation
	 */
	public void useCard() {
		System.out.print("Please input the card no. which you want to use: ");
		String cardForTrans = readInput();
		Card c = getCardByNo(cardForTrans);
		if(c == null) {
			System.out.println("No such card, transaction cannot be processed");
		}
		else {
			if (c.type.equals("Debit")) {
				boolean passwordCorrect = false;
				while (!passwordCorrect) {
					System.out.print("Please input the password: ");
					String password = readInput();
					if (password.equals(c.password)) {
						System.out.println("Password correct");
						passwordCorrect = true;
						break;
					}
					System.out.println("Password incorrect");
				}
			}
			System.out.print("Please input the transaction amount: ");
			String money = readInput();
			String time = getCurrentTime();
			String record = "Transaction detail: spend $" + money + ", time: " + time;
			c.transaction.add(record);
			System.out.println(record);
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Wallet w = new Wallet();
		while(true) {
			System.out.print("Please choose next action (add, use, remove, show, close): ");
			String choice = w.readInput();
			if(choice.equals("add")) {
				w.addCard();
			}
			else if(choice.equals("show")){
				w.showCard();
			}
			else if(choice.equals("remove")) {
				w.removeCard();
			}
			else if(choice.equals("use")) {
				w.useCard();
			}
			else if(choice.equals("close")) {
				break;
			}
		}
		
		
	}
	
	/**
	 * Read the user input from the console
	 * @return the user input as a String
	 */	
	public String readInput() {
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
	 * You can use this method to get current system time as a String
	 * @return a String for the system time in specific format.
	 */
	public String getCurrentTime() {
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd '-' HH:mm:ss z");
		Date date = new Date(System.currentTimeMillis());
		//System.out.println(formatter.format(date));
		return formatter.format(date);
	}
	
	/**
	 * @param cardNo, find card according to user input cardNo 
	 * @return no match: null; match: CreditCard object
	 */
	public Card getCardByNo(String cardNo) {
		Card result = null;
		for (Card c : cardList) {
			if(cardNo.equals(c.getCardNo())) {
				result = c;
			}
		}
		return result;
	}

}
