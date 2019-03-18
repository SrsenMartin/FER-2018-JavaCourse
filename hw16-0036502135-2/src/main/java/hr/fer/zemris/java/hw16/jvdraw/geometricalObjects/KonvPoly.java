package hr.fer.zemris.java.hw16.jvdraw.geometricalObjects;

import java.awt.Color;
import java.awt.Point;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw16.jvdraw.editor.GeometricalObjectEditor;
import hr.fer.zemris.java.hw16.jvdraw.editor.KonvPolyEditor;
import hr.fer.zemris.java.hw16.jvdraw.visitor.GeometricalObjectVisitor;

public class KonvPoly extends GeometricalObject {

	private List<Point> points;
	private Color fColor;
	private Color bColor;
	
	/**
	 * Default constructor that initializes line state.
	 * 
	 * @param start	Start point.
	 * @param end	End point.
	 * @param color	Line color.
	 */
	public KonvPoly(List<Point> points, Color fColor, Color bColor) {
		Objects.requireNonNull(points);
		Objects.requireNonNull(fColor);
		Objects.requireNonNull(bColor);
		
		this.points = points;
		this.fColor = fColor;
		this.bColor = bColor;
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
	public List<Point> getPoints(){
		return points;
	}

	/**
	 * Getter for line color.
	 * 
	 * @return	line color.
	 */
	public Color getFColor() {
		return fColor;
	}
	
	public Color getBColor() {
		return bColor;
	}
	
	public void setPoints(List<Point> points) {		
		this.points = points;
		notifyListeners();
	}

	/**
	 * Getter for line color.
	 * 
	 * @return	line color.
	 */
	public void setFColor(Color fColor) {
		this.fColor = fColor;
		notifyListeners();
	}
	
	public void setBColor(Color bColor) {
		this.bColor = bColor;
		notifyListeners();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("KonvPoly-> [");
		
		for(Point point: points) {
			sb.append("(").append(point.x).append(", ").append(point.y).append(") ");
		}
		
		sb.append("]");
		return sb.toString();
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new KonvPolyEditor(this);
	}
	
	/**
	 * Method that notifies all observers about change.
	 */
	private void notifyListeners() {
		this.listenerCopy().forEach(listener -> listener.geometricalObjectChanged(this));
	}
	
	@Override
	public String getDataString() {
		StringBuilder sb = new StringBuilder();
		sb.append("FPOLY ").append(points.size()).append(" ");
		
		for(Point point: points) {
			sb.append(point.x).append(" ").append(point.y).append(" ");
		}
		
		sb.append(String.format("%d %d %d %d %d %d", fColor.getRed(), fColor.getGreen(), fColor.getBlue(), bColor.getRed(), bColor.getGreen(), bColor.getBlue()));
		
		return sb.toString();
	}
}
