package hr.fer.zemris.java.custom.scripting.parser;

import java.util.Arrays;

import hr.fer.zemris.java.custom.scripting.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.scripting.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexerException;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptToken;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptTokenType;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;

/**
 * Class responsible for parsing tokens got by SmartScriptLexer.
 * Checks whether syntax is valid ,or throws SmartScriptParserException
 * if it is not.
 * 
 * @author Martin Sršen
 *
 */
public class SmartScriptParser {

	/**
	 * responsible for lexing document text.
	 */
	private SmartScriptLexer lexer;
	/**
	 * reference on document node.
	 */
	private DocumentNode documentNode;

	/**
	 * Construstor that takes document to be parsed and
	 * passes it to lexer.
	 * Calls methods that will parse tokens.
	 * 
	 * @param document	String to parse.
	 * @throws SmartScriptParserException	if document is null.
	 */
	public SmartScriptParser(String document) {
		if (document == null) {
			throw new SmartScriptParserException("Parser can't accept document as null value");
		}

		lexer = new SmartScriptLexer(document);

		parseDocument();
	}

	/**
	 * Getter method for documentNode.
	 * 
	 * @return	documentNode.
	 */
	public DocumentNode getDocumentNode() {
		return documentNode;
	}

	/**
	 * Check whether valid tag or text is next.
	 * If tag is valid calls helper methods that will check syntax.
	 * 
	 * @throws SmartScriptParserException	if document has invalid syntax.
	 */
	private void parseDocument() {
		documentNode = new DocumentNode();

		ObjectStack stack = new ObjectStack();
		stack.push(documentNode);

		try {
			SmartScriptToken token;

			while ((token = lexer.nextToken()).getType() != SmartScriptTokenType.EOF) {
				if (isTagOpener(token)) {
					token = lexer.nextToken();

					switch ((String) token.getValue()) {
					case "FOR":
						parseFor(stack);
						break;
					case "END":
						parseEnd(stack);
						break;
					case "=":
						parseEcho(stack);
						break;
					default:
						throw new SmartScriptParserException("Invalid tag keyword: " + token.getValue());
					}
				} else {
					Node parentNode = (Node) stack.peek();
					parentNode.addChildNode(new TextNode((String) token.getValue()));
				}
			}
		} catch (SmartScriptLexerException ex) {
			throw new SmartScriptParserException(ex.getMessage());
		}

		if (stack.size() > 1) {
			throw new SmartScriptParserException("You didn't end all non-empty tags.");
		}
	}

	/**
	 * Check whether current token is tag opener.
	 * 
	 * @param token	token to check if is tag opener.
	 * @return	true if token is tag opener, false otherwise.
	 */
	private boolean isTagOpener(SmartScriptToken token) {
		return token.getType() == SmartScriptTokenType.TAG && token.getValue().equals("open");
	}

	/**
	 * Check whether current token is tag closer.
	 * 
	 * @param token	token to check if is tag closer.
	 * @return	true if token is tag closer, false otherwise.
	 */
	private boolean isTagCloser(SmartScriptToken token) {
		return token.getType() == SmartScriptTokenType.TAG && token.getValue().equals("close");
	}

	/**
	 * Checks if FOR tag has valid syntax.
	 * 
	 * @param stack	ObjectStack with parent nodes in it.
	 * @throws SmartScriptParserException	if FOR tag has invalid syntax.
	 */
	private void parseFor(ObjectStack stack) {
		SmartScriptToken current = lexer.nextToken();
		if (current.getType() != SmartScriptTokenType.VARIABLE) {
			throw new SmartScriptLexerException(current.getValue() + " is not valid variable name.");
		}

		ArrayIndexedCollection forCol = new ArrayIndexedCollection();

		while (!isTagCloser(current)) {
			if (isValidForElement(current)) {
				forCol.add(createElement(current));
			} else if (current.getType() == SmartScriptTokenType.EOF) {
				throw new SmartScriptLexerException("FOR not closed.");
			} else if (current.getType() == SmartScriptTokenType.FUNCTION) {
				throw new SmartScriptLexerException("@" + current.getValue() + " function element.");
			} else {
				throw new SmartScriptLexerException("Invalid FOR element: " + current.getValue());
			}

			current = lexer.nextToken();
		}

		ForLoopNode forNode = getForLoopNode(forCol);

		Node parentNode = (Node) stack.peek();
		parentNode.addChildNode(forNode);
		stack.push(forNode);
	}

