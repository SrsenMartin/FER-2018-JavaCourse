package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Class that represents turtle scale command.
 * Takes factor through constructor and 
 * alters turtle scale by multiplication with given factor.
 * 
 * @author Martin Sršen
 *
 */
public class ScaleCommand implements Command {

	/**
	 * Factor used to change turtle scale.
	 */
	private double factor;
	
	/**
	 * Constructor that takes factor.
	 * 
	 * @param factor	Used to calculate new scale of turtle.
	 */
	public ScaleCommand(double factor) {
		this.factor = factor;
	}
	
	/**
	 * Calculates new turtle scale and alters it.
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState current = ctx.getCurrentState();
		
		double currentScale = current.getScale();
		double newScale = currentScale * factor;
		
		current.setScale(newScale);
	}
}
