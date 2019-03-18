package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Class that represents pop command.
 * Pops current TurtleState on top of the stack.
 * 
 * @author Martin Sršen
 *
 */
public class PopCommand implements Command {

	/**
	 * Pops current turtle state from top of the stack.
	 * 
	 * @throws EmptyStackException there is no turtle state on stack.
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.popState();
	}
}
