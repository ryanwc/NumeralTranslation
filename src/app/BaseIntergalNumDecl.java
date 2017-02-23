package app;

/**
 * Hold all relevant information about a base intergalactic numeral 
 * declaration from notes about intergalactic commodity markets.
 * 
 * A base intergalactic numeral declaration has the following form:
 * '[base intergal numeral] is [base roman numeral]'
 * 
 * @author ryanwilliamconnor
 */
public class BaseIntergalNumDecl extends Declaration {
	
	private String baseRomanNum, baseIntergalNum;

	/**
	 * Create a new BaseIntergalNumDecl from an UnknownNote.
	 * 
	 * Keeps only the information that is relevant to a base intergal 
	 * commodity declaration.
	 * 
	 * WARNING: Assumes the UnknownNote actually conforms to 
	 * base intergal numeral declaration specs.
	 * 
	 * @param note is the UnknownNote to convert to a BaseIntergalNumDecl.
	 */
	public BaseIntergalNumDecl(UnknownNote uNote) {
		super(uNote);
		this.baseRomanNum = uNote.getComponents()[2];
		this.baseIntergalNum = uNote.getComponents()[0];
	}
	
	@Override
	public String toString() {
		
		String str = super.toString();
		str += "Base roman numeral: " + baseRomanNum;
		str += "\n";
		str += "Base intergalactic numeral: " + baseIntergalNum;
		str += "\n";
		
		return str;
	}
	
	public String getBaseIntergalNum() {
		return baseIntergalNum;
	}
	
	public String getBaseRomanNum() {
		return baseRomanNum;
	}
}
