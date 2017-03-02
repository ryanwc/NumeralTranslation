package utility;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/**
 * Translate values from one unit system to another.
 * 
 * Handles Roman <-> arabic numeral conversion statically.
 * Needs to learn Roman <-> intergalactic numerals and 
 * arabic <-> intergalactic numerals.
 * 
 * @author ryanwilliamconnor
 *
 */
public class Translator {
	
	// keep intergalNum-romanNum translations as a 
	// simulated bi-directional hashmap (two arrays of numStrings
	// and two maps of {numString: Integer rank/arrayIndex})
	private String[] rankToIntergalNum;
	private Map<String, Integer> intergalNumRank;
	private String[] rankToRomanNum;
	private int numPairs;
	
	// static final for roman numeral vals
	public static final Map<String, Integer> ROMAN_NUM_RANK;
	public static final int[] RANK_TO_VAL = {1, 5, 10, 50, 100, 500, 1000};
	public static final String[] NO_REPEATS = {"V", "L", "D"};
	public static final Set<String> NO_REPEAT_SET = 
			new HashSet<String>(Arrays.asList(NO_REPEATS));
	public static final String[] SUBTRACTORS = {"I", "X", "C"};
	public static final Set<String> SUBTRACTOR_SET = 
			new HashSet<String>(Arrays.asList(SUBTRACTORS));
	public static final Map<String, Set<String>> MINUENDS;
		
	// initialization for static final maps
	static {
        Map<String, Integer> numMap = new HashMap<String, Integer>();
        String[] nums = {"I", "V", "X", "L", "C", "D", "M"};
        for (int i = 0; i < nums.length; i++)
            numMap.put(nums[i], i);
        ROMAN_NUM_RANK = Collections.unmodifiableMap(numMap);
        
        Map<String, Set<String>> minuends = 
        		new HashMap<String, Set<String>>();
        Set<String> i = new HashSet<String>();
        Set<String> x = new HashSet<String>();
        Set<String> c = new HashSet<String>();
        
        i.add("V");
        i.add("X");
        x.add("L");
        x.add("C");
        c.add("D");
        c.add("M");
        
        minuends.put("I", i);
        minuends.put("X", x);
        minuends.put("C", c);
        MINUENDS = Collections.unmodifiableMap(minuends);
    }
	
	/**
	 * Create a new Translator with no knowledge of any
	 * intergalactic to roman numeral conversions.
	 */
	public Translator() {
		this.rankToIntergalNum = new String[ROMAN_NUM_RANK.size()];
		this.intergalNumRank = new HashMap<String, Integer>();
		this.rankToRomanNum = new String[ROMAN_NUM_RANK.size()];
		this.numPairs = 0;
	}
	
	/**
	 * Set the value of an intergalactic unit to a base roman numeral.
	 * 
	 * Allows only one-to-one mapping.
	 * 
	 * @param baseIntergalNum is a string representing the intergalactic unit
	 * @param baseRomanNum is a string representing the base roman numeral
	 * @throws IllegalArgumentException if base roman numeral is not valid or 
	 * if base intergalactic number is the empty string, and
	 * NullPointerException if either argument is null
	 */
	public void setIntergalToRomanValue(String baseIntergalNum, 
			String baseRomanNum) {
		
		if (baseIntergalNum == null)
			throw new NullPointerException("Intergal num can't be null");
		if (baseIntergalNum.length() < 1) 
			throw new IllegalArgumentException("Intergal num can't be empty");
		if (baseRomanNum == null)
			throw new NullPointerException("Base roman num can't be null");
		
		Integer rank = ROMAN_NUM_RANK.get(baseRomanNum);
		
		if (rank == null)
			throw new IllegalArgumentException("Base roman numeral invalid");
		
		String previous = rankToIntergalNum[rank];
		if (previous != null) decoupleIntergalNum(previous, rank);
		
		rankToIntergalNum[rank] = baseIntergalNum;
		intergalNumRank.put(baseIntergalNum, rank);
		rankToRomanNum[rank] = baseRomanNum;
		numPairs++;
	}
	
