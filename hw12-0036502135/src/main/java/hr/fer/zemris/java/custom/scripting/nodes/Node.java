package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Base class for all different node types that parser can check.
 * 
 * @author Martin Sršen
 *
 */
public abstract class Node {

	/**
	 * Inner collection where each node stores its child nodes.
	 */
	private List<Node> childNodes;

	/**
	 * Adds child node into collection that stores them.
	 * If collection is null,method creates it.
	 * 
	 * @param child Node that will be added to collection of node children.
	 */
	public void addChildNode(Node child) {
		Objects.requireNonNull(child, "Can't add null node.");
		if (childNodes == null) {
			childNodes = new ArrayList<>();
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
		
		return childNodes.get(index);
	}
	
	/**
	 * Method that takes INodeVisitor through argument
	 * which represents action to do on current node
	 * when called.
	 * 
	 * @param visitor	Represents job to do when node is visited.
	 */
	public abstract void accept(INodeVisitor visitor);
}
