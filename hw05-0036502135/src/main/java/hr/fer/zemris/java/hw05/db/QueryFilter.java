package hr.fer.zemris.java.hw05.db;

import java.util.List;
import java.util.Objects;

/**
 * Implementation of IFilter interfrace.
 * Checks whether given StudentRecord satisfy list of conditions.
 * 
 * @author Martin Sršen
 *
 */
public class QueryFilter implements IFilter {

	/**
	 * List of conditions.
	 */
	private List<ConditionalExpression> list;
	
	/**
	 * Constructor that initializes list of conditions.
	 * 
	 * @param list	List of conditions.
	 */
	public QueryFilter(List<ConditionalExpression> list) {
		Objects.requireNonNull(list, "List can't be null value.");
		
		this.list = list;
	}

	/**
	 * Returns true if StudentRecord satisfies all conditions from list.
	 * 
	 * @return true if StudentRecord satisfies all conditions from list,
	 * 		false otherwise.
	 */
	@Override
	public boolean accepts(StudentRecord record) {
		if(record == null) {
			return false;
		}
		
		boolean valid = true;
		
		for(ConditionalExpression exp : list) {
			if(!(exp.getComparisonOperator().satisfied(
					exp.getFieldGetter().get(record), exp.getStringLiteral()))) {
				valid = false;
			}
		}
		
		return valid;
	}
}
