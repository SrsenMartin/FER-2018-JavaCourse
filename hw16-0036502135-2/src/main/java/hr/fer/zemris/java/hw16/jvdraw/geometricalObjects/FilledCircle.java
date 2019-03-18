package hr.fer.zemris.java.hw16.jvdraw.geometricalObjects;

import java.awt.Color;
import java.awt.Point;

import hr.fer.zemris.java.hw16.jvdraw.editor.FilledCircleEditor;
import hr.fer.zemris.java.hw16.jvdraw.editor.GeometricalObjectEditor;
import hr.fer.zemris.java.hw16.jvdraw.visitor.GeometricalObjectVisitor;

public class FilledCircle extends AbstractCircle {

	/**
	 * Default constructor that initializes filled circles state.
	 * 
	 * @param center	Center of circle.
	 * @param radius	Circle radius.
	 * @param fColor	Circle foreground color.
	 * @param bColor	Circle background color.
	 */
	public FilledCircle(Point center, int radius, Color fColor, Color bColor) {
		super(center, radius, fColor, bColor);
	}
	
	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new FilledCircleEditor(this);
	}
	
	@Override
	public String toString() {
		return String.format("Filled circle (%d,%d), %d, %s", getCenter().x, getCenter().y, getRadius(), getHexColor());
	}

	/**
	 * Helper method that will return hex string of background color.
	 * 
	 * @return	hex string of background color.
	 */
	private String getHexColor() {
		Color color = getBColor();
	
		String red = getPartHex(color.getRed());
		String green = getPartHex(color.getGreen());
		String blue = getPartHex(color.getBlue());
		
		return "#" + red + green + blue;
	}
	
	/**
	 * Returns hex string from given color part.
	 * 
	 * @param value	Value of one color part.
	 * @return	hex string from given color part.
	 */
	private String getPartHex(int value) {
		String hex = Integer.toHexString(value).toUpperCase();
		
		if(hex.length() == 1) {
			hex = "0" + hex;
		}
		
		return hex;
	}
	
	@Override
	public String getDataString() {
		Color fColor = getFColor();
		Color bColor = getBColor();
		int r = fColor.getRed();
		int g = fColor.getGreen();
		int b = fColor.getBlue();
		int r2 = bColor.getRed();
		int g2 = bColor.getGreen();
		int b2 = bColor.getBlue();
		
		return String.format("FCIRCLE %d %d %d %d %d %d %d %d %d", getCenter().x, getCenter().y, getRadius(), r, g, b, r2, g2, b2);
	}
}
