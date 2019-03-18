package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
* Class responsible for lexing query.
* Accepts query as input and returns valid tokens,
* or throws exception if given text contains invalid elements. 
* 
* @author Martin Sršen
*
*/
public class QueryLexer {

	/**
	 * Given text as array of chars.
	 */
	private char[] data;
	/**
	 * Current index in data array that was read.
	 */
	private int currentIndex;
	/**
	 * Last token that was processed
	 */
	private QueryLexerToken token;
	
	/**
	 * List of all allowed operators.
	 */
	private List<String> operators;
	
	/**
	 * Constructor that takes string query and makes data array out of it.
	 * 
	 * @param query	Input query string that will be processed.
	 * @throws NullPointerException if text is null.
	 */
	public QueryLexer(String query) {
		Objects.requireNonNull(query, "Query can't be null value.");
		
		data = query.toCharArray();
		operators = new ArrayList<String>();
		addOperators();
	}
	
	/**
	 * Adds possible operators to list of operators.
	 */
	private void addOperators() {
		operators.add("<");
		operators.add("<=");
		operators.add(">");
		operators.add(">=");
		operators.add("=");
		operators.add("LIKE");
		operators.add("!=");
	}
	
	/**
	 * Resets lexer to starting state.
	 */
	public void reset() {
		token = null;
		currentIndex = 0;
	}
	
	/**
	 * Returns current token value.
	 * 
	 * @return	token value.
	 */
	public QueryLexerToken getToken() {
		return token;
	}
	
	/**
	 * Process next token and assign its value to token variable,
	 * return that token.
	 * 
	 * @return	Next valid token.
	 * @throws	QueryLexerException	if there is no more data to read,
	 * 						or if next element is invalid.
	 */
	public QueryLexerToken nextToken() {
		getNextToken();
		return token;
	}
	
	/**
	 * Checks if next token is valid type,
	 * if it is creates one,else throws exception.
	 * 
	 * @throws QueryLexerException if next token is invalid type.
	 */
	private void getNextToken() {
		if (token != null && token.getType() == QueryLexerTokenType.EOF) {
			throw new QueryLexerException("No more data to read.");
		}
		
		skipBlanks();
		
		if (currentIndex >= data.length) {
			token = new QueryLexerToken(QueryLexerTokenType.EOF, null);
			return;
		}
		
		if(isComparisonOperator())	return;
		if(isFieldValue())	return;
		if(isStringLiteral())	return;
		if(isAnd())	return;
		
		throw new QueryLexerException("Invalid token type: " + data[currentIndex] + "...");
	}
	
	/**
	 * Checks if next token is field value.
	 * If it is ,that creates new token and moves currentIndex,returns true.
	 * Else return false;
	 * 
	 * @return true if next token is field value token,else false.
	 */
	private boolean isFieldValue() {
		if(Character.isLetter(data[currentIndex])) {
			int start = currentIndex;
			int end = start;
			while(end < data.length && Character.isLetter(data[end])) {
				end++;
			}
			
			String value = new String(data, start, end - start);
			
			if(value.equals("jmbag") ||
					value.equals("lastName") ||
					value.equals("firstName")) {
				token = new QueryLexerToken(QueryLexerTokenType.FIELD_VALUE, value);
				currentIndex = end;
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Checks if next token is comparison operator.
	 * If it is ,that creates new token and moves currentIndex,returns true.
	 * Else return false;
	 * 
	 * @return true if next token is comparison operator token,else false.
	 */
	private boolean isComparisonOperator() {
		if(data[currentIndex] == '"' ||
			(Character.isLetter(data[currentIndex]) && data[currentIndex] != 'L')) {
			return false;
		}
		
		int start = currentIndex;
		int end = start;
		while(end < data.length && data[end] != '"') {
			end++;
		}
		
		String value = new String(data, start, end - start).trim();
		
		if(operators.contains(value)) {
			token = new QueryLexerToken(QueryLexerTokenType.COMPARISON_OPERATOR, value);
			currentIndex = end;
			return true;
		}

		return false;
	}
	
	/**
	 * Checks if next token is stringLiteral.
	 * If it is ,that creates new token and moves currentIndex,returns true.
	 * Else return false;
	 * 
	 * @return true if next token is stringLiteral token,else false.
	 * @throws QueryLexerException if string literal is not closed.
	 */
	private boolean isStringLiteral() {
		if(data[currentIndex] == '"') {
			currentIndex++;
			
			int start = currentIndex;
			while(currentIndex < data.length && data[currentIndex] != '"') {
				currentIndex++;
			}
			int end = currentIndex;
			
			String value = new String(data, start, end - start);
			
			if(data[end] != '"') {
				throw new QueryLexerException("Literal string not closed: " + value);
			}
			currentIndex++;
			
			token = new QueryLexerToken(QueryLexerTokenType.STRING_LITERAL, value);
			
			return true;
		}
		
		
		return false;
	}
	
	/**
	 * Checks if next token is AND token.
	 * If it is ,that creates new token and moves currentIndex,returns true.
	 * Else return false;
	 * 
	 * @return true if next token is AND token,else false.
	 */
	private boolean isAnd() {
		if(currentIndex + 2 < data.length) {
			if(	Character.toUpperCase(data[currentIndex]) == 'A' &&
				Character.toUpperCase(data[currentIndex + 1]) == 'N' &&
				Character.toUpperCase(data[currentIndex + 2]) == 'D' &&
				(!(currentIndex + 3 < data.length) || data[currentIndex + 3] == ' ')) {
				
				token = new QueryLexerToken(QueryLexerTokenType.AND, null);
				currentIndex += 3;
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Goes through all blanks in text.
	 */
	private void skipBlanks() {
		while (currentIndex < data.length) {
			char current = data[currentIndex];

			if (current == ' ') {
				currentIndex++;
				continue;
			}

			break;
		}
	}
	
	/**
	 * Enumeration that represents all possible QueryLexerToken types.
	 */
	enum QueryLexerTokenType {
		FIELD_VALUE,
		COMPARISON_OPERATOR,
		STRING_LITERAL,
		AND,
		EOF
	}
	
	/**
	 * Class that represents one token created by SmartScriptLexer.
	 */
	static class QueryLexerToken {
		/**
		 * Any token type specified in QueryLexerTokenType enumeration.
		 */
		private QueryLexerTokenType type;
		/**
		 * String value of a token.
		 */
		private String value;
		
		/**
		 * Constructor that sets value to token.
		 * 
		 * @param type	token type.
		 * @param value	token value.
		 * @throws NullPointerException if token type is null.
		 */
		public QueryLexerToken(QueryLexerTokenType type, String value) {
			Objects.requireNonNull(type, "QueryLexerToken can't have null type.");
			
			this.type = type;
			this.value = value;
		}

		/**
		 * Returns token value.
		 * 
		 * @return	token value.
		 */
		public QueryLexerTokenType getType() {
			return type;
		}

		/**
		 * Returns token type.
		 * 
		 * @return	token type.
		 */
		public String getValue() {
			return value;
		}
	}
	
	/**
	 * Exception that extends from RunTimeException.
	 * It is thrown if something wrong happens during lexing.
	 *
	 */
	static class QueryLexerException extends RuntimeException {

		private static final long serialVersionUID = 1L;
		
		/**
		 * Default QueryLexerException constructor.
		 */
		public QueryLexerException() {
		}
		
		/**
		 * QueryLexerException constructor with message argument.
		 * 
		 * @param message	Cause of exception.
		 */
		public QueryLexerException(String message) {
			super(message);
		}
	}
}
