import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * @author adrianli375
 * 
 * AMM is a class which stores the variables and methods needed for implementing a stock 
 * automated machine maker (AMM)<br>
 * 
 * Line 338 of this file contains the file directory, set as "700_orders.csv" for submission
 */
public class AMM {
	
	ArrayList<Order> orders = new ArrayList<Order>();
	ArrayList<logMessage> transactionLog = new ArrayList<logMessage>();
	PriorityQueue<Order> bidQueue = new PriorityQueue<Order>(new OrderComparator());
	PriorityQueue<Order> askQueue = new PriorityQueue<Order>(new OrderComparator());
	
	/**
	 * tradeOrder is a method which takes in orders, processes them and updates in the 
	 * bid/ask queue
	 * @param isMarketOrder whether the order is a market order or not
	 * @param mode a string to indicate the current mode of trading, can be either "BUY" or "SELL"
	 * @param thisOrder the current processing order that is being prompted
	 * @param w the writer to write the transaction records to the log file
	 * @throws IOException
	 */
	public void tradeOrder(boolean isMarketOrder, String mode, Order thisOrder, 
			BufferedWriter w) throws IOException {
		//queue1: ask queue if buy order, bid queue if sell order, the other way round for queue2
		PriorityQueue<Order> queue1 = mode.equals("BUY") ? askQueue : bidQueue;
		PriorityQueue<Order> queue2 = mode.equals("BUY") ? bidQueue : askQueue;
		boolean executable = !queue1.isEmpty() && ((isMarketOrder ? true :  
				 (queue1.peek().price == thisOrder.price)) || (queue1.peek().price<0));
		if(executable) {
			int volDifference = thisOrder.volume - queue1.peek().volume;
			while (thisOrder.volume>0 && executable) {
				if(volDifference<0 && executable) { 
					//negative means order volume is less than first ask queue volume
					Order otherOrder = queue1.peek();
					otherOrder.volume -= thisOrder.volume;
					boolean otherOrderIsMarketOrder = otherOrder.price < 0;
					writeExecutedMessage(w, thisOrder, mode, thisOrder.orderID, 
							isMarketOrder ? otherOrder.price : thisOrder.price, thisOrder.volume);
					queue2.remove(thisOrder);
					writeMessage(w, thisOrder, "COMPLETED");
					writeExecutedMessage(w, otherOrder, mode.equals("BUY") ? "SELL" : "BUY", 
						otherOrder.orderID, otherOrderIsMarketOrder? thisOrder.price : otherOrder.price, 
						thisOrder.volume);
					break;
				}
				else if(volDifference == 0) {
					Order otherOrder = queue1.poll();
					boolean otherOrderIsMarketOrder = otherOrder.price < 0;
					writeExecutedMessage(w, thisOrder, mode, thisOrder.orderID, 
							isMarketOrder ? otherOrder.price : thisOrder.price, thisOrder.volume);
					queue2.remove(thisOrder);
					writeMessage(w, thisOrder, "COMPLETED");
					writeExecutedMessage(w, otherOrder, mode.equals("BUY") ? "SELL" : "BUY", 
							otherOrder.orderID, otherOrderIsMarketOrder? thisOrder.price : otherOrder.price, 
							otherOrder.volume);
					writeMessage(w, otherOrder, "COMPLETED");
					break;
				}
				else if(volDifference>0 && executable) {
					Order otherOrder = queue1.peek();
					boolean otherOrderIsMarketOrder = otherOrder.price < 0;
					thisOrder.volume -= otherOrder.volume;
					if(thisOrder.volume>=0) {
						otherOrder = queue1.poll();
						writeExecutedMessage(w, thisOrder, mode, thisOrder.orderID, 
								isMarketOrder ? otherOrder.price : thisOrder.price, otherOrder.volume);
					}
					if(!queue1.isEmpty()) volDifference = thisOrder.volume - queue1.peek().volume;
					if(queue1.isEmpty() && isMarketOrder) {
						queue2.remove(thisOrder);
						queue2.add(thisOrder); 
					}
					writeExecutedMessage(w, otherOrder, mode.equals("BUY") ? "SELL" : "BUY", 
							otherOrder.orderID, otherOrderIsMarketOrder? thisOrder.price : otherOrder.price, 
							otherOrder.volume);
					otherOrder.volume = 0;
					writeMessage(w, otherOrder, "COMPLETED");
					if (!queue1.isEmpty()) {
						otherOrder = queue1.peek();
						otherOrderIsMarketOrder = otherOrder.price < 0;
					}
					executable = !queue1.isEmpty() && (isMarketOrder || 
							(otherOrderIsMarketOrder && otherOrder.volume > 0) || 
							(!isMarketOrder) && (queue1.peek().price == thisOrder.price)
							&& thisOrder.volume - otherOrder.volume >= 0);
				}
			}
		}
	}
	
