package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Arrays;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * Node class that represents echo node.
 * 
 * @author Martin Sršen
 *
 */

public class EchoNode extends Node {

	/**
	 * Array of elements in echo node.
	 */
	private Element[] elements;

	/**
	 * Constructor that assigns value to node.
	 * 
	 * @param elements array of elements in echo node.
	 */
	public EchoNode(Element[] elements) {
		this.elements = elements;
	}

	/**
	 * Getter method that returns array of echo elements.
	 * 
	 * @return array of Echo elements.
	 */
	public Element[] getElements() {
		return Arrays.copyOf(elements, elements.length);
	}

	/**
	 * Method that takes INodeVisitor through argument
	 * which represents action to do on current EchoNode object
	 * when called.
	 * 
	 * @param visitor	Represents job to do when EchoNode is visited.
	 */
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitEchoNode(this);
	}
}