	/**
	 * Determine whether the translator has perfect info related to
	 * intergalNum-romanNum pairings.
	 * 
	 * @return true if each romanNum has a paired intergalNum,
	 * false otherwise
	 */
	public boolean completeInfo() {
		return numPairs == rankToRomanNum.length;
	}
	
	/**
	 * Adds an intergal number to the map of known intergal numbers,
	 * but does not assign a value.
	 * 
	 * Useful for situations where you have a note like "glob prok 
	 * Silver is 68 Credits", but no notes that contain a declaration
	 * for 'prok'.
	 * 
	 * @param intergalNum to add
	 * @throws IllegalArgumentException if intergalNum is empty or null 
	 */
	public void putUnknownIntergalNum(String intergalNum) {
		
		if (intergalNum == null)
			throw new NullPointerException("Intergal num can't be null");
		if (intergalNum.length() < 1) 
			throw new IllegalArgumentException("Intergal num can't be empty");
			
		if (intergalNumRank.containsKey(intergalNum)) 
			throw new IllegalArgumentException(intergalNum + 
					" already known");
		
		intergalNumRank.put(intergalNum, null);
	}
	
	/**
	 * Remove an intergalNum-romanNum pairing, but leave intergalNum
	 * in the translator for further consideration.
	 * 
	 * Useful for situations where a num was previously thought known,
	 * but new information has confused the matter.
	 * 
	 * @param intergalNum
	 */
	public void decoupleIntergalNum(String intergalNum, int rank) {
		
		rankToIntergalNum[rank] = null;
		rankToRomanNum[rank] = null;
		intergalNumRank.put(intergalNum, null);
		numPairs--;
	}
	
	/**
	 * Convert an intergal numeral to an arabic numeral.
	 * 
	 * @param intergalNum is a string: the intergal numeral 
	 * to convert
	 * @return an int: the arabic numeral representing the
	 * given intergal numeral.
	 */
	public int intergalNumToArabic(String intergalNum) {

		String romanNum = intergalNumToRoman(intergalNum);
		return romanNumToArabic(romanNum);
	}
	
	/**
	 * Convert an arabic numeral to a Roman numeral.
	 * 
	 * @param arabicNum is an int, the arabic numeral to conver
	 * @return a string: the intergal numeral representing the given
	 * arabic numeral.
	 */
	public String arabicNumToIntergal(int arabicNum) {

		String romanNum = arabicNumToRoman(arabicNum);
		return romanNumToIntergal(romanNum);
	}
	
	/**
	 * Convert a number from intergal numerals to roman numerals.
	 * 
	 * WARNING: Does not guarantee the intergal numeral is well formed,
	 * which means the returned roman numeral may not be well formed.
	 * To check, can run the returned string through translation
	 * from roman numeral to arabic numeral.
	 * 
	 * @param intergalNum is a string: the intergal numeral to convert
	 * @throws an IllegalArgumentException if a word within the given
	 * intergal numeral is not a base intergal numeral or if the translator 
	 * does not know the corresponding base roman numeral for a given base 
	 * intergal numeral.
	 * @return a string: the roman numeral corresponding to the 
	 * given intergal numeral.
	 */
	public String intergalNumToRoman(String intergalNum) {
		
		String romanNum = "";
		String[] baseIGnums = intergalNum.split(" ");
		
		Integer rank;
		for (int i = 0; i < baseIGnums.length; i++) {
			rank = intergalNumRank.get(baseIGnums[i]);
			if (rank == null) 
				throw new IllegalArgumentException("Intergal numeral to Roman"
						+ " numeral translation failed: the Roman numeral for"
						+ baseIGnums[i] + " is not recorded.");
			romanNum += rankToRomanNum[rank];
		}
		
		return romanNum;
	}
	
