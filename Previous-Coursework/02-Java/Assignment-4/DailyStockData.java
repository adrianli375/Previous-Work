import java.io.Serializable;

/**
 * @author adrianli375
 * The DailyStockData is an object, which stores the basic information of the stock (name, date, open, 
 * high, low, close and volume), and has a constructor and an overriding method
 */
public class DailyStockData implements Comparable<DailyStockData>, Serializable {

	private static final long serialVersionUID = 1L;
	String name, date;
	double open, high, low, close;
	int volume;
	
	/**
	 * Constructor of a DailyStockData object
	 * @param name The name of the stock
	 * @param date The trading day of the stock
	 * @param open The opening price of the trading day of the stock
	 * @param high The highest price of the trading day of the stock
	 * @param low The lowest price of the trading day of the stock
	 * @param close The adjusted closing price of the trading day of the stock
	 * @param volume The trading volume of the stock
	 */
	DailyStockData(String name, String date, double open, double high, double low, double close, 
			int volume) {
		this.name = name;
		this.date = date;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
	}

	/**
	 * An overriding method which compares the stock object by the trading volume
	 */
	@Override
	public int compareTo(DailyStockData data) {
		return data.volume - this.volume;
	}
	
}
