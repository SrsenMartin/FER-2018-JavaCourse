package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element type that represents variable.
 * 
 * @author Martin Sršen
 *
 */
public class ElementVariable extends Element {

	/**
	 * Name of a variable.
	 */
	private String name;

	/**
	 * Constructor that assigns value to element.
	 * 
	 * @param name Name of a variable.
	 */
	public ElementVariable(String name) {
		this.name = name;
	}

	@Override
	public String asText() {
		return name;
	}
}
