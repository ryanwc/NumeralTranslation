package test;

import static org.junit.Assert.*;
import org.junit.Test;

import app.Translator;

/**
 * Unit test processing notes about intergalactic commodity markets.
 * 
 * @author ryanwilliamconnor
 *
 */
public class NoteProcessorTester {
	
	public static void main(String[] args) {
		
		NoteProcessorTester test = new NoteProcessorTester();
		
		// unit testing for parsing
		
		// unit testing for queries
		
		// unit testing for translation
		test.testRomanNumToArabicNum();
		
		// unit testing for ledger
		
		// unit testing for processing entire set of notes
		
	}

    @Test
    public void testRomanNumToArabicNum() {
    	
    	assertEquals(1, Translator.romanNumToArabic("I"));
    	assertEquals(5, Translator.romanNumToArabic("V"));
    	assertEquals(10, Translator.romanNumToArabic("X"));
    	assertEquals(50, Translator.romanNumToArabic("L"));
    	assertEquals(100, Translator.romanNumToArabic("C"));
    	assertEquals(500, Translator.romanNumToArabic("D"));
    	assertEquals(1000, Translator.romanNumToArabic("M"));
    	
    	assertEquals(2, Translator.romanNumToArabic("II"));
    	
    	assertEquals(4, Translator.romanNumToArabic("IV"));
    	assertEquals(6, Translator.romanNumToArabic("VI"));
    	assertEquals(7, Translator.romanNumToArabic("VII")); 
    	
    	assertEquals(9, Translator.romanNumToArabic("IX"));
    	assertEquals(11, Translator.romanNumToArabic("XI")); 
    	
    	assertEquals(14, Translator.romanNumToArabic("XIV"));
    	assertEquals(15, Translator.romanNumToArabic("XV"));
    	
    	assertEquals(2006, Translator.romanNumToArabic("MMVI"));
    	assertEquals(1944, Translator.romanNumToArabic("MCMXLIV"));
    }
}
