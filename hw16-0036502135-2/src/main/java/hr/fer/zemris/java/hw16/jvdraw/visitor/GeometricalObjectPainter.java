package hr.fer.zemris.java.hw16.jvdraw.visitor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.List;

import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.KonvPoly;
import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.Line;

/**
 * Implementation of GeometricalObjectVisitor
 * used draw all elements onto screen.
 * 
 * @author Martin Sršen
 *
 */
public class GeometricalObjectPainter implements GeometricalObjectVisitor {

	/**
	 * Object used to draw elements on screen.
	 */
	private Graphics2D g2d;
	
	/**
	 * Default constructor that takes Graphics2D object used to draw elements on screen.
	 * 
	 * @param g2d	Object used to draw elements on screen.
	 */
	public GeometricalObjectPainter(Graphics2D g2d) {
		this.g2d = g2d;
	}
	
	@Override
	public void visit(Line line) {
		g2d.setColor(line.getColor());
		g2d.drawLine(line.getStart().x, line.getStart().y, line.getEnd().x, line.getEnd().y);
	}

	@Override
	public void visit(Circle circle) {
		createOval(g2d, circle.getFColor(), circle.getRadius(), circle.getCenter(), false);
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		int radius = filledCircle.getRadius();
		createOval(g2d, filledCircle.getFColor(), radius, filledCircle.getCenter(), false);
		createOval(g2d, filledCircle.getBColor(), radius, filledCircle.getCenter(), true);
	}
	
	/**
	 * Helper method that will draw filled or unfilled circle onto screen based
	 * on given parameters.
	 * 
	 * @param g2d	Object used to draw elements on screen.
	 * @param color	Color to use when drawing circle.
	 * @param radius	Circle radius.
	 * @param center	Circle center.
	 * @param fill	whether circle should be filled or not.
	 */
	public static void createOval(Graphics2D g2d, Color color, int radius, Point center, boolean fill) {
		g2d.setColor(color);
		
		if(!fill) {
			g2d.drawOval(center.x-radius, center.y-radius, 2*radius, 2*radius);
		}else {
			g2d.fillOval(center.x-radius, center.y-radius, 2*radius, 2*radius);
		}
	}

	@Override
	public void visit(KonvPoly konvPoly) {
		createPoly(g2d, konvPoly.getBColor(), konvPoly.getPoints(), null, true);
		createPoly(g2d, konvPoly.getFColor(), konvPoly.getPoints(), null, false);
	}
	
	public static void createPoly(Graphics2D g2d, Color color,List<Point> points, Point moving, boolean fill) {
		g2d.setColor(color);
		
		Polygon poly = new Polygon();
		for(Point point: points) {
			poly.addPoint(point.x, point.y);
		}
		if(moving != null) {
			poly.addPoint(moving.x, moving.y);
		}
		
		if(!fill) {
			g2d.drawPolygon(poly);
		}else {
			g2d.fillPolygon(poly);
		}
	}

}
