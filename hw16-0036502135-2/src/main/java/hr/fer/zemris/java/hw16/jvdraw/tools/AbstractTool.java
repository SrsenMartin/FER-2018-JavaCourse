package hr.fer.zemris.java.hw16.jvdraw.tools;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import hr.fer.zemris.java.hw16.jvdraw.components.IColorProvider;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModel;

/**
 * Class representing abstract tool that
 * all geometrical object share
 * to create new geometrical objects.
 * 
 * @author Martin Sršen
 *
 */
public abstract class AbstractTool implements Tool {

	/**
	 * Whether mouse was pressed once.
	 */
	protected boolean pressed;
	/**
	 * Object start press point.
	 */
	protected Point start;
	/**
	 * Object pressed end point.
	 */
	protected Point end;
	
	/**
	 * Model used to store saved geometrical objects.
	 */
	protected DrawingModel model;
	/**
	 * Foreground color.
	 */
	protected IColorProvider fColor;
	/**
	 * Canvas used to draw on.
	 */
	protected JComponent canvas;
	
	/**
	 * Default constructor that initializes each tool state.
	 * 
	 * @param model	Model used to store saved geometrical objects.
	 * @param fColor	Foreground color.
	 * @param canvas	Canvas used to draw on.
	 */
	public AbstractTool(DrawingModel model, IColorProvider fColor, JComponent canvas) {
		this.model = model;
		this.fColor = fColor;
		this.canvas = canvas;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(!pressed)	return;

		end = e.getPoint();
		canvas.repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
