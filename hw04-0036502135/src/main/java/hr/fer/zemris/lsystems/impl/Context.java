package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Class that represents current context of turtle states.
 * Ensures methods that can return current state,
 * add new state or remove current state.
 * Internally uses stack to save states.
 * 
 * @author Martin Sršen
 *
 */
public class Context {

	/**
	 * Stack that saves all turtleStates.
	 */
	private ObjectStack turtleStates;
	
	/**
	 * Default constructor that makes new stack
	 * used to save turtleStates.
	 */
	public Context() {
		turtleStates = new ObjectStack();
	}
	
	/**
	 * Returns current(last added) turtle state 
	 * from top of the stack.
	 * 
	 * @return	current last added TurtleState
	 * @throws EmptyStackException if stack doesn't contain
	 * any turtle state.
	 */
	public TurtleState getCurrentState() {
		return (TurtleState) turtleStates.peek();
	}
	
	/**
	 * Adds new turtle state on top of the stack.
	 * 
	 * @param state	TurtleState to be added.
	 * @throws NullPointerException if given state is null.
	 */
	public void pushState(TurtleState state) {
		if(state == null) {
			throw new NullPointerException("State can't have null value");
		}
		
		turtleStates.push(state);
	}
	
	/**
	 * Removes current(last added) turtle state
	 * from top of the stack.
	 * 
	 * @throws EmptyStackException if stack doesn't contain
	 * any turtle state.
	 */
	public void popState() {
		turtleStates.pop();
	}
}
