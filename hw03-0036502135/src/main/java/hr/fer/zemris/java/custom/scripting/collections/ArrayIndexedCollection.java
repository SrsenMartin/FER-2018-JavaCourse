package hr.fer.zemris.java.custom.scripting.collections;

import java.util.Arrays;

/**
 * 	Resizable array implementation  of Collection class used to store Objects.
 * 	All objects are allowed except for null value.
 * 	Each ArrayIndexedCollection instance has a capacity.
 * 	The capacity is the size of the array used to store the elements in the list.
 * 	The capacity can be set through constructor or if there is no more space in array
 * 	,it will be expanded automatically.
 * 	The add(except when array needs to expand O(n)),get operations run in constant time(O(1)).
 * 	The insert,indexOf,contains,remove operations require O(n) time.
 * 
 * @author Martin Sršen
 *
 */
public class ArrayIndexedCollection extends Collection {

	/**
	 * Constant that defines default array capacity.
	 */
	public static final int DEFAULT_SIZE = 16;

	/**
	 * Current size of Collection.
	 */
	private int size;
	/**
	 * Current max capacity of Collection.
	 */
	private int capacity;
	/**
	 * Array where Objects are added.
	 */
	private Object[] elements;

	/**
	 * Construct an empty ArrayIndexedCollection with chosen capacity and
	 * add all Objects from other Collection.
	 * 
	 * @param other	Collection which we add Objects from.
	 * @param initialCapacity	Initial capacity of Collection.
	 */
	public ArrayIndexedCollection(Collection other, int initialCapacity) {
		initCollection(initialCapacity);
		addAll(other);
	}

	/**
	 * 	Make a ArrayIndexedCollection with Objects from other Collection.
	 * 
	 * @param other	Collection which we add Objects from.
	 */
	public ArrayIndexedCollection(Collection other) {
		this(other, DEFAULT_SIZE);
	}

	/**
	 * 	Construct an empty ArrayIndexedCollection with chosen capacity.
	 * 
	 * @param initialCapacity Initial capacity of ArrayIndexedCollection.
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		initCollection(initialCapacity);
	}

	/**
	 * 	Construct an empty ArrayIndexedCollection with initial capacity of 16.
	 */
	public ArrayIndexedCollection() {
		this(DEFAULT_SIZE);
	}

	/**
	 * Helper method that initialize ArrayIndexedCollection.
	 * 
	 * @param initialCapacity	Initial capacity of ArrayIndexedCollection.
	 * @throws IllegalArgumentException if initialCapacity is less than 1.
	 */
	private void initCollection(int initialCapacity) {
		if (initialCapacity < 1) {
			throw new IllegalArgumentException(
					"Capacity must be higher than 0,tried to set capacity to " + initialCapacity + ".");
		}

		capacity = initialCapacity;
		elements = new Object[capacity];
	}

	/**
	 * Returns the number of elements in Collection.
	 * 
	 * @return	Number of elements in Collection.
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * Adds Object to the end of Collection.
	 * Complexity is O(1),except when array needs to expand O(n).
	 * 
	 * @param value Object to add to Collection.
	 * @throws IllegalArgumentException if value is null.
	 */
	@Override
	public void add(Object value) {
		if (value == null) {
			throw new NullPointerException("Can't add null value to collection.");
		} else if (capacity == size) {
			capacity *= 2;
			elements = Arrays.copyOf(elements, capacity);
		}

		elements[size++] = value;
	}

	/**
	 * Check whether Collection contains specified Object.
	 * Complexity is O(n).
	 * 
	 * @param value Object to check its presence.
	 * @return true if Collection contains at least one specified Object.
	 */
	@Override
	public boolean contains(Object value) {
		if (value == null) {
			return false;
		}

		for (int index = 0; index < size; index++) {
			if (elements[index].equals(value)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Removes the first occurrence of specified Object, if it exists.
	 * Complexity is O(n).
	 * 
	 * @param value Object to be removed from Collection,if present.
	 * @return true if specified Object was removed, false otherwise.
	 */
	@Override
	public boolean remove(Object value) {
		if (!contains(value)) {
			return false;
		}

		int valueIndex = indexOf(value);
		remove(valueIndex);

		return true;
	}

	/**
	 * Returns new array with all Objects from this Collection in same order.
	 * 
	 * @return new Array with all Objects from this Collection.
	 */
	@Override
	public Object[] toArray() {
		return Arrays.copyOf(elements, size);
	}

	/**
	 * Calls method process for each Object of Collection.
	 * Action is specified by implementation of Processor class. 
	 */
	@Override
	public void forEach(Processor processor) {
		for (int index = 0; index < size; index++) {
			processor.process(elements[index]);
		}
	}

	/**
	 * Removes all Objects from Collection.
	 * Leaves its capacity at current value.
	 */
	@Override
	public void clear() {
		for (int index = 0; index < size; index++) {
			elements[index] = null;
		}

		size = 0;
	}

	/**
	 * Returns Object at specified index in Collection.
	 * Complexity is O(1).
	 * 
	 * @param index Index of element to return.
	 * @return Object at specified index in Collection.
	 * @throws IndexOutOfBoundsException if index is less than 0
	 * 			or greater or equal to size.
	 */
	public Object get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException(
					"Can't get value from invalid index,tried to get at index " + index + ".");
		}

		return elements[index];
	}

	/**
	 * Inserts Object at specified position in Collection.
	 * Moves current Object at that position and all right to it to the right for one position.
	 * Complexity is O(n).
	 * 
	 * @param value	Object to be inserted.
	 * @param position	Index where Object will be add.
	 * @throws IndexOutOfBoundsException if position is less than 0 or greater than size.
	 * @throws IllegalArgumentException if value is null.
	 */
	public void insert(Object value, int position) {
		if (position < 0 || position > size) {
			throw new IndexOutOfBoundsException(
					"Can't insert value on invalid position, tried to add at + " + position + ".");
		} else if (value == null) {
			throw new NullPointerException("Can't insert null value to collection.");
		}

		// zadnji element dodaj s add ako treba prosiriti array,ostale pomakni
		add(elements[size - 1]);
		for (int index = size - 3; index >= position; index--) {
			elements[index + 1] = elements[index];
		}
		elements[position] = value;
	}

	/**
	 * Returns index of first occurrence of specified Object, if it exists,
	 * otherwise -1, if there is no Object in Collection.
	 * Complexity is O(n).
	 * 
	 * @param value Object to look for.
	 * @return	Index of first occurrence of specified Object, if it exists,
	 * otherwise -1, if there is no Object in Collection.
	 */
	public int indexOf(Object value) {
		if (!contains(value)) {
			return -1;
		}

		int valueIndex = 0;
		for (; valueIndex < size; valueIndex++) {
			if (elements[valueIndex].equals(value)) {
				break;
			}
		}

		return valueIndex;
	}
	
	/**
	 * Removes Object at specified index in Collection.
	 * Moves every following Object to the left for one position.
	 * Complexity is O(n).
	 * 
	 * @param index	Index of element to be removed.
	 * @throws IndexOutOfBoundsException if index is less than 0
	 * 			or greater or equal to size.
	 */
	public void remove(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException(
					"Can't remove value from invalid index,tried to remove at index " + index + ".");
		}

		for (; index < size - 1; index++) {
			elements[index] = elements[index + 1];
		}

		elements[--size] = null;
	}
}
