package hr.fer.zemris.java.hw16.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import hr.fer.zemris.java.hw16.jvdraw.components.IColorProvider;
import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.visitor.GeometricalObjectPainter;

import static java.lang.Math.sqrt;

/**
 * Class representing circle tool used to create not filled circles.
 * 
 * @author Martin Sršen
 *
 */
public class CircleTool extends AbstractTool {

	/**
	 * Constructor used to take all needed objects to create new Circle object.
	 * 
	 * @param model	Model used to store saved geometrical objects.
	 * @param fColor	Foreground color.
	 * @param canvas	Canvas used to draw on.
	 */
	public CircleTool(DrawingModel model, IColorProvider fColor, JComponent canvas) {
		super(model, fColor, canvas);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(!pressed) {
			start = e.getPoint();
			pressed = true;
		}else {
			pressed = false;
			model.add(new Circle(start, getRadius(e.getPoint()), fColor.getCurrentColor()));
		}
	}

	@Override
	public void paint(Graphics2D g2d) {
		if(!pressed)	return;
		
		GeometricalObjectPainter.createOval(g2d, fColor.getCurrentColor(), getRadius(end), start, false);
	}
	
	/**
	 * Method used to calculate radius from center and end point.
	 * 
	 * @param end	End mouse pressed point.
	 * @return	circle radius.
	 */
	protected int getRadius(Point end) {
		int xComp = end.x - start.x;
		int yComp = end.y - start.y;
		double a = sqrt(xComp*xComp + yComp*yComp);
		
		return (int) a;
	}
}
