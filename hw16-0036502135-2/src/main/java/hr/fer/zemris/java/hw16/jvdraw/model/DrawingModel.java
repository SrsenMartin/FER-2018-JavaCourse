package hr.fer.zemris.java.hw16.jvdraw.model;

import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.GeometricalObject;

/**
 * Interface that each implementation of drawing model must have.
 * 
 * @author Martin Sršen
 */
public interface DrawingModel {
	/**
	 * Getter for size of drawing model.
	 * 
	 * @return	drawing model size.
	 */
	int getSize();
	/**
	 * Getter for GeometricalObject at given index.
	 * 
	 * @param index	Index in collection of object to get.
	 * @return	GeometricalObject at given index, or null if not contained.
	 */
	GeometricalObject getObject(int index);
	/**
	 * Method used to add new GeometricalObject into drawing model.
	 * 
	 * @param object	GeometricalObject to add.
	 */
	void add(GeometricalObject object);
	
	/**
	 * Method used to remove given object from drawing model.
	 * 
	 * @param object	GeometricalObject to remove.
	 */
	void remove(GeometricalObject object);
	/**
	 * Method used to change order of GeometricalObject in
	 * drawing model collection.
	 * 
	 * @param object	GeometricalObject to change its order.
	 * @param offset	Move object offset places up.
	 */
	void changeOrder(GeometricalObject object, int offset);
	
	/**
	 * Method used to add drawing model listener.
	 * 
	 * @param l	DrawingModelListener to add.
	 */
	void addDrawingModelListener(DrawingModelListener l);
	/**
	 * Method used to remove drawing model listener.
	 * 
	 * @param l	DrawingModelListener to remove.
	 */
	void removeDrawingModelListener(DrawingModelListener l);
}
