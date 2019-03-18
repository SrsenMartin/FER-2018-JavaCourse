package hr.fer.zemris.java.hw03;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * Program responsible for testing SmartScriptParser
 *  and SmartScriptLexer classes.
 * 
 * @author Martin Sršen
 *
 */
public class SmartScriptTester {

	/**
	 * Called when program is started.
	 * Gets file location through command prompt.
	 * Crates parser and calls it.If exception happens,prints cause.
	 * 
	 * @param args Arguments from command prompt.
	 * @throws IOException if something happends when reading file.
	 */
	public static void main(String[] args) throws IOException {
		if(args.length != 1) {
			throw new IllegalArgumentException("Only allowed 1 argument- file location and name");
		}
		
		String docBody = new String(Files.readAllBytes(Paths.get(args[0])), StandardCharsets.UTF_8);

		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);
		} catch (SmartScriptParserException e) {
			System.out.println("Unable to parse document!");
			System.out.println(e.getMessage());
			System.exit(-1);
		} catch (Exception e) {
			System.out.println("If this line ever executes, you have failed this class!");
			System.exit(-1);
		}

		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = createOriginalDocumentBody(document);
		System.out.println(originalDocumentBody);
	}

	/**
	 * Creates document body that was created by parser.
	 * 
	 * @param document	Document node used to create body.
	 * @return	String representation of document body.
	 * @throws IllegalArgumentException	if document is null.
	 */
	public static String createOriginalDocumentBody(DocumentNode document) {
		if (document == null) {
			throw new IllegalArgumentException("Document can't be null.");
		}

		StringBuilder sb = new StringBuilder();

		for (int childIndex = 0; childIndex < document.numberOfChildren(); childIndex++) {
			Node docChild = document.getChild(childIndex);
			createOriginalChild(sb, docChild);
		}

		return sb.toString();
	}

	/**
	 * Checks which node is child of given node and calls method that will make body for it.
	 * 
	 * @param sb	StringBuilder used to create document body.
	 * @param docChild	parent node.
	 */
	private static void createOriginalChild(StringBuilder sb, Node docChild) {
		if (docChild instanceof ForLoopNode) {
			createOriginalFor(sb, (ForLoopNode) docChild);
		} else if (docChild instanceof TextNode) {
			createOriginalText(sb, (TextNode) docChild);
		} else {
			createOriginalEcho(sb, (EchoNode) docChild);
		}
	}

	/**
	 * Crates body of given EchoNode.
	 * 
	 * @param sb	StringBuilder used to make document body.
	 * @param docChild	EchoNode which is used to make its body.
	 */
	private static void createOriginalEcho(StringBuilder sb, EchoNode docChild) {
		Element[] elements = docChild.getElements();

		sb.append("{$").append("= ");

		for (int index = 0; index < elements.length; index++) {
			String element = generateTagElement(elements[index]);
			sb.append(element + " ");
		}

		sb.append("$}");
	}

	/**
	 * Crates body of given TextNode.
	 * 
	 * @param sb	StringBuilder used to make document body.
	 * @param docChild	TextNode which is used to make its body.
	 */
	private static void createOriginalText(StringBuilder sb, TextNode docChild) {
		String text = docChild.getText().replace("\\", "\\\\").replace("{$", "\\{$");

		sb.append(text);
	}

	/**
	 * Crates body of given ForLoopNode.
	 * 
	 * @param sb	StringBuilder used to make document body.
	 * @param docChild	ForLoopNode which is used to make its body.
	 */
	private static void createOriginalFor(StringBuilder sb, ForLoopNode docChild) {
		String variable = generateTagElement(docChild.getVariable());
		String startExpression = generateTagElement(docChild.getStartExpression());
		String stepExpression = generateTagElement(docChild.getStepExpression());
		String endExpression = generateTagElement(docChild.getEndExpression());

		sb.append("{$").append("FOR ").append(variable).append(" ").append(startExpression).append(" ");

		if (stepExpression != null) {
			sb.append(stepExpression).append(" ");
		}

		sb.append(endExpression).append(" ").append("$}");

		for (int index = 0; index < docChild.numberOfChildren(); index++) {
			Node forChild = docChild.getChild(index);
			createOriginalChild(sb, forChild);
		}

		sb.append("{$END$}");
	}

	/**
	 * Returns String representation of given element.
	 * Adds certain elements if were lose during parsing.
	 * 
	 * @param element	Element that is used to generate String.
	 * @return	String representation of given element.
	 */
	private static String generateTagElement(Element element) {
		if (element == null) {
			return null;
		}

		if (element instanceof ElementString) {
			String innerOriginal = element.asText().replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "\\r")
					.replace("\n", "\\n").replace("\t", "\\t");
			return "\"" + innerOriginal + "\"";
		} else if (element instanceof ElementFunction) {
			return "@" + element.asText();
		} else {
			return element.asText();
		}
	}
}
