package hr.fer.zemris.java.hw06.observer1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class that represents storage of integer
 * whose change can be observed by different observer classes
 * that implements IntegerStorageObsever interface.
 * Contains methods for adding and removing observers,
 * and getter, setter for value.
 * 
 * @author Martin Sršen
 *
 */
public class IntegerStorage {
	/**
	 * Current IntegerStorage int value.
	 */
	private int value;
	/**
	 * List of registered observers.
	 */
	private List<IntegerStorageObserver> observers; 

	/**
	 * Constructor that takes initialValue for IntegerStorage.
	 * 
	 * @param initialValue	IntegerStorage initial value.
	 */
	public IntegerStorage(int initialValue) {
		this.value = initialValue;
		observers = new ArrayList<>();
	}

	/**
	 * Used to add observer into list of registered observers.
	 * Observer must implement IntegerStorageObserver interface.
	 * 
	 * @param observer	Observer to add into list of registered observers.
	 * @throws NullPointerException if given observer is null value.
	 */
	public void addObserver(IntegerStorageObserver observer) {
		Objects.requireNonNull(observer, "Observer cant't be null value");		
		if(observers.contains(observer))	return;
		
		observers.add(observer);
	}

	/**
	 * Used to remove observer from list of registered observers.
	 * Observer must implement IntegerStorageObserver interface.
	 * 
	 * @param observer	Observer to remove from list of registered observers.
	 */
	public void removeObserver(IntegerStorageObserver observer) {
		observers.remove(observer);
	}

	/**
	 * Used to remove all observers from list of registered observers.
	 */
	public void clearObservers() {
		observers.clear();
	}

	/**
	 * Getter method for IntegerStorage value.
	 * 
	 * @return	value.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Sets new value for IntegerStorage value if value is not equal to current one.
	 * If there are registered observers into list of observers, calls valueChanged method for each.
	 * 
	 * @param value	New value for IntegerStorage value.
	 */
	public void setValue(int value) {
		if (this.value != value) {
			this.value = value;
			
			if (observers != null) {
				List<IntegerStorageObserver> helper = new ArrayList<>(observers);
				
				for (IntegerStorageObserver observer : helper) {
					observer.valueChanged(this);
				}
			}
		}
	}
}
