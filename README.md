# Overview

This program translates unknown numerals that follow Roman numeral conventions into arabic numerals. For example, if input text declares "foo" is "X" and "bar" is "I", the program determines "foo foo bar" is "21", "bar foo" is "9", and "bar bar foo" is an ill-formed numeral.

The backstory is this: The user is a merchant that travels across the galaxy selling various commodities. The user of this program -- the merchant -- uses Arabic numerals and money called Credits, but the rest of the galaxy uses intergalactic numerals.

This program takes a text file as input. Each line of the text file is either 1) a note about intergalactic numerals or commodity prices, or 2) a query. Queries are questions about intergalactic numerals or commodities (e.g., "how much is foo foo bar ?" or "how many Credits is foo bar Silver?"). The program reads the file and responds to queries through standard output.

# Assumptions about the Numeral System and Input Format

1. Intergalactic numerals follow “strict” Roman numeral rules, including that they can only represent arabic numerals greater than 0 and less than 4000.
2. Queries have a “?” at the end, and this “?” is separated from the last word by whitespace.
4. The input to the program is a string which is the path to a text file, where each line of the file is a note about the markets, with each line formatted exactly as specified in the next assumption.
5. Notes can take one of five forms:
	1. Base intergalactic numeral declaration. Statement about which single intergalactic numeral corresponds to which Roman numeral. Takes the form '[base intergal numeral] is [base roman numeral]’.
	2. Composite intergalactic numeral declaration. Statement about which string of base intergalactic numerals corresponds to which string of Roman numerals. Takes the form ‘[intergal num longer than 1 word] is [roman num longer than 1 char]’.
	3. Commodity declaration. Specifies the number of units of a commodity in intergal numerals that is worth an arabic numeral amount of Credits. Takes the form '[intergal numeral] [commodity] is [arabic numeral] Credits'.
	4. Query. Contains one '?' -- the very last character on the line -- and is separated from the previous word by a space. Queries come in two forms:
		1. 'many': 'how many Credits is [intergal numeral] [commodity] ?'
		2. 'much': 'how much is [intergal numeral] ?'
	5. Unknown. These are notes that do not conform to types 1-4. They are kept for later analysis and/or classification. For example, a previously unanswerable Query might be answerable when a new declaration is added.

# Design Notes

- Main goals:
	1. Translate a unknown numerals that follow roman numeral conventions to arabic numerals.
	2. Extract and retain as much information as possible about a given set of notes such that the application can easily be extended to support interactive adjustments, exploration, and deductions from incomplete information.
	3. Provide classes that can be re-used by other applications (e.g., Ledger, Translator).
- Hierarchy and Class/Package Design Considerations:
	1. Delegate discrete pieces of work from main class NoteProcessor to dedicated worker classes with one job (NoteParser, Ledger, Translator, and QueryHandler) to make code manageable and benefit other applications.
	2. Make a specific note easy to identify, hard to misuse, and share appropriate functionality/fields by extending classes Query, BaseIntergalNumDecl, CompIntergalNumDecl, CommodityDecl, and UnknownNote from Declaration and/or ParsedNote, as appropriate. 
	3. Make ParsedNote and Declaration abstract to provide common functionality to children while not allowing instantiation.
	4. Provide inner class to Ledger so it can bookkeep with a tuple of prices.
	5. Design UnknownNote to hold all interesting data about a note. Later, can convert to other types (if possible).
	6. Sort classes into packages based on purpose — app for the main purpose of the application, notes for note data, and utility for workhorses that look useful for other applications.

# For Future Expansion

Currently, the program answers queries where all of the relevant intergalactic numerals are provided a corresponding Roman numeral in the given notes. An expansion could be making deductions. For example, imagine a scenario where the user (merchant) has declarative statements for all numerals but one, and has a query asking about the arabic numeral value of the missing intergalactic numeral. In this example, the program could deduce that [intergalactic numeral not explicitly provided] must be the missing numeral and answer correctly.

Also, the program has no "memory". That is, the program runs one time with one set of notes, outputs answers for those notes, and terminates, keeping no store of the knowledge just obtained. The program could be expanded to remember all given notes, make deductions from different sets of notes, overwrite notes previously provided, and re-examine previously unanswerable queries based on new information, all within an interactive command line session or desktop application.

Finally, the syntax for note interpretation is quite rigid. This syntax could be modified, or a machine learning algorithm could be implemented to aid input classifications.

# Instructions for Running the Program

- On Mac OSX (and Linux is probably similar, but not tested):
	1. Unzip all of the files into the same directory.
	2. Compile the program by navigating to that directory on the command line and entering the command: 
	javac -classpath /path/to/dir /path/to/dir/NoteProcessor.java
	3. In the same directory, run the program with the provided input with the following command:
	java NoteProcessor notes/intergal_test1.txt
	The output to the terminal should match the test case provided.
	4. Run the program with other input with the following command:
	java NoteProcessor [path to text file]
- On Windows (not tested):
	1. Unzip all of the files into the same directory.
	2. Compile the program by navigating to that directory on the command line and entering the command:
	javac NoteProcessor.java
	Note: you may have to add the other classes to the class path as specified in step 2 for the OSX instructions, but I’m not sure the correct command for Windows.
	3. In the same directory, run the program with the provided input with the following command:
	java NoteProcessor notes\intergal_test1.txt
	The output to the terminal should match the test case provided.
	4. Run the program with other input with the following command:
	java NoteProcessor [path to text file]

# Technologies Used

- Java. Standard libraries only, except for JUnit testing in the test class.

# License

Created by Ryan William Connor in February 2017.  
Copyright © 2017 Ryan William Connor. All rights reserved.
