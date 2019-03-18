package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Interface that respresents node visitor.
 * Has methods that represents actions 
 * that will be executed when given node is visited.
 * 
 * @author Martin Sršen
 *
 */
public interface INodeVisitor {

	/**
	 * Action that will be execute when DoucmentNode node is visited.
	 * 
	 * @param node	DocumentNode to perform action on.
	 */
	public void visitDocumentNode(DocumentNode node);
	/**
	 * Action that will be execute when EchoNode node is visited.
	 * 
	 * @param node	EchoNode to perform action on.
	 */
	public void visitEchoNode(EchoNode node);
	/**
	 * Action that will be execute when ForLoopNode node is visited.
	 * 
	 * @param node	ForLoopNode to perform action on.
	 */
	public void visitForLoopNode(ForLoopNode node);
	/**
	 * Action that will be execute when TextNode node is visited.
	 * 
	 * @param node	TextNode to perform action on.
	 */
	public void visitTextNode(TextNode node);
}
