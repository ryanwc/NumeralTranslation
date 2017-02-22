package app;

import java.util.HashMap;
import java.util.Map;

/**
 * Bookkeep information about prices of various commodities.
 * 
 * Prices are reported in dollars and/or intergalactic currency.
 * 
 * @author ryanwilliamconnor
 *
 */
public class Ledger {

	private Map<String, PricePair> priceBook;
	
	public Ledger() {
		this.priceBook = new HashMap<String, PricePair>();
	}
	
	public Map<String, PricePair> getPriceBook() {
		return priceBook;
	}
	
	public void setPriceBook(Map<String, PricePair> priceBook) {
		this.priceBook = priceBook;
	}
	
	public void setDollarPrice(String commodity, int price) {
		PricePair pair = priceBook.get(commodity);
		pair.setDollarPrice(price);
		priceBook.put(commodity, pair);
	}
	
	public void setIntergalPrice(String commodity, String price) {
		PricePair pair = priceBook.get(commodity);
		pair.setIntergalPrice(price);
		priceBook.put(commodity, pair);
	}
	
	public int getDollarPrice(String commodity) {
		return priceBook.get(commodity).getDollarPrice();
	}
	
	public String getIntergalPrice(String commodity) {
		return priceBook.get(commodity).getIntergalPrice();
	}
	
	/**
	 * A tuple of (int:dollar price, String:intergalactice price). 
	 */
	class PricePair {
		
		private Integer dollarPrice; // Integer so can be null
		private String intergalPrice;
		
		public PricePair(int dollarPrice, String intergalPrice) {
			this.dollarPrice = dollarPrice;
			this.intergalPrice = intergalPrice;
		}
		
		public Integer getDollarPrice() {
			return dollarPrice;
		}
		
		public void setDollarPrice(int price) {
			this.dollarPrice = price;			
		}
		
		public String getIntergalPrice() {
			return intergalPrice;
		}
		
		public void setIntergalPrice(String intergalPrice) {
			this.intergalPrice = intergalPrice;			
		}
	}
}
