package hr.fer.zemris.java.hw07.shell;

/**
 * Exception that extends from RunTimeException.
 * It is thrown if something wrong happens during reading or writing.
 * 
 * @author Martin Sršen
 *
 */
public class ShellIOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default ShellIOException constructor.
	 */
	public ShellIOException() {
	}

	/**
	 * ShellIOException constructor with message argument.
	 * 
	 * @param message	Cause of exception.
	 */
	public ShellIOException(String message) {
		super(message);
	}
}
