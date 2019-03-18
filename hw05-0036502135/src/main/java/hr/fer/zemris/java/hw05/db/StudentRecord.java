package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Class represeting one student record.
 * Every record is contained of 4 values,none of them allows null values.
 * 
 * @author Martin Sršen
 *
 */
public class StudentRecord {

	/**
	 * Student jmbag, must be unique value.
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
	 * Student final grade.
	 */
	private int finalGrade;

	/**
	 * Constructor used to create and initialize StudentRecord.
	 * 
	 * @param jmbag	Student jmbag.
	 * @param lastName	Student last name.
	 * @param firstName	Student first name.
	 * @param finalGrade	Student final grade.
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
		Objects.requireNonNull(jmbag, "Jmbag can't be null value.");
		Objects.requireNonNull(lastName, "lastName can't be null value.");
		Objects.requireNonNull(firstName, "firstName can't be null value.");
		Objects.requireNonNull(finalGrade, "finalGrade can't be null value.");
		
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.finalGrade = finalGrade;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jmbag == null) ? 0 : jmbag.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudentRecord other = (StudentRecord) obj;
		if (jmbag == null) {
			if (other.jmbag != null)
				return false;
		} else if (!jmbag.equals(other.jmbag))
			return false;
		return true;
	}

	/**
	 * Getter method for student jmbag.
	 * 
	 * @return	jmbag
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Getter method for student last name.
	 * 
	 * @return	last name.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Getter method for student first name.
	 * 
	 * @return	first name.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Getter method for student final grade.
	 * 
	 * @return	final grade.
	 */
	public int getFinalGrade() {
		return finalGrade;
	}
}
