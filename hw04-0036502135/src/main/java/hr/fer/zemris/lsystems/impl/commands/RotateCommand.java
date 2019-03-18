package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;
import hr.fer.zemris.math.Vector2D;

/**
 * Class that represents turtle rotate command.
 * Takes angle through constructor and 
 * alters turtle direction rotating its direction vector.
 * 
 * @author Martin Sršen
 *
 */
public class RotateCommand implements Command {

	/**
	 * Angle used to rotate turtle direction.
	 */
	private double angle;
	
	/**
	 * Constructor that takes angle.
	 * 
	 * @param angle	Used to calculate new direction of turtle.
	 */
	public RotateCommand(double angle) {
		this.angle = angle;
	}
	
	/**
	 * Changes new turtle direction based on given angle,
	 * and sets turtle its new direction.
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState current = ctx.getCurrentState();
		Vector2D direction = current.getDirection();
		
		direction.rotate(angle);
	}
	
}
