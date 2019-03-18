package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Enumeration that represents all possible SmartScriptToken types.
 * 
 * @author Martin Sršen
 *
 */
public enum SmartScriptTokenType {
	EOF,
	VARIABLE,
	CONSTANT_INTEGER,
	CONSTANT_DOUBLE,
	TEXT,
	FUNCTION,
	OPERATOR,
	TAG,
	ELEMENT_STRING
}
