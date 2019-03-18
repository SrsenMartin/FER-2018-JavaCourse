package hr.fer.zemris.java.custom.collections;

/**
 * Class that represents simple stack.
 * Stack has LIFO(Last In First Out) strategy of adding/removing Objects.
 * Stack is empty at first.
 * Operations on empty stack (pop and peek) throws EmptyStackException.
 * 
 * @author Martin Sršen
 *
 */
public class ObjectStack {
	
	/**
	 * Adaptee class that will be used to store Objects.
	 */
	private ArrayIndexedCollection collection;

	/**
	 * Default constructor.
	 * Initializes Collection that will represent stack.
	 */
	public ObjectStack() {
		collection = new ArrayIndexedCollection();
	}

	/**
	 * Returns true if Stack is empty,false otherwise.
	 * 
	 * @return true if Stack is empty.
	 */
	public boolean isEmpty() {
		return collection.isEmpty();
	}

	/**
	 * Returns the number of Objects in Stack.
	 * 
	 * @return number of Objects in Stack.
	 */
	public int size() {
		return collection.size();
	}

	/**
	 * Adds specified Object at top of the Stack.
	 * 
	 * @param value	Object to add in Stack.
	 */
	public void push(Object value) {
		collection.add(value);
	}

	/**
	 * Removes and returns Object from top of the Stack,
	 * if Stack is not empty,otherwise throws EmptyStackException.
	 * 
	 * @return	Object removed from top of the Stack.
	 * @throws EmptyStackException if Stack was empty.
	 */
	public Object pop() {
		Object wantedValue = peek();

		int lastElementIndex = size() - 1;
		collection.remove(lastElementIndex);

		return wantedValue;
	}

	/**
	 * Returns Object from top of the Stack,
	 * if Stack is not empty,otherwise throws EmptyStackException.
	 * 
	 * @return	Object from top of the Stack.
	 * @throws EmptyStackException if Stack was empty.
	 */
	public Object peek() {
		if (isEmpty()) {
			throw new EmptyStackException("Can't read/remove from empty stack.");
		}

		int lastElementIndex = size() - 1;
		Object wantedValue = collection.get(lastElementIndex);

		return wantedValue;
	}

	/**
	 * Removes all Object from Stack.
	 */
	public void clear() {
		collection.clear();
	}
}
