package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Class used to store implementations of IComparisonOperator interface.
 * All of them have one method satisfied that returns true if given 
 * condition is true, false otherwise.
 * 
 * @author Martin Sršen
 *
 */
public class ComparisonOperators {

	/**
	 * Checks whether first string is less than other string.
	 */
	public static final IComparisonOperator LESS;
	/**
	 * Checks whether first string is less or equal to other string.
	 */
	public static final IComparisonOperator LESS_OR_EQUALS;
	/**
	 * Checks whether first string is greater than other string.
	 */
	public static final IComparisonOperator GREATER;
	/**
	 * Checks whether first string is greater or equal to other string.
	 */
	public static final IComparisonOperator GREATER_OR_EQUALS;
	/**
	 * Checks whether first string is equal to other string.
	 */
	public static final IComparisonOperator EQUALS;
	/**
	 * Checks whether first string is not equal to other string.
	 */
	public static final IComparisonOperator NOT_EQUALS;
	/**
	 * Checks whether first string is like other string.
	 */
	public static final IComparisonOperator LIKE;
	
	/**
	 * Static initialization block for all IComparisonOperator implementations.
	 */
	static {
		LESS = (value1, value2) -> {
			Objects.requireNonNull(value1, "Can't compare null values.");
			Objects.requireNonNull(value2, "Can't compare null values.");
			
			return value1.compareTo(value2) < 0;
		};
		LESS_OR_EQUALS = (value1, value2) -> {
			Objects.requireNonNull(value1, "Can't compare null values.");
			Objects.requireNonNull(value2, "Can't compare null values.");
			
			return value1.compareTo(value2) <= 0;
		};
		GREATER = (value1, value2) -> {
			Objects.requireNonNull(value1, "Can't compare null values.");
			Objects.requireNonNull(value2, "Can't compare null values.");
			
			return value1.compareTo(value2) > 0;
		};
		GREATER_OR_EQUALS = (value1, value2) -> {
			Objects.requireNonNull(value1, "Can't compare null values.");
			Objects.requireNonNull(value2, "Can't compare null values.");
			
			return value1.compareTo(value2) >= 0;
		};
		EQUALS = (value1, value2) -> {
			Objects.requireNonNull(value1, "Can't compare null values.");
			Objects.requireNonNull(value2, "Can't compare null values.");
			
			return value1.compareTo(value2) == 0;
		};
		NOT_EQUALS = (value1, value2) -> {
			Objects.requireNonNull(value1, "Can't compare null values.");
			Objects.requireNonNull(value2, "Can't compare null values.");
			
			return value1.compareTo(value2) != 0;
		};
		LIKE = (value1, value2) -> {
			Objects.requireNonNull(value1, "Can't compare null values.");
			Objects.requireNonNull(value2, "Can't compare null values.");
			
			if(value2.lastIndexOf('*') != value2.indexOf('*')) {
				throw new IllegalArgumentException("Like operator can't have multiple * signs.");
			}
			
			return value1.matches(value2.replace("*", ".*"));
		};
	}
}
