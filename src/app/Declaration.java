package app;

public abstract class Declaration extends ParsedNote {

	private int countIs, isPos;
	
	public Declaration(UnknownNote uNote) {
		super(uNote.getNote(), uNote.getComponents());	
		this.countIs = uNote.getCountIs();
		this.isPos = uNote.getIsPos();
	}
	
	public int getCountIs() {
		return countIs;
	}
	
	public int getIsPos() {
		return isPos;
	}
}