	/**
	 * Convert a number from roman numerals to intergal numerals.
	 * 
	 * WARNING: Does not guarantee the roman is well formed,
	 * which means the returned intergal numeral may not be well formed.
	 * To check, can run the roman numeral first through translation
	 * to arabic numeral.
	 * 
	 * @param romanNum is a string: the roman numeral to convert
	 * @throws an IllegalArgumentException if a character within the given
	 * roman numeral is not a base roman numeral or if the translator 
	 * does not know the corresponding intergal numeral for a given base 
	 * roman numeral.
	 * @return a string: the intergal numeral corresponding to the 
	 * given roman numeral.
	 */
	public String romanNumToIntergal(String romanNum) {
	
		String intergalNum = "";
		
		Integer rank;
		String baseR;
		for (int i = 0; i < romanNum.length(); i++) {
			baseR = romanNum.substring(i, i+1);
			rank = ROMAN_NUM_RANK.get(baseR);
			if (rank == null) 
				throw new IllegalArgumentException("Roman numeral to intergal"
						+ " numeral translation failed: '" + baseR + " is not"
						+ " a base Roman numeral");
			intergalNum += rankToIntergalNum[rank];
		}
		
		return intergalNum;
	}
	
	/**
	 * Convert a roman numeral to an arabic numeral.
	 * 
	 * Relies on helper method to recursively calculate the value
	 * of smaller pieces of the numeral.
	 * 
	 * @param romanNumeral is a string: the roman numeral to convert
	 * @throws IllegalArgumentException if the given string is less than
	 * length 1 or if each character is not a base roman numeral
	 * @return an int: the value of the given roman numeral
	 */
	public static int romanNumToArabic(String romanNumeral) {
		if (romanNumeral.length() < 1)
			throw new IllegalArgumentException("Roman numeral can't be empty");
		// sanity check each char is a base roman numeral before starting
		return romanNumToArabicRecurse(romanNumeral,
				new boolean[RANK_TO_VAL.length], 
				new boolean[RANK_TO_VAL.length]);
	}

	/**
	 * Helper method to recursively convert a roman numeral to 
	 * an arabic numeral.
	 * 
	 * @param romanNumeral is a string: the roman numeral to convert
	 * @param wasInAdd: a boolean where wasInAdd[rank] is true if 
	 * the roman numeral of that rank has been used in an addition
	 * @param wasInSubtract: a boolean where wasInSubtract[rank] is true if 
	 * the roman numeral of that rank has been used in a subtraction
	 * @throws NullPointerException if the roman numeral is null, and
	 * IllegalArgumentException if the roman numeral is not well formed
	 * @return an int: the arabic numeral representing the next legal
	 * portion of the given roman numeral.
	 */
	private static int romanNumToArabicRecurse(String romanNumeral, 
			boolean[] wasInAdd, boolean[] wasInSubtract) {
		
		if (romanNumeral == null)
			throw new NullPointerException("Roman numeral can't be null");
		
		// base case is empty string
		if (romanNumeral.length() < 1) return 0;
		
		// plan: check if next portion of given romanNumeral satisfies rules
		// add to total and recurse if so, throw exception if not
		String initBase = romanNumeral.substring(0, 1);
		
		checkBaseRomanNumeral(initBase);
		
		// get number of initial numeral in a row
		int numInRow = 1;
		while (numInRow < romanNumeral.length() &&
		       romanNumeral.charAt(numInRow) == initBase.charAt(0))
			numInRow++;
		
		checkRepeatRules(numInRow, initBase);
		
		// determine whether a subtraction or addition
		String minuend = getMinuend(romanNumeral);
		
		// check against appropriate math operation rules and do the operation
		String recurseNum = "";
		int thisValue = 0;
		int initBaseRank = ROMAN_NUM_RANK.get(initBase);
		
		if (minuend != null) {
			checkSubtractionRules(minuend, initBase, wasInSubtract);
			int minuendRank = ROMAN_NUM_RANK.get(minuend);
			thisValue = RANK_TO_VAL[minuendRank] - RANK_TO_VAL[initBaseRank];
			recurseNum = romanNumeral.substring(numInRow+1);
			wasInSubtract[initBaseRank] = true;
			wasInSubtract[minuendRank] = true;
			wasInAdd[minuendRank] = true;
		}
		else {
			checkAdditionRules(initBase, wasInAdd);
			recurseNum = romanNumeral.substring(numInRow);	
			thisValue = numInRow * RANK_TO_VAL[initBaseRank];
			wasInAdd[initBaseRank] = true;
		}

		return thisValue+romanNumToArabicRecurse(recurseNum, 
												 wasInAdd, 
												 wasInSubtract);
	}
	
