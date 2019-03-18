package hr.fer.zemris.java.hw16.jvdraw.geometricalObjects;

import java.awt.Color;
import java.awt.Point;
import java.util.Objects;

/**
 * Class representing default circle behavior for both filled and normal.
 * 
 * @author Martin Sršen
 *
 */
public abstract class AbstractCircle extends GeometricalObject {
	
	/**
	 * Center of circle.
	 */
	private Point center;
	/**
	 * Circle radius.
	 */
	private int radius;
	/**
	 * Circle foreground color.
	 */
	private Color fColor;
	/**
	 * Circle background color.
	 */
	private Color bColor;
	
	/**
	 * Default constructor that initializes circles state.
	 * 
	 * @param center	Center of circle.
	 * @param radius	Circle radius.
	 * @param fColor	Circle foreground color.
	 * @param bColor	Circle background color.
	 */
	public AbstractCircle(Point center, int radius, Color fColor, Color bColor) {
		Objects.requireNonNull(center);
		Objects.requireNonNull(fColor);
		
		this.center = center;
		this.radius = radius;
		this.fColor = fColor;
		this.bColor = bColor;
	}

	/**
	 * Getter for center of circle.
	 * 
	 * @return	center of circle.
	 */
	public Point getCenter() {
		return center;
	}

	/**
	 * Getter for circle radius.
	 * 
	 * @return	circle radius.
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * Getter for foreground color.
	 * 
	 * @return	foreground color.
	 */
	public Color getFColor() {
		return fColor;
	}

	/**
	 * Getter for background color.
	 * 
	 * @return	background color.
	 */
	public Color getBColor() {
		return bColor;
	}
	
	/**
	 * Setter for center of circle.
	 * 
	 * @param center	center of circle.
	 */
	public void setCenter(Point center) {
		if(center.equals(this.center))	return;
		
		this.center = center;
		notifyListeners();
	}

	/**
	 * Setter for circle radius.
	 * 
	 * @param radius	circle radius.
	 */
	public void setRadius(int radius) {
		if(radius == this.radius)	return;
		
		this.radius = radius;
		notifyListeners();
	}

	/**
	 * Setter for foreground color.
	 * 
	 * @param fColor	foreground color.
	 */
	public void setfColor(Color fColor) {
		if(fColor.equals(this.fColor))	return;
		
		this.fColor = fColor;
		notifyListeners();
	}

	/**
	 * Setter for background color.
	 * 
	 * @param bColor	background color.
	 */
	public void setbColor(Color bColor) {
		if(bColor.equals(this.bColor))	return;
		
		this.bColor = bColor;
		notifyListeners();
	}
	
	/**
	 * Helper method that will notify all listeners when change occur on circles.
	 */
	private void notifyListeners() {
		this.listenerCopy().forEach(listener -> listener.geometricalObjectChanged(this));
	}
}
