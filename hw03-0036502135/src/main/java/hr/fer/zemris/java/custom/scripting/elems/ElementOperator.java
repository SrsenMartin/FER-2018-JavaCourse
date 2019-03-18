package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element type that represents operator.
 * 
 * @author Martin Sršen
 *
 */
public class ElementOperator extends Element {

	/**
	 * Operator.
	 */
	private String symbol;

	/**
	 * Constructor that assigns value to element.
	 * 
	 * @param symbol Symbol of operator.
	 */
	public ElementOperator(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public String asText() {
		return symbol;
	}
}
