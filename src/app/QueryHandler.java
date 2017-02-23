package app;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;

/**
 * Handle queries regarding unit conversions and prices of various commodities
 * in the intergalactic market.
 *  
 * @author ryanwilliamconnor
 *
 */
public class QueryHandler {
	
	Translator translator;
	Ledger ledger;
	NumberFormat priceOutFormat;
	
	public QueryHandler(Translator translator, Ledger ledger) {
		this.translator = translator;
		this.ledger = ledger;
		this.priceOutFormat = new DecimalFormat("#.##");
	}

	/**
	 * Answer the given query.
	 * 
	 * @param q
	 */
	public void answer(Query q) {
		
		if (!isWellFormed(q)) {
			System.out.println("I don't know what '" + q.getNote() + "' "
					+ "is trying to ask.");
			return;
		}
		
		if (q.getComponents()[1].equals("much")) {
			handleMuchQ(q);
		}
		else {
			handleManyQ(q);
		}
	}
	
	/**
	 * Handle a 'much' query, which has this form:
	 * 'how much is [intergalNum]?'
	 * 
	 * @param q is a Query: the 'much' query to handle
	 */
	private void handleMuchQ(Query q) {
		String answer = q.intergalNum + " is ";
		answer += translator.intergalNumToArabic(q.intergalNum);
		System.out.println(answer);
	}
	
	/**
	 * Handle a 'many' query, which has this form:
	 * 'how many Credits is [intergalNum] [commodity]?'
	 * 
	 * @param q is a Query: the 'many' query to handle
	 */
	private void handleManyQ(Query q) {
		// format to show decimal only if necessary
		int aCommAmnt = translator.intergalNumToArabic(q.intergalNum);
		BigDecimal aCommPrice = ledger.getCreditPrice(q.commodity).
				multiply(new BigDecimal(aCommAmnt));
		String answer = q.intergalNum + " " + q.commodity + " is ";
		answer += priceOutFormat.format(aCommPrice) + " Credits";
		System.out.println(answer);
	}
	
	/**
	 * Determine whether a given query is well-formed.
	 * 
	 * @param q
	 */
	private boolean isWellFormed(Query q) {
		
		//System.out.println("checking " + q.getNote());
		String[] components = q.getComponents();
		
		if (components.length < 4) return false;
		//System.out.println("1");
		if (!components[0].equals("how")) return false;
		//System.out.println("2");		
		String qWord = components[1];
		
		if (!qWord.equals("much") && !qWord.equals("many")) return false;
		//System.out.println("3");		
		// will need to check if a cluster is intergal num
		Map<String, Integer> intergalNumRank = translator.getIntergalNumRank();
		
		if (qWord.equals("much")) {
			// it's a 'much' question, with form 'how much is [intergalNum]?'
			if (!components[2].equals("is")) return false;
			//System.out.println("4");
			for (int i = 3; i < 3+q.getIntergalNumLength(); i++) {
				//System.out.println("checking " + components[i]);
				if (!intergalNumRank.containsKey(components[i]))
					return false;
			}
			//System.out.println("5");
		}
		else {
			// it's a 'many' question, 
			// with form 'how many Credits is [intergalNum] [commodity]?'
			if (!components[2].equals("Credits")) return false;
			//System.out.println("6");
			if (!components[3].equals("is")) return false;
			//System.out.println("7");
			
			for (int i = 4; i < 4+q.getIntergalNumLength(); i++) {
				if (!intergalNumRank.containsKey(components[i]))
					return false;
			}
			//System.out.println("8");
			if (!ledger.getPriceBook().containsKey(components[components.length-2]))
				return false;
			//System.out.println("9");
		}
		
		return true;
	}
}
