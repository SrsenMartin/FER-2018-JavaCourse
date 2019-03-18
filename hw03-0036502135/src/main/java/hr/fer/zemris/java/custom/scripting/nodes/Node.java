package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.collections.ArrayIndexedCollection;

/**
 * Base class for all different node types that parser can check.
 * 
 * @author Martin Sršen
 *
 */
public class Node {

	/**
	 * Inner collection where each node stores its child nodes.
	 */
	private ArrayIndexedCollection childNodes;

	/**
	 * Adds child node into collection that stores them.
	 * If collection is null,method creates it.
	 * 
	 * @param child Node that will be added to collection of node children.
	 */
	public void addChildNode(Node child) {
		if (childNodes == null) {
			childNodes = new ArrayIndexedCollection();
		}

		childNodes.add(child);
	}

	/**
	 * Returns number of children nodes this node currently has.
	 * 
	 * @return number of children this node has.
	 */
	public int numberOfChildren() {
		if(childNodes == null)	return 0;
		
		return childNodes.size();
	}

	/**
	 * Returns child node at certain index in collection of children.
	 * 
	 * @param index	Index of child node to return.
	 * @return	Child node at given index.
	 * @throws IndexOutOfBoundsException if invalid index is given.
	 */
	public Node getChild(int index) {
		if(childNodes == null) {
			throw new NullPointerException("You need to add elements brfore getting.");
		}
		
		return (Node) childNodes.get(index);
	}
}
