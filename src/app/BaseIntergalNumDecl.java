package app;

public class BaseIntergalNumDecl extends Declaration {
	
	private String baseRomanNum, baseIntergalNum;

	/**
	 * Create a new BaseIntergalNumDecl from an UnknownNote.
	 * 
	 * Keeps only the information is relevant to a base intergal commodity
	 * declaration.
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
		String str = "";
		return str;
	}
	
	public String getBaseIntergalNum() {
		return baseIntergalNum;
	}
	
	public String getBaseRomanNum() {
		return baseRomanNum;
	}
}
