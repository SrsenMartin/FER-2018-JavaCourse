package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Class responsible for lexing input.
 * Accepts text as input and returns valid tokens,
 * or throws exception if given text contains invalid elements. 
 * 
 * @author Martin Sršen
 *
 */
public class SmartScriptLexer {

	/**
	 * Given text as array of chars.
	 */
	private char[] data;
	/**
	 * Last token that was processed
	 */
	private SmartScriptToken token;
	/**
	 * Current index in data array that was read.
	 */
	private int currentIndex;
	/**
	 * Current SmartScriptLexer state.
	 */
	private SmartScriptLexerState state;

	/**
	 * Constructor that takes text document and makes data array out of it.
	 * Sets SmartScriptLexer state to Text.
	 * 
	 * @param text	Input document that will be processed.
	 * @throws SmartScriptLexerException if text is null.
	 */
	public SmartScriptLexer(String text) {
		if (text == null) {
			throw new SmartScriptLexerException("Lexer can't accept text as null value.");
		}

		data = text.toCharArray();
		setState(SmartScriptLexerState.TEXT);
	}

	/**
	 * Process next token and assign its value to token variable,
	 * return that token.
	 * 
	 * @return	Next valid token.
	 * @throws	SmartScriptLexerException	if there is no more data to read,
	 * 						or if next element is invalid.
	 */
	public SmartScriptToken nextToken() {
		getNextToken();
		return token;
	}

	/**
	 * Returns current token value.
	 * 
	 * @return	token value.
	 */
	public SmartScriptToken getToken() {
		return token;
	}

	/**
	 * Sets SmartScriptLexer state to any allowed state.
	 * 
	 * @param	state	State to set SmartScriptLexer to.
	 * @throws	SmartScriptLexerException if state s null;
	 */
	public void setState(SmartScriptLexerState state) {
		if (state == null) {
			throw new SmartScriptLexerException("Lexer state can't be null.");
		}

		this.state = state;
	}

	/**
	 * Check if end was already read or
	 * passes lexing to next methods.
	 * 
	 * @throws SmartScriptLexerException if end was read or next element is invalid.
	 */
	private void getNextToken() {
		if (token != null && token.getType() == SmartScriptTokenType.EOF) {
			throw new SmartScriptLexerException("No more data to read.");
		}

		if (currentIndex >= data.length) {
			token = new SmartScriptToken(SmartScriptTokenType.EOF, null);
			return;
		}

		if (state == SmartScriptLexerState.TEXT) {
			getTextToken();
		} else if (state == SmartScriptLexerState.TAG) {
			getTagToken();
		}
	}

	/**
	 * Checks what next element is in Tag state, and passes lexing to next method.
	 * 
	 * @throws SmartScriptLexerException if next element is invalid.
	 */
	private void getTagToken() {
		skipBlanks();
		if (currentIndex >= data.length)	return;

		if (isTagCloser())	return;
		if (isKeyWord())	return;
		if (isVariable())	return;
		if (isElementString())	return;
		if (isNumberConstant())	return;
		if (isFunction())	return;
		if (isOperator())	return;

		throw new SmartScriptLexerException("Invalid Tag character: " + data[currentIndex]);
	}

	/**
	 * Reads until next element is tag opener and makes new token.
	 * 
	 * @throws SmartScriptLexerException if invalid text element was read.
	 */
	private void getTextToken() {
		if (isTagOpener())	return;

		StringBuilder sb = new StringBuilder();

		while (currentIndex < data.length && !checkIfOpenerNext()) {
			checkEscape();
			sb.append(data[currentIndex++]);
		}

		String tokenValue = sb.toString();
		token = new SmartScriptToken(SmartScriptTokenType.TEXT, tokenValue);
	}

	/**
	 * Checks whether next element is valid operator.
	 * 
	 * @return	true if next element is operator,false otherwise.
	 */
	private boolean isOperator() {
		char current = data[currentIndex];

		if (current == '+' || current == '/' || current == '*' || current == '^'
				|| (current == '-' && !isNegativeDigit())) {
			currentIndex++;
			token = new SmartScriptToken(SmartScriptTokenType.OPERATOR, current);

			return true;
		}

		return false;
	}

