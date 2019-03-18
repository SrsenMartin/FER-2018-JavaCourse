package hr.fer.zemris.java.hw16.jvdraw.geometricalObjects;

import java.awt.Color;
import java.awt.Point;

import hr.fer.zemris.java.hw16.jvdraw.editor.CircleEditor;
import hr.fer.zemris.java.hw16.jvdraw.editor.GeometricalObjectEditor;
import hr.fer.zemris.java.hw16.jvdraw.visitor.GeometricalObjectVisitor;

/**
 * Class representing circle geometrical object.
 * 
 * @author Martin Sršen
 */
public class Circle extends AbstractCircle {

	/**
	 * Default constructor that initializes circles state.
	 * 
	 * @param center	Center of circle.
	 * @param radius	Circle radius.
	 * @param fColor	Circle foreground color.
	 * @param bColor	Circle background color.
	 */
	public Circle(Point center, int radius, Color fColor) {
		super(center, radius, fColor, null);
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new CircleEditor(this);
	}
	
	@Override
	public String toString() {
		return String.format("Circle (%d,%d), %d", getCenter().x, getCenter().y, getRadius());
	}
	
	@Override
	public String getDataString() {
		Color fColor = getFColor();
		int r = fColor.getRed();
		int g = fColor.getGreen();
		int b = fColor.getBlue();
		
		return String.format("CIRCLE %d %d %d %d %d %d", getCenter().x, getCenter().y, getRadius(), r, g, b);
	}
}
