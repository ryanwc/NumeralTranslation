package app;

public class Query extends ParsedNote {

	// if have commodity, it's a 'many' question
	// if not, it's a 'much' question
	String commodity, intergalNum;
	int intergalNumLength;
	
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
		super(note);
		
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
