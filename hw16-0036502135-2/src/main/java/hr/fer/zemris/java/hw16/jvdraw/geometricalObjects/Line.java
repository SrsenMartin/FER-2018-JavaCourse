package hr.fer.zemris.java.hw16.jvdraw.geometricalObjects;

import java.awt.Color;
import java.awt.Point;
import java.util.Objects;

import hr.fer.zemris.java.hw16.jvdraw.editor.GeometricalObjectEditor;
import hr.fer.zemris.java.hw16.jvdraw.editor.LineEditor;
import hr.fer.zemris.java.hw16.jvdraw.visitor.GeometricalObjectVisitor;

public class Line extends GeometricalObject {

	/**
	 * Start point.
	 */
	private Point start;
	/**
	 * End point.
	 */
	private Point end;
	/**
	 * Line color.
	 */
	private Color color;
	
	/**
	 * Default constructor that initializes line state.
	 * 
	 * @param start	Start point.
	 * @param end	End point.
	 * @param color	Line color.
	 */
	public Line(Point start, Point end, Color color) {
		Objects.requireNonNull(start);
		Objects.requireNonNull(end);
		Objects.requireNonNull(color);
		
		this.start = start;
		this.end = end;
		this.color = color;
	}
	
	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	/**
	 * Getter for line start point.
	 * 
	 * @return	start point.
	 */
	public Point getStart() {
		return start;
	}

	/**
	 * Getter for line end point.
	 * 
	 * @return	end point.
	 */
	public Point getEnd() {
		return end;
	}

	/**
	 * Getter for line color.
	 * 
	 * @return	line color.
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Setter for line start point.
	 * 
	 * @param start	start point.
	 */
	public void setStart(Point start) {
		if(start.equals(this.start))	return;
		
		this.start = start;
		notifyListeners();
	}

	/**
	 * Setter for line end point.
	 * 
	 * @param end	end point.
	 */
	public void setEnd(Point end) {
		if(end.equals(this.end))	return;
		
		this.end = end;
		notifyListeners();
	}

	/**
	 * Setter for line color.
	 * 
	 * @param color	line color.
	 */
	public void setColor(Color color) {
		if(color.equals(this.color))	return;
		
		this.color = color;
		notifyListeners();
	}

	@Override
	public String toString() {
		return String.format("Line (%d,%d)-(%d,%d)", start.x, start.y, end.x, end.y);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new LineEditor(this);
	}
	
	/**
	 * Method that notifies all observers about change.
	 */
	private void notifyListeners() {
		this.listenerCopy().forEach(listener -> listener.geometricalObjectChanged(this));
	}
	
	@Override
	public String getDataString() {
		Color fColor = getColor();
		int r = fColor.getRed();
		int g = fColor.getGreen();
		int b = fColor.getBlue();
		
		return String.format("LINE %d %d %d %d %d %d %d", start.x, start.y, end.x, end.y, r, g, b);
	}
}
