package hr.fer.zemris.java.hw03.prob1;

/**
 * Exception that extends from RunTimeException.
 * It is thrown if something wrong happens during lexing.
 * 
 * @author Martin Sršen
 *
 */
public class LexerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default LexerException constructor.
	 */
	public LexerException() {
	}

	/**
	 * LexerException constructor with message argument.
	 * 
	 * @param message	Cause of a exception.
	 */
	public LexerException(String message) {
		super(message);
	}
}
