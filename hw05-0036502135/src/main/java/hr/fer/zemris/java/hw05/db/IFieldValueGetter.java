package hr.fer.zemris.java.hw05.db;

/**
 * Interface that represents getters for certains StudentRecord fields.
 * 
 * @author Martin Sršen
 *
 */
public interface IFieldValueGetter {
	
	/**
	 * Returns certain StudentRecord field based on implementation.
	 * 
	 * @param record	StudentRecord to extract from.
	 * @return	Certain StudentRecord field.
	 */
	String get(StudentRecord record);
}
