package app;

/**
 * Hold all relevant information about a composite intergalactic numeral 
 * declaration from notes about intergalactic commodity markets.
 * 
 * A composite intergalactic numeral declaration has the following form:
 * '[intergal num longer than 1 word] is [roman num longer than 1 char]'
 * 
 * NOTE: This class is currently not in use. It is incomplete and provided
 * as a demonstration on how the application can be expanded.
 * It is useful for giving the translator more complete information from 
 * spotty or incomplete notes. For example, if we have:
 * 1. prok is I
 * 2. prok glob is IV
 * 3. how much is glob ?
 * 
 * We can answer 3 with the use of 1 and 2 combined.
 * 
 * @author ryanwilliamconnor
 */
public class CompIntergalNumDecl extends Declaration {

	private String romanNum, intergalNum;
	
	/**
	 * Create a new CompIntergalNumDecl from an UnknownNote.
	 * 
	 * Keeps only the information is relevant to a composite intergal
	 * numeral declaration.
	 * 
	 * WARNING: Assumes the UnknownNote actually conforms to composite
	 * intergal numeral declaration specs.
	 * 
	 * @param note is the UnknownNote to convert to a CompIntergalNumDecl
	 */
	public CompIntergalNumDecl(UnknownNote note) {
		super(note);
		String[] components = note.getComponents();
		this.romanNum = components[components.length-1];
		String str = "";
		for (int i = 0; i < components.length-3; i++) {
			str += components[i] + " ";
		}
		str += components[components.length-3];
		this.intergalNum = str;
	}
	
	@Override
	public String toString() {
		
		String str = super.toString();
		str += "Base roman numeral: " + romanNum;
		str += "\n";
		str += "Base intergalactic numeral: " + intergalNum;
		str += "\n";
		
		return str;
	}

	public String getRomanNum() {
		return romanNum;
	}

	public void setRomanNum(String romanNum) {
		this.romanNum = romanNum;
	}

	public String getIntergalNum() {
		return intergalNum;
	}

	public void setIntergalNum(String intergalNum) {
		this.intergalNum = intergalNum;
	}
}
