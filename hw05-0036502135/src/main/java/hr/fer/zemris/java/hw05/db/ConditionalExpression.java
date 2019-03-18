package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Class that represents one conditional expression.
 * Conditional expression is structured from field getter-operator-string literal.
 * 
 * @author Martin Sršen
 *
 */
public class ConditionalExpression {

	/**
	 * FIeld getter of conditional expression.
	 */
	private IFieldValueGetter fieldGetter;
	/**
	 * String literal for conditional expression.
	 */
	private String stringLiteral;
	/**
	 * Comparison operator for condition expression.
	 */
	private IComparisonOperator comparisonOperator;
	
	/**
	 * Constructs conditional expression based on given values.
	 * 
	 * @param getterStrategy	FIeld getter of conditional expression.
	 * @param literal	String literal for conditional expression.
	 * @param operatorStrategy	Comparison operator for condition expression.
	 */
	public ConditionalExpression(IFieldValueGetter getterStrategy, String literal, IComparisonOperator operatorStrategy) {
		Objects.requireNonNull(getterStrategy, "Getter strategy can't have null value.");
		Objects.requireNonNull(literal, "Literal can't have null value.");
		Objects.requireNonNull(operatorStrategy, "Operator strategy can't gave null value.");
		
		this.fieldGetter = getterStrategy;
		this.stringLiteral = literal;
		this.comparisonOperator = operatorStrategy;
	}

	/**
	 * Getter for field getter.
	 * 
	 * @return	fieldGetter
	 */
	public IFieldValueGetter getFieldGetter() {
		return fieldGetter;
	}

	/**
	 * Getter for string literal.
	 * 
	 * @return	stringLiteral
	 */
	public String getStringLiteral() {
		return stringLiteral;
	}

	/**
	 * Getter for comparison operator.
	 * 
	 * @return	comparisonOperator
	 */
	public IComparisonOperator getComparisonOperator() {
		return comparisonOperator;
	}
}
