package hr.fer.zemris.java.hw16.jvdraw.geometricalObjects;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw16.jvdraw.editor.GeometricalObjectEditor;
import hr.fer.zemris.java.hw16.jvdraw.visitor.GeometricalObjectVisitor;

/**
 * Abstract class acting as base for each geometrical object.
 * Has methods to add and remove listeners.
 * 
 * @author Martin Sršen
 *
 */
public abstract class GeometricalObject {
	/**
	 * List of GeometricalObjectListener objects.
	 */
	private List<GeometricalObjectListener> listeners = new ArrayList<>();;
	
	/**
	 * Method that takes visitor which determines
	 * job that each geometrical object must do.
	 * 
	 * @param v	GeometricalObjectVisitor class.
	 */
	public abstract void accept(GeometricalObjectVisitor v);
	/**
	 * Method used to create geometrical object editor.
	 * 
	 * @return	GeometricalObjectEditor used to edit object.
	 */
	public abstract GeometricalObjectEditor createGeometricalObjectEditor();
	
	/**
	 * Method used to add GeometricalObjectListener into list of listeners.
	 * 
	 * @param l	GeometricalObjectListener to add.
	 */
	 public void addGeometricalObjectListener(GeometricalObjectListener l) {
		 if(l == null)	return;
		 
		 listeners.add(l);
	 }
	 
		/**
		 * Method used to remove GeometricalObjectListener from list of listeners.
		 * 
		 * @param l	GeometricalObjectListener to remove.
		 */
	 public void removeGeometricalObjectListener(GeometricalObjectListener l) {
		 listeners.remove(l);
	 }
	 
	 /**
	  * Method that returns copy of list of listeners.
	  * Makes it easier to notify.
	  * 
	  * @return	Copy of list of listeners.
	  */
	 public List<GeometricalObjectListener> listenerCopy(){
		 return new ArrayList<>(listeners);
	 }
	 
	 /**
	  * Method used to return geometrical object string representation.
	  * 	
	  * @return	geometrical object string representation.
	  */
	 public abstract String getDataString();
}
