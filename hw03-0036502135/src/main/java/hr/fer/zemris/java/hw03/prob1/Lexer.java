package hr.fer.zemris.java.hw03.prob1;

/**
 * Class that accepts text as input and returns valid tokens,
 * or throws exception if given text contains invalid elements. 
 * 
 * @author Martin Sršen
 *
 */
public class Lexer {

	/**
	 * Given text as array of chars.
	 */
	private char[] data;
	/**
	 * Last token that was processed
	 */
	private Token token;
	/**
	 * Current index in data array that was read.
	 */
	private int currentIndex;
	/**
	 * Current lexer state.
	 */
	private LexerState state;

	/**
	 * Constructor that takes text sample and makes data array out of it.
	 * Sets state to BASIC Lexer State.
	 * 
	 * @param text	Input text that will be processed.
	 * @throws IllegalArgumentException if text is null.
	 */
	public Lexer(String text) {
		if (text == null) {
			throw new IllegalArgumentException("Lexer can't accept text as null value.");
		}

		data = text.toCharArray();
		state = LexerState.BASIC;
	}

	/**
	 * Process next token and assign its value to token variable,
	 * return that token.
	 * 
	 * @return	Next valid token.
	 * @throws	LexerException	if there is no more data to read,
	 * 						or if next element is invalid.
	 */
	public Token nextToken() {
		getNextToken();
		return token;
	}

	/**
	 * Returns current token value.
	 * 
	 * @return	token value.
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * Sets lexer state to any allowed state.
	 * 
	 * @param	state	State to set lexer to.
	 * @throws	IllegalArgumentException if state s null;
	 */
	public void setState(LexerState state) {
		if (state == null) {
			throw new IllegalArgumentException("Lexer state can't be null.");
		}

		this.state = state;
	}

	/**
	 * Check if end was already read or
	 * passes lexing to next methods.
	 * 
	 * @throws LexerException if end was read or next element is invalid.
	 */
	private void getNextToken() {
		if (token != null && token.getType() == TokenType.EOF) {
			throw new LexerException("No more data to read.");
		}

		skipBlanks();

		if (currentIndex >= data.length) {
			token = new Token(TokenType.EOF, null);
			return;
		}

		if (state == LexerState.EXTENDED) {
			getExtendedToken();
		} else {
			getBasicToken();
		}
	}

	/**
	 * Checks what next element is in Basic state, and passes lexing to next method.
	 * Changes state if # was read to Extended state.
	 * 
	 * @throws LexerException if next element is invalid.
	 */
	private void getBasicToken() {
		if (isNextString())	return;
		if (isNextNumber())	return;
		// u ostalim slucajevima je simbol sigurno.
		getNextSymbol();

		if (token.getValue().equals("#")) {
			setState(LexerState.EXTENDED);
		}
	}

	/**
	 * Reads until next element is # and makes new token.
	 * After # is read changes state to Basic.
	 */
	private void getExtendedToken() {
		int start = currentIndex;

		while (currentIndex < data.length && data[currentIndex] != '#' && !isBlank(data[currentIndex])) {
			currentIndex++;
		}

		int end = currentIndex;

		String tokenValue = new String(data, start, end - start);
		token = new Token(TokenType.WORD, tokenValue);

		if (data[currentIndex] == '#') {
			setState(LexerState.BASIC);
		}
	}

	/**
	 * Checks if next element is String element,
	 * if it is makes new token.
	 * 
	 * @return	true if next element is String element.
	 * @throws	LexerException if next element is invalid.
	 */
	private boolean isNextString() {
		if (Character.isLetter(data[currentIndex]) || isNextEscape()) {
			StringBuilder wordBuilder = new StringBuilder();

			if (isNextEscape()) {
				currentIndex++;
			}

			wordBuilder.append(data[currentIndex++]);

			while (currentIndex < data.length && (Character.isLetter(data[currentIndex]) || isNextEscape())) {
				if (isNextEscape()) {
					currentIndex++;
				}

				wordBuilder.append(data[currentIndex++]);
			}

			String tokenValue = wordBuilder.toString();
			token = new Token(TokenType.WORD, tokenValue);

			return true;
		}

		return false;
	}

	/**
	 * Checks if next element is valid escape element.
	 * 
	 * @return true if next element is escape.
	 * @throws	LexerException	if invalid escape sequence was read.
	 */
	private boolean isNextEscape() {
		if (data[currentIndex] == '\\') {
			if (data.length > currentIndex + 1
					&& (Character.isDigit(data[currentIndex + 1]) || data[currentIndex + 1] == '\\')) {
				return true;
			}

			throw new LexerException("Invalid escape sequence.");
		}

		return false;
	}

	/**
	 * Checks if next element is valid number,
	 * if it is makes new token.
	 * 
	 * @return	true if next element is number.
	 * @throws	LexerException if read number is too big.
	 */
	private boolean isNextNumber() {
		if (Character.isDigit(data[currentIndex])) {
			int start = currentIndex;

			currentIndex++;
			while (currentIndex < data.length && Character.isDigit(data[currentIndex])) {
				currentIndex++;
			}

			int end = currentIndex;

			String numberAsString = new String(data, start, end - start);

			Long longValue = null;
			try {
				longValue = Long.parseLong(numberAsString);
			} catch (NumberFormatException ex) {
				throw new LexerException("Too big number: " + numberAsString);
			}

			token = new Token(TokenType.NUMBER, longValue);

			return true;
		}

		return false;
	}

	/**
	 * Makes new node if next element is symbol.
	 */
	private void getNextSymbol() {
		token = new Token(TokenType.SYMBOL, data[currentIndex]);
		currentIndex++;
	}

	/**
	 * Goes through all blanks in text.
	 */
	private void skipBlanks() {
		while (currentIndex < data.length) {
			char current = data[currentIndex];

			if (isBlank(current)) {
				currentIndex++;
				continue;
			}

			break;
		}
	}

	/**
	 * Checks if given element is blank or not.
	 * 
	 * @param character	char to check if it is blank.
	 * @return	true if character is blank, false otherwise.
	 */
	private boolean isBlank(char character) {
		return character == '\r' || character == '\n' || character == '\t' || character == ' ';
	}
}
