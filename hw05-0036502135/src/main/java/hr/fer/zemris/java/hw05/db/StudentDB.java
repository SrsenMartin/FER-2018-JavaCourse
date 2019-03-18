package hr.fer.zemris.java.hw05.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import hr.fer.zemris.java.hw05.db.QueryParser.QueryParserException;

/**
 * Program that loads database from text file,
 *  reads queries from standard input one by one
 * and prints filtered results to standard output.
 * Queries are read until "exit" is read.
 * 
 * @author Martin Sršen
 * @version 1.0
 */
public class StudentDB {

	/**
	 * Called when program is started.
	 * 
	 * @param args Arguments from command prompt.Not used in this example.
	 */
	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(
				 Paths.get("src/main/resources/database.txt"),
				 StandardCharsets.UTF_8
				);
		
		StudentDatabase db = new StudentDatabase(lines);
		String query = null;
		QueryParser parser = null;
		
		try(Scanner scanner = new Scanner(System.in)){
			while(true) {
				System.out.print("> ");
				
				try {
					query = scanner.nextLine();
					
					if(query.toLowerCase().equals("exit")) {
						break;
					}
					
					query = checkAndTrimQuery(query);
					parser = new QueryParser(query);
					
					if(parser.isDirectQuery()) {
						StudentRecord record = db.forJMBAG(parser.getQueriedJMBAG());
						printIndexed(record);
					}else {
						 List<StudentRecord> records = db.filter(new QueryFilter(parser.getQuery()));
						 printDatabase(records);
					}
				}catch(QueryParserException | IllegalArgumentException ex) {
					System.out.println(ex.getMessage() + "\n");
				}
			}
		}
		
		System.out.println("Goodbye!");
	}
	
	/**
	 * Checks whether query keyword is valid and at right place.
	 * Removes it if valid.
	 * 
	 * @param query	Query read from standard input.
	 * @return	String without query keyword if valid query was read.
	 * @throws IllegalArgumentException if query keyword is alone,not existed or at wrong place.
	 */
	private static String checkAndTrimQuery(String query) {
		if(query.trim().equals("query")) {
			throw new IllegalArgumentException("query keyword can't stay alone.");
		}
		
		if(!query.trim().startsWith("query")) {
			throw new IllegalArgumentException("Keyword query at wrong place or not existing: " + query);
		}
		query = query.replaceFirst("query", "");
		
		return query;
	}
	
	/**
	 * Responsible for printing list of records if fast indexing is not used.
	 * 
	 * @param records	List of records to print.
	 */
	private static void printDatabase(List<StudentRecord> records) {
		int longestFirstName = 0;
		int longestLastName = 0;
		
		for(StudentRecord record : records) {
			if(record.getFirstName().length() > longestFirstName) {
				longestFirstName = record.getFirstName().length();
			}
			if(record.getLastName().length() > longestLastName) {
				longestLastName = record.getLastName().length();
			}
		}
		
		String header = null;
		if(records.size() != 0) {
			header = generateHeader(longestFirstName, longestLastName);
			System.out.println(header);
		}
		
		for(StudentRecord record : records) {
			System.out.println(generateRow(record, longestFirstName, longestLastName));
		}
		
		if(records.size() != 0) {
			System.out.println(header);
		}
		System.out.println("Records selected: " + records.size());
		System.out.println();
	}
	
	/**
	 * Responsible for printing StudentRcord if fast indexing is used.
	 * 
	 * @param record	StudentRecord to print.
	 */
	private static void printIndexed(StudentRecord record) {
		System.out.println("Using index for record retrieval.");
		
		if(record != null) {
			int fNameLen = record.getFirstName().length();
			int lNameLen = record.getLastName().length();
			String header = generateHeader(fNameLen, lNameLen);
			
			System.out.println(header);
			System.out.println(generateRow(record, fNameLen, lNameLen));
			System.out.println(header);
		}
		
		System.out.println("Records selected: " + (record == null ? 0 : 1));
		System.out.println();
	}
	
	/**
	 * Responsible for generating table header.
	 * 
	 * @param longestFirstName	Length of longest first name in list of StudentRecords.
	 * @param longestLastName	Length of longest last name in list of StudentRecords.
	 * @return	String representing table header.
	 */
	private static String generateHeader(int longestFirstName, int longestLastName) {
		StringBuilder builder;
		
		builder = new StringBuilder("");
		builder.append("+============+");
		
		while(longestLastName > 0) {
			builder.append("=");
			longestLastName--;
		}
		builder.append("==+");
		
		while(longestFirstName > 0) {
			builder.append("=");
			longestFirstName--;
		}
		builder.append("==+===+");
		
		return builder.toString();
	}
	
	/**
	 * Responsible for generating table row.
	 * 
	 * @param record	StudentRecord to used to get information to print.
	 * @param longestFirstName	Length of longest first name in list of StudentRecords.
	 * @param longestLastName	Length of longest last name in list of StudentRecords.
	 * @return	String representing table row.
	 */
	private static String generateRow(StudentRecord record, int longestFirstName, int longestLastName) {
		int firstNameLength = record.getFirstName().length();
		int lastNameLength = record.getLastName().length();
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("| ").append(record.getJmbag()).append(" | ");
		builder.append(record.getLastName());
		builder.append(generateEmpty(lastNameLength, longestLastName));
		builder.append(record.getFirstName());
		builder.append(generateEmpty(firstNameLength, longestFirstName));
		builder.append(record.getFinalGrade()).append(" |");
		
		return builder.toString();
	}
	
	/**
	 * Helper method used to generate empty spaces after first names and last names if needed.
	 * 
	 * @param nameLength	Length of current StudentRecord name.
	 * @param longestNameLength	Length of longest StudentRecord name.
	 * @return	String representing needed empty spaces after name.
	 */
	private static String generateEmpty(int nameLength, int longestNameLength) {
		StringBuilder b = new StringBuilder("");
		while(nameLength < longestNameLength) {
			b.append(" ");
			nameLength++;
		}
		b.append(" | ");
		
		return b.toString();
	}
}