	/**
	 *  printQueue is a method used to print the bid queue and ask queue in a designated format
	 */
	public void printQueue() {
		System.out.println("==========Bid==========");
		PriorityQueue<Order> copiedBidQueue = new PriorityQueue<Order>(bidQueue);
		while(!copiedBidQueue.isEmpty()) {
			System.out.println(copiedBidQueue.poll().printOrdertoQueue());
		}
		if(bidQueue.isEmpty()) System.out.println("Null");
		System.out.println("==========Ask==========");
		PriorityQueue<Order> copiedAskQueue = new PriorityQueue<Order>(askQueue);
		while(!copiedAskQueue.isEmpty()) {
			System.out.println(copiedAskQueue.poll().printOrdertoQueue());
		}
		if(askQueue.isEmpty()) System.out.println("Null");
	}
	
	/**
	 * writeExecuted Message is a method used to write transaction records with Tx-code 
	 * "ORDER-EXECUTED" to the logfile
	 * @param writer the writer to write the transaction records to the log file
	 * @param o the current processing order object
	 * @param action the action of the order object, i.e. "BUY" or "SELL"
	 * @param oID the transaction ID of the order
	 * @param price the transaction price of the order
	 * @param volume the transaction volume of the order
	 * @throws IOException
	 */
	public void writeExecutedMessage(BufferedWriter writer, Order o, String action, 
			int oID, double price, int volume) throws IOException {
		int logTXID = transactionLog.size()+1;
		logMessage orderExecutedMessage = o.orderExecuted(action, oID, price, volume, logTXID);
		writer.write(orderExecutedMessage.message);
		transactionLog.add(orderExecutedMessage);
	}
	
	/**
	 * writeMessage is a method used to write transaction records to the logfile
	 * @param writer the writer to write the transaction records to the log file
	 * @param o the current processing order object
	 * @param action the action of the order object, i.e. "BUY" or "SELL"
	 * @throws IOException
	 */
	public void writeMessage(BufferedWriter writer, Order o, String action) throws IOException {
		int logTXID = transactionLog.size()+1;
		switch(action) {
		case "RECEIVED":
			logMessage orderReceivedMessage = o.orderReceived(logTXID);
			writer.write(orderReceivedMessage.message);
			transactionLog.add(orderReceivedMessage);
			break;
		case "REJECTED":
			logMessage orderRejectedMessage = o.orderRejected(logTXID);
			writer.write(orderRejectedMessage.message);
			transactionLog.add(orderRejectedMessage);
			break;
		case "UPDATED":
			logMessage orderUpdatedMessage = o.orderUpdated(o.details, logTXID);
			writer.write(orderUpdatedMessage.message);
			transactionLog.add(orderUpdatedMessage);
			break;
		case "DELETED":
			logMessage orderDeletedMessage = o.orderDeleted(o.details, logTXID);
			writer.write(orderDeletedMessage.message);
			transactionLog.add(orderDeletedMessage);
			break;
		case "COMPLETED":
			logMessage orderCompletedMessage = o.orderCompleted(logTXID);
			writer.write(orderCompletedMessage.message);
			transactionLog.add(orderCompletedMessage);
			break;
		case "SHOW":
			logMessage orderShowMessage = o.orderShow(o.details, logTXID);
			writer.write(orderShowMessage.message);
			transactionLog.add(orderShowMessage);
			break;
		case "SHOWQUEUE":
			logMessage orderShowQueueMessage = o.orderShowQueue(logTXID);
			writer.write(orderShowQueueMessage.message);
			transactionLog.add(orderShowQueueMessage);
			break;
		}
	}
	
	/**
	 * getBuySellStatus is a method used to obtain the status of an order ("BUY or SELL")
	 * @param details
	 * @return the action of the order ("BUY or SELL"), if the order type is not found 
	 * in the ArrayList storing the orders, it will return "not found"
	 */
	public String getBuySellStatus(String details) {
		String[] message = details.split(";");
		int ID = Integer.parseInt(message[0]);
		for(Order o : orders) {
			if(o.orderID == ID)  {
				return o.action;
			}
		}
		return "not found";
	}
	
	/**
	 * processBuySellDetails is a function to process the details of a BUY or SELL order
	 * @param o the current processing order
	 * @param action "BUY" or "SELL"
	 * @param details the transaction details associated with the order
	 */
	public void processBuySellDetails(Order o, String action, String details) {
		String[] orderDetails = details.split(";");
		if(action.equals("BUY") || action.equals("SELL")) {
			o.price = Double.parseDouble(orderDetails[0]);
			o.volume = Integer.parseInt(orderDetails[1]);
		}
	}
	
	/**
	 * processModifyDetails is a function to process the details of a MODIFY order
	 * @param details the transaction details associated with the updates of an order
	 * @param status "BUY" or "SELL"
	 * @return true if the order is being modified, false if not
	 */
	public boolean processModifyDetails(String details, String status) {
		boolean modified = false;
		Order oldOrder = null;
		Order newOrder = null;
		String[] orderDetails = details.split(";");
		int oID = Integer.parseInt(orderDetails[0]);
		for(Order o : status.equals("BUY") ? bidQueue : askQueue) {
			if (o.orderID == oID) {
				newOrder = new Order(o.receivedTime, o.orderID, o.action, 
						Double.parseDouble(orderDetails[1]) + ";" + 
						Integer.parseInt(orderDetails[2]));
				newOrder.price = Double.parseDouble(orderDetails[1]);
				newOrder.volume = Integer.parseInt(orderDetails[2]);
				oldOrder = o;
				modified = true;
			}
		}
		if(modified) {
			if (status.equals("BUY")) {
				bidQueue.add(newOrder);
				bidQueue.remove(oldOrder);
			}
			else if(status.equals("SELL")) {
				askQueue.add(newOrder);
				askQueue.remove(oldOrder);
			}
		}
		return modified;
	}
	
	/**
	 * processDeleteDetails is a function to process the details of a DELETE order
	 * @param details the order to be deleted
	 * @param status "BUY" or "SELL"
	 * @return true if the order is being deleted, false if not
	 */
	public boolean processDeleteDetails(String details, String status) {
		boolean deleted = false;
		int oID = Integer.parseInt(details);
		switch(status) {
		case "BUY":
			Order buyToBeRemoved = null;
			for(Order o : bidQueue) {
				if (o.orderID == oID) {
					buyToBeRemoved = o;
					deleted = true;
				}
			}
			bidQueue.remove(buyToBeRemoved);
			orders.remove(buyToBeRemoved);
		case "SELL":
			Order sellToBeRemoved = null;
			for(Order o : askQueue) {
				if (o.orderID == oID) {
					sellToBeRemoved = o;
					deleted = true;
				}
			}
			askQueue.remove(sellToBeRemoved);
			orders.remove(sellToBeRemoved);
		}
		return deleted;
	}
	
	/**
	 * printAllRecords is a method to print all the transaction records associated with an order
	 * @param ID the ID of the order
	 * @return true if all of the records of the order is shown, false if not
	 */
	public boolean printAllRecords(String ID) {
		boolean shown = false;
		for(logMessage lm : transactionLog) {
			if (lm.orderID.equals(ID)) {
				shown = true;
				System.out.print(lm.message);
			}
		}
		String status = getBuySellStatus(ID);
		if(status.equals("BUY")) {
			PriorityQueue<Order> copiedBidQueue = new PriorityQueue<Order>(bidQueue);
			int counter = 0;
			while(!copiedBidQueue.isEmpty()) {
				counter++;
				Order o = copiedBidQueue.poll();
				if(Integer.parseInt(ID) == o.orderID) {
					System.out.println("No." + counter + " in Bid Queue: " + o.printOrdertoQueue());
					break;
				}
			}
		}
		else if(status.equals("SELL")) {
			PriorityQueue<Order> copiedAskQueue = new PriorityQueue<Order>(askQueue);
			int counter = 0;
			while(!copiedAskQueue.isEmpty()) {
				counter++;
				Order o = copiedAskQueue.poll();
				if(Integer.parseInt(ID) == o.orderID) {
					System.out.println("No." + counter + " in Ask Queue: " + o.printOrdertoQueue());
					break;
				}
			}
		}
		return shown;
	}
	
