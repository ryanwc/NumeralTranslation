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
	
	// simulate a bi-directional hashmap with two arrays of strings
	// and two maps of {string: array index}
	private String[] rankToIntergalNum;
	private Map<String, Integer> intergalNumRank;
	private String[] rankToRomanNum;
	public static final Map<String, Integer> ROMAN_NUM_RANK;
	
	private int numPairs;
			
	// allow easy conversion from base roman numeral to rank
	static {
        Map<String, Integer> map = new HashMap<String, Integer>();
        String[] nums = new String[] {"I", "V", "X", "L", "C", "D", "M"};
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], i);
        }
        ROMAN_NUM_RANK = Collections.unmodifiableMap(map);
    }
	
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
	
	public int intergalNumToArabic(String intergalNum) {

	}
	
	public String arabicNumToIntergal(int arabicNum) {

	}
	
	public String intergalNumToRoman(String intergalNum) {
		
	}
	
	public String romanNumToIntergal(String romanNum) {
		
	}
	
	/**
	 * Convert a roman numeral to arabic numeral.
	 * 
	 * @param romanNumeral is a string: the roman numeral to convert
	 * @throws IllegalArgumentException if the given string is not
	 * a well-formed roman numeral
	 * @return an int representing the given roman numeral
	 */
	public static int romanNumToArabic(String romanNumeral) {
		
		/*
		 * Roman numerals are based on seven symbols:
		 * Symbol	I	V	X	L	C	D	M
		 * Value	1	5	10	50	100	500	1,000
		 * 
		 * Numbers are formed by combining symbols together and adding the values. 
		 * For example, MMVI is 1000 + 1000 + 5 + 1 = 2006. 
		 * Generally, symbols are placed in order of value, starting with the 
		 * largest values. When smaller values precede larger values, 
		 * the smaller values are subtracted from the larger values, and the 
		 * result is added to the total. 
		 * For example MCMXLIV = 1000 + (1000 - 100) + (50 - 10) + (5 - 1) = 1944.
		 * 
		 * Rules:
		 * 1. The symbols "I", "X", "C", and "M" can be repeated three times in 
		 * succession, but no more. (They may appear four times if the third and 
		 * fourth are separated by a smaller value, such as XXXIX.) "D", "L", and 
		 * "V" can never be repeated.
		 * 2. "I" can be subtracted from "V" and "X" only. "X" can be subtracted 
		 * from "L" and "C" only. "C" can be subtracted from "D" and "M" only. "V", 
		 * "L", and "D" can never be subtracted.
		 * 3. Only one small-value symbol may be subtracted from any 
		 * large-value symbol.
		 * 4. A number written in Arabic numerals can be broken into digits. 
		 * For example, 1903 is composed of 1, 9, 0, and 3. To write the Roman 
		 * numeral, each of the non-zero digits should be treated separately. 
		 * In the above example, 1,000 = M, 900 = CM, and 3 = III. 
		 * Therefore, 1903 = MCMIII.
		 */
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
		
	}
}
