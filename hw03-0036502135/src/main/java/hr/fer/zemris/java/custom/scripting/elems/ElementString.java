package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element type that represents String
 * 
 * @author Martin Sršen
 *
 */
public class ElementString extends Element {

	/**
	 * String representation of ElementString.
	 */
	private String value;

	/**
	 * Constructor that assigns value to element.
	 * 
	 * @param value String value of ElementString.
	 */
	public ElementString(String value) {
		this.value = value;
	}

	@Override
	public String asText() {
		return value;
	}
}
