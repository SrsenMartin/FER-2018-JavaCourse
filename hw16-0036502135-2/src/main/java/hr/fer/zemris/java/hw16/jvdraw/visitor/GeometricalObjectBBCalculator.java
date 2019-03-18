package hr.fer.zemris.java.hw16.jvdraw.visitor;

import java.awt.Point;
import java.awt.Rectangle;

import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.KonvPoly;
import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.Line;

/**
 * Implementation of GeometricalObjectVisitor
 * used to calculate smallest rectangle where
 * all elements are contained.
 * 
 * @author Martin Sršen
 *
 */
public class GeometricalObjectBBCalculator implements GeometricalObjectVisitor {

	/**
	 * Rectangle start corner.
	 */
	private Point start = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
	/**
	 * Rectangle end corner.
	 */
	private Point end = new Point(0, 0);
	
	@Override
	public void visit(Line line) {
		Point lStart = line.getStart();
		Point lEnd = line.getEnd();
		
		if(lStart.x < start.x)	start.x = lStart.x;
		if(lEnd.x < start.x)	start.x = lEnd.x;
		if(lStart.y < start.y)	start.y = lStart.y;
		if(lEnd.y < start.y)	start.y = lEnd.y;
		
		if(lStart.x > end.x)	end.x = lStart.x;
		if(lEnd.x > end.x)	end.x = lEnd.x;
		if(lStart.y > end.y)	end.y = lStart.y;
		if(lEnd.y > end.y)	end.y = lEnd.y;
	}

	@Override
	public void visit(Circle circle) {
		Point center = circle.getCenter();
		int r = circle.getRadius();
		
		countCircle(center, r);
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		Point center = filledCircle.getCenter();
		int r = filledCircle.getRadius();
		
		countCircle(center, r);
	}
	
	/**
	 * Helper method used to check circle coordinates.
	 * 
	 * @param center	Circle center point.
	 * @param r	Circle radius.
	 */
	private void countCircle(Point center, int r) {
		if(center.x - r < start.x)	start.x = center.x - r;
		if(center.y - r < start.y)	start.y = center.y - r;
		if(center.x + r > end.x)	end.x = center.x + r;
		if(center.y + r > end.y)	end.y = center.y + r;
	}
	
	/**
	 * Method that returns rectangle where all elements are contained.
	 * 
	 * @return	rectangle where all elements are contained.
	 */
	public Rectangle getBoundingBox() {
		return new Rectangle(start.x, start.y, end.x - start.x, end.y - start.y);
	}

	@Override
	public void visit(KonvPoly konvPoly) {
		for(Point p : konvPoly.getPoints()) {
			if(p.x < start.x)	start.x = p.x;
			if(p.x > end.x)	end.x = p.x;
			if(p.y < start.y)	start.y = p.y;
			if(p.y > end.y)	end.y = p.y;
		}
	}
}
