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
		test.testArabicNumToRomanNum();
		
		// unit testing for ledger
		
		// unit testing for processing entire set of notes
		
	}

    @Test
    public void testRomanNumToArabicNum() {
    	
    	// basic tests
    	assertEquals(1, Translator.romanNumToArabic("I"));
    	assertEquals(5, Translator.romanNumToArabic("V"));
    	assertEquals(10, Translator.romanNumToArabic("X"));
    	assertEquals(50, Translator.romanNumToArabic("L"));
    	assertEquals(100, Translator.romanNumToArabic("C"));
    	assertEquals(500, Translator.romanNumToArabic("D"));
    	assertEquals(1000, Translator.romanNumToArabic("M"));
    	
    	// easy addition and subtraction
    	assertEquals(2, Translator.romanNumToArabic("II"));
    	assertEquals(4, Translator.romanNumToArabic("IV"));
    	assertEquals(6, Translator.romanNumToArabic("VI"));
    	assertEquals(7, Translator.romanNumToArabic("VII")); 
    	assertEquals(9, Translator.romanNumToArabic("IX"));
    	assertEquals(11, Translator.romanNumToArabic("XI")); 
    	assertEquals(14, Translator.romanNumToArabic("XIV"));
    	assertEquals(15, Translator.romanNumToArabic("XV"));
    	
    	// more complex operations
    	assertEquals(2006, Translator.romanNumToArabic("MMVI"));
    	assertEquals(1944, Translator.romanNumToArabic("MCMXLIV"));
    	assertEquals(569, Translator.romanNumToArabic("DLXIX"));
    	assertEquals(3608, Translator.romanNumToArabic("MMMDCVIII"));
    	assertEquals(3927, Translator.romanNumToArabic("MMMCMXXVII"));
    	assertEquals(1656, Translator.romanNumToArabic("MDCLVI"));
    	assertEquals(884, Translator.romanNumToArabic("DCCCLXXXIV"));
    	assertEquals(3166, Translator.romanNumToArabic("MMMCLXVI"));
    	assertEquals(198, Translator.romanNumToArabic("CXCVIII"));
    	assertEquals(1720, Translator.romanNumToArabic("MDCCXX"));
    	assertEquals(3232, Translator.romanNumToArabic("MMMCCXXXII"));
    	assertEquals(3487, Translator.romanNumToArabic("MMMCDLXXXVII"));
    	
    	// invalid inputs
    	try {
    	    Translator.romanNumToArabic("MMMM");
    	    fail( "Did not throw too many repeated digits" );
    	} catch (IllegalArgumentException e) {
    		
    	}
    	try {
    	    Translator.romanNumToArabic("IIII");
    	    fail( "Did not throw too many repeated digits" );
    	} catch (IllegalArgumentException e) {
    		
    	}
    	try {
    	    Translator.romanNumToArabic("XXXX");
    	    fail( "Did not throw too many repeated digits" );
    	} catch (IllegalArgumentException e) {
    		
    	}
    	try {
    	    Translator.romanNumToArabic("CCCC");
    	    fail( "Did not throw too many repeated digits" );
    	} catch (IllegalArgumentException e) {
    		
    	}
    	try {
    	    Translator.romanNumToArabic("IXX");
    	    fail( "Did not throw base used after minuend" );
    	} catch (IllegalArgumentException e) {
    		
    	}
    	try {
    	    Translator.romanNumToArabic("IC");
    	    fail( "Did not throw illegal minuend" );
    	} catch (IllegalArgumentException e) {
    		
    	}
    }
    
    @Test
    public void testArabicNumToRomanNum() {
    	
    	// basic tests
    	assertEquals("I", Translator.arabicNumToRoman(1));
    	assertEquals("V", Translator.arabicNumToRoman(5));
    	assertEquals("X", Translator.arabicNumToRoman(10));
    	assertEquals("L", Translator.arabicNumToRoman(50));
    	assertEquals("C", Translator.arabicNumToRoman(100));
    	assertEquals("D", Translator.arabicNumToRoman(500));
    	assertEquals("M", Translator.arabicNumToRoman(1000));
    	
    	// a bit harder
    	assertEquals("II", Translator.arabicNumToRoman(2));
    	assertEquals("IV", Translator.arabicNumToRoman(4));
    	assertEquals("VI", Translator.arabicNumToRoman(6));
    	assertEquals("VII", Translator.arabicNumToRoman(7)); 
    	assertEquals("IX", Translator.arabicNumToRoman(9));
    	assertEquals("XI", Translator.arabicNumToRoman(11)); 
    	assertEquals("XIV", Translator.arabicNumToRoman(14));
    	assertEquals("XV", Translator.arabicNumToRoman(15));

    	// more complex operations
    	assertEquals("MMVI", Translator.arabicNumToRoman(2006));
    	assertEquals("MCMXLIV", Translator.arabicNumToRoman(1944));
    	assertEquals("DLXIX", Translator.arabicNumToRoman(569));
    	assertEquals("MMMDCVIII", Translator.arabicNumToRoman(3608));
    	assertEquals("MMMCMXXVII", Translator.arabicNumToRoman(3927));
    	assertEquals("MDCLVI", Translator.arabicNumToRoman(1656));
    	assertEquals("DCCCLXXXIV", Translator.arabicNumToRoman(884));
    	assertEquals("MMMCLXVI", Translator.arabicNumToRoman(3166));
    	assertEquals("CXCVIII", Translator.arabicNumToRoman(198));
    	assertEquals("MDCCXX", Translator.arabicNumToRoman(1720));
    	assertEquals("MMMCCXXXII", Translator.arabicNumToRoman(3232));
    	assertEquals("MMMCDLXXXVII", Translator.arabicNumToRoman(3487));
    }
}
