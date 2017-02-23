package app;

/**
 * Blueprint for store metadata from a parsed note (single line of notes).
 * 
 * Allows constant-time reuse of info learned from linear parsing routine
 * (e.g., by note type checks, QueryHandler, Translator).
 * 
 * @author ryanwilliamconnor
 */
public abstract class ParsedNote {
	
	private String note;
	private String[] components;
	
	public ParsedNote(String note, String[] components) {
		this.note = note;
		this.components = components;
	}
	
	public ParsedNote(UnknownNote note) {
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
		return str;
	}
}
