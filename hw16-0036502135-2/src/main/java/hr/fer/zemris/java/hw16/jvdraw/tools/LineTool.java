package hr.fer.zemris.java.hw16.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import hr.fer.zemris.java.hw16.jvdraw.components.IColorProvider;
import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.Line;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModel;

/**
 * Class representing line tool used to create Line objects.
 * 
 * @author Martin Sršen
 *
 */
public class LineTool extends AbstractTool {

	/**
	 * Constructor used to take all needed objects to create new Line object.
	 * 
	 * @param model	Model used to store saved geometrical objects.
	 * @param fColor	Foreground color.
	 * @param canvas	Canvas used to draw on.
	 */
	public LineTool(DrawingModel model, IColorProvider fColor, JComponent canvas) {
		super(model, fColor, canvas);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(!pressed) {
			start = e.getPoint();
			pressed = true;
		}else {
			pressed = false;
			model.add(new Line(start, e.getPoint(), fColor.getCurrentColor()));
		}
	}

	@Override
	public void paint(Graphics2D g2d) {
		if(!pressed || start == null || end == null)	return;
		
		g2d.setColor(fColor.getCurrentColor());
		g2d.drawLine(start.x, start.y, end.x, end.y);
	}
}
