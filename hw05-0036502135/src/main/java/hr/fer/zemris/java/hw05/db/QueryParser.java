package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw05.db.QueryLexer.QueryLexerException;
import hr.fer.zemris.java.hw05.db.QueryLexer.QueryLexerToken;
import hr.fer.zemris.java.hw05.db.QueryLexer.QueryLexerTokenType;

/**
 * Class responsible for parsing tokens got by QueryLexer.
 * Checks whether syntax is valid ,or throws QueryParserException
 * if it is not.
 * 
 * @author Martin Sršen
 *
 */
public class QueryParser {

	/**
	 * Given query to parse and use for filtering.
	 */
	private String query;
	/**
	 * responsible for lexing document text.
	 */
	private QueryLexer lexer;
	
	/**
	 * Construstor that takes query to be parsed and
	 * passes it to lexer.
	 * 
	 * @param query	Query string to parse.
	 * @throws NullPointerException	if document is null.
	 */
	public QueryParser(String query) {
		this.query = query;
		lexer = new QueryLexer(query);
	}
	
	/*
	 * Checks whether given query is direct query.
	 * 
	 * @return true if given query is direct query,false otherwise.
	 */
	public boolean isDirectQuery() {
		lexer.reset();
		
		try {
			if(lexer.nextToken().getType() != QueryLexerTokenType.FIELD_VALUE ||
					!lexer.getToken().getValue().equals("jmbag")) {
				return false;
			}else if(lexer.nextToken().getType() != QueryLexerTokenType.COMPARISON_OPERATOR ||
					!lexer.getToken().getValue().equals("=")) {
				return false;
			}else if(lexer.nextToken().getType() != QueryLexerTokenType.STRING_LITERAL) {
				return false;
			}else if(lexer.nextToken().getType() != QueryLexerTokenType.EOF) {
				return false;
			}
		}catch(QueryLexerException ex) {
			return false;
		}
		
		
		return true;
	}

	/**
	 * If given query is direct query ,returns string literal representing jmbag,
	 * else throws exception.
	 * 
	 * @return	String representing direct query jmbag.
	 * @throws IllegalArgumentException if query is not direct query.
	 */
	public String getQueriedJMBAG() {	
		if(!isDirectQuery()) {
			throw new IllegalStateException("Query => " + query + " <= is not direct query.");
		}
		
		lexer.reset();
		
		String jmbag = null;
		try {
			while(lexer.nextToken().getType() != QueryLexerTokenType.EOF) {
				if(lexer.getToken().getType() == QueryLexerTokenType.STRING_LITERAL) {
					jmbag = lexer.getToken().getValue();
				}
			}
		}catch(QueryLexerException ex) {
			throw new QueryParserException(ex.getMessage());
		}
		
		return jmbag;
	}
	
	/**
	 * Returns list of conditional expressions parsed using given query.
	 * 
	 * @return	List of conditional expressions.
	 * @throws QueryParserException if something invalid happens during parsing.
	 */
	public List<ConditionalExpression> getQuery() {
		List<ConditionalExpression> list = new ArrayList<>();
		lexer.reset();
		
		try {
			while(lexer.nextToken().getType() != QueryLexerTokenType.EOF) {			
				IFieldValueGetter fieldGetter = getFieldGetter();
				IComparisonOperator operatorGetter = getOperatorGetter();
				String stringLiteral = getStringLiteral();
				
				list.add(new ConditionalExpression(fieldGetter, stringLiteral, operatorGetter));
				if(lexer.nextToken().getType() != QueryLexerTokenType.EOF) {
					checkForAnd();
				}else {
					break;
				}
			}
		}catch(QueryLexerException ex) {
			throw new QueryParserException(ex.getMessage());
		}
		
		return list;
	}
	
	/**
	 * Checks if current token is field value token.
	 * If it is ,returns FieldValueGetter type.
	 * 
	 * @return	FieldValueGetter type based on token value.
	 * @throws QueryParserException if next token is not field getter token.
	 */
	private IFieldValueGetter getFieldGetter() {
		QueryLexerToken current = lexer.getToken();
		
		if(!(current.getType() == QueryLexerTokenType.FIELD_VALUE)) {
			throw new QueryParserException("Invalid query: " + query);
		}
		
		String value = current.getValue();
		if(value.equals("jmbag"))	return FieldValueGetters.JMBAG;
		if(value.equals("firstName"))	return FieldValueGetters.FIRST_NAME;
		
		return FieldValueGetters.LAST_NAME;
	}
	
	/**
	 * Checks if next token is comparison operator token.
	 * If it is ,returns ComparisonOperator type.
	 * 
	 * @return	ComparisonOperator type based on token value.
	 * @throws QueryParserException if next token is not comparison operator token.
	 */
	private IComparisonOperator getOperatorGetter() {
		QueryLexerToken current = lexer.nextToken();
		
		if(!(current.getType() == QueryLexerTokenType.COMPARISON_OPERATOR) ||
				current.getType() == QueryLexerTokenType.EOF) {
			throw new QueryParserException("Invalid query: " + query);
		}
		
		String value = current.getValue();
		if(value.equals("<"))	return ComparisonOperators.LESS;
		if(value.equals("<="))	return ComparisonOperators.LESS_OR_EQUALS;
		if(value.equals(">"))	return ComparisonOperators.GREATER;
		if(value.equals(">="))	return ComparisonOperators.GREATER_OR_EQUALS;
		if(value.equals("="))	return ComparisonOperators.EQUALS;
		if(value.equals("!="))	return ComparisonOperators.NOT_EQUALS;
		if(value.equals("<"))	return ComparisonOperators.LESS;
		
		return ComparisonOperators.LIKE;
	}
	
	/**
	 * Checks if next token is string literal token.
	 * If it is ,returns its value.
	 * 
	 * @return	value of string literal token.
	 * @throws QueryParserException if next token is not string literal token.
	 */
	private String getStringLiteral() {
		QueryLexerToken current = lexer.nextToken();
		
		if(!(current.getType() == QueryLexerTokenType.STRING_LITERAL) ||
				current.getType() == QueryLexerTokenType.EOF) {
			throw new QueryParserException("Invalid query: " + query);
		}
		
		return current.getValue();
	}
	
	/**
	 * Checks if expected current token is AND token.
	 * 
	 * @throws QueryParserException if current token is not AND token.
	 */
	private void checkForAnd() {
		QueryLexerToken current = lexer.getToken();
	
		if(!(current.getType() == QueryLexerTokenType.AND)) {
			throw new QueryParserException("Invalid query: " + query);
		}
	}
	
	/**
	 * Exception that extends from RunTimeException.
	 * It is thrown if something wrong happens during parsing.
	 */
	class QueryParserException extends RuntimeException	{
		
		private static final long serialVersionUID = 1L;

		/**
		 * Default SmartScriptParserException constructor.
		 */
		public QueryParserException() {
		}
		
		/**
		 * QueryParserException constructor with message argument.
		 * 
		 * @param message	Cause of exception.
		 */
		public QueryParserException(String message) {
			super(message);
		}
	}
}
