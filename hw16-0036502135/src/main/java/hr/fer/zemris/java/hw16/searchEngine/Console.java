package hr.fer.zemris.java.hw16.searchEngine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Simple cmd program that acts like simple search engine.
 * Takes one parameter through command line: path to directory
 * containing document files.
 * Accepts 4 commands:
 * query >>search sentence<< will give best matched results,
 * type >>index<< will print document at given index in queried results,
 * results will print out queried results
 * and exit will exit program.
 * 
 * @author Martin Sršen
 *
 */
public class Console {

	/**
	 * Path to file containing stop words.
	 */
	private static final String STOP_WORDS_FILE = "src/main/resources/hrvatski_stoprijeci.txt";
	/**
	 * Object representing vocabulary.
	 */
	private static Vocabulary vocabulary;
	/**
	 * Map where query results are stored.
	 */
	private static Map<Double, Document> results;
	
	/**
	 * Called when program is started.
	 * 
	 * @param args Arguments from command prompt.Not used in this example.
	 */
	public static void main(String[] args) throws IOException {
		if(args.length != 1) {
			throw new IllegalArgumentException("Program accepts single argument: path to directory with text files.");
		}
		
		vocabulary = new Vocabulary(STOP_WORDS_FILE, args[0]);
		results = new TreeMap<>((k1, k2) -> Double.compare(k2, k1));
		
		try(Scanner scanner = new Scanner(System.in)){
			String input;
			
			while(true) {
				System.out.print("Enter command > ");
				input = scanner.nextLine().trim().toLowerCase();
				
				if(input.equals("exit"))	break;
				else if(input.startsWith("query"))	executeQuery(input);
				else if(input.startsWith("type"))	printDocument(input);
				else if(input.equals("results"))	printResults();
				else	System.out.println("Invalid command.");
				
				System.out.println();
			}
		}
	}
	
	/**
	 * Method that prints document at index given by type command.
	 * If invalid index is given, print message and returns to taking 
	 * new command.
	 * 
	 * @param input	type command input.
	 * @throws IOException	If error happens reading file.
	 */
	private static void printDocument(String input) throws IOException {
		input = input.replaceFirst("type", "").trim();
		
		int index;
		try {
			index = getIndex(input);
		}catch(IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
			return;
		}
		
		Document doc = getDocument(index);
		int len = doc.getName().length();
		
		drawLines(len);
		System.out.println(doc.getName());
		drawLines(len);
		
		List<String> text = Files.readAllLines(Paths.get(doc.getName()));		
		text.forEach(line -> System.out.println(line));
		drawLines(len);
	}

	/**
	 * Method that executes query command.
	 * Calculates similarity with each document
	 * and returns best 10 matches.
	 * 
	 * @param input	Input query.
	 * @throws IOException	If error happens reading file.
	 */
	private static void executeQuery(String input) throws IOException {
		results.clear();
		input = input.replaceFirst("query", "").trim();
		
		Document document = new Document("Query");
		document.setKeyWords(vocabulary.getStopWords(), input);
		document.calculateTfidf(vocabulary, input);
		
		System.out.println("Query is: " + document.getKeyWords());
		System.out.println("Najboljih 10 rezultata:");
		
		for(Document doc : vocabulary.getDocuments()) {
			double sim = document.getSim(doc);
			if(sim < 10E-5)	continue;
			
			results.put(sim, doc);
		}

		printResults();
	}
	
	/**
	 * Method that prints query results onto standard output.
	 * Prints best 10 matches.
	 */
	private static void printResults() {
		int index = 0;
		for(Entry<Double, Document> entry : results.entrySet()) {
			System.out.printf("[ %d] (%.4f) %s", index++, entry.getKey(), entry.getValue());
			System.out.println();
			
			if(index == 10)	break;
		}
	}
	
	/**
	 * Helper method that checks whether given type command index is valid.
	 * If index is invalid throws exception with message, else returns index.
	 * 
	 * @param input	String representation of index to check.
	 * @return	parsed index from input.
	 * @throws IllegalArgumentException	if invalid index is given.
	 */
	private static int getIndex(String input) {
		int index;
		try {
			index = Integer.parseInt(input);
		}catch(Exception ex) {
			throw new IllegalArgumentException("Type command takes 1 argument: index of recieved query results.");
		}
		
		if(index < 0) {
			throw new IllegalArgumentException("Index must be number greater tna 0.");
		}
		
		if(results.size() <= index) {
			if(results.size() == 0) {
				throw new IllegalArgumentException("There are no query results.");
			}else {
				throw new IllegalArgumentException("Given index is out of range, available query indexes are [0," + (results.size() - 1) + "]");
			}
		}
		
		return index;
	}
	
	/**
	 * Helper method that will get document at certain index in TreeMap.
	 * 
	 * @param index	Index of document in map to return.
	 * @return	Document at given index, or null if doesn't exist.
	 */
	private static Document getDocument(int index) {
		Document doc = null;
		
		for(Double val : results.keySet()) {
			if(index == 0) {
				doc = results.get(val);
				break;
			}else {
				index--;
			}
		}
		
		return doc;
	}
	
	/**
	 * Helper method that will write lines onto standard output
	 * based on number of letters document name has.
	 * 
	 * @param length	Length of document name.
	 */
	private static void drawLines(int length) {
		for(int i = 0; i < length; i++) {
			System.out.print("-");
		}
		
		System.out.println();
	}
}
