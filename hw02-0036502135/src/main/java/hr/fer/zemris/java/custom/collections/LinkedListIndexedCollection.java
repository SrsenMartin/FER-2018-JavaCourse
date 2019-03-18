package hr.fer.zemris.java.custom.collections;

/**
 * Double linked list implementation of Collection class used to store Objects.
 * All objects are allowed except for null value.
 * For each new Object to add new ListNode is created and added
 * at the end of LinkedListIndexedCollection.
 * The add operation runs in constant time,O(1).
 * The get,contains,insert,indexOf,remove operations require O(n) time.
 * Actually,all operations that work with index have average O(n/2 + 1).
 * 
 * @author Martin Sršen
 *
 */

public class LinkedListIndexedCollection extends Collection {

	/**
	 *	Class that represents one ListNode in LinkedListIndexedCollection.
	 */
	private static class ListNode {
		/**
		 * Points to previous node in Collection.
		 */
		ListNode previous;
		/**
		 * Points to next node in Collection.
		 */
		ListNode next;
		/**
		 * Value of node.
		 */
		Object value;
		
		ListNode(Object value, ListNode next, ListNode previous) {
			this.value = value;
			this.next = next;
			this.previous = previous;
		}
	}

	/**
	 * Current size of Collection.
	 */
	private int size;
	/**
	 * Points to first ListNode in Collection.
	 */
	private ListNode first;
	/**
	 * Points to last ListNode in Collection.
	 */
	private ListNode last;

	/**
	 * Make a LinkedListIndexedCollection with Objects from other Collection.
	 * 
	 * @param other	Collection which we add Objects from.
	 */
	public LinkedListIndexedCollection(Collection other) {
		addAll(other);
	}

	/**
	 * Default constructor.
	 */
	// first i last automatski su null
	public LinkedListIndexedCollection() {

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
	 * Complexity is O(1).
	 * 
	 * @param value Object to add to Collection.
	 * @throws IllegalArgumentException if value is null.
	 */
	@Override
	public void add(Object value) {
		if (value == null) {
			throw new NullPointerException("Can't add null value to collection.");
		}

		ListNode novi = new ListNode(value, null, last);
		if (first == null) {
			first = novi;
			last = novi;
		} else {
			last.next = novi;
			last = novi;
		}

		size++;
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

		for (ListNode current = first; current != null; current = current.next) {
			if (current.value.equals(value)) {
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
		Object[] arrayToReturn = new Object[size];
		int index = 0;

		for (ListNode current = first; current != null; current = current.next) {
			arrayToReturn[index++] = current.value;
		}

		return arrayToReturn;
	}

	/**
	 * Calls method process for each Object of Collection.
	 * Action is specified by implementation of Processor class. 
	 */
	@Override
	public void forEach(Processor processor) {
		for (ListNode current = first; current != null; current = current.next) {
			processor.process(current.value);
		}
	}

	/**
	 * Removes all Objects from Collection.
	 */
	@Override
	public void clear() {
		first = null;
		last = null;
		size = 0;
	}

	/**
	 * Returns Object at specified index in Collection.
	 * Complexity is O(n).
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

		Object searchedValue = getNode(index).value;

		return searchedValue;
	}

	/**
	 * Inserts Object at specified position in Collection.
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

		if (position == size) {
			add(value);
			return;
		}

		// ubacivamo node prije trenutnog na toj poziciji
		ListNode currentAtPosition = getNode(position);
		ListNode nodeToAdd = new ListNode(value, currentAtPosition, currentAtPosition.previous);
		if (currentAtPosition.previous != null) {
			currentAtPosition.previous.next = nodeToAdd;
		}
		currentAtPosition.previous = nodeToAdd;

		if (position == 0) {
			first = nodeToAdd;
		}

		size++;
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
		for (ListNode current = first; current != null; current = current.next) {
			if (current.value.equals(value)) {
				break;
			}
			valueIndex++;
		}

		return valueIndex;
	}

	/**
	 * Removes Object at specified index in Collection.
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

		// izbacivamo node ,te spajamo veze prijasnjeg i sljedeceg
		ListNode nodeToRemove = getNode(index);
		if (nodeToRemove.previous != null) {
			nodeToRemove.previous.next = nodeToRemove.next;
		}
		if (nodeToRemove.next != null) {
			nodeToRemove.next.previous = nodeToRemove.previous;
		}

		// ako je izbačeni node prvi ili posljednji to mijenjamo
		if (nodeToRemove == first) {
			first = nodeToRemove.next;
		} else if (nodeToRemove == last) {
			last = nodeToRemove.previous;
		}

		size--;
	}

	/**
	 * Helper method that returns TreeNode at specified index.
	 * 
	 * @param index	Index of searched TreeNode.
	 * @return	TreeNode at specified index.
	 */
	private ListNode getNode(int index) {
		ListNode searchedNode = null;

		// ako je objekt u prvoj polovici kreni od početka, inače od kraja.
		if (index < size / 2) {
			searchedNode = first;
			
			for (int i = 0; i < index; i++) {
				searchedNode = searchedNode.next;
			}
		} else {
			searchedNode = last;

			for (int i = size - 1; i > index; i--) {
				searchedNode = searchedNode.previous;
			}
		}

		return searchedNode;
	}
}
