package hr.fer.zemris.java.hw16.jvdraw.model;

import javax.swing.AbstractListModel;

import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.GeometricalObject;

/**
 * List model for list showing all currently stored objects.
 * 
 * @author Martin Sršen
 *
 */
public class DrawingObjectListModel extends AbstractListModel<GeometricalObject> implements DrawingModelListener {

	private static final long serialVersionUID = 1L;

	/**
	 * Model used as adaptee.
	 */
	private DrawingModel model;
	
	/**
	 * Default constructor that takes model.
	 * 
	 * @param model	used to take informations from.
	 */
	public DrawingObjectListModel(DrawingModel model) {
		this.model = model;
		model.addDrawingModelListener(this);
	}
	
	@Override
	public GeometricalObject getElementAt(int index) {
		return model.getObject(index);
	}

	@Override
	public int getSize() {
		return model.getSize();
	}
	
	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		fireIntervalAdded(source, index0, index1);
	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		fireIntervalRemoved(source, index0, index1);
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		fireContentsChanged(source, index0, index1);
	}
}