	/**
	 * Creates and returns new ForLoopNode based on number of elements.
	 * Elements are contained in ArrayIndexedCollection.
	 * 
	 * @param forCol	collection that contains elements for forLoopNode.
	 * @return	new ForLoopNode.
	 * @throws SmartScriptParserException	if FOR tag has too many or too few arguments.
	 */
	private ForLoopNode getForLoopNode(ArrayIndexedCollection forCol) {
		ElementVariable variable = null;
		Element startExpression = null;
		Element endExpression = null;
		Element stepExpression = null;

		if (forCol.size() == 3) {
			variable = (ElementVariable) forCol.get(0);
			startExpression = (Element) forCol.get(1);
			endExpression = (Element) forCol.get(2);
		} else if (forCol.size() == 4) {
			variable = (ElementVariable) forCol.get(0);
			startExpression = (Element) forCol.get(1);
			stepExpression = (Element) forCol.get(2);
			endExpression = (Element) forCol.get(3);
		} else if (forCol.size() < 3) {
			throw new SmartScriptLexerException("Too few FOR arguments: " + forCol.size());
		} else {
			throw new SmartScriptLexerException("Too many FOR arguments: " + forCol.size());
		}

		return new ForLoopNode(variable, startExpression, endExpression, stepExpression);
	}

	/**
	 * Checks whether given token is valid FOR tag element.
	 * 
	 * @param token	token to check validation.
	 * @return	true if token is valid element, false otherwise.
	 */
	private boolean isValidForElement(SmartScriptToken token) {
		return token.getType() == SmartScriptTokenType.VARIABLE
				|| token.getType() == SmartScriptTokenType.CONSTANT_DOUBLE
				|| token.getType() == SmartScriptTokenType.CONSTANT_INTEGER
				|| token.getType() == SmartScriptTokenType.ELEMENT_STRING;
	}

	/**
	 * Checks if END tag has valid syntax.
	 * 
	 * @param stack	ObjectStack with parent nodes in it.
	 * @throws SmartScriptParserException	if END tag has invalid syntax or
	 * 			if there is no opened tag to end.
	 */
	private void parseEnd(ObjectStack stack) {
		SmartScriptToken current = lexer.nextToken();

		if (!isTagCloser(current)) {
			if (current.getType() == SmartScriptTokenType.EOF) {
				throw new SmartScriptLexerException("END not closed.");
			} else {
				throw new SmartScriptLexerException("Invalid END element: " + current.getValue());
			}
		}

		stack.pop();
		if (stack.isEmpty()) {
			throw new SmartScriptLexerException("You can not end something that is not opened");
		}
	}

	/**
	 * Checks if echo(=) tag has valid syntax.
	 * 
	 * @param stack	ObjectStack with parent nodes in it.
	 * @throws SmartScriptParserException	if echo(=) tag has invalid syntax.
	 */
	private void parseEcho(ObjectStack stack) {
		SmartScriptToken current = lexer.nextToken();

		ArrayIndexedCollection echoCol = new ArrayIndexedCollection();

		while (!isTagCloser(current)) {
			if (isValidEchoElement(current)) {
				echoCol.add(createElement(current));
			} else if (current.getType() == SmartScriptTokenType.EOF) {
				throw new SmartScriptLexerException("ECHO not closed.");
			} else {
				throw new SmartScriptLexerException("Invalid ECHO element: " + current.getValue());
			}

			current = lexer.nextToken();
		}

		Element[] elements = Arrays.copyOf(echoCol.toArray(), echoCol.size(), Element[].class);

		Node parentNode = (Node) stack.peek();
		parentNode.addChildNode(new EchoNode(elements));
	}

	/**
	 * Checks whether given token is valid ECHO(=) tag element.
	 * 
	 * @param token	token to check validation.
	 * @return	true if token is valid element, false otherwise.
	 */
	private boolean isValidEchoElement(SmartScriptToken token) {
		return token.getType() == SmartScriptTokenType.VARIABLE
				|| token.getType() == SmartScriptTokenType.CONSTANT_DOUBLE
				|| token.getType() == SmartScriptTokenType.CONSTANT_INTEGER
				|| token.getType() == SmartScriptTokenType.FUNCTION || token.getType() == SmartScriptTokenType.OPERATOR
				|| token.getType() == SmartScriptTokenType.ELEMENT_STRING;
	}

	/**
	 * Creates and returns new element based on received token.
	 * 
	 * @param token	Token used to create element.
	 * @return	new Element type based on token value.
	 */
	private Element createElement(SmartScriptToken token) {
		switch (token.getType()) {
		case CONSTANT_DOUBLE:
			return new ElementConstantDouble((Double) token.getValue());
		case CONSTANT_INTEGER:
			return new ElementConstantInteger((Integer) token.getValue());
		case FUNCTION:
			return new ElementFunction((String) token.getValue());
		case OPERATOR:
			return new ElementOperator(((Character) token.getValue()).toString());
		case ELEMENT_STRING:
			return new ElementString((String) token.getValue());
		default:
			return new ElementVariable((String) token.getValue());
		}
	}
}
