package hr.fer.zemris.java.hw05.db;

/**
 * Interface that represents classes that checks whether certain StudentRecord satisfy given filter.
 * 
 * @author Martin Sršen
 *
 */
public interface IFilter {
	
	/**
	 * Returns true if given StudentRecord satisfy given filter.
	 * 
	 * @param record	StudentRecord to filter.
	 * @return	true if given StudentRecord satisfy given filter,false otherwise.
	 */
	boolean accepts(StudentRecord record);
}
