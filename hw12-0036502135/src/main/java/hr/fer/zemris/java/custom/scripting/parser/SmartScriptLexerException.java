package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Exception that extends from RunTimeException.
 * It is thrown if something wrong happens during lexing.
 * 
 * @author Martin Sršen
 *
 */
public class SmartScriptLexerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default SmartScriptLexerException constructor.
	 */
	public SmartScriptLexerException() {
	}

	/**
	 * SmartScriptLexerException constructor with message argument.
	 * 
	 * @param message	Cause of exception.
	 */
	public SmartScriptLexerException(String message) {
		super(message);
	}
}
