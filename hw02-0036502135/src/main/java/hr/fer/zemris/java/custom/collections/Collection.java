package hr.fer.zemris.java.custom.collections;

/**
 * Base class for all implementations of different kinds of collections.
 * In this example,base class for ArrayIndexedCollection and LinkedListInedexedCollection.
 * 
 * @author Martin Sršen
 *
 */
public class Collection {

	/**
	 * Default Constructor.
	 */
	protected Collection() {
	}

	/**
	 * Returns true if Collection is empty,false otherwise.
	 * 
	 * @return true if Collection is empty.
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Returns the number of Objects in Collection.
	 * 
	 * @return number of Objects in Collection.
	 */
	public int size() {
		return 0;
	}

	/**
	 * Adds specified Object in the Collection.
	 * 
	 * @param value	Object to add in Collection.
	 */
	public void add(Object value) {
	}

	/**
	 * Returns true if Collection contains specified Object.
	 * 
	 * @param value Object to look for in Collection.
	 * @return	true if Collection contains Object.
	 */
	public boolean contains(Object value) {
		return false;
	}

	/**
	 * Removes Object from Collection,if it exists.
	 * 
	 * @param value Object to remove from Collection.
	 * @return	true if Object was removed from Collection,false otherwise.
	 */
	public boolean remove(Object value) {
		return false;
	}

	/**
	 * Returns new Array with all elements from Collection.
	 * 	
	 * @return	new Array with all elements from Collection.
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException("Method not implemented.");
	}

	/**
	 * Does certain action for each element of Collection.
	 * Action is determined by implementation of Processor class.
	 * 
	 * @param processor
	 */
	public void forEach(Processor processor) {
	}

	/**
	 * Adds all elements from other Collection to this collection.
	 * 
	 * @param other	Collection where Object are added from.
	 * @throws IllegalArgumentException when collection we try to add from is null.
	 */
	public void addAll(Collection other) {
		if (other == null) {
			throw new NullPointerException("Can't add collection as null value to other collection.");
		}

		class MyProcessor extends Processor {
			@Override
			public void process(Object value) {
				add(value);
			}
		}

		other.forEach(new MyProcessor());
	}

	/**
	 * Removes all Objects from Collection.
	 */
	public void clear() {
	}

}
