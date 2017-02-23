package app;

public class CommodityDecl extends Declaration {

	private String intergalNum;
	private int arabicNum;
	private String commodity;
	
	/**
	 * Create a new CommodityDecl from an UnknownNote.
	 * 
	 * Keeps only the information is relevant to a commodity declaration.
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
		// TODO Auto-generated method stub
		return null;
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
