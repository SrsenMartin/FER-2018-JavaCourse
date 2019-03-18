package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Class that represents one token created by SmartScriptLexer.
 * 
 * @author Martin Sršen
 *
 */
public class SmartScriptToken {

	/**
	 * Any token type specified in SmartScriptTokenType enumeration.
	 */
	private SmartScriptTokenType type;
	/**
	 * Object value of a token.
	 */
	private Object value;

	/**
	 * Constructor that sets value to token.
	 * 
	 * @param type	token type.
	 * @param value	token value.
	 * @throws SmartScriptLexerException if token type is null.
	 */
	public SmartScriptToken(SmartScriptTokenType type, Object value) {
		if (type == null) {
			throw new SmartScriptLexerException("Token type can't be null.");
		}

		this.type = type;
		this.value = value;
	}

	/**
	 * Returns token value.
	 * 
	 * @return	token value.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Returns token type.
	 * 
	 * @return	token type.
	 */
	public SmartScriptTokenType getType() {
		return type;
	}
}
