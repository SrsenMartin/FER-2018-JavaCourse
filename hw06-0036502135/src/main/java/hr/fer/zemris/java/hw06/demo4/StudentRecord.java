package hr.fer.zemris.java.hw06.demo4;

/**
 * Class that represents one student record.
 * Each record is contained of jmbag, last name, first name, score on first exam,
 * score on second exam, score on labs and final grade.
 * 
 * @author Martin Sršen
 *
 */
public class StudentRecord {

	/**
	 * Student jmbag.
	 */
	private String jmbag;
	/**
	 * Student last name.
	 */
	private String lastName;
	/**
	 * Student first name.
	 */
	private String firstName;
	/**
	 * Student score on first exam.
	 */
	private double scoreMI;
	/**
	 * Student score on second exam.
	 */
	private double scoreZI;
	/**
	 * Student score on labs.
	 */
	private double scoreLab;
	/**
	 * Student final grade.
	 */
	private int grade;
	
	/**
	 * Constructor that makes new StudentRecord with given values.
	 * 
	 * @param jmbag	Student jmbag.
	 * @param lastName	Student last name.
	 * @param firstName	Student first name.
	 * @param scoreMI	Student score on first exam.
	 * @param scoreZI	Student score on second exam.
	 * @param scoreLab	Student score on labs.
	 * @param grade	Student final grade.
	 */
	public StudentRecord(String jmbag, String lastName, String firstName,
						double scoreMI, double scoreZI, double scoreLab, int grade) {
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.scoreMI = scoreMI;
		this.scoreZI = scoreZI;
		this.scoreLab = scoreLab;
		this.grade = grade;
	}

	/**
	 * Getter method for jmbag.
	 * 
	 * @return	jmbag.
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Getter method for last name.
	 * 
	 * @return	lastName.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Getter method for first name.
	 * 
	 * @return	firstName.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Getter method for first exam score.
	 * 
	 * @return	scoreMI.
	 */
	public double getScoreMI() {
		return scoreMI;
	}

	/**
	 * Getter method for second exam score.
	 * 
	 * @return	scoreZI.
	 */
	public double getScoreZI() {
		return scoreZI;
	}

	/**
	 * Getter method for labs score.
	 * 
	 * @return	scoreLab.
	 */
	public double getScoreLab() {
		return scoreLab;
	}
	
	/**
	 * Getter method for final grade.
	 * 
	 * @return	grade.
	 */
	public int getGrade() {
		return grade;
	}
	
	@Override
	public String toString() {
		return String.format("%s\t%s\t%s\t%f\t%f\t%f\t%d", jmbag, lastName, firstName, scoreMI, scoreZI, scoreLab, grade);
	}
}
