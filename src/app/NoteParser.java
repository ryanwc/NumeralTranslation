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
	public static final int BASE_NUM_DECL = 0;
	public static final int COMPOSITE_NUM_DECL = 1;
	public static final int COMMODITY_DECL = 2;
	public static final int QUERY = 3;
	
	public static final int[] NOTE_TYPES = {UNKNOWN, BASE_NUM_DECL, 
			COMPOSITE_NUM_DECL, COMMODITY_DECL, QUERY};
	
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
		
		if (isQuery(note)) return QUERY;
		
		String[] noteWords = note.split("[^a-zA-Z_0-9]");
		if (isBaseNumDecl(noteWords)) return BASE_NUM_DECL;
		if (isCompositeNumDecl(noteWords)) return COMPOSITE_NUM_DECL;
		if (isCommodityDecl(noteWords)) return COMMODITY_DECL;
		
		// note does not conform to any known note types
		return UNKNOWN;
	}
	
	/**
	 * Determine whether a string is a base number declaration.
	 * 
	 * Base number declarations have three words, in order:
	 * 1) the intergalactic base number, 2) 'is', and 3) a base roman numeral.
	 * 
	 * Intergalactic base numbers are assumed to be entirely 
	 * alphabetic and lowercase.
	 * 
	 * @param note is a string array made of the words from a single line
	 * of notes about intergalactic commodities
	 * @return true if the given note is a base number declaration,
	 * false otherwise.
	 */
	private boolean isBaseNumDecl(String[] noteWords) {
		return noteWords.length == 3 && 
			   !noteWords[0].matches("[0-9_A-Z]") &&
			   noteWords[1].equals("is") &&
			   Translator.ROMAN_NUM_RANK.containsKey(noteWords[2]);
	}
	
	/**
	 * Determine whether a string is a composite number declaration.
	 * 
	 * Composite number declarations have three components, in order:
	 * 1) two or more intergalactic base numbers in a row, 
	 * 2) the word 'is', and 3) a composite roman numeral.
	 * 
	 * Intergalactic base numbers are assumed to be entirely 
	 * alphabetic and lowercase.
	 * 
	 * @param note is a string array made of the words from a single line
	 * of notes about intergalactic commodities
	 * @return true if the given note is a composite number declaration,
	 * false otherwise.
	 */
	private boolean isCompositeNumDecl(String[] noteWords) {
		
		if (noteWords.length < 4) return false;
		
		// well formed roman numeral check
		try {
			Translator.romanNumToArabic(noteWords[noteWords.length-1]);
		} catch (IllegalArgumentException e) {
			return false;
		}
		
		// is check
		if (!noteWords[noteWords.length-2].equals("is")) return false;
		
		// intergalactic alphabetic check
		for (int i = 0; i < noteWords.length-2; i++)
			if (noteWords[i].matches("[0-9_A-Z]")) return false;
			
		return true;
	}
	
	/**
	 * Determine whether a string is a commodity declaration.
	 * 
	 * @param note is a string formatted as single line from 
	 * intergalactic commodity notes
	 * @return true if the given string is a commodity declaration,
	 * false otherwise.
	 */
	public boolean isCommodityDecl(String[] note) {
		return false;
	}
	
	/**
	 * Determine whether a string is a query.
	 * 
	 * @param note a string formatted as single line from intergalactic
	 * commodity notes
	 * @return true if the given string is a query, false otherwise.
	 */
	public boolean isQuery(String note) {
		return note.charAt(note.length()-1) == '?';
	}
}
