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
	
	/**
	 * Create a new QueryHandler.
	 * 
	 * @param translator is a Translator: the store of knowledge and translation
	 * services which the QueryHandler will draw upon to answer queries
	 * @param ledger is a Ledger: the store of commodity prices the QueryHandler
	 * will use to answer queries.
	 */
	public QueryHandler(Translator translator, Ledger ledger) {
		this.translator = translator;
		this.ledger = ledger;
		this.priceOutFormat = new DecimalFormat("#.##");
	}

	/**
	 * Answer the given query.
	 * 
	 * Assumes the query actually attempts to be a query (i.e., has a '?'
	 * at the end).
	 * 
	 * Prints response (which could be an answer or failure message)
	 * to standard output.
	 * 
	 * @param q is a Query: the query to answer.
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
	 * Helper method for handling a query.
	 * Handle a 'much' query, which has this form:
	 * 'how much is [intergalNum]?'
	 * 
	 * Prints response to standard output.
	 * 
	 * @param q is a Query: the 'much' query to handle
	 */
	private void handleMuchQ(Query q) {
		try {
			String answer = q.getIntergalNum() + " is ";
			answer += translator.intergalNumToArabic(q.getIntergalNum());
			System.out.println(answer);	
		} catch (Exception e) {
			System.out.println("I don't know how to answer '" 
					+ q.getNote() + "'");
		}
	}
	
	/**
	 * Helper method for handling a query.
	 * Handle a 'many' query, which has this form:
	 * 'how many Credits is [intergalNum] [commodity]?'
	 * 
	 * Prints response to standard output.
	 * 
	 * @param q is a Query: the 'many' query to handle
	 */
	private void handleManyQ(Query q) {
		// format to show decimal only if necessary
		try {
			int aCommAmnt = translator.intergalNumToArabic(q.getIntergalNum());
			BigDecimal aCommPrice = ledger.getCreditPrice(q.getCommodity()).
					multiply(new BigDecimal(aCommAmnt));
			String answer = q.getIntergalNum() + " " + q.getCommodity() 
				+ " is ";
			answer += priceOutFormat.format(aCommPrice) + " Credits";
			System.out.println(answer);
		} catch (Exception e) {
			System.out.println("I don't know how to answer '" 
					+ q.getNote() + "'");
		}
	}
	
	/**
	 * Determine whether a given query is well-formed.
	 * 
	 * A query has one of the following forms:
	 * 1) 'how much is [intergalactic numeral]?'
	 * 2) 'how many Credits is [intergalactic numeral] [commodity]?'
	 * 
	 * Prints response to standard output if ill-formed.
	 * 
	 * @param q is a Query: the query to check.
	 * @return a boolean: true if the query is well-formed, false otherwise
	 */
	private boolean isWellFormed(Query q) {
		
		String[] components = q.getComponents();
		
		if (components.length < 4) return false;
		if (!components[0].equals("how")) return false;		
		String qWord = components[1];
		
		if (!qWord.equals("much") && !qWord.equals("many")) return false;
	
		// will need to check if a cluster is intergal num
		Map<String, Integer> intergalNumRank = translator.getIntergalNumRank();
		
		if (qWord.equals("much")) {
			// it's a 'much' question, with form 'how much is [intergalNum]?'
			if (!components[2].equals("is")) return false;

			for (int i = 3; i < 3+q.getIntergalNumLength(); i++) {
				if (!intergalNumRank.containsKey(components[i]))
					return false;
			}
		}
		else {
			// it's a 'many' question, 
			// with form 'how many Credits is [intergalNum] [commodity]?'
			if (!components[2].equals("Credits")) return false;

			if (!components[3].equals("is")) return false;
			
			for (int i = 4; i < 4+q.getIntergalNumLength(); i++) {
				if (!intergalNumRank.containsKey(components[i]))
					return false;
			}
			
			if (!ledger.getPriceBook().containsKey(components[components.length-2]))
				return false;
		}
		
		return true;
	}
}
