package utility;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import notes.CommodityDecl;

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
	
	/**
	 * Create a new Ledger.
	 * 
	 * @param translator is a Translator: the store of translation
	 * knowledge and services to associate with this Ledger
	 */
	public Ledger(Translator translator) {
		this.priceBook = new HashMap<String, PricePair>();
		this.translator = translator;
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
	
	public void setCreditPrice(String commodity, BigDecimal price) {
		if (!priceBook.containsKey(commodity)) createLedgerEntry(commodity);
		PricePair pair = priceBook.get(commodity);
		pair.setCreditPrice(price);
		priceBook.put(commodity, pair);
	}
	
	public void setIntergalPrice(String commodity, String price) {
		if (!priceBook.containsKey(commodity)) createLedgerEntry(commodity);
		PricePair pair = priceBook.get(commodity);
		pair.setIntergalPrice(price);
		priceBook.put(commodity, pair);
	}
	
	public BigDecimal getCreditPrice(String commodity) {
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
	 * A well-formed commodity declaration has the following components:
	 * 1) an intergal numeral, 2) a commodity, 3) the word 'is',
	 * 4) an arabic numeral, and 5) the word 'Credits'.
	 * 
	 * The ledger will speak with the translator and perform the 
	 * appropriate calculations to record the value of "1 unit" of the
	 * commodity in the price book in both intergalactic numerals and
	 * Credits (represented by arabic numerals).
	 * 
	 * Note, the valid arabic range of intergal numerals is 1-3999, if
	 * intergal numerals follow to strict usage of Roman numeral rules.
	 * If the unit price falls outside this range, the ledger makes
	 * an appropriate note instead of recording an intergal price.
	 * 
	 * @param cDec is a CommodityDecl with the commodity and price
	 * information to record in the ledger.
	 * @throws IllegalArgumentException if cDec is null
	 * @param overwrite is a boolean: pass true to overwrite any existing
	 * prices, false to overwrite only null prices
	 */
	public void recordCommDecl(CommodityDecl cDec, boolean overwrite) {
		
		if (cDec == null)
			throw new IllegalArgumentException("cDec cannot be null");
		
		String commodity = cDec.getCommodity();
		int aPrice = cDec.getArabicNum();
		String iAmnt = cDec.getIntergalNum();
		int aAmnt = translator.intergalNumToArabic(iAmnt);
		BigDecimal aUnitPrice = new BigDecimal((double)aPrice / aAmnt);
		
		String iUnitPrice;
		try {
			iUnitPrice = translator.arabicNumToIntergal(aUnitPrice.intValue());	
		}
		catch (IllegalArgumentException e) {
			iUnitPrice = "unit price outside valid range of intergal numerals";
		}
		
		PricePair prices = priceBook.containsKey(commodity) ? 
			priceBook.get(commodity) : new PricePair(aUnitPrice, iUnitPrice);
			
		if (overwrite) {
			priceBook.put(commodity, prices);
		}
		else {
			// if not overwrite, only add if null
			if (prices.getCreditPrice() != null)
				prices.setCreditPrice(aUnitPrice);
			if (prices.getIntergalPrice() != null)
				prices.setIntergalPrice(iUnitPrice);
			priceBook.put(commodity, prices);
		}
	}
	
	/**
	 * A tuple of (BigDecimal:credit price, String:intergalactice price). 
	 */
	class PricePair {
		
		private BigDecimal creditPrice; // money: minimize precision lost :)
		private String intergalPrice;
		
		/**
		 * Create a new price pair.
		 * 
		 * @param creditPrice is a BigDecimal: the credit price
		 * @param intergalPrice is a string: the intergal numeral price
		 */
		public PricePair(BigDecimal creditPrice, String intergalPrice) {
			this.creditPrice = creditPrice;
			this.intergalPrice = intergalPrice;
		}
		
		public BigDecimal getCreditPrice() {
			return creditPrice;
		}
		
		public void setCreditPrice(BigDecimal price) {
			this.creditPrice = price;			
		}
		
		public String getIntergalPrice() {
			return intergalPrice;
		}
		
		public void setIntergalPrice(String intergalPrice) {
			this.intergalPrice = intergalPrice;			
		}
		
		public String toString() {
			return "(Intergal: " + intergalPrice + ", Credits: " + creditPrice + ")";
		}
	}
}
