package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.lsystems.Painter;

/**
 * The root command in commands hierarchy.
 * Represents command to be executed using given context and painter.
 * 
 * @author Martin Sršen
 *
 */
public interface Command {

	/**
	 * Method that every class that implements this interface must have.
	 * Does certain action on current context.
	 * 
	 * @param ctx	Represents turtle states.
	 * @param painter	Used to draw lines by a given coordinates.
	 */
	void execute(Context ctx, Painter painter);
}
