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
	
	private List<String> notes;
	private List<BaseIntergalNumDecl> baseIntergalNumDecs;
	private List<CompIntergalNumDecl> compIntergalNumDecs;
	private List<CommodityDecl> commodityDecs;
	private List<Query> queries;
	private List<UnknownNote> unknownNotes;
	private Map<Class, List<? extends ParsedNote>> pNotes;
	
	/**
	 * Construct a new NoteParser.
	 */
	public NoteParser() {
		this.baseIntergalNumDecs = new ArrayList<BaseIntergalNumDecl>();
		this.compIntergalNumDecs = new ArrayList<CompIntergalNumDecl>();
		this.commodityDecs = new ArrayList<CommodityDecl>();
		this.queries = new ArrayList<Query>();
		this.unknownNotes = new ArrayList<UnknownNote>();
		this.pNotes = new HashMap<Class, List<? extends ParsedNote>>();
	}
	
	/**
	 * Parses and stores information about the intergalactic commodity markets.
	 * 
	 * @param notes is a list of strings where each string is a line
	 * from intergalactic commodity notes
	 */
	public void parseNotes(List<String> notes) {
		
		this.notes = notes; // store the notes
		
		// parse and sort the notes
		for (String note : notes) sortNote(parse(note));
		
		// put read-only lists in map for easy bulk transfer
		pNotes.put(BaseIntergalNumDecl.class, baseIntergalNumDecs);
		pNotes.put(CompIntergalNumDecl.class, compIntergalNumDecs);
		pNotes.put(CommodityDecl.class, commodityDecs);
		pNotes.put(Query.class, queries);
		pNotes.put(UnknownNote.class, unknownNotes);
	}
	
	/**
	 * Add a parsed note to list of like notes.
	 * 
	 * @param pNote is a ParsedNote: the one to add
	 * @return true if the note was added, false otherwise
	 */
	public boolean sortNote(ParsedNote pNote) {

		if (pNote instanceof BaseIntergalNumDecl) {
			baseIntergalNumDecs.add((BaseIntergalNumDecl)pNote);
			return true;
		}
		
		if (pNote instanceof CompIntergalNumDecl) {
			compIntergalNumDecs.add((CompIntergalNumDecl)pNote);
			return true;
		}
		
		if (pNote instanceof CommodityDecl) {
			commodityDecs.add((CommodityDecl)pNote);		
			return true;
		}
		
		if (pNote instanceof Query) {
			queries.add((Query)pNote);			
			return true;
		}
		
		if (pNote instanceof UnknownNote) {
			unknownNotes.add((UnknownNote)pNote);	
			return true;
		}
		
		return false;
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
		
		// create an unknown note with clean data 
		UnknownNote uNote = new UnknownNote(note);
		int numComponents = uNote.getComponents().length;
		
		// parse the note (set its metadata and identify it)
		for (int i = 0; i < numComponents; i++) {
			if (checkAndSetIs(uNote, i)) continue;
			if (checkAndSetCredits(uNote, i)) continue;
			if (checkAndSetHow(uNote, i)) continue;
			if (checkAndSetMuch(uNote, i)) continue;
			if (checkAndSetMany(uNote, i)) continue;
			if (checkAndSetQ(uNote, i)) continue;
			if (checkAndSetArabic(uNote, i)) continue;
			if (checkAndSetCommodity(uNote, i)) continue;
			if (checkAndSetBaseRomanNum(uNote, i)) continue;
			if (checkAndSetCompRomanNum(uNote, i)) continue;
			if (checkAndSetStartIntergalNum(uNote, i))
				i = setEndIntergalNum(uNote, i);
		}
		
		return identifyNote(uNote);
	}
	
	/**
	 * Checks whether the component at the given index of the given
	 * unknown note is a commodity. If so, sets relevant data in the
	 * unknown note and returns true. If not, returns false.
	 * 
	 * @param uNote: the note to check
	 * @param index: the index of the note component to check
	 * @return true if the check succeeded and info was set, false otherwise
	 */
	private boolean checkAndSetCommodity(UnknownNote uNote, int index) {
		if (isCommodity(uNote.getComponents()[index])) {
			if (uNote.getCountComm() == 0) {
				uNote.setComm1Pos(index);
			}
			else {
				uNote.setComm2Pos(index);
			}
			uNote.setCountComm(uNote.getCountComm()+1);
		}
		return false;
	} 
	
	/**
	 * Checks whether the component at the given index of the given
	 * unknown note is a base roman numeral. If so, sets relevant data in the
	 * unknown note and returns true. If not, returns false.
	 * 
	 * @param uNote: the note to check
	 * @param index: the index of the note component to check
	 * @return true if the check succeeded and info was set, false otherwise
	 */
	private boolean checkAndSetBaseRomanNum(UnknownNote uNote, int index) {
		if (Translator.ROMAN_NUM_RANK.
				containsKey(uNote.getComponents()[index])){
			uNote.setRomanPos(index);
			uNote.setCountRomanBase(uNote.getCountRomanBase()+1);
		}
		return false;
	} 
	
	/**
	 * Checks whether the component at the given index of the given
	 * unknown note is a composite roman numeral. If so, sets relevant data
	 * in the unknown note and returns true. If not, returns false.
	 * 
	 * @param uNote: the note to check
	 * @param index: the index of the note component to check
	 * @return true if the check succeeded and info was set, false otherwise
	 */
	private boolean checkAndSetCompRomanNum(UnknownNote uNote, int index) {
		try {
			Translator.romanNumToArabic(uNote.getComponents()[index]);
			uNote.setRomanPos(index);
			uNote.setCountRomanComp(uNote.getCountRomanComp()+1);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	} 
	
	/**
	 * Checks whether the component at the given index of the given
	 * unknown note is a base intergal numeral. If so, sets relevant data 
	 * in the unknown note and returns true. If not, returns false.
	 * 
	 * @param uNote: the note to check
	 * @param index: the index of the note component to check
	 * @return true if the check succeeded and info was set, false otherwise
	 */
	private boolean checkAndSetStartIntergalNum(UnknownNote uNote, int index) {
		if (isIntergalNum(uNote.getComponents()[index])) {	
			if (uNote.getCountIntergalClust() == 0) {
				uNote.setStart1IntergalClust(index);
			}
			else {
				uNote.setStart2IntergalClust(index);
			}
			uNote.setCountIntergalClust(uNote.getCountIntergalClust()+1);
			return true;
		}
		return false;
	}
	
	/**
	 * Sets the relevant info for the end of an intergal numeral cluster
	 * given an unknown note and the index of the start of an intergal numeral
	 * cluster within that note.
	 * 
	 * @param uNote: the note whose info to set
	 * @param index: the index of the start of an intergal numeral cluster
	 * @return an int: the index of the end of the intergal numeral cluster
	 */
	private int setEndIntergalNum(UnknownNote uNote, int index) {
		// from the start of this intergal num cluster, count base 
		// intergal nums, set end of cluster, and return end index
		do {
			index++;
		} while (isIntergalNum(uNote.getComponents()[index]));
		
		index--;
		
		if (uNote.getCountIntergalClust() == 1) {
			uNote.setEnd1IntergalClust(index);
		}
		else {
			uNote.setEnd2IntergalClust(index);
		}
		
		return index;
	}
	
	/**
	 * Checks whether the component at the given index of the given
	 * unknown note is 'how'. If so, sets relevant data 
	 * in the unknown note and returns true. If not, returns false.
	 * 
	 * @param uNote: the note to check
	 * @param index: the index of the note component to check
	 * @return true if the check succeeded and info was set, false otherwise
	 */	
	private boolean checkAndSetHow(UnknownNote uNote, int index) {
		if (uNote.getComponents()[index].equals("how")) {
			uNote.setHowPos(index);
			uNote.setCountHow(uNote.getCountHow()+1);
			return true;
		}
		return false;
	}
	
	/**
	 * Checks whether the component at the given index of the given
	 * unknown note is 'much'. If so, sets relevant data 
	 * in the unknown note and returns true. If not, returns false.
	 * 
	 * @param uNote: the note to check
	 * @param index: the index of the note component to check
	 * @return true if the check succeeded and info was set, false otherwise
	 */
	private boolean checkAndSetMuch(UnknownNote uNote, int index) {
		if (uNote.getComponents()[index].equals("much")) {
			uNote.setMuchPos(index);
			uNote.setCountMuch(uNote.getCountMuch()+1);
			return true;
		}
		return false;
	}
	
	/**
	 * Checks whether the component at the given index of the given
	 * unknown note is 'many'. If so, sets relevant data 
	 * in the unknown note and returns true. If not, returns false.
	 * 
	 * @param uNote: the note to check
	 * @param index: the index of the note component to check
	 * @return true if the check succeeded and info was set, false otherwise
	 */
	private boolean checkAndSetMany(UnknownNote uNote, int index) {
		if (uNote.getComponents()[index].equals("many")) {
			uNote.setManyPos(index);
			uNote.setCountMany(uNote.getCountMany()+1);
			return true;
		}
		return false;
	} 
	
	/**
	 * Checks whether the component at the given index of the given
	 * unknown note is '?'. If so, sets relevant data 
	 * in the unknown note and returns true. If not, returns false.
	 * 
	 * @param uNote: the note to check
	 * @param index: the index of the note component to check
	 * @return true if the check succeeded and info was set, false otherwise
	 */
	private boolean checkAndSetQ(UnknownNote uNote, int index) {
		if (uNote.getComponents()[index].equals("?")) {
			uNote.setqPos(index);
			uNote.setCountQ(uNote.getCountQ()+1);
			return true;
		}
		return false;
	}
	
	/**
	 * Checks whether the component at the given index of the given
	 * unknown note is an arabic numeral. If so, sets relevant data 
	 * in the unknown note and returns true. If not, returns false.
	 * 
	 * @param uNote: the note to check
	 * @param index: the index of the note component to check
	 * @return true if the check succeeded and info was set, false otherwise
	 */
	private boolean checkAndSetArabic(UnknownNote uNote, int index) {
		if (isArabic(uNote.getComponents()[index])) {
			uNote.setArabicPos(index);
			uNote.setCountArabic(uNote.getCountArabic()+1);
			return true;
		}
		return false;
	}
	
	/**
	 * Checks whether the component at the given index of the given
	 * unknown note is 'Credits'. If so, sets relevant data 
	 * in the unknown note and returns true. If not, returns false.
	 * 
	 * @param uNote: the note to check
	 * @param index: the index of the note component to check
	 * @return true if the check succeeded and info was set, false otherwise
	 */
	private boolean checkAndSetCredits(UnknownNote uNote, int index) {
		if (uNote.getComponents()[index].equals("Credits")) {
			uNote.setCreditPos(index);
			uNote.setCountCredits(uNote.getCountCredits()+1);
			return true;
		}
		return false;
	}
	
	/**
	 * Checks whether the component at the given index of the given
	 * unknown note is 'is'. If so, sets relevant data 
	 * in the unknown note and returns true. If not, returns false.
	 * 
	 * @param uNote: the note to check
	 * @param index: the index of the note component to check
	 * @return true if the check succeeded and info was set, false otherwise
	 */
	private boolean checkAndSetIs(UnknownNote uNote, int index) {
		if (uNote.getComponents()[index].equals("is")) {
			uNote.setIsPos(index);
			uNote.setCountIs(uNote.getCountIs()+1);
			return true;
		}
		return false;
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
	
	public Map<Class, List<? extends ParsedNote>> getParsedNotes() {
		return pNotes;
	}
	
	public List<BaseIntergalNumDecl> getBaseIntergalNumDecs() {
		return baseIntergalNumDecs;
	}
	
	public List<CompIntergalNumDecl> getCompIntergalNumDecs() {
		return compIntergalNumDecs;
	}
	
	public List<CommodityDecl> getCommodityDecs() {
		return commodityDecs;
	}
	
	public List<Query> getQueries() {
		return queries;
	}
	
	public List<UnknownNote> getUnkownNotes() {
		return unknownNotes;
	}
}
