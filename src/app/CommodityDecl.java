package app;

/**
 * Hold all relevant information about a commodity declaration from notes
 * about intergalactic commodity markets.
 * 
 * A well-formed commodity declaration consists of the following:
 * 1) an intergal numeral, 2) a commodity, 3) the word 'is',
 * 4) an arabic numeral, and 5) the word 'Credits'.
 * 
 * @author ryanwilliamconnor
 */
public class CommodityDecl extends Declaration {

	private String intergalNum;
	private int arabicNum;
	private String commodity;
	
	/**
	 * Convert an Unknown note into a new CommodityDecl.
	 * Keeps only the information is relevant to a commodity declaration.
	 * 
	 * A well-formed commodity declaration consists of the following:
	 * 1) an intergal numeral, 2) a commodity, 3) the word 'is',
	 * 4) an arabic numeral, and 5) the word 'Credits'.
	 * 
	 * WARNING: Assumes the UnknownNote actually conforms to commodity
	 * declaration specs. 
	 * 
	 * @param note is the UnknownNote to convert to a CommodityDecl
	 */
	public CommodityDecl(UnknownNote note) {
		super(note);
		
		String[] components = super.getComponents();
		int iStart = note.getStart1IntergalClust();
		int iEnd = note.getEnd1IntergalClust();
		String str = "";
		int i;
		for (i = iStart; i < iEnd; i++) 
			str += components[i] + " ";
		str += components[i];
		this.intergalNum = str;
		
		this.arabicNum = Integer.parseInt(components[note.getArabicPos()]);
		this.commodity = components[note.getComm1Pos()];
	}
	
	@Override
	public String toString() {
		
		String str = super.toString();
		str += "Arabic numeral: " + arabicNum; 
		str += "Intergal numeral: " + intergalNum;
		str += "\n";
		str += "Commodity: " + commodity;
		str += '\n';
			
		return str;
	}

	public String getIntergalNum() {
		return intergalNum;
	}

	public void setIntergalNum(String intergalNum) {
		this.intergalNum = intergalNum;
	}

	public int getArabicNum() {
		return arabicNum;
	}

	public void setArabicNum(int arabicNum) {
		this.arabicNum = arabicNum;
	}

	public String getCommodity() {
		return commodity;
	}

	public void setCommodity(String commodity) {
		this.commodity = commodity;
	}
}
