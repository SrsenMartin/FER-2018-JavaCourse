package hr.fer.zemris.java.gui.prim;

import static java.lang.Math.sqrt;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * ListModel that stores prime numbers generated by method next(),
 * and outputs them as soon as they are calculated to lists 
 * that uses it as model, that are added as listeners of this model.
 * Has methods to add list data listeners, get element at position, 
 * get list model current size, to remove listeners, and method next
 * that calculates next prime number and saves it into list model data,
 * and after writes it to all listener lists.
 * 
 * @author Martin Sršen
 *
 */
public class PrimListModel<T> implements ListModel<Integer> {

	/**
	 * List of all listeners to this model.
	 */
	private List<ListDataListener> listeners;
	/**
	 * List of all generated prime numbers data.
	 */
	private List<Integer> primeNumbers;
	/**
	 * Current generated prime number.
	 */
	int currentPrime;
	
	/**
	 * Default constructor that initializes model state.
	 */
	public PrimListModel() {
		listeners = new ArrayList<>();
		primeNumbers = new ArrayList<>();
		currentPrime = 1;
		primeNumbers.add(currentPrime);
	}
	
	/**
	 * Adds new listener into list of listeners.
	 * If null as listener is given, does nothing.
	 * 
	 * @param listener Listener to add to list of listeners.
	 */
	@Override
	public void addListDataListener(ListDataListener listener) {
		if(listener == null)	return;
		
		listeners.add(listener);
	}

	/**
	 * Returns element on given position in list.
	 * 
	 * @param position	Position of element in list to return.
	 * @return element at given position in data list.
	 */
	@Override
	public Integer getElementAt(int position) {
		return primeNumbers.get(position);
	}

	/**
	 * Returns current size of data in model.
	 * 
	 * @return	Number of elements in data list.
	 */
	@Override
	public int getSize() {
		return primeNumbers.size();
	}

	/**
	 * Removes listener from list of listeners if it exists.
	 * If null as listener is given,or if it does not exist, does nothing.
	 * 
	 * @param listener Listener to remove from list of listeners.
	 */
	@Override
	public void removeListDataListener(ListDataListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Calculates next prime number, saves it into model data at last position
	 * and notifies all registered listeners that change occurred.
	 */
	public void next() {
		int pos = primeNumbers.size();
		
		while(!isPrime(++currentPrime));
		primeNumbers.add(currentPrime);
		
		ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, pos, pos);
		listeners.forEach(listener -> listener.intervalAdded(event));
	}

	/**
	 * Helper method that checks if given number is prime number or not.
	 * 
	 * @param n Given number to check if it is prime number.
	 * @return true if given number is prime,false otherwise.
	 */
	private boolean isPrime(int n) {
		for(int i = 2; i <= sqrt(n); i++) {
			if(n%i == 0)	return false;
		}
		
		return true;
	}
}