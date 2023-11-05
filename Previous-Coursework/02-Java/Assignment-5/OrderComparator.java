import java.util.Comparator;

/**
 * @author adrianli375
 * 
 * OrderComparator is an object storing the method of comparing two order objects
 */
public class OrderComparator implements Comparator<Order> {

	/**
	 * compares two order objects and ranks them according to the priority in the bid and ask queue<br>
	 * for the bid queue, market order comes first, and others are arranged 
	 * in descending order in terms of price<br>
	 * for the ask queue, market order comes first, and others are arranged 
	 * in ascending order in terms of price <br>
	 * if two orders of the same type ("BUY" or "SELL") and have the same price, they will be arranged 
	 * in ascending order in terms of order ID
	 */
	@Override
	public int compare(Order o1, Order o2) {
		switch(o1.action) {
		case "BUY":
			//market price comes first
			if(o1.price<0 || o2.price<0) return -1;
			//then descending order in terms of price
			else if (o1.price < o2.price) return 1;
			else if (o1.price > o2.price) return -1;
			//smaller order number comes first (bid ask queues are FIFO)
			else return o1.orderID - o2.orderID;
		case "SELL":
			//natural ascending order in terms of price
			if(o1.price < o2.price) return -1;
			else if (o1.price > o2.price) return 1;
			//smaller order number comes first (bid ask queues are FIFO)
			else return o1.orderID - o2.orderID;
		}
		return 0;
	}
	
}
