package hr.fer.zemris.java.hw05.db;

/**
 * Class used to store implementations of IFieldValueGetter interface.
 * All of them have one method get that returns value based on class implementation.
 * 
 * @author Martin Sršen
 *
 */
public class FieldValueGetters {

	/**
	 * Used as getter for StudentRecord firstName.
	 */
	public static final IFieldValueGetter FIRST_NAME;
	/**
	 * Used as getter for StudentRecord lastName.
	 */
	public static final IFieldValueGetter LAST_NAME;
	/**
	 * Used as getter for StudentRecord jmbag.
	 */
	public static final IFieldValueGetter JMBAG;
	
	/**
	 * Static initialization block for all IFieldValueGetter implementations.
	 */
	static {
		FIRST_NAME = record -> record.getFirstName();
		LAST_NAME = record -> record.getLastName();
		JMBAG = record -> record.getJmbag();
	}
}
