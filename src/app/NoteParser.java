package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parse notes about the intergalactic commodity markets. 
 * 
 * @author ryanwilliamconnor
 *
 */
public class NoteParser {

	public static final int UNKNOWN = -1;
	public static final int NUMBER_DECL = 0;
	public static final int COMMODITY_DECL = 1;
	public static final int QUERY = 2;
	
	public static final int[] NOTE_TYPES = {UNKNOWN, NUMBER_DECL, 
			COMMODITY_DECL, QUERY};
	
	/**
	 * Parses information about the intergalactic commodity markets.
	 * 
	 * @param notes is a list of strings where each string is a line
	 * from intergalactic commodity notes
	 * @return a map of {NOTE_TYPE: List<String>}, where List<String>
	 * is the list of strings in the given notes that have the
	 * type NOTE_TYPE.
	 */
	public Map<Integer, List<String>> parseNotes(List<String> notes) {
		
		Map<Integer, List<String>> parsedNotes = 
				new HashMap<Integer, List<String>>();
		
		for (int i = 0; i < NOTE_TYPES.length; i++)
			parsedNotes.put(NOTE_TYPES[i], new ArrayList<String>());
		
		List<String> similarNotes;
		int noteType;
		for (String note : notes) {
			noteType = getNoteType(note);
			similarNotes = parsedNotes.get(noteType);
			similarNotes.add(note);
			parsedNotes.put(noteType, similarNotes);
		}
		
		return parsedNotes;
	}
	
	/**
	 * Get the type of intergalactic commodity information 
	 * this note holds.
	 * 
	 * @param note is a string formatted as single line from 
	 * intergalactic commodity notes
	 * @return an int representing the type of intergalactic commodity
	 * information the given note holds
	 */
	public int getNoteType(String note) {
		
		if (isNumberDecl(note)) return NUMBER_DECL;
		if (isCommodityDecl(note)) return COMMODITY_DECL;
		if (isQuery(note)) return QUERY;
		
		// note does not conform to any known note types
		return UNKNOWN;
	}
	
	/**
	 * Determine whether a string is a number declaration.
	 * 
	 * @param note is a string formatted as single line from 
	 * intergalactic commodity notes
	 * @return true if the given string is a number declaration,
	 * false otherwise.
	 */
	public boolean isNumberDecl(String note) {
		
	}
	
	/**
	 * Determine whether a string is a commodity declaration.
	 * 
	 * @param note is a string formatted as single line from 
	 * intergalactic commodity notes
	 * @return true if the given string is a commodity declaration,
	 * false otherwise.
	 */
	public boolean isCommodityDecl(String note) {
		
	}
	
	/**
	 * Determine whether a string is a query.
	 * 
	 * @param note a string formatted as single line from intergalactic
	 * commodity notes
	 * @return true if the given string is a query, false otherwise.
	 */
	public boolean isQuery(String note) {
		
	}
}