	/**
	 * Checks whether next element is negative number.
	 * 
	 * @return	if next number is negative digit, false otherwise.
	 */
	private boolean isNegativeDigit() {
		int nextPosition = currentIndex + 1;

		if (data[currentIndex] == '-' && nextPosition < data.length && Character.isDigit(data[nextPosition])) {
			return true;
		}

		return false;
	}

	/**
	 * Checks whether next element is valid function element.
	 * 
	 * @return	true if next element is function,false otherwise.
	 * @throws	SmartScriptLexerException	if function has invalid name.
	 */
	private boolean isFunction() {
		if (data[currentIndex] == '@') {
			currentIndex++;

			String variable = getValidName();

			if (variable == null) {
				throw new SmartScriptLexerException("Function must have valid name.");
			}

			token = new SmartScriptToken(SmartScriptTokenType.FUNCTION, variable);
			return true;
		}

		return false;
	}

	/**
	 * Checks whether next element is valid element String.
	 * 
	 * @return	true if next element is element String ,false otherwise.
	 * @throws	SmartScriptLexerException	if invalid element String was read.
	 */
	private boolean isElementString() {
		if (data[currentIndex] == '"') {
			StringBuilder sb = new StringBuilder();

			currentIndex++;
			while (currentIndex < data.length && data[currentIndex] != '"') {
				if (checkEscape()) {
					if (isEscapeBlankChars(sb)) {
						currentIndex++;
						continue;
					}
				}

				sb.append(data[currentIndex++]);
			}
			currentIndex++;

			String tokenValue = sb.toString();
			token = new SmartScriptToken(SmartScriptTokenType.ELEMENT_STRING, tokenValue);

			return true;
		}

		return false;
	}

	/**
	 * Checks if current element is escape blank in element String.
	 * 
	 * @param sb	StringBuilder that	creates element String.
	 * @return	true if current element is escape blank element.
	 */
	private boolean isEscapeBlankChars(StringBuilder sb) {
		if (data[currentIndex] == 'r') {
			sb.append('\r');
		} else if (data[currentIndex] == 't') {
			sb.append('\t');
		} else if (data[currentIndex] == 'n') {
			sb.append('\n');
		} else {
			return false;
		}

		return true;
	}

	/**
	 * Checks whether next element is valid variable.
	 * 
	 * @return	true if next element is variable ,false otherwise.
	 * @throws	SmartScriptLexerException	if variable has invalid name.
	 */
	private boolean isVariable() {
		String variable = getValidName();

		if (variable == null) {
			return false;
		}

		token = new SmartScriptToken(SmartScriptTokenType.VARIABLE, variable);

		return true;
	}

	/**
	 * Checks if function or variable have valid name, and returns it.
	 * 
	 * @return	variable name if it is valid name or null if next element is not variable or function.
	 * @throws	SmartScriptLexerException	if invalid function or variable name is read.
	 */
	private String getValidName() {
		if (data[currentIndex] == '_') {
			throw new SmartScriptLexerException("Invalid variable/function name.Can't start with _");
		}

		if (!Character.isLetter(data[currentIndex])) {
			return null;
		}

		int start = currentIndex;

		while (currentIndex < data.length && (Character.isDigit(data[currentIndex])
				|| Character.isLetter(data[currentIndex]) || data[currentIndex] == '_')) {
			currentIndex++;
		}

		int end = currentIndex;
		String variable = new String(data, start, end - start);

		return variable;

	}

	/**
	 * Checks if next element is valid Tag keyword.
	 * 
	 * @return	true if next element is valid keyword,false otherwise.
	 */
	private boolean isKeyWord() {
		if (Character.isLetter(data[currentIndex])) {
			int start = currentIndex;

			while (currentIndex < data.length && Character.isLetter(data[currentIndex])) {
				currentIndex++;
			}

			int end = currentIndex;

			String word = new String(data, start, end - start);

			if (word.toUpperCase().equals("FOR") || word.toUpperCase().equals("END")) {
				token = new SmartScriptToken(SmartScriptTokenType.TAG, word.toUpperCase());
				return true;
			}

			currentIndex = start;
		} else if (data[currentIndex] == '=') {
			token = new SmartScriptToken(SmartScriptTokenType.TAG, "=");
			currentIndex++;
			return true;
		}

		return false;
	}

