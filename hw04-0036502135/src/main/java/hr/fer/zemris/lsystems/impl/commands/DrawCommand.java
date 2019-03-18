package hr.fer.zemris.lsystems.impl.commands;

import java.awt.Color;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;
import hr.fer.zemris.math.Vector2D;

/**
 * Class that represents turtle draw command.
 * Takes step through constructor and calculates
 * where turtle must go,
 * paints line from old to new turtle position,
 * alters position of turtle.
 * 
 * @author Martin Sršen
 *
 */
public class DrawCommand implements Command {

	/**
	 * Step used to calculate new position.
	 */
	private double step;
	
	/**
	 * Constructor that takes step.
	 * 
	 * @param step	Used to calculate new position of turtle.
	 */
	public DrawCommand(double step) {
		this.step = step;
	}
	
	/**
	 * Calculates where turtle must go with step,
	 * paints line from old to new turtle position,
	 * changes current turtle position based on step.
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState current = ctx.getCurrentState();
		Vector2D position = current.getPosition();
		Vector2D direction = current.getDirection();
		
		double scale = current.getScale();
		
		Vector2D translateBy = direction.scaled(step * scale);
		
		double x0 = position.getX();
		double y0 = position.getY();
		double x1 = x0 + translateBy.getX();
		double y1 = y0 + translateBy.getY();
		Color color = current.getColor();
		float size = 1f;
		
		painter.drawLine(x0, y0, x1, y1, color, size);
		
		position.translate(translateBy);
	}
}
