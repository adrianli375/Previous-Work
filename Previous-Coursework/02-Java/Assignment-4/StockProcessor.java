import java.io.*;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

/**
 * @author adrianli375
 * StockProcessor is a class which stores the main function and has
 * other functions to process the stock object data
 */
public class StockProcessor extends Application {
	
	/**
	 * A static ArrayList storing all the stock information
	 */
	static ArrayList<Stock> allStockData = new ArrayList<Stock>();
	
	/**
	 * A function used to read the .csv files and returns a stock object
	 * @param stockName The name of the stock, e.g. "Apple"
	 * @param path The relative path of the .csv file to be read, e.g. "Apple.csv"
	 * @return A stock object which stores the information of the stock
	 * @throws IOException
	 */
	public Stock readCSVFile(String stockName, String path) throws IOException {
		BufferedReader reader = null;
		String line = "";
		Stock stk = new Stock();
		
		try {
			reader = new BufferedReader(new FileReader(path));
			reader.readLine();
			while((line = reader.readLine()) != null) {
				String[] row = line.split(",");
				DailyStockData data = new DailyStockData(stockName, row[0], 
						Double.parseDouble(row[1]), Double.parseDouble(row[2]), 
						Double.parseDouble(row[3]), Double.parseDouble(row[4]), 
						Integer.parseInt(row[5]));
				stk.listOfData.add(data);
			}
			reader.close();
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return stk;
	}
	
	/**
	 * A function which serializes the stock object and is saved as .ser format
	 * @param stk A stock object
	 * @throws IOException
	 */
	public void serialize(Stock stk) throws IOException {
		System.out.println("--------------------Serialization----------------");
		for(int i=0; i<3; i++) {
			System.out.println("Stock name:" + stk.listOfData.get(0).name +
					", open:" + stk.listOfData.get(i).open + ", \nhigh:" + stk.listOfData.get(i).high + 
					", low:" + stk.listOfData.get(i).low + ", \nclose:" + stk.listOfData.get(i).close + 
					", volume:" + stk.listOfData.get(i).volume);
		}
		System.out.println("--------------------Save " + stk.listOfData.get(0).name + 
				" Stock----------------");
		FileOutputStream fos = new FileOutputStream(stk.listOfData.get(0).name + "Stock.ser");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(stk);
		oos.close();
	}
	
	/**
	 * Prints out the basic statistics of a stock, which is the volume of the top 3 
	 * trading days and the average close price for each stock
	 */
	public void printStat() {
		System.out.println("--------------------Top 3 Volume----------------");
		for(Stock stk : allStockData) {
			stk.printTradingVolume();
		}
		System.out.println("-------------------Average Close Price----------------");
		for(Stock stk : allStockData) {
			stk.printAverageClosePrice();
		}
	}
	
	/**
	 * A function that reads in a serialized object and recovers the original stock object
	 * @param name The name of the stock, e.g. Apple
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void readSerializedFile(String name) throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(name + "Stock.ser");
		ObjectInputStream ois = new ObjectInputStream(fis);
		Stock stk = (Stock) ois.readObject();
		System.out.println("--------------------Restore " + name + " Stock----------------");
		for(int i=0; i<3; i++) {
			System.out.println("Stock name:" + stk.listOfData.get(0).name +
					", open:" + stk.listOfData.get(i).open + ", \nhigh:" + stk.listOfData.get(i).high + 
					", low:" + stk.listOfData.get(i).low + ", \nclose:" + stk.listOfData.get(i).close + 
					", volume:" + stk.listOfData.get(i).volume);
		}
		ois.close();
	}
	
	/**
	 * A function which visualizes the stock data based on the processed stock objects
	 */
	@Override
	public void start(Stage stage) throws Exception {
		final CategoryAxis xAxis = new CategoryAxis();
	    final NumberAxis yAxis = new NumberAxis();
	    xAxis.setLabel("Date");
	    final LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);
	    lineChart.setTitle("Stock Line Chart");

	    XYChart.Series<String, Number> seriesApple = new XYChart.Series<String, Number>();
	    seriesApple.setName("Apple");
	    for(int i=0; i<allStockData.get(0).listOfData.size(); i+=10) {
	    	seriesApple.getData().add(new XYChart.Data<String, Number>
	    	(allStockData.get(0).listOfData.get(i).date, allStockData.get(0).listOfData.get(i).close));
	    }
	    
	    XYChart.Series<String, Number> seriesHSBC = new XYChart.Series<String, Number>();
	    seriesHSBC.setName("HSBC");
	    for(int i=0; i<allStockData.get(1).listOfData.size(); i+=10) {
	    	seriesHSBC.getData().add(new XYChart.Data<String, Number>
	    	(allStockData.get(1).listOfData.get(i).date, allStockData.get(1).listOfData.get(i).close));
	    }
	    
	    XYChart.Series<String, Number> seriesNike = new XYChart.Series<String, Number>();
	    seriesNike.setName("Nike");
	    for(int i=0; i<allStockData.get(2).listOfData.size(); i+=10) {
	    	seriesNike.getData().add(new XYChart.Data<String, Number>
	    	(allStockData.get(2).listOfData.get(i).date, allStockData.get(2).listOfData.get(i).close));
	    }
	    
	    XYChart.Series<String, Number> seriesOracle = new XYChart.Series<String, Number>();
	    seriesOracle.setName("Oracle");
	    for(int i=0; i<allStockData.get(3).listOfData.size(); i+=10) {
	    	seriesOracle.getData().add(new XYChart.Data<String, Number>
	    	(allStockData.get(3).listOfData.get(i).date, allStockData.get(3).listOfData.get(i).close));
	    }
	    
	    Scene scene = new Scene(lineChart, 800, 600);
	    lineChart.getData().addAll(seriesApple, seriesHSBC, seriesNike, seriesOracle);

	    stage.setScene(scene);
	    stage.show();
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		StockProcessor s = new StockProcessor();
		Stock appleStockData = s.readCSVFile("Apple", "Apple.csv");
		Stock hsbcStockData = s.readCSVFile("HSBC", "HSBC.csv");
		Stock nikeStockData = s.readCSVFile("Nike", "Nike.csv");
		Stock oracleStockData = s.readCSVFile("Oracle", "Oracle.csv");
		allStockData.add(appleStockData);
		allStockData.add(hsbcStockData);
		allStockData.add(nikeStockData);
		allStockData.add(oracleStockData);
		s.printStat();
		s.serialize(appleStockData);
		s.readSerializedFile("Apple");
		Application.launch(args);
	}

}
