package hr.fer.zemris.java.gui.layouts;

/**
 * Exception that extends from RunTimeException.
 * It is thrown if something wrong happens with CalcLayout.
 * 
 * @author Martin Sršen
 *
 */
public class CalcLayoutException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default CalcLayoutException constructor.
	 */
	public CalcLayoutException() {
	}

	/**
	 * CalcLayoutException constructor with message argument.
	 * 
	 * @param message	Cause of exception.
	 */
	public CalcLayoutException(String message) {
		super(message);
	}
}
