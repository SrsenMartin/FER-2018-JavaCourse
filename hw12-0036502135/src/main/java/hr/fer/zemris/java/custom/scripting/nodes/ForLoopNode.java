package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

/**
 * Node class that represents for loop.
 * 
 * @author Martin Sršen
 *
 */
public class ForLoopNode extends Node {

	/**
	 * Name of for loop variable.
	 */
	private ElementVariable variable;
	/**
	 * Start expression of foor loop.
	 */
	private Element startExpression;
	/**
	 * End expression of foor loop.
	 */
	private Element endExpression;
	/**
	 * Step expression of foor loop.
	 */
	private Element stepExpression;

	/**
	 * Constructor that assigns value to node.
	 * 
	 * @param variable	Name of for loop variable.
	 * @param startExpression	Start expression of foor loop.
	 * @param endExpression	End expression of foor loop.
	 * @param stepExpression	Step expression of foor loop.
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression,
			Element stepExpression) {
		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}

	/**
	 * Getter method for variable.
	 * 
	 * @return	For loop variable.
	 */
	public ElementVariable getVariable() {
		return variable;
	}

	/**
	 * Getter method for start expression.
	 * 
	 * @return	For loop start expression.
	 */
	public Element getStartExpression() {
		return startExpression;
	}

	/**
	 * Getter method for end expression.
	 * 
	 * @return	For loop end expression.
	 */
	public Element getEndExpression() {
		return endExpression;
	}
	
	/**
	 * Getter method for step expression.
	 * 
	 * @return	For loop step expression.
	 */
	public Element getStepExpression() {
		return stepExpression;
	}

	/**
	 * Method that takes INodeVisitor through argument
	 * which represents action to do on current ForLoopNode object
	 * when called.
	 * 
	 * @param visitor	Represents job to do when ForLoopNode is visited.
	 */
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitForLoopNode(this);
	}

}
