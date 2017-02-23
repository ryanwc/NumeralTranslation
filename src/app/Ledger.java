package app;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Bookkeep information about prices of various commodities.
 * 
 * Prices are reported in credits and/or intergalactic currency.
 * 
 * @author ryanwilliamconnor
 *
 */
public class Ledger {

	private Map<String, PricePair> priceBook;
	Translator translator;
	
	public Ledger(Translator translator) {
		this.priceBook = new HashMap<String, PricePair>();
		this.translator = translator;
	}
	
	public Ledger(Set<String> entries) {
		this.priceBook = new HashMap<String, PricePair>();
		for (String entry : entries)
			createLedgerEntry(entry);
	}
	
	public Map<String, PricePair> getPriceBook() {
		return priceBook;
	}
	
	public void createLedgerEntry(String commodity) {
		priceBook.put(commodity, new PricePair(null, null));
	}
	
	public void setPriceBook(Map<String, PricePair> priceBook) {
		this.priceBook = priceBook;
	}
	
	public void setCreditPrice(String commodity, Integer price) {
		if (!priceBook.containsKey(commodity))
			createLedgerEntry(commodity);
		PricePair pair = priceBook.get(commodity);
		pair.setCreditPrice(price);
		priceBook.put(commodity, pair);
	}
	
	public void setIntergalPrice(String commodity, String price) {
		if (!priceBook.containsKey(commodity))
			createLedgerEntry(commodity);
		PricePair pair = priceBook.get(commodity);
		pair.setIntergalPrice(price);
		priceBook.put(commodity, pair);
	}
	
	public Integer getCreditPrice(String commodity) {
		return priceBook.get(commodity).getCreditPrice();
	}
	
	public String getIntergalPrice(String commodity) {
		return priceBook.get(commodity).getIntergalPrice();
	}
	
	/**
	 * Record a commodity price in the ledger.
	 * 
	 * Assumes the commodity declaration is well-formed.
	 * 
	 * @param cDec is a CommodityDecl with the commodity and price
	 * information to record in the ledger.
	 * @param overwrite is a boolean: pass true to overwrite any existing
	 * prices, false to overwrite only null prices
	 */
	public void recordCommDecl(CommodityDecl cDec, boolean overwrite) {
		
		String commodity = cDec.getCommodity();
		int arabicNum = cDec.getArabicNum();
		String intergalNum = cDec.getIntergalNum();
		
		PricePair prices = priceBook.containsKey(commodity) ? 
			priceBook.get(commodity) : new PricePair(arabicNum, intergalNum);
			
		if (overwrite) {
			priceBook.put(commodity, prices);
		}
		else {
			// if not overwrite, only add if null
			if (prices.getCreditPrice() != null)
				prices.setCreditPrice(arabicNum);
			if (prices.getIntergalPrice() != null)
				prices.setIntergalPrice(intergalNum);
			priceBook.put(commodity, prices);
		}
	}
	
	/**
	 * A tuple of (int:credit price, String:intergalactice price). 
	 */
	class PricePair {
		
		private Integer creditPrice; // Integer so can be null
		private String intergalPrice;
		
		public PricePair(Integer creditPrice, String intergalPrice) {
			this.creditPrice = creditPrice;
			this.intergalPrice = intergalPrice;
		}
		
		public Integer getCreditPrice() {
			return creditPrice;
		}
		
		public void setCreditPrice(Integer price) {
			this.creditPrice = price;			
		}
		
		public String getIntergalPrice() {
			return intergalPrice;
		}
		
		public void setIntergalPrice(String intergalPrice) {
			this.intergalPrice = intergalPrice;			
		}
		
		public String toString() {
			return "(" + intergalPrice;
		}
	}
}
