package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Class that represents push command.
 * Pushes copy of current turleState on stack.
 * 
 * @author Martin Sršen
 *
 */
public class PushCommand implements Command {

	/**
	 * Copies current turtle state and pushes it on top of the stack.
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState current = ctx.getCurrentState();
		TurtleState copy = current.copy();

		ctx.pushState(copy);
	}
}