	/**
	 * readLine is a function which controls the overall flow of the AMM program
	 * @throws IOException
	 */
	public void readLine() throws IOException {
		String line;
		BufferedReader reader = new BufferedReader(new FileReader("market_orders_100.csv"));
		BufferedWriter writer = new BufferedWriter(new FileWriter("AMM_transactions.log"));
		int counter = 1;
		reader.readLine();
		writer.write("Tx-ID,time-stamp,Tx-code,transaction-details\n");
		while((line = reader.readLine()) != null) {
			if (counter > 1) System.out.println();
			String[] row = line.split(",");
			Order o = new Order(getCurrentTime(), Integer.parseInt(row[0]), row[1], row[2]);
			orders.add(o);
			writeMessage(writer, o, "RECEIVED");
			switch (row[1]) {
			case "BUY": 
				processBuySellDetails(o, row[1], row[2]);
				bidQueue.add(o);
				tradeOrder(o.price<0, row[1], o, writer);
				break;
			case "SELL":
				processBuySellDetails(o, row[1], row[2]);
				askQueue.add(o);
				tradeOrder(o.price<0, row[1], o, writer);
				break;
			case "MODIFY":
				String modifyStatus = getBuySellStatus(o.details);
				boolean modified = false, foundOrder = false;
				int index = Integer.parseInt(row[2].split(";")[0]); 
				if(!modifyStatus.equals("not found")) {
					PriorityQueue<Order> queue = modifyStatus.equals("BUY") ? bidQueue : askQueue; 
					for (Order ord : queue) {
						if((modifyStatus.equals("BUY") || modifyStatus.equals("SELL")) 
								&& queue.contains(ord)) {
							foundOrder = true;
							break;
						}
					}
				}
				if(foundOrder) modified = processModifyDetails(row[2], modifyStatus);
				writeMessage(writer, o, "UPDATED");
				if(!modified) {
					writeMessage(writer, o, "REJECTED");
				}
				else {
					writeMessage(writer, o, "COMPLETED");
					//get details of the modified order
					Order updatedOrder = null;
					PriorityQueue<Order> queue = modifyStatus.equals("BUY") ? bidQueue : askQueue; 
					for(Order ord : queue) if(ord.orderID == index) updatedOrder = ord;
					//after modified, if the order is first in queue and fits the queue, then need buy/sell
					tradeOrder(updatedOrder.price<0, modifyStatus, updatedOrder, writer);
				}
				break;
			case "DELETE":
				String deleteStatus = getBuySellStatus(o.details);
				boolean deleted = false;
				if(deleteStatus.equals("BUY") || deleteStatus.equals("SELL")) {
					deleted = processDeleteDetails(row[2], deleteStatus);
				}
				writeMessage(writer, o, "DELETED");
				if(!deleted) {
					writeMessage(writer, o, "REJECTED");
				}
				else writeMessage(writer, o, "COMPLETED");
				break;
			case "SHOWQUEUE":
				writeMessage(writer, o, "SHOWQUEUE");
				printQueue();
				writeMessage(writer, o, "COMPLETED");
				break;
			case "SHOWSTATUS":
				boolean showed = printAllRecords(row[2]);
				writeMessage(writer, o, "SHOW");
				if(!showed) System.out.println("Order " + row[2] + " not found.");
				writeMessage(writer, o, "COMPLETED");
				break;
			}
			counter++;
		}
		reader.close();
		writer.close();
	}
	
	/**
	 * getCurrentTime is a method to obtain the current time recorded
	 * @return the formatted string of the current time
	 */
	public String getCurrentTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd '-' HH:mm:ss z");
		Date date = new Date(System.currentTimeMillis());
		return formatter.format(date);
	}
	
	public static void main(String[] args) throws IOException {
		AMM a = new AMM();
		a.readLine();
	}

}
