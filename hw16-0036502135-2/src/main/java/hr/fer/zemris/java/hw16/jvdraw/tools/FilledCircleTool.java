package hr.fer.zemris.java.hw16.jvdraw.tools;

import static java.lang.Math.sqrt;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import hr.fer.zemris.java.hw16.jvdraw.components.IColorProvider;
import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.visitor.GeometricalObjectPainter;

/**
 * Class representing filled circle tool used to create filled circles.
 * 
 * @author Martin Sršen
 *
 */
public class FilledCircleTool extends AbstractTool {

	/**
	 * Background filled circle color.
	 */
	private IColorProvider bColor;
	
	/**
	 * Constructor used to take all needed objects to create new FilledCircle object.
	 * 
	 * @param model	Model used to store saved geometrical objects.
	 * @param fColor	Foreground color.
	 * @param bColor	Background color.
	 * @param canvas	Canvas used to draw on.
	 */
	public FilledCircleTool(DrawingModel model, IColorProvider fColor, IColorProvider bColor, JComponent canvas) {
		super(model, fColor, canvas);
		this.bColor = bColor;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(!pressed) {
			start = e.getPoint();
			pressed = true;
		}else {
			pressed = false;
			model.add(new FilledCircle(start, getRadius(e.getPoint()), fColor.getCurrentColor(), bColor.getCurrentColor()));
		}
	}

	@Override
	public void paint(Graphics2D g2d) {
		if(!pressed)	return;
		
		GeometricalObjectPainter.createOval(g2d, fColor.getCurrentColor(), getRadius(end), start, false);
		GeometricalObjectPainter.createOval(g2d, bColor.getCurrentColor(), getRadius(end), start, true);
	}
	
	/**
	 * Method used to calculate radius from center and end point.
	 * 
	 * @param end	End mouse pressed point.
	 * @return	circle radius.
	 */
	private int getRadius(Point end) {
		int xComp = end.x - start.x;
		int yComp = end.y - start.y;
		double a = sqrt(xComp*xComp + yComp*yComp);
		
		return (int) a;
	}
}
