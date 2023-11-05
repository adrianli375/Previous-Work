import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author adrianli375
 * The stock class has two functions, to print the average close price and the trading volume
 * It also has an ArrayList storing the information of the stock data
 */
public class Stock implements Serializable {
	
	private static final long serialVersionUID = 1L;
	ArrayList<DailyStockData> listOfData = new ArrayList<DailyStockData>();
	
	/**
	 * A function which prints the average close price of the stock
	 */
	public void printAverageClosePrice() {
		float avg = 0;
		for(int i = 0; i < listOfData.size(); i++) {
			avg += listOfData.get(i).close;
		}
		avg = avg / listOfData.size();
		System.out.println("Stock name:" + listOfData.get(0).name + ", average close price: " + avg);
	}
	
	/**
	 * A function which prints the date and trading volume of the top 3 days 
	 * with the highest trading volume
	 */
	public void printTradingVolume() {
		ArrayList<DailyStockData> listOfData2 = new ArrayList<>(listOfData);
		//sort the list of DailyStockData by descending order of trading volume
		Collections.sort(listOfData2);
		for(int i=0; i<3; i++) {
			System.out.println("Stock name:" + listOfData2.get(i).name + 
					", date:" + listOfData2.get(i).date + 
					", volume:" + listOfData2.get(i).volume);
		}
	}
	
}
