package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Exception that extends from RunTimeException.
 * It is thrown if something wrong happens during parsing.
 * 
 * @author Martin Sršen
 *
 */
public class SmartScriptParserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default SmartScriptParserException constructor.
	 */
	public SmartScriptParserException() {
	}

	/**
	 * SmartScriptParserException constructor with message argument.
	 * 
	 * @param message	Cause of exception.
	 */
	public SmartScriptParserException(String message) {
		super(message);
	}
}