	/**
	 * Throw exception if the given base numeral violates addition rules
	 * given previous usage in a roman numeral string. 
	 * 
	 * @param baseNum is the 
	 * @param wasInAdd: a boolean where wasInAdd[rank] is true if 
	 * the roman numeral of that rank has been used in an addition
	 */
	private static void checkAdditionRules(String baseNum, boolean[] wasInAdd){
		
		if (wasInAdd[ROMAN_NUM_RANK.get(baseNum)])
			throw new IllegalArgumentException("Roman numeral is not "
					+ "well formed: '" + baseNum + "' appears too many times");
	}
	
	/**
	 * Throw exception if the minuend and subtractor violate roman numeral
	 * subtraction rules given previous usage in a roman numeral string.
	 * 
	 * @param minuend
	 * @param subtractor
	 * @param wasInSubtract: a boolean where wasInSubtract[rank] is true if 
	 * the roman numeral of that rank has been used in a subtraction
	 * an addition. 
	 */
	private static void checkSubtractionRules(String minuend, 
			String subtractor, boolean[] wasInSubtract) {
		
		if (wasInSubtract[ROMAN_NUM_RANK.get(subtractor)])
			throw new IllegalArgumentException("Roman numeral is "
					+ "not well formed: '" + subtractor + "' was "
					+ "already used as a subtractor.");
		
		if (wasInSubtract[ROMAN_NUM_RANK.get(minuend)])
			throw new IllegalArgumentException("Roman numeral is "
					+ "not well formed: '" + minuend + "' was "
					+ "already used as a minuend.");
		
		if (!MINUENDS.get(subtractor).contains(minuend))
			throw new IllegalArgumentException("Roman numeral is "
					+ "not well formed: '" + subtractor + "' can only be"
					+ " subtracted from " + MINUENDS.get(subtractor));
	}
	
	/**
	 * Return the minuend if the initial two base roman numerals of the given
	 * numeral string makes a subtraction, null if not.

	 * @param romanNumeralStr
	 * @return a string: the minuend of the subtraction operation if there is 
	 * a subtraction operation at the start of the given roman numeral
	 * string, null otherwise
	 */
	private static String getMinuend(String romanNumeralStr) {
		
		if (romanNumeralStr.length() < 2) return null;
		
		String subtractor = romanNumeralStr.substring(0, 1);
		
		if (!SUBTRACTOR_SET.contains(subtractor)) return null;
				
		String minuend = romanNumeralStr.substring(1, 2);
		checkBaseRomanNumeral(minuend);

		if (ROMAN_NUM_RANK.get(minuend) <= ROMAN_NUM_RANK.get(subtractor))
			return null; // minuend must be greater than subtractor
		
		return minuend;
	}
	
	/**
	 * Throw exception if the given numeral is a base Roman numeral.
	 * 
	 * @param numeral
	 * @throws an IllegalArgumentException if the given string is not
	 * a base Roman numeral
	 */
	private static void checkBaseRomanNumeral(String numeral) {
		if (!ROMAN_NUM_RANK.containsKey(numeral)) {
			throw new IllegalArgumentException("Roman numeral is not "
					+ "well formed: '" + numeral + "' is not a "
					+ "base roman numeral.");			
		}
	}
	