	/**
	 * Checks whether next element is valid tag opener,if it is
	 * makes new token and moves currentIndex.
	 * 
	 * @return	true if next element is tag opener,false otherwise.
	 */
	private boolean isTagOpener() {
		if (checkIfOpenerNext()) {
			currentIndex += 2;
			token = new SmartScriptToken(SmartScriptTokenType.TAG, "open");

			setState(SmartScriptLexerState.TAG);

			return true;
		}

		return false;
	}

	/**
	 * Checks if tag opener is next.
	 * 
	 * @return	true if tag opener is next, false otherwise.
	 */
	private boolean checkIfOpenerNext() {
		int nextPosition = currentIndex + 1;

		if (data[currentIndex] == '{' && nextPosition < data.length && data[nextPosition] == '$') {
			return true;
		}

		return false;
	}

	/**
	 * Checks whether next element is valid tag close,if it is
	 * makes new token and moves currentIndex.
	 * 
	 * @return	true if next element is tag closer,false otherwise.
	 */
	private boolean isTagCloser() {

		if (data[currentIndex] != '$') {
			return false;
		}

		int nextPosition = currentIndex + 1;

		if (nextPosition < data.length && data[nextPosition] == '}') {
			currentIndex += 2;
			token = new SmartScriptToken(SmartScriptTokenType.TAG, "close");

			setState(SmartScriptLexerState.TEXT);

			return true;
		}

		return false;
	}

	/**
	 * Checks whether next element is valid number and creates
	 * new token if it is.
	 * 
	 * @return	true if next element is number, false otherwise.
	 * @throws SmartScriptLexerException	if invalid number was read.
	 */
	private boolean isNumberConstant() {
		if (Character.isDigit(data[currentIndex]) || isNegativeDigit()) {
			String numberAsString = getNumberAsString();

			try {
				if (!numberAsString.contains(".")) {
					int integer = Integer.parseInt(numberAsString);
					token = new SmartScriptToken(SmartScriptTokenType.CONSTANT_INTEGER, integer);
				} else {
					double doubleValue = Double.parseDouble(numberAsString);
					token = new SmartScriptToken(SmartScriptTokenType.CONSTANT_DOUBLE, doubleValue);
				}
			} catch (NumberFormatException ex) {
				throw new SmartScriptLexerException("Invalid number: " + numberAsString);
			}

			return true;
		}

		return false;
	}

	/**
	 * Returns next element's string representation of number.
	 * 
	 * @return	number if valid number as String was read.
	 * @throws SmartScriptLexerException	if invalid number format is read.
	 */
	private String getNumberAsString() {
		int dots = 0;
		int start = currentIndex;

		if (isNegativeDigit()) {
			currentIndex++;
		}
		while (currentIndex < data.length && Character.isDigit(data[currentIndex])) {
			currentIndex++;

			if (currentIndex < data.length && data[currentIndex] == '.') {
				currentIndex++;
				dots++;
			}
		}

		if (dots > 1) {
			throw new SmartScriptLexerException("Invalid number: too many dots");
		}

		int end = currentIndex;

		String numberAsString = new String(data, start, end - start);

		if (currentIndex - 1 > 0 && data[currentIndex - 1] == '.') {
			throw new SmartScriptLexerException(
					"Invalid decimal number.There must be something after dot: " + numberAsString);
		}

		if (currentIndex < data.length && (Character.isLetter(data[currentIndex]) || data[currentIndex] == '_')) {
			throw new SmartScriptLexerException("Invalid variable name: " + numberAsString + "...");
		}

		return numberAsString;
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
	 * Checks if next element is valid escape element.
	 * 
	 * @return true if next element is valid escape.
	 * @throws	SmartScriptLexerException	if invalid escape sequence was read.
	 */
	private boolean checkEscape() {
		if (data[currentIndex] != '\\') {
			return false;
		}

		if (currentIndex + 1 >= data.length) {
			throw new SmartScriptLexerException("Invalid text escape sequence, out of bounds.");
		}

		char next = data[++currentIndex];

		if (state == SmartScriptLexerState.TEXT && (next == '\\' || next == '{')) {
			return true;
		}

		if (state == SmartScriptLexerState.TAG
				&& (next == '"' || next == '\\' || next == 'r' || next == 't' || next == 'n')) {
			return true;
		}

		throw new SmartScriptLexerException("Invalid text escape sequence. \\" + next);
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
