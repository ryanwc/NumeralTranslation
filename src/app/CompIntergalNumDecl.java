package app;

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
		// TODO Auto-generated method stub
		return null;
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
