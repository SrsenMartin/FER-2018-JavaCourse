package hr.fer.zemris.java.custom.scripting.collections;

/**
 * Exception thrown when user tries to do operations
 * pop and peek on empty stack.
 * 
 * @author Martin Sršen
 *
 */
public class EmptyStackException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public EmptyStackException() {

	}

	/**
	 * Constructor that takes message of a problem,...
	 * 
	 * @param message	Message to user about a problem.
	 */
	public EmptyStackException(String message) {
		super(message);
	}
}