	/**
	 * Throw exception if the given roman numeral repeats 
	 * too many times in a row.
	 * 
	 * @param numInRow is an int: the number of times the given numeral repeats
	 * @param numeral is a String: the base roman numeral to check
	 * @throws IllegalArgumentException if the roman numeral repeats too
	 * many times
	 */
	private static void checkRepeatRules(int numInRow, String numeral) {
		
		if (NO_REPEAT_SET.contains(numeral)) {
			if (numInRow > 1)
				throw new IllegalArgumentException("Roman numeral is not "
						+ "well formed: '" + numeral + "' appears "
						+ "more than once.");
		}
		
		if (numInRow > 3)
			throw new IllegalArgumentException("Roman numeral is not well "
					+ "formed: '" + numeral + "' appears more than three"
					+ " times in succession.");
	}
	
	/**
	 * Convert an arabic numeral to a roman numeral.
	 * 
	 * @param arabicNumeral is an int: the arabic numeral to convert 
	 * @throws IllegalArgumentException if the given int is less than 1
	 * or greater than 3999
	 * @return a string representing the given arabic numeral 
	 * as a roman numeral
	 */
	public static String arabicNumToRoman(int arabicNumeral) {
		
		if (arabicNumeral < 1 || arabicNumeral > 3999)
			throw new IllegalArgumentException("Given int '" + arabicNumeral
					+ " is outside valid range. Under strict interpretation, "
					+ "Roman numerals cannot represent arabic numerals less "
					+ "than 1 or greater than 3999.");
		
		String romanNum = "";
		int thousands = arabicNumeral / 1000;
		int hundreds = (arabicNumeral % 1000) / 100;
		int tens = (arabicNumeral % 100) / 10;
		int ones = arabicNumeral % 10;
		
		for (int i = 0; i < thousands; i++)
			romanNum += "M";
		if (hundreds > 0) 
			romanNum += addRomanPortion(hundreds, 3);
		if (tens > 0)
			romanNum += addRomanPortion(tens, 2);
		if (ones > 0)
			romanNum += addRomanPortion(ones, 1);
		
		return romanNum;
	}
	
	/**
	 * Helper method for converting an arabic numeral to roman
	 * numeral by digit.
	 * 
	 * @param value is a positive int between 1 and 9, inclusive
	 * @param place is an int: a power of ten, either 1, 2, or 3
	 * @return the roman numeral that matches value and place
	 */
	private static String addRomanPortion(int arabicNum, int place) {

		String portion = "";
		String low = "", mid = "", high = "";
		
		// get the right set of numerals
		if (place == 3) {
			low = "C";
			mid = "D";
			high = "M";
		}
		else if (place == 2) {
			low = "X";
			mid = "L";
			high = "C";
		}
		else {
			low = "I";
			mid = "V";
			high = "X";
		}
		
		// attach 
		if (arabicNum < 4) {
			for (int i = 0; i < arabicNum; i++)
				portion += low;
		}
		else if (arabicNum == 4) {
			portion += low;
			portion += mid;
		}
		else if (arabicNum == 5) {
			portion += mid;
		}
		else if (arabicNum < 9) {
			portion += mid;
			for (int i = 5; i < arabicNum; i++)
				portion += low;	
		}
		else {
			portion += low;
			portion += high;
		}
		
		return portion;
	}
	
	public String[] getRankToIntergalNum() {
		return rankToIntergalNum;
	}
	
	public Map<String, Integer> getIntergalNumRank() {
		return intergalNumRank;
	}
	
	public String[] getRankToRomanNum() {
		return rankToRomanNum;
	}
	
	public int getNumPairs() {
		return numPairs;
	}
}
