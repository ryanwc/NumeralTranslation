package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import notes.BaseIntergalNumDecl;
import notes.CommodityDecl;
import notes.CompIntergalNumDecl;
import notes.ParsedNote;
import notes.Query;
import notes.UnknownNote;
import utility.Translator;

/**
 * Parse notes about the intergalactic commodity markets. 
 * 
 * @author ryanwilliamconnor
 *
 */
public class NoteParser {
	
	/**
	 * Parses information about the intergalactic commodity markets.
	 * Returns organized, parsed notes.
	 * 
	 * @param notes is a list of strings where each string is a line
	 * from intergalactic commodity notes
	 * @return a map of {String: List<ParsedNote>}, where List<ParsedNote>
	 * is the list of ParsedNotes that have the type String.
	 */
	public Map<String, List<ParsedNote>> parseNotes(List<String> notes) {
		
		Map<String, List<ParsedNote>> parsedNotes = 
				new HashMap<String, List<ParsedNote>>();
		
		// parse each given note and group with like notes
		List<ParsedNote> similarNotes;
		ParsedNote parsedNote;
		String noteType;
		for (String note : notes) {
			parsedNote = parse(note);
			noteType = parsedNote.getClass().getSimpleName();
			if (!parsedNotes.containsKey(noteType))
				parsedNotes.put(noteType, new ArrayList<ParsedNote>());
			similarNotes = parsedNotes.get(noteType);
			similarNotes.add(parsedNote);
			parsedNotes.put(noteType, similarNotes);
		}
		
		return parsedNotes;
	}
	
	/**
	 * Parse a note, meaning split the note string into relevant components and
	 * track metadata based on best guess of how the metadata determines the 
	 * type and meaning of the note.
	 * 
	 * Ideally, would work based on perfect information about note syntax 
	 * (like a programming language compiler). Could also learn syntax via 
	 * machine learning and analysis of "unknown" note types.
	 * But, that is out of scope for this project.
	 * 
	 * Tracks note metadata for type checker based on following syntax rules:
	 * 1) can have 0 or 1 one 'is', 2) can have 0, 1, or 2 commodities, 
	 * 3) commodities are one entirely alphabetic, non 'Credits' word starting 
	 * with a capital letter, 4) can have 0, 1, or 2 clusters of base intergal 
	 * numerals, 5) base intergal numerals are entirely alphabetic lowercase 
	 * words not 'is', 'how', 'much', or 'many', 6) can have 0 or 1 roman 
	 * numeral (base or composite), 7) can have 0 or 1 'Credits', 8) can have 
	 * 0 or 1 question mark, 9) can have 0 or 1 arabic numerals, 10) can have
	 * 0 or 1 'how', 11) can have 0 or 1 'much', 12) can have 0 or 1 'many'.
	 * 
	 * @param note is a string: the note to parse
	 * @return a type of ParsedNote (the string split into an array of words 
	 * and metadata about the contents of the array, and identified 
	 * if possible)
	 */
	public ParsedNote parse(String note) {
		
		// split into just words and question marks
		// (remove white space, all punctuation except '?')
		String[] components = note.split("[^a-zA-Z_0-9\\?]");
		
		// initialize counts of interesting data
		int countIs=0, countIntergalClust=0, countRomanBase=0, 
				countRomanComp=0, countComm=0,countQ=0, countArabic=0, 
				countCredits=0, countHow=0, countMuch=0, countMany=0;
		
		// set indices of non-existent data to -1
		int isPos=-1, start1IntergalClust=-1, end1IntergalClust=-1,
				start2IntergalClust=-1, end2IntergalClust=-1, 
				romanPos=-1, comm1Pos=-1, comm2Pos=-1, qPos=-1, 
				arabicPos=-1, creditPos=-1, howPos=-1, muchPos=-1, manyPos=-1;
		
		// gather data
		for (int i = 0; i < components.length; i++) {
			if (components[i].equals("is")) {
				isPos = i;
				countIs++;
				continue;
			}
			if (components[i].equals("Credits")) {
				countCredits++;
				creditPos = i;
				continue;
			}
			if (components[i].equals("how")) {
				countHow++;
				howPos = i;
				continue;
			}
			if (components[i].equals("much")) {
				countMuch++;
				muchPos = i;
				continue;
			}
			if (components[i].equals("many")) {
				countMany++;
				manyPos = i;
				continue;
			}
			if (components[i].equals("?")) {
				countQ++;
				qPos = i;
				continue;
			}
			if (isArabic(components[i])) {
				countArabic++;
				arabicPos = i;
				continue;
			}
			if (isCommodity(components[i])) {
				// redundant check on !=Credits because if series above
				if (countComm == 0) {
					comm1Pos = i;
				}
				else {
					comm2Pos = i;
				}
				countComm++;
				continue;
			}
			if (Translator.ROMAN_NUM_RANK.containsKey(components[i])) {
				countRomanBase++;
				romanPos = i;
				continue;
			}
			try {
				// composite roman numeral check
				Translator.romanNumToArabic(components[i]);
				countRomanComp++;
				romanPos = i;
				continue;
			} catch (IllegalArgumentException e) {
				// move to next test
			}
			if (isIntergalNum(components[i])) {
				// checks on != how, is, much redundant in this if series
				if (countIntergalClust == 0) {
					start1IntergalClust = i;
				}
				else {
					start2IntergalClust = i;
				}
				countIntergalClust++;
				// from the start of this cluster, count intergal nums
				// set end of cluster and i appropriately
				do {
					i++;
				} while (isIntergalNum(components[i]));
				i--;
				if (countIntergalClust == 1) {
					end1IntergalClust = i;
				}
				else {
					end2IntergalClust = i;
				}
			}
		}
		
		// create an unknown note to be identified (or not)
		UnknownNote uNote = new UnknownNote(note, components, countIs, 
				countIntergalClust, countRomanBase, countRomanComp, 
				countComm, countQ, countArabic, countCredits, countHow, 
				countMuch, countMany, isPos, start1IntergalClust, 
				end1IntergalClust, start2IntergalClust, end2IntergalClust, 
				romanPos, comm1Pos, comm2Pos, qPos, arabicPos, creditPos, 
				howPos, muchPos, manyPos);
		
		return identifyNote(uNote);
	}
	
