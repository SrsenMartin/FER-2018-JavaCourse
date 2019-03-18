package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;
import hr.fer.zemris.math.Vector2D;

/**
 * Class that represents turtle skip command.
 * Takes step through constructor and calculates
 * where turtle must go,
 * alters position of turtle.
 * 
 * @author Martin Sršen
 *
 */
public class SkipCommand implements Command {

	/**
	 * Step used to calculate new position.
	 */
	private double step;
	
	/**
	 * Constructor that takes step.
	 * 
	 * @param step	Used to calculate new position of turtle.
	 */
	public SkipCommand(double step) {
		this.step = step;
	}
	
	/**
	 * Calculates where turtle must go with step,
	 * changes current turtle position based on step.
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState current = ctx.getCurrentState();
		Vector2D position = current.getPosition();
		Vector2D direction = current.getDirection();
		
		double scale = current.getScale();
		
		Vector2D translateBy = direction.scaled(step * scale);
		
		position.translate(translateBy);
	}
}
