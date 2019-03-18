package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element type that represents one number as double.
 * 
 * @author Martin Sršen
 *
 */
public class ElementConstantDouble extends Element {

	/**
	 * Value of ElementConstantDouble element.
	 */
	private double value;

	/**
	 * Constructor that assigns value to element.
	 * 
	 * @param value double value of element.
	 */
	public ElementConstantDouble(double value) {
		this.value = value;
	}

	@Override
	public String asText() {
		return Double.toString(value);
	}
}
