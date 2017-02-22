package app;

import java.io.File;

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

	File rawNotes;
	NoteParser parser;
	Ledger ledger;
	QueryHandler qHandler;
	Translator translator;
	
	/**
	 * Initiate an interactive session with the specified
	 * set of notes about the intergalactic commodity markets.
	 * 
	 * Parses notes, if given, and outputs stats, including
	 * answers to any queries in the notes.
	 * 
	 * Interactive command line session can be used to query 
	 * or to add new information.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		
	}
	
	public NoteProcessor(File notes) {
		
		this.rawNotes = notes;
		this.parser = new NoteParser();
		this.ledger = new Ledger();
		this.qHandler = new QueryHandler();
		this.translator = new Translator();
	}
	
	
}
