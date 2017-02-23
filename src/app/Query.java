package app;

/**
 * 
 * Hold all relevant information about a query from notes about 
 * intergalactic commodity markets.
 * 
 * Queries ask something about the intergalactic commodity markets.
 * 
 * A query has one of the following forms:
 * 1) 'how much is [intergalactic numeral]?'
 * 2) 'how many Credits is [intergalactic numeral] [commodity]?'
 * 
 * @author ryanwilliamconnor
 */
public class Query extends ParsedNote {

	// if have commodity, it's a 'many' question
	// if not, it's a 'much' question
	private String commodity, intergalNum;
	private int intergalNumLength;
	
	/**
	 * Create a new Query from an UnknownNote.
	 * 
	 * Keeps only the information is relevant to a query.
	 * 
	 * WARNING: Assumes the UnknownNote actually conforms to query specs.
	 * 
	 * @param note is the UnknownNote to convert to a Query
	 */
	public Query(UnknownNote note) {
		super(note.getNote(), note.getComponents());
		
		String[] components = note.getComponents();
		
		if (note.getCountComm() > 0) {
			this.commodity = components[note.getComm1Pos()];
		}
		else {
			this.commodity = null;
		}
		
		int iStart = note.getStart1IntergalClust();
		int iEnd = note.getEnd1IntergalClust();
		int i;
		String str = "";
		int len = 1;
		for (i = iStart; i < iEnd; i++) {
			str += components[i] + " ";
			len++;
		}
		str += components[i];
		this.intergalNum = str;
		this.intergalNumLength = len; 
	}
	
	@Override
	public String toString() {
		// note commodity will print null if it's a 'much' question
		String str = super.toString();
		str += "Commodity: " + commodity;
		str += "\n";
		str += "Intergalactic numeral: " + intergalNum;
		str += "\n";
		
		return str;
	}

	public String getCommodity() {
		return commodity;
	}

	public void setCommodity(String commodity) {
		this.commodity = commodity;
	}

	public String getIntergalNum() {
		return intergalNum;
	}

	public void setIntergalNum(String intergalNum) {
		this.intergalNum = intergalNum;
	}

	public int getIntergalNumLength() {
		return intergalNumLength;
	}

	public void setIntergalNumLength(int intergalNumLength) {
		this.intergalNumLength = intergalNumLength;
	}
}
