package hr.fer.zemris.java.hw03.prob1;

/**
 * Class that represents one token created by lexing.
 * 
 * @author Martin Sršen
 *
 */
public class Token {

	/**
	 * Any token type specified in TokenType enumeration.
	 */
	private TokenType type;
	/**
	 * Object value of a token.
	 */
	private Object value;

	/**
	 * Constructor that sets value to token.
	 * 
	 * @param type	token type.
	 * @param value	token value.
	 * @throws IllegalArgumentException if token type is null.
	 */
	public Token(TokenType type, Object value) {
		if (type == null) {
			throw new IllegalArgumentException("Token type can't be null.");
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
	public TokenType getType() {
		return type;
	}
}
