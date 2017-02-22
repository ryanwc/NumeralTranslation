package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.NoteParser.ParsedNote;

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
	Map<Integer, List<ParsedNote>> parsedNotes;
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
        for (ParsedNote baseDec : processor.parsedNotes.get(NoteParser.BASE_NUM_DECL)) {
        	String[] components = baseDec.components;
        	processor.translator.setIntergalToRomanValue(components[0], components[2]);
        }
        
        /*
        String[] toIg = processor.translator.getRankToIntergalNum();
        String[] toR = processor.translator.getRankToRomanNum();
        for (int i = 0; i < toIg.length; i++) {
        	System.out.println(toIg[i] + ", " + toR[i]);
        }
        */
        
        // send commodity declarations to the ledger
        for (ParsedNote commDec : processor.parsedNotes.get(NoteParser.COMMODITY_DECL))
        	processor.ledger.recordCommDecl(commDec);
        
        // output stats, for fun
        /*
        String[] toIg = processor.translator.getRankToIntergalNum();
        String[] toR = processor.translator.getRankToRomanNum();
        for (int i = 0; i < toIg.length; i++) {
        	System.out.println(toIg[i] + ", " + toR[i]);
        }
        */
        
        // handle queries
        for (ParsedNote query : processor.parsedNotes.get(NoteParser.QUERY))
        	processor.qHandler.answer(query);
	}
	
	public NoteProcessor(List<String> notes) {
		
		this.rawNotes = notes;
		this.parser = new NoteParser();
		this.ledger = new Ledger();
		this.qHandler = new QueryHandler();
		this.translator = new Translator();
	}
}
