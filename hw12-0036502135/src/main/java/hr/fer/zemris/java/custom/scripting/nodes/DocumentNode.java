package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Node class that represents document node.
 * 
 * @author Martin Sršen
 *
 */
public class DocumentNode extends Node {

	/**
	 * Method that takes INodeVisitor through argument
	 * which represents action to do on current DocumentNode object
	 * when called.
	 * 
	 * @param visitor	Represents job to do when DocumentNode is visited.
	 */
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitDocumentNode(this);
	}
}
