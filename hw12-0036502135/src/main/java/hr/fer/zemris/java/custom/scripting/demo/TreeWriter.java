package hr.fer.zemris.java.custom.scripting.demo;

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
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * Program responsible for testing SmartScriptParser
 *  and SmartScriptLexer classes.
 *  Visitor pattern is used to go through nodes and execute job for each.
 * 
 * @author Martin Sršen
 *
 */
public class TreeWriter {

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
		WriterVisitor visitor = new WriterVisitor();
		document.accept(visitor);
	}
	
	/**
	 * Implementation of INodeVisitor.
	 * On each visit in node reconstructs node original look
	 * and appends it into StringBuilder.
	 * If valid input is given,
	 * same document body will be printed.
	 */
	public static class WriterVisitor implements INodeVisitor {

		/**
		 * Used to create original document body.
		 */
		StringBuilder sb = new StringBuilder();
		
		/**
		 * Calls accept method on each child.
		 * After body is generated prints it on standard output.
		 * 
		 * @param node DocumentNode to reconstruct.
		 */
		@Override
		public void visitDocumentNode(DocumentNode node) {
			callChildren(node);

			System.out.println(sb.toString());
		}

		/**
		 * When called, reconstruct given EchoNode into original state.
		 * Appends it in stringBuilder.
		 * 
		 * @param node	EchoNode to reconstruct.
		 */
		@Override
		public void visitEchoNode(EchoNode node) {
			Element[] elements = node.getElements();

			sb.append("{$").append("= ");

			for (int index = 0; index < elements.length; index++) {
				String element = generateTagElement(elements[index]);
				sb.append(element + " ");
			}

			sb.append("$}");
		}

		/**
		 * When called, reconstructs given ForLoopNode into original state.
		 * Appends it in stringBuilder.
		 * 
		 * @param node	ForLoopNode to reconstruct.
		 */
		@Override
		public void visitForLoopNode(ForLoopNode node) {
			String variable = generateTagElement(node.getVariable());
			String startExpression = generateTagElement(node.getStartExpression());
			String stepExpression = generateTagElement(node.getStepExpression());
			String endExpression = generateTagElement(node.getEndExpression());

			sb.append("{$").append("FOR ").append(variable).append(" ").append(startExpression).append(" ");

			sb.append(endExpression).append(" ");
			
			if (stepExpression != null) {
				sb.append(stepExpression).append(" ");
			}

			sb.append(("$}"));
			callChildren(node);

			sb.append("{$END$}");
		}

		/**
		 * When called, reconstructs given TextNode into original state.
		 * Appends it in stringBuilder.
		 * 
		 * @param node	ForLoopNode to reconstruct.
		 */
		@Override
		public void visitTextNode(TextNode node) {
			String text = node.getText().replace("\\", "\\\\").replace("{$", "\\{$");

			sb.append(text);
		}
		
		/**
		 * Returns String representation of given element.
		 * Adds certain elements if were lose during parsing.
		 * 
		 * @param element	Element that is used to generate String.
		 * @return	String representation of given element.
		 */
		private String generateTagElement(Element element) {
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
		
		/**
		 * Method that calls accept method on all given nodes' children.
		 * 
		 * @param node	Node used to call its children.
		 */
		private void callChildren(Node node) {
			for (int index = 0; index < node.numberOfChildren(); index++) {
				Node forChild = node.getChild(index);
				forChild.accept(this);
			}
		}
	}
	
}
