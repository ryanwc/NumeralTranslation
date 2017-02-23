package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Merchant's Guide to the Galaxy.
 * fly all over the galaxy to sell common metals and dirt 
 * (which apparently is worth a lot).
 * Buying and selling over the galaxy requires you to convert numbers 
 * and units, and you decided to write a program to help you.
 * 
 * The numbers used for intergalactic transactions follows similar convention 
 * to the roman numerals and you have painstakingly collected the appropriate 
 * translation between them.
 * 
 * Input to your program consists of lines of text detailing your notes on 
 * the conversion between intergalactic units and roman numerals.
 * 
 * You are expected to handle invalid queries appropriately.
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
	 * Initiate an interactive session with the specified
	 * set of notes about the intergalactic commodity markets.
	 * 
	 * Parses text file given as command line argument, 
	 * and outputs stats to standard output, 
	 * including answers to any queries in the notes.
	 * 
	 * Interactive command line session can be used to query 
	 * or to add new information.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
    	
        String fileName = args[0];
        String note = null;
        ArrayList<String> notes = new ArrayList<String>();

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
		
        processor.parsedNotes = processor.parser.parseNotes(notes);
        
        /*
    	for (Integer type : parsedNotes.keySet()) {
    		for (ParsedNote pNote: parsedNotes.get(type))
    			System.out.println(pNote.toString());
    	}
    	*/
        
        // send base intergal declarations to the translator
        for (ParsedNote pNote : processor.parsedNotes.
        							get("BaseIntergalNumDecl")) {
        	if (pNote instanceof BaseIntergalNumDecl) {
        		BaseIntergalNumDecl dec = ((BaseIntergalNumDecl)pNote);
        		processor.translator.
        			setIntergalToRomanValue(dec.getBaseIntergalNum(), 
        					dec.getBaseRomanNum());
        	}
        }
        
        /*
        String[] toIg = processor.translator.getRankToIntergalNum();
        String[] toR = processor.translator.getRankToRomanNum();
        for (int i = 0; i < toIg.length; i++) {
        	System.out.println(toIg[i] + ", " + toR[i]);
        }
        */
        
        // send commodity declarations to the ledger
        for (ParsedNote pNote : processor.parsedNotes.get("CommodityDecl")) {
        	if (pNote instanceof CommodityDecl) {
        		CommodityDecl cDec = ((CommodityDecl) pNote);
            	processor.ledger.recordCommDecl(cDec, true); // overwrite price
        	}
        }
        
        // output stats, for fun
        /*
        String[] toIg = processor.translator.getRankToIntergalNum();
        String[] toR = processor.translator.getRankToRomanNum();
        for (int i = 0; i < toIg.length; i++) {
        	System.out.println(toIg[i] + ", " + toR[i]);
        }
        */
        
        // handle queries
        for (ParsedNote pNote : processor.parsedNotes.get("Query"))
        	if (pNote instanceof Query) {
        		Query q = ((Query)pNote);
        		processor.qHandler.answer(q);
        	}
	}
	
	/**
	 * Create a new NoteProcessor.
	 * 
	 * @param notes is a list of strings, which is the set of raw notes
	 * to process.
	 */
	public NoteProcessor(List<String> notes) {
		
		this.rawNotes = notes;
		this.parser = new NoteParser();
		this.translator = new Translator();
		this.ledger = new Ledger(translator);
		this.qHandler = new QueryHandler(translator, ledger);
	}
}
