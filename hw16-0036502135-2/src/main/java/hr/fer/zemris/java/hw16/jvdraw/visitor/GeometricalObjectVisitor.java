package hr.fer.zemris.java.hw16.jvdraw.visitor;

import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.KonvPoly;
import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.Line;

/**
 * Base for all visitors.
 * Used to do job on visited object whenever visit method with given
 * geometrical object is given.
 * 
 * @author Martin Sršen
 */
public interface GeometricalObjectVisitor {
	
	/**
	 * Job to do on Line geometrical object
	 * whenever it is visited.
	 * 
	 * @param line	Line object to do job when visited.
	 */
	void visit(Line line);
	/**
	 * Job to do on Circle geometrical object
	 * whenever it is visited.
	 * 
	 * @param line	Circle object to do job when visited.
	 */
	void visit(Circle circle);
	/**
	 * Job to do on FilledCircle geometrical object
	 * whenever it is visited.
	 * 
	 * @param line	FilledCircle object to do job when visited.
	 */
	void visit(FilledCircle filledCircle);
	void visit(KonvPoly konvPoly);
}
