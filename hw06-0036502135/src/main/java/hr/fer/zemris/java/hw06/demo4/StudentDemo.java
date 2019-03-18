package hr.fer.zemris.java.hw06.demo4;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Program that illustrates use of stream API.
 * It loads from text files and creates list of StudentRecord classes.
 * Uses stream API to calculate given actions.
 * 
 * @author Martin Sršen
 *
 */
public class StudentDemo {

	/**
	 * Called when program is started.
	 * 
	 * @param args Arguments from command prompt.Not used in this example.
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(
				 Paths.get("src/main/resources/studenti.txt"),
				 StandardCharsets.UTF_8
				);
		
		List<StudentRecord> records = convert(lines);
		
		long broj = vratiBodovaViseOd25(records);
		long broj5 = vratiBrojOdlikasa(records);
		List<StudentRecord> odlikasi = vratiListuOdlikasa(records);
		List<StudentRecord> odlikasiSortirano = vratiSortiranuListuOdlikasa(records);
		List<String> nepolozeniJMBAGovi = vratiPopisNepolozenih(records);
		Map<Integer, List<StudentRecord>> mapaPoOcjenama = razvrstajStudentePoOcjenama(records);
		Map<Integer, Integer> mapaPoOcjenama2 = vratiBrojStudenataPoOcjenama(records);
		Map<Boolean, List<StudentRecord>> prolazNeprolaz = razvrstajProlazPad(records);
		
	}
	
	/**
	 * Counts number of students with sum of all scores that are greater than 25.
	 * Uses stream API to do it.
	 * 
	 * @param records	List of StudentRecord objects that represents students.
	 * @return	number of students with sum of scores greater than 25.
	 */
	public static long vratiBodovaViseOd25(List<StudentRecord> records) {
		return records.stream()
				.filter(record -> record.getScoreMI()+record.getScoreZI()+record.getScoreLab() > 25)
				.count();
	}
	
	/**
	 * Counts number of students with 5 as final grade.
	 * Uses stream API to do it.
	 * 
	 * @param records	List of StudentRecord objects that represents students.
	 * @return	number of students with 5 as final grade.
	 */
	public static long vratiBrojOdlikasa(List<StudentRecord> records) {
		return records.stream()
				.filter(record -> record.getGrade() == 5)
				.count();
	}
	
	/**
	 * Returns list of StudentRecord objects that have 5 as final grade.
	 * Uses stream API to do it.
	 * 
	 * @param records	List of StudentRecord objects that represents students.
	 * @return	List of StudentRecord objects where final grade is 5.
	 */
	public static List<StudentRecord> vratiListuOdlikasa(List<StudentRecord> records){
		return records.stream()
				.filter(record -> record.getGrade() == 5)
				.collect(Collectors.toList());
	}
	
	/**
	 * Returns list of StudentRecord objects that have 5 as final grade sorted by sum of scores descending.
	 * Uses stream API to do it.
	 * 
	 * @param records	List of StudentRecord objects that represents students.
	 * @return	List of StudentRecord objects where final grade is 5 sorted by sum of scores descending.
	 */
	public static List<StudentRecord> vratiSortiranuListuOdlikasa(List<StudentRecord> records){
		return records.stream()
				.filter(record -> record.getGrade() == 5)
				.sorted((r1, r2) -> Double.valueOf(r2.getScoreMI()+r2.getScoreZI()+r2.getScoreLab())
						.compareTo(Double.valueOf(r1.getScoreMI()+r1.getScoreZI()+r1.getScoreLab())))
				.collect(Collectors.toList());
	}

	/**
	 * Returns list of StudentRecord jmbags that have 1 as final grade sorted ascending.
	 * Uses stream API to do it.
	 * 
	 * @param records	List of StudentRecord objects that represents students.
	 * @return	List of StudentRecord jmbags where final grade is 1 sorted ascending.
	 */
	public static List<String> vratiPopisNepolozenih(List<StudentRecord> records){
		return records.stream()
				.filter(record -> record.getGrade() == 1)
				.map(record -> record.getJmbag())
				.sorted()
				.collect(Collectors.toList());
	}
	
	/**
	 * Returns map where key is grade and value is list of StudentRecords that contains that grade.
	 * Uses stream API to do it.
	 * 
	 * @param records	List of StudentRecord objects that represents students.
	 * @return	Map where key is grade and value is list of StudentRecords that contains that grade.
	 */
	public static Map<Integer, List<StudentRecord>> razvrstajStudentePoOcjenama(List<StudentRecord> records){
		return records.stream()
				.collect(Collectors.groupingBy(record -> record.getGrade()));
	}
	
	/**
	 * Returns map where key is grade and value number of StudentRecords that contains that grade.
	 * Uses stream API to do it.
	 * 
	 * @param records	List of StudentRecord objects that represents students.
	 * @return	Map where key is grade and value number of StudentRecords that contains that grade.
	 */
	public static Map<Integer, Integer> vratiBrojStudenataPoOcjenama(List<StudentRecord> records){
		return records.stream()
				.collect(Collectors.toMap(record -> record.getGrade(), counter -> Integer.valueOf(1), (original, duplicate) -> original + 1));
	}
	
	/**
	 * Returns map where key is boolean value and value number of StudentRecords with grade 1 for false key
	 * or number of StudentRecords with other grades for true key.
	 * Uses stream API to do it.
	 * 
	 * @param records	List of StudentRecord objects that represents students.
	 * @return	Map where key is boolean value and value number of StudentRecords with grade 1 for false key
	 * or number of StudentRecords with other grades for true key.
	 */
	public static Map<Boolean, List<StudentRecord>> razvrstajProlazPad(List<StudentRecord> records){
		return records.stream().collect(Collectors.partitioningBy(record -> record.getGrade() > 1));
	}
	
	/**
	 * Converts list of input String lines from text file into list of StudentRecords.
	 * 
	 * @param lines	List of String lines from text file.
	 * @return	List of StudentRecord objects.
	 */
	private static List<StudentRecord> convert(List<String> lines) {
		List<StudentRecord> records = new ArrayList<>();
		
		for(String line: lines) {
			records.add(makeRecord(line.trim()));
		}
		
		return records;
	}
	
	/**
	 * Helper method that creates StudentRecord from given String row.
	 * 
	 * @param line	String row to transform into StudentRecord.
	 * @return	StudentRecord created from given String row.
	 * @throws	IllegalArgumentException if given line is invalid.
	 */
	private static StudentRecord makeRecord(String line) {
		String[] pts = line.split("\\s+");
		
		if(pts.length != 7) {
			throw new IllegalArgumentException("Invalid number of columns,was " + pts.length);
		}
		
		String jmbag = pts[0];
		String lastName = pts[1];
		String firstName = pts[2];
		double scoreMI, scoreZI, scoreLab;
		int grade;
		
		try {
			scoreMI = Double.parseDouble(pts[3]);
			scoreZI = Double.parseDouble(pts[4]);
			scoreLab = Double.parseDouble(pts[5]);
			grade = Integer.parseInt(pts[6]);
		}catch(NumberFormatException ex) {
			throw new IllegalArgumentException("Expected double, but was something else: " + line);
		}
		
		return new StudentRecord(jmbag, lastName, firstName, scoreMI, scoreZI, scoreLab, grade);
	}
}
