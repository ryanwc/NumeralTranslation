package app;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import notes.BaseIntergalNumDecl;
import notes.CommodityDecl;
import notes.CompIntergalNumDecl;
import notes.ParsedNote;
import notes.Query;
import utility.Ledger;
import utility.Translator;

/**
 * Process notes about intergalactic commodity markets; part of the 
 * Merchant's Guide to the Galaxy.
 * 
 * Intergalactic merchants fly all over the galaxy to sell common metals 
 * and dirt (dirt is worth a lot). Buying and selling all over the galaxy 
 * often requires conversion between numbers and units.
 * 
 * The numbers used for intergalactic transactions follows similar convention 
 * to roman numerals. Strict roman numeral conventions can be read about here:
 * https://en.wikipedia.org/wiki/Roman_numerals
 * 
 * Notably, roman numerals can only be written from 1-3999 under strict
 * interpretation. This application follows the strict interpretation,
 * so hopefully big intergalactic numerals are not needed!
 * 
 * Input to this program consists of a string, which is the path to a text 
 * file detailing notes on the conversion between intergalactic units 
 * and roman numerals.
 * 
 * @author ryanwilliamconnor
 * 
 */
public class NoteProcessor {

	List<String> rawNotes;
	Map<String, List<ParsedNote>> parsedNotes;
	NoteParser parser;
	Ledger ledger;
	QueryHandler qHandler;
	Translator translator;
	
	/**
	 * Begin a program that:
	 * 
	 * 1) reads the text file located at the path given as a command-line
	 * argument to this program
	 * 2) parses the contents of the file, extracting relevant information
	 * about the intergalactic commodity markets.
	 * 3) responds to any queries contained in the file by printing
	 * to standard output.
	 * 
	 * For expansion:
	 * Create interactive command-line session which the user can add new 
	 * information, query existing information, and perform other operations, 
	 * such as attempt to identify previously un-parsable notes. 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
    	
        String fileName = args[0];
        String note = null;
        ArrayList<String> notes = new ArrayList<String>();

		// get the file and build the list of notes
        try {
            FileReader fR = new FileReader(fileName);
            BufferedReader bR = new BufferedReader(fR);

            while((note = bR.readLine()) != null)
                notes.add(note);

            bR.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");            
        }
        catch(IOException ex) {               
            ex.printStackTrace();
        }
    	
		NoteProcessor processor = new NoteProcessor(notes);
		
		// parse the notes
        processor.parsedNotes = processor.parser.parseNotes(notes);
        
        List<ParsedNote> pNotes;
        
        // send base intergalactic numeral declarations to the translator
        pNotes = processor.parsedNotes.get("BaseIntergalNumDecl");
        if (pNotes != null ) {
	        for (ParsedNote pNote : pNotes) {
	        	if (pNote instanceof BaseIntergalNumDecl) {
	        		BaseIntergalNumDecl dec = ((BaseIntergalNumDecl)pNote);
	        		processor.translator.
	        			setIntergalToRomanValue(dec.getBaseIntergalNum(), 
	        					dec.getBaseRomanNum());
	        	}
	        }
        }
        
        // send composite intergal numeral declarations to the translator
        pNotes = processor.parsedNotes.get("CompIntergalNumDecl");
        if (pNotes != null) {
	        for (ParsedNote pNote : pNotes) {
	        	if (pNote instanceof CompIntergalNumDecl) {
	        		CompIntergalNumDecl dec = ((CompIntergalNumDecl)pNote);
	        		String[] bases = dec.getIntergalNum().split(" ");
	        		String romanNum = dec.getRomanNum();
	        		for (int i = 0; i < bases.length; i++)
	        			processor.translator.setIntergalToRomanValue(bases[i],
	        					romanNum.substring(i, i+1));
	        	}
	        }
        }
        
        // send commodity declarations to the ledger
        pNotes = processor.parsedNotes.get("CommodityDecl");
        if (pNotes != null) {
	        for (ParsedNote pNote : pNotes) {
	        	if (pNote instanceof CommodityDecl) {
	        		CommodityDecl cDec = ((CommodityDecl) pNote);
	        		// overwrite price
	            	processor.ledger.recordCommDecl(cDec, true);
	        	}
	        }
        }
        
        // handle any queries
        pNotes = processor.parsedNotes.get("Query");
        if (pNotes != null) {
	        for (ParsedNote pNote : pNotes) {
	        	if (pNote instanceof Query) {
	        		Query q = ((Query)pNote);
	        		processor.qHandler.answer(q);
	        	}
	        }
        }
	}
	
	/**
	 * Create a new NoteProcessor.
	 * 
	 * @param notes is a list of strings: the raw notes -- lines from a 
	 * text file -- to process.
	 * @throws IllegalArgumentException if notes is null
	 */
	public NoteProcessor(List<String> notes) {
		
		if (notes == null)
			throw new IllegalArgumentException("notes canont be null");
		
		this.rawNotes = notes;
		this.parser = new NoteParser();
		this.translator = new Translator();
		this.ledger = new Ledger(translator);
		this.qHandler = new QueryHandler(translator, ledger);
	}
}
