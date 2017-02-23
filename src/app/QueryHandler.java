package app;

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
	
	public QueryHandler(Translator translator, Ledger ledger) {
		this.translator = translator;
		this.ledger = ledger;
	}
	
	private boolean currIsMuch;
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
	}
	
	/**
	 * Determine whether a given query is well-formed.
	 * 
	 * @param q
	 */
	private boolean isWellFormed(Query q) {
		
		String[] components = q.getComponents();
		
		if (components.length < 4) return false;
		if (!components[0].equals("how")) return false;
		
		String qWord = components[1];
		
		if (qWord.equals("much")) {
			currIsMuch = true;
		}
		else if (!qWord.equals("many")) {
			return false;
		}
		
		// will need to check if a cluster is intergal num
		Map<String, Integer> intergalNumRank = translator.getIntergalNumRank();
		
		if (currIsMuch) {
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
			
			if (!ledger.getPriceBook().containsKey(components[components.length-1]))
				return false;
		}
		
		return true;
	}
}
