package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Node class that represents text.
 * 
 * @author Martin Sršen
 *
 */
public class TextNode extends Node {

	/**
	 * String value of TextNode
	 */
	private String text;

	/**
	 * Constructor that assigns value to node.
	 * 
	 * @param text String to assign to TextNode.
	 */
	public TextNode(String text) {
		this.text = text;
	}

	/**
	 * Getter method that returns text.
	 * 
	 * @return String representation of TextNode.
	 */
	public String getText() {
		return text;
	}

}
