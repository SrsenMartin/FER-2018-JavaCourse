package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element type that represents function.
 * 
 * @author Martin Sršen
 *
 */
public class ElementFunction extends Element {

	/**
	 * Function name.
	 */
	private String name;

	/**
	 * Constructor that assigns value to element.
	 * 
	 * @param name Name of a function.
	 */
	public ElementFunction(String name) {
		this.name = name;
	}

	/**
	 * Returns element value as String.
	 * 
	 * @return element as String.
	 */
	@Override
	public String asText() {
		return name;
	}
}
