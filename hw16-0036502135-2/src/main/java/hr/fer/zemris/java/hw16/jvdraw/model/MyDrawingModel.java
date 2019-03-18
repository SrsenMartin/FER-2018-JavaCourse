package hr.fer.zemris.java.hw16.jvdraw.model;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.GeometricalObject;

/**
 * Implementation of drawing model used to store GeometricalObject objects.
 * 
 * @author Martin Sršen
 *
 */
public class MyDrawingModel implements DrawingModel {

	/**
	 * List of DrawingModelListener listeners.
	 */
	private List<DrawingModelListener> listeners;
	/**
	 * List of GeometricalObject objects.
	 */
	private List<GeometricalObject> objects;
	
	/**
	 * Default constructor.
	 */
	public MyDrawingModel() {
		listeners = new ArrayList<>();
		objects = new ArrayList<>();
	}
	
	@Override
	public int getSize() {
		return objects.size();
	}

	@Override
	public GeometricalObject getObject(int index) {
		return objects.get(index);
	}

	@Override
	public void add(GeometricalObject object) {
		if(object == null)	return;
		
		int index = objects.size();
		objects.add(object);
		object.addGeometricalObjectListener(o -> changeOrder(o, 0));
		
		listCopy().forEach(listener -> listener.objectsAdded(this, index, index));
	}

	@Override
	public void remove(GeometricalObject object) {
		if(object == null)	return;
		
		int index = objects.indexOf(object);
		objects.remove(object);
		listCopy().forEach(listener -> listener.objectsRemoved(this, index, index));
	}

	@Override
	public void changeOrder(GeometricalObject object, int offset) {
		if(object == null)	return;
		
		int startIndex = objects.indexOf(object);
		int endIndex = startIndex + offset;
		
		if(startIndex < 0 || startIndex >= objects.size() ||
				endIndex < 0 || endIndex >= objects.size())		return;
		
		if(startIndex < endIndex) {
			for(int i = startIndex; i < endIndex; i++) {
				objects.set(i, objects.get(i+1));
			}
		}else {
			for(int i = startIndex; i > endIndex; i--) {
				objects.set(i, objects.get(i-1));
			}
		}
		
		objects.set(endIndex, object);
		listCopy().forEach(listener -> listener.objectsChanged(this, 
				startIndex < endIndex ? startIndex : endIndex, startIndex < endIndex ? endIndex : startIndex));
	}

	@Override
	public void addDrawingModelListener(DrawingModelListener l) {
		if(l == null)	return;
		listeners.add(l);
	}

	@Override
	public void removeDrawingModelListener(DrawingModelListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Method that creates copy of listeners list.
	 * 
	 * @return	copy of listeners list.
	 */
	private List<DrawingModelListener> listCopy(){
		return new ArrayList<>(listeners);
	}
}
