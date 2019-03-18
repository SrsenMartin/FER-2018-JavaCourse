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

	/**
	 * Method that takes INodeVisitor through argument
	 * which represents action to do on current TextNode object
	 * when called.
	 * 
	 * @param visitor	Represents job to do when TextNode is visited.
	 */
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitTextNode(this);
	}
}
