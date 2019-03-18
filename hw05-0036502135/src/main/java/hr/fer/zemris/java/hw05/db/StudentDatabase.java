package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw05.collections.SimpleHashtable;

/**
 * Class representing StudentRecord database.
 * Stores all StudentRecords into List and into SImpleHashTable
 * jmbag->StudentRecord.
 * 
 * SimpleHashTable is used for fast indexing, recordList for quering
 * many StudentRecords.
 * 
 * @author Martin Sršen
 *
 */
public class StudentDatabase {

	/**
	 * List of all StudentRecords.
	 */
	private List<StudentRecord> recordList;
	/**
	 * SimpleHashTable containing entries jmbag->StudentRecord.
	 */
	private SimpleHashtable<String, StudentRecord> index;
	
	/**
	 * Constructor that takes list of rows each representing one
	 * StudentRecord input, creates StudentRecords and stores them into
	 * List and SimpleHashtable.
	 * 
	 * @param rowList	List of rows representing one StudentRecord.
	 * @throws NullPointerException if rowList is null.
	 */
	public StudentDatabase(List<String> rowList) {
		Objects.requireNonNull(rowList, "Can't accept null as rowList value.");
		
		recordList = new ArrayList<>();
		index = new SimpleHashtable<>();
		
		for(String row : rowList) {
			String[] parts = splitRow(row);
			StudentRecord record = createRecord(parts);
			
			if(!recordList.contains(record)) {
				recordList.add(record);
			}
			index.put(record.getJmbag(), record);
		}
	}
	
	/**
	 * Returns StudentRecord based on given jmbag.
	 * 
	 * @param jmbag	Used to get StudentRecord.
	 * @return	StudentRecord with given jmbag,or null if StudentRecord
	 * with given jmbag is not contained into SimpleHashtable.
	 */
	public StudentRecord forJMBAG(String jmbag) {
		return index.get(jmbag);
	}

	/**
	 * Helper method that splits one input row into it parts.
	 * 
	 * @param row	Input row representing StudentRecord.
	 * @return	parts of row.
	 * @throws	IllegalArgumentException if invalid row was read.
	 */
	private String[] splitRow(String row) {
		String[] parts = row.trim().split("\\s+");
		
		if(parts.length != 4 && parts.length != 5) {
			throw new IllegalArgumentException("Wrong input: " + row);
		}
		
		return parts;
	}
	
	/**
	 * Returns list of StudentRecords that passed given filter conditions.
	 * 
	 * @param filter	Condition that is used to check if StudentRecord is wanted one.
	 * @return	List of StudentRecords that passed given filter conditions.
	 */
	public List<StudentRecord> filter(IFilter filter) {
		Objects.requireNonNull(filter, "Filter can't be null value");
		
		List<StudentRecord> filteredList = new ArrayList<>();
		
		for(StudentRecord record : recordList) {
			if(filter.accepts(record)) {
				filteredList.add(record);
			}
		}
		
		return filteredList;
	}
	
	/**
	 * Helper method used to create StudentRecord based on given parts of input row.
	 * 
	 * @param parts	Parts of one input row.
	 * @return	StudentRecord made from given parts.
	 * @throws	IllegalArgumentException	if invalid input was given.
	 */
	private StudentRecord createRecord(String[] parts) {
		String jmbag = parts[0];
		String lastName = (parts.length == 5 ? parts[1] + " " + parts[2] : parts[1]);
		String firstName = parts[parts.length - 2];
		int finalGrade;
		
		try {
			finalGrade = Integer.parseInt(parts[parts.length - 1]);
		}catch(NumberFormatException ex) {
			throw new IllegalArgumentException("Wrong input, expected final grade but was: " + parts[parts.length - 1]);
		}
		
		return new StudentRecord(jmbag, lastName, firstName, finalGrade);
	}
}
