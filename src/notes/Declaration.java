package notes;

/**
 * Blueprint for a declaration, which is a type of parsed note.
 * 
 * Declarations state some fact about the intergalactic commodity markets.
 * 
 * A well-fromed declaration has the word 'is' and no '?'
 * 
 * @author ryanwilliamconnor
 */
public abstract class Declaration extends ParsedNote {

	private int countIs, isPos;
	
	/**
	 * Convert an UnknownNote into a new Declaration.
	 * Keeps only the information that is relevant to all declarations;
	 * classes that extend Declaration must keep other data if desired.
	 * 
	 * A well-fromed declaration has the word 'is' and no '?'
	 * 
	 * WARNING: Assumes the UnknownNote actually conforms to commodity
	 * declaration specs. 
	 * 
	 * @param note is the UnknownNote to convert to a Declaration
	 */
	public Declaration(UnknownNote uNote) {
		super(uNote.getNote(), uNote.getComponents());	
		this.countIs = uNote.getCountIs();
		this.isPos = uNote.getIsPos();
	}
	
	@Override
	public String toString() {

		String str = super.toString();
		str += "Cis: " + countIs;
		str += "\n";
		str += "isP: " + isPos;
		str += "\n";
		
		return str;
	}
	
	public int getCountIs() {
		return countIs;
	}
	
	public int getIsPos() {
		return isPos;
	}
}
