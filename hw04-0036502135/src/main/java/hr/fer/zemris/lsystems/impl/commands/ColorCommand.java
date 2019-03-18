package hr.fer.zemris.lsystems.impl.commands;

import java.awt.Color;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Class that represents turtle color command.
 * Takes color through constructor and 
 * alters turtle line color.
 * 
 * @author Martin Sršen
 *
 */
public class ColorCommand implements Command {

	/**
	 * Color used to alter turtle line color.
	 */
	private Color color;

	/**
	 * Constructor that takes color.
	 * 
	 * @param color	Used to change line color of turtle.
	 */
	public ColorCommand(Color color) {
		this.color = color;
	}

	/**
	 * Changes turtle line color by a given color.
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState current = ctx.getCurrentState();
		
		current.setColor(color);
	}
}
