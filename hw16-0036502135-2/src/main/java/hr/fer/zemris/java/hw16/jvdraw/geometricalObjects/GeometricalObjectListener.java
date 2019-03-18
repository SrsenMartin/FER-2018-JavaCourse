package hr.fer.zemris.java.hw16.jvdraw.geometricalObjects;

/**
 * Listener that notifies all observers when
 * change happens in geometrical object.
 * 
 * @author Martin Sršen
 *
 */
public interface GeometricalObjectListener {
	/**
	 * Method called when geometrical object changes.
	 * 
	 * @param o	Changed geometrical object.
	 */
	public void geometricalObjectChanged(GeometricalObject o);
}
