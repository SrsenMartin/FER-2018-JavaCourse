package hr.fer.zemris.java.hw05.db;

/**
 * Interface that has one method that returns true if
 * two strings satisfy certain operation condition.
 * 
 * @author Martin Sršen
 *
 */
public interface IComparisonOperator {

	/**
	 * Returns true if two strings satisfy
	 * certain operation condition.
	 * 
	 * @param value1	Value to compare from.
	 * @param value2	Value to compare to.
	 * @return	true if strings satisfy certain operation condition.
	 */
	boolean satisfied(String value1, String value2);
}
