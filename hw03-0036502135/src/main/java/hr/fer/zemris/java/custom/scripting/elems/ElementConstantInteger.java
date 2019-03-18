package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element type that represents one number as integer.
 * 
 * @author Martin Sršen
 *
 */
public class ElementConstantInteger extends Element {

	/**
	 * Value of ElementConstantInteger element.
	 */
	private int value;

	/**
	 * Constructor that assigns value to element.
	 * 
	 * @param value integer value of element.
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}

	@Override
	public String asText() {
		return Integer.toString(value);
	}
}
