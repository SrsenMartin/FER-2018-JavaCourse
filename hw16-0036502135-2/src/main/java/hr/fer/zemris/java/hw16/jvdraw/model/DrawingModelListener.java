package hr.fer.zemris.java.hw16.jvdraw.model;

/**
 * Listener on drawing model.
 * When something is added, removed or changed
 * this listener will fire.
 * 
 * @author Martin Sršen
 *
 */
public interface DrawingModelListener {
	/**
	 * Called whenever new object is added into drawing model.
	 * 
	 * @param source	DrawingModel where object is added.
	 * @param index0	where was element added.
	 * @param index1	where was element added.
	 */
	public void objectsAdded(DrawingModel source, int index0, int index1);
	/**
	 * Called whenever new object is removed from drawing model.
	 * 
	 * @param source	DrawingModel where object is removed from.
	 * @param index0	where was element removed.
	 * @param index1	where was element removed.
	 */
	public void objectsRemoved(DrawingModel source, int index0, int index1);
	/**
	 * Called whenever object changes position in drawing model.
	 * 
	 * @param source	DrawingModel where object is changed.
	 * @param index0	Added from index.
	 * @param index1	Added to index.
	 */
	public void objectsChanged(DrawingModel source, int index0, int index1);
}
