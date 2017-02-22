package app;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/**
 * Translate values from one unit system to another.
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
	
	// static final for roman numeral vals because we know them
	// and they should never change
	public static final Map<String, Integer> ROMAN_NUM_RANK;
	public static final int[] RANK_TO_VAL = {1, 5, 10, 50, 100, 500, 1000};
	public static final String[] NO_REPEATS = {"V", "L", "D"};
	public static final Set<String> NO_REPEAT_SET = 
			new HashSet<String>(Arrays.asList(NO_REPEATS));
	public static final String[] SUBTRACTORS = {"I", "X", "C"};
	public static final Set<String> SUBTRACTOR_SET = 
			new HashSet<String>(Arrays.asList(SUBTRACTORS));
	public static final Map<String, Set<String>> MINUENDS;
		
	// static final initialization for maps
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
		
		rankToIntergalNum[rank] = baseRomanNum;
		intergalNumRank.put(baseIntergalNum, rank);
		rankToRomanNum[rank] = baseIntergalNum;
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
	
	/*
	public int intergalNumToArabic(String intergalNum) {

	}
	
	public String arabicNumToIntergal(int arabicNum) {

	}
	
	public String intergalNumToRoman(String intergalNum) {
		
	}
	
	public String romanNumToIntergal(String romanNum) {
		
	}
	*/
	
	/**
	 * Convert a roman numeral to an arabic numeral.
	 * 
	 * Relies on helper method to recursively calculate the value
	 * of smaller pieces of the numeral.
	 * 
	 * @param romanNumeral is a string: the roman numeral to convert
	 * @throws IllegalArgumentException if the given string is less than
	 * length 1
	 * @return an int: the value of the given roman numeral
	 */
	public static int romanNumToArabic(String romanNumeral) {
		if (romanNumeral.length() < 1)
			throw new IllegalArgumentException("Roman numeral can't be empty");
		return romanNumToArabicRecurse(romanNumeral,
				new boolean[RANK_TO_VAL.length][2]);
	}

	/**
	 * Helper method to recursively convert a roman numeral to 
	 * an arabic numeral.
	 * 
	 * @param romanNumeral is a string: the roman numeral to convert
	 * @param total
	 * @param used
	 * @throws NullPointerException if the roman numeral is null, and
	 * IllegalArgumentException if the roman numeral is not well formed
	 * @return 
	 */
	private static int romanNumToArabicRecurse(String romanNumeral, 
			boolean[][] used) {
		
		if (romanNumeral == null)
			throw new NullPointerException("Roman numeral can't be null");
		
		// base case is empty string
		if (romanNumeral.length() < 1) return 0;
		
		// check if next portion of given romanNumeral satisfies rules
		// add to total and recurse if so, throw exception if not
		String firstBase = romanNumeral.substring(0, 1);
		
		if (!ROMAN_NUM_RANK.containsKey(firstBase)) {
			throw new IllegalArgumentException("Roman numeral is not "
					+ "well formed: '" + firstBase + "' is not a "
					+ "base roman numeral.");			
		}
		
		int numInRow = 1;
		while (numInRow < romanNumeral.length() &&
		       romanNumeral.charAt(numInRow) == firstBase.charAt(0))
			numInRow++;
		
		// check against roman numeral rules
		
		// check repeat limits
		if (numInRow > 3)
			throw new IllegalArgumentException("Roman numeral is not well "
					+ "formed: '" + firstBase + "' appears more than three"
					+ " times in succession.");
		
		if (NO_REPEAT_SET.contains(firstBase)) {
			if (numInRow > 1)
				throw new IllegalArgumentException("Roman numeral is not "
						+ "well formed: '" + firstBase + "' appears "
						+ "more than once.");
		}
		
		// check if a subtraction
		int firstBaseRank = ROMAN_NUM_RANK.get(firstBase);
		int firstBaseVal = RANK_TO_VAL[firstBaseRank];
		String secondBase = null;
		int secondBaseRank = -1;
		int secondBaseVal = -1;
		if (SUBTRACTOR_SET.contains(firstBase)) {
			if (numInRow == 1 && romanNumeral.length() > 1) {
				secondBase = romanNumeral.substring(1, 2);
				secondBaseRank = ROMAN_NUM_RANK.get(secondBase);
				secondBaseVal = RANK_TO_VAL[secondBaseRank];
				if (secondBaseRank < firstBaseRank)
					secondBase = null; // not a subtraction
			}
		}
		
		// now check against subtraction rules (if necessary) and
		// any previously processed numerals
		// then, set the values for the next recursive call.
		String subRomanNum = "";
		int thisValue = 0;
		if (secondBase != null) {
			
			if (used[firstBaseRank][1])
				throw new IllegalArgumentException("Roman numeral is "
						+ "not well formed: '" + firstBase + "' was "
						+ "already used as a subtractor.");
			
			if (used[secondBaseRank][1])
				throw new IllegalArgumentException("Roman numeral is "
						+ "not well formed: '" + secondBase + "' was "
						+ "already used as a minuend.");
			
			if (MINUENDS.get(firstBase).contains(secondBase)) {
				
				thisValue = secondBaseVal-firstBaseVal;
				subRomanNum = romanNumeral.substring(numInRow+1);
				
				// subtractor and minuend can't be used in another subtraction
				used[firstBaseRank][1] = true;
				used[secondBaseRank][1] = true;
				// a minuend can't be used on its own after used as minuend
				used[secondBaseRank][0] = true; 
			}
			else {
				throw new IllegalArgumentException("Roman numeral is "
						+ "not well formed: '" + firstBase + "' can only be"
						+ " subtracted from " + MINUENDS.get(firstBase));
			}
		}
		else {
			
			if (used[firstBaseRank][0])
				throw new IllegalArgumentException("Roman numeral is not "
						+ "well formed: '" + firstBase + "' can't be used "
						+ "starting at " + romanNumeral);
			
			subRomanNum = romanNumeral.substring(numInRow);	
			thisValue = numInRow*firstBaseVal;
			used[firstBaseRank][0] = true;
		}

		return thisValue+romanNumToArabicRecurse(subRomanNum, used);
	}
	
	/**
	 * Convert an arabic numeral to a roman numeral.
	 * 
	 * @param arabicNumeral is an int: the arabic numeral to convert 
	 * @throws IllegalArgumentException if the given int is less than 1
	 * @return a string representing the given arabic numeral 
	 * as a roman numeral
	 */
	public static String arabicNumToRoman(int arabicNumeral) {
		
		 // A number written in Arabic numerals can be broken into digits. 
		 // For example, 1903 is composed of 1, 9, 0, and 3. To write the Roman 
		 // numeral, each of the non-zero digits should be treated separately. 
		 // In the above example, 1,000 = M, 900 = CM, and 3 = III. 
		 // Therefore, 1903 = MCMIII.
		return "";
	}
}
