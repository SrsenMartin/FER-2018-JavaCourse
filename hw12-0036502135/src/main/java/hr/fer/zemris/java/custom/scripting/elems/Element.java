package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Base class for all different element types that lexer can recognise.
 * 
 * @author Martin Sršen
 *
 */
public abstract class Element {

	/**
	 * Returns element value as String.
	 * 
	 * @return element as String.
	 */
	public abstract String asText();
}
