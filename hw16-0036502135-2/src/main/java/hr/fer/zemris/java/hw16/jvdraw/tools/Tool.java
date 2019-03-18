package hr.fer.zemris.java.hw16.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * Interface representing tools used
 * for creating new GeometricalObject objects.
 * 
 * @author Martin Sršen
 *
 */
public interface Tool {
	/**
	 * Called when user press mouse JDrawingCanvas.
	 * 
	 * @param e	MouseEvent object.
	 */
	public void mousePressed(MouseEvent e);
	/**
	 * Called when user release mouse JDrawingCanvas.
	 * 
	 * @param e	MouseEvent object.
	 */
	public void mouseReleased(MouseEvent e);
	/**
	 * Called when user click mouse JDrawingCanvas.
	 * 
	 * @param e	MouseEvent object.
	 */
	public void mouseClicked(MouseEvent e);
	/**
	 * Called when user move mouse JDrawingCanvas.
	 * 
	 * @param e	MouseEvent object.
	 */
	public void mouseMoved(MouseEvent e);
	/**
	 * Called when user drag mouse JDrawingCanvas.
	 * 
	 * @param e	MouseEvent object.
	 */
	public void mouseDragged(MouseEvent e);
	/**
	 * Method used to draw following animation.
	 * 
	 * @param g2d	Graphical2D object used for drawing.
	 */
	public void paint(Graphics2D g2d);
}