	/**
	 * Check whether the given string is entirely an arabic numeral.
	 * 
	 * @param str is a string: the string to check
	 * @throws IllegalArgumentException if str is null
	 * @return true if the string is contains (and contains nothing
	 * but) an arabic numeral, false otherwise
	 */
	private boolean isArabic(String str) {
		
		if (str == null) 
			throw new IllegalArgumentException("str cannot be null");
		
		for (int i = 0; i < str.length(); i++) {
			try {
				Integer.parseInt(str.substring(i, i+1));	
			}
			catch (Exception e){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Check whether the given string matches the form of 
	 * a commodity in notes about intergalactic commodity markets.
	 * 
	 * Commodities are entirely alphabetic and start with a capital
	 * letter followed by at least one lowercase letter, 
	 * and are not some reserved key words.
	 * 
	 * @param str is a string: the string to check
	 * @return true if the string is a commodity, false otherwise
	 */
	private boolean isCommodity(String str) {
		
		if (str == null) 
			throw new IllegalArgumentException("str cannot be null");
		
		if (str.length() < 2) return false;
		if (!Character.isAlphabetic(str.charAt(0))) return false;
		if (!Character.isUpperCase(str.charAt(0))) return false;
		if (str.equals("Credits")) return false;
		
		for (int i = 1; i < str.length(); i++) {
			if (!Character.isAlphabetic(str.charAt(i))) return false;
			if (Character.isUpperCase(str.charAt(i))) return false;
		}
		
		return true;
	}
	
	/**
	 * Check whether the given string matches the form of an
	 * intergalactic numeral form intergalactic commodity market
	 * notes.
	 * 
	 * An intergalactic numeral in intergalactic commodity notes
	 * is entirely alphabetic and lowercase, and is not 
	 * some reserved key words.
	 * 
	 * @param str is the string to check
	 * @throws IllegalArgumentException is the string is null
	 * @return true if the string is an intergalactic numeral, 
	 * false otherwise
	 */
	private boolean isIntergalNum(String str) {
		
		if (str == null) 
			throw new IllegalArgumentException("str cannot be null");
		
		if (str.length() < 1) return false;
		if (str.equals("Credits")) return false;
		if (str.equals("how")) return false;
		if (str.equals("much")) return false;
		if (str.equals("is")) return false;
		
		// must be alphabetic and lower case
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isAlphabetic(str.charAt(i)) || 
				!Character.isLowerCase(str.charAt(i)))
				return false;
		}
		
		return true;
	}
	
	/**
	 * Attempt to identify an UnknownNote.
	 * If matching against all known ParsedNote types fails, 
	 * returns the passed UnknownNote.
	 * 
	 * @param note is the UnknownNote to identify
	 * @throws IllegalArgumentException if note is null
	 * @return an instance of ParsedNote that is also an instance of
	 * the appropriate subclass based on checks of note metadata
	 */
	public ParsedNote identifyNote(UnknownNote note) {
		
		if (note == null) 
			throw new IllegalArgumentException("note cannot be null");
		
		if (isQuery(note)) return new Query(note);
		if (isBaseNumDecl(note)) return new BaseIntergalNumDecl(note);
		if (isCompositeNumDecl(note)) return new CompIntergalNumDecl(note);
		if (isCommodityDecl(note)) return new CommodityDecl(note);
		
		// note does not conform to any known note types, return unchanged
		return note;
	}
	
	/**
	 * Determine whether an UnknownNote is a base number declaration
	 * based on metadata.
	 * 
	 * Base number declarations have three components, in order:
	 * 1) a 'cluster' of one intergalactic numeral, 2) the word 'is', 
	 * and 3) a base roman numeral.
	 * 
	 * @param note is an UnknownNote: the note to check
	 * @throws IllegalArgumentException if the note is null
	 * @return true if the given note is a base number declaration,
	 * false otherwise.
	 */
	private boolean isBaseNumDecl(UnknownNote note) {
		
		if (note == null) 
			throw new IllegalArgumentException("note cannot be null");
		
		return note.getComponents().length == 3 && 
			note.getCountIntergalClust() == 1 &&
			note.getCountRomanBase() == 1 && note.getCountIs() == 1 && 
			note.getRomanPos() == 2 && note.getIsPos() == 1 &&
			note.getStart1IntergalClust() == 0 && 
			note.getEnd1IntergalClust() == 0;
	}
	
	/**
	 * Determine whether an UnknownNote is a composite number declaration
	 * based on metadata.
	 * 
	 * Composite number declarations have three components, in order:
	 * 1) a cluster of intergalactic numerals, 2) the word 'is', 
	 * and 3) a composite roman numeral.
	 * 
	 * @param note is an UnknownNote: the note to check
	 * @throws IllegalArgumentException if the note is null
	 * @return true if the given note is a composite number 
	 * declaration, false otherwise.
	 */
	private boolean isCompositeNumDecl(UnknownNote note) {
		
		if (note == null) 
			throw new IllegalArgumentException("note cannot be null");
		
		return note.getComponents().length > 3 && 
			note.getCountIntergalClust() == 1 &&
			note.getCountRomanComp() == 1 && note.getCountIs() == 1 && 
			note.getRomanPos() == note.getComponents().length-1 && 
			note.getIsPos() == note.getComponents().length-2 &&
			note.getStart1IntergalClust() == 0 && 
			note.getEnd1IntergalClust() == note.getComponents().length-3;
	}
	
	/**
	 * Determine whether an UnknownNote is a commodity declaration
	 * based on metadata.
	 * 
	 * Commodity declarations have the following components, in order:
	 * 1) a cluster of intergalactic numerals, 2) a commodity,
	 * 3) the word 'is', 4) an arabic numeral, 5) the word 'Credits'.
	 * 
	 * NOTE: The above criteria could be expanded to allow more diverse
	 * declarations, leading to possibly more complete intergalactic
	 * to roman numeral translation if the base intergalactic numerals
	 * are not well known.
	 * 
	 * @param note is an UnknownNote: the note to check
	 * @throws IllegalArgumentException if the note is null
	 * @return true if the given note is a commodity declaration,
	 * false otherwise.
	 */
	public boolean isCommodityDecl(UnknownNote note) {
		
		if (note == null) 
			throw new IllegalArgumentException("note cannot be null");
		
		return note.getComponents().length > 4 && 
				note.getCountIntergalClust() == 1 &&
				note.getCountCredits() == 1 && note.getCountIs() == 1 && 
				note.getCountArabic() == 1 && 
				note.getCreditPos() == note.getComponents().length-1 &&
				note.getArabicPos() == note.getComponents().length-2 &&
				note.getIsPos() == note.getComponents().length-3 &&
				note.getComm1Pos() == note.getComponents().length - 4 &&
				note.getStart1IntergalClust() == 0 && 
				note.getEnd1IntergalClust() == note.getComponents().length-5;
	}
	
	/**
	 * Determine whether an UnknownNote is a query based on metadata.
	 * 
	 * This test is very simple: true if the final component of the 
	 * note is '?', false otherwise.
	 * 
	 * This is by design; a '?' at the last position means a note is
	 * query regardless of whether the query makes sense or is 
	 * answerable; the latter determinations are left to 
	 * the query handler.
	 * 
	 * @param note is an UnkownNote: the note to check
	 * @throws IllegalArgumentException if the note is null
	 * @return true if the given note is a query, false otherwise.
	 */
	public boolean isQuery(UnknownNote note) {
		return note.getqPos() == note.getComponents().length-1;
	}
}
