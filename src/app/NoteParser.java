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
	public Map<Integer, List<ParsedNote>> parseNotes(List<String> notes) {
		
		Map<Integer, List<ParsedNote>> parsedNotes = 
				new HashMap<Integer, List<ParsedNote>>();
		
		for (int i = 0; i < NOTE_TYPES.length; i++)
			parsedNotes.put(NOTE_TYPES[i], new ArrayList<ParsedNote>());
		
		// parse each given note and group with like notes
		List<ParsedNote> similarNotes;
		ParsedNote parsedNote;
		int noteType;
		for (String note : notes) {
			parsedNote = parse(note);
			noteType = getNoteType(parsedNote);
			parsedNote.noteType = noteType;
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
	 * Track note metadata for type checker based on following syntax rules:
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
	 * @return a ParsedNote (the string split into an array of words and
	 * metadata about the contents of the array)
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
			//System.out.println("getting data about '" + components[i] + "'");
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
				//System.out.println(components[i] + " is arabic");
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
				// just move on to next check
			}
			if (isIntergalNum(components[i])) {
				// checks on != how, is, much redundant in this if series
				//System.out.println("'" + components[i] + "' is intergal");
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
					//System.out.println("Cluster includes '" + components[i] + "'");
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
		
		return new ParsedNote(note, components, countIs, countIntergalClust, 
				countRomanBase, countRomanComp, countComm, countQ, 
				countArabic, countCredits, countHow, countMuch, countMany, 
				isPos, start1IntergalClust, end1IntergalClust, 
				start2IntergalClust, end2IntergalClust, romanPos, comm1Pos, 
				comm2Pos, qPos, arabicPos, creditPos, 
				howPos, muchPos, manyPos);
	}
	
	private boolean isArabic(String str) {
		
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
	
	private boolean isCommodity(String str) {
		
		if (str.length() < 2) return false;
		if (!Character.isUpperCase(str.charAt(0))) return false;
		if (str.equals("Credits")) return false;
		
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isAlphabetic(str.charAt(i)))
				return false;
		}
		
		return true;
	}
	
	private boolean isIntergalNum(String str) {
		
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
	 * Get the type of intergalactic commodity information 
	 * this note holds.
	 * 
	 * @param note is a string formatted as single line from 
	 * intergalactic commodity notes
	 * @return an int representing the type of intergalactic commodity
	 * information the given note holds
	 */
	public int getNoteType(ParsedNote note) {
		
		if (isQuery(note)) return QUERY;
		if (isBaseNumDecl(note)) return BASE_NUM_DECL;
		if (isCompositeNumDecl(note)) return COMPOSITE_NUM_DECL;
		if (isCommodityDecl(note)) return COMMODITY_DECL;
		
		// note does not conform to any known note types
		return UNKNOWN;
	}
	
	/**
	 * Determine whether a string is a base number declaration.
	 * 
	 * Base number declarations have three components, in order:
	 * 1) a 'cluster' of one intergalactic numeral, 2) the word 'is', 
	 * and 3) a base roman numeral.
	 * 
	 * @param note is a ParsedNote from intergalactic commodity notes
	 * @return true if the given ParsedNote is a base number declaration,
	 * false otherwise.
	 */
	private boolean isBaseNumDecl(ParsedNote note) {
		return note.components.length == 3 && note.countIntergalClust == 1 &&
			note.countRomanBase == 1 && note.countIs == 1 && 
			note.romanPos == 2 && note.isPos == 1 &&
			note.start1IntergalClust == 0 && 
			note.end1IntergalClust == 0;
	}
	
	/**
	 * Determine whether a string is a composite number declaration.
	 * 
	 * Composite number declarations have three components, in order:
	 * 1) a cluster of intergalactic numerals, 2) the word 'is', 
	 * and 3) a composite roman numeral.
	 * 
	 * @param note is a ParsedNote from intergalactic commodity notes
	 * @return true if the given ParsedNote is a composite number 
	 * declaration, false otherwise.
	 */
	private boolean isCompositeNumDecl(ParsedNote note) {
		return note.components.length > 3 && note.countIntergalClust == 1 &&
			note.countRomanComp == 1 && note.countIs == 1 && 
			note.romanPos == note.components.length-1 && 
			note.isPos == note.components.length-2 &&
			note.start1IntergalClust == 0 && 
			note.end1IntergalClust == note.components.length-3;
	}
	
	/**
	 * Determine whether a string is a commodity declaration.
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
	 * @param note is a string formatted as single line from 
	 * intergalactic commodity notes
	 * @return true if the given string is a commodity declaration,
	 * false otherwise.
	 */
	public boolean isCommodityDecl(ParsedNote note) {
		return note.components.length > 4 && note.countIntergalClust == 1 &&
				note.countCredits == 1 && note.countIs == 1 && 
				note.countArabic == 1 && 
				note.creditPos == note.components.length-1 &&
				note.arabicPos == note.components.length-2 &&
				note.isPos == note.components.length-3 &&
				note.comm1Pos == note.components.length - 4 &&
				note.start1IntergalClust == 0 && 
				note.end1IntergalClust == note.components.length-5;
	}
	
	/**
	 * Determine whether a string is a query.
	 * 
	 * Queries begin with 'how', followed by 'much' or 'many', end
	 * with '?', and have one 'is' somewhere in between. 
	 * 
	 * Does not determine whether the query asks an answerable question.
	 * 
	 * @param note a string formatted as single line from intergalactic
	 * commodity notes
	 * @return true if the given string is a query, false otherwise.
	 */
	public boolean isQuery(ParsedNote note) {
		return note.qPos == note.components.length-1;
	}
	
	/**
	 * Determine whether a parsed note has legal syntax.
	 * 
	 * Not complete/bullet-proof, could expand. Only checks raw
	 * counts of relevant data, not, for example, relative positions of data.
	 * 
	 * @param note is a ParsedNote: the note to check
	 * @return true if the note has illegal syntax, false otherwise
	 */
	public boolean isIllegal(ParsedNote note) {
		
		// check raw counts
		if (note.countIs > 1) return true;
		if (note.countQ > 1) return true;
		if (note.countHow > 1) return true;
		if (note.countMuch > 1) return true;
		if (note.countMany > 1) return true;
		if (note.countArabic > 1) return true;
		if (note.countCredits > 1) return true;
		if (note.countRomanBase > 1) return true;
		if (note.countRomanComp > 1) return true;
		if (note.countIntergalClust > 2) return true;
		if (note.countComm > 2) return true;
		
		// check combinations
		if (note.countMany + note.countMuch > 1) return true;
		if (note.countRomanBase + note.countRomanComp > 1) return true;
		
		return false;
	}
	
	/**
	 * Store metadata from a parsed note (single line of notes).
	 * Allows constant-time reuse of info learned from linear parsing routine
	 * (e.g., by note type checks, QueryHandler, Translator).
	 * 
	 * Non-existant represented as '-1'. For example, if there is no 'is',
	 * isPos = -1.
	 */
	public class ParsedNote {
		String note;
		String[] components;
		int countIs, countIntergalClust, countRomanBase, 
			countRomanComp, countComm, countQ,
			countArabic, countCredits, countHow, countMuch, countMany, isPos, 
			start1IntergalClust, end1IntergalClust, start2IntergalClust, 
			end2IntergalClust, romanPos, comm1Pos, comm2Pos, qPos, arabicPos, 
			creditPos, howPos, muchPos, manyPos, noteType;
		
		public ParsedNote(String note, String[] components, int countIs, 
				int countIntergalClust, int countRomanBase,
				int countRomanComp, int countComm, 
				int countQ, int countArabic, int countCredits, int countHow, 
				int countMuch, int countMany, int isPos, 
				int start1IntergalClust, int end1IntergalClust, 
				int start2IntergalClust, int end2IntergalClust, int romanPos, 
				int comm1Pos, int comm2Pos, int qPos, int arabicPos, 
				int creditPos, int howPos, int muchPos, int manyPos) {
			
			this.note = note;
			this.components = components;
			this.countIs = countIs;
			this.countIntergalClust = countIntergalClust;
			this.countRomanBase = countRomanBase;
			this.countRomanComp = countRomanComp;
			this.countComm = countComm;
			this.countQ = countQ;
			this.countArabic = countArabic;
			this.countCredits = countCredits;
			this.countHow = countHow;
			this.countMuch = countMuch;
			this.countMany = countMany;
			this.isPos = isPos;
			this.start1IntergalClust = start1IntergalClust;
			this.end1IntergalClust = end1IntergalClust;
			this.start2IntergalClust = start2IntergalClust;
			this.end2IntergalClust = end2IntergalClust;
			this.romanPos = romanPos;
			this.comm1Pos = comm1Pos;
			this.comm2Pos = comm2Pos;
			this.qPos = qPos;
			this.arabicPos = arabicPos;
			this.creditPos = creditPos;
			this.howPos = howPos;
			this.muchPos = muchPos;
			this.manyPos = manyPos;
			
			this.noteType = -1;
		}
		
		public String toString() {
			
			String str = "";
			str += note;
			str += '\n';
			str += "Components: ";
			for (int i = 0; i < components.length; i++) {
				str += "'" + components[i] + "', ";
			}
			str += '\n';
			str += "Cis: " + countIs + ", CIntCl: " + countIntergalClust + 
					", CRomB: " + countRomanBase + ", CRomC: " + countRomanComp 
					+ ", CCm: " + countComm + ", CQ: " + countQ + ", CA: " + 
					countArabic + ", CCr: " + countCredits + ", CH: " + 
					countHow + ", CMu: " + countMuch + ", CM: " + countMany;
			str += '\n';
			str += "isP: " + isPos + ", " + "sIC1: " + start1IntergalClust 
					+ ", eIC1: " + end1IntergalClust + ", sIC2: " +
					+ start2IntergalClust + ", eIC2: " + end2IntergalClust + 
					", RP: " + romanPos + ", C1P: " + comm1Pos + 
					", C2P: " + comm2Pos + ", QP: " + qPos + 
					", AP: " + arabicPos + ", credP: " + creditPos + 
					", HP: " + howPos + ", muchP: " + muchPos + 
					", manyP: " + manyPos;
			str += '\n';
			str += "Note type: " + noteType;
				
			return str;
		}
	}
}
