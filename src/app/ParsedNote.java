package app;

/**
 * Blueprint to storing and accessing metadata from a single line of notes
 * about the intergalactic commodity markets.
 * 
 * @author ryanwilliamconnor
 */
public abstract class ParsedNote {
	
	private String note;
	private String[] components;
	
	/**
	 * Create a new parsed note.
	 * 
	 * @param note is a string: the raw note.
	 * @param components is a string array: the components of the parsed note.
	 */
	public ParsedNote(String note, String[] components) {
		// throw exceptions
		this.note = note;
		this.components = components;
	}
	
	/**
	 * Create a new parsed note from an UnknownNote.
	 * 
	 * Useful for conversions from an UnknownNote to a known
	 * note type.
	 * 
	 * @param note is an UnknownNote: the note on which to base the
	 * new ParsedNote.
	 */
	public ParsedNote(UnknownNote note) {
		// throw exceptions
		this.note = note.getNote();
		this.components = note.getComponents();
	}
	
	public String getNote() {
		return note;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
	
	public String[] getComponents() {
		return components;
	}
	
	public void setComponents(String[] components) {
		this.components = components;
	}
	
	public String toString() {
		
		String str = "";
		str += note;
		str += '\n';
		str += "Components: ";
		for (int i = 0; i < components.length; i++) 
			str += "'" + components[i] + "', ";
		str += '\n';
		
		return str;
	}
}
