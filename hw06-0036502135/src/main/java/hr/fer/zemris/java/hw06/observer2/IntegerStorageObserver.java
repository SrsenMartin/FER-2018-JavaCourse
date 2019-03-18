package hr.fer.zemris.java.hw06.observer2;

/**
 * Base interface for all IntegerStorage observers.
 * Observers job is to do certain action every time certain tracked value into IntegerStorgae changes.
 * Interface has one method for that job called valueChanged.
 * 
 * @author Martin Sršen
 */
public interface IntegerStorageObserver {
	/**
	 * Method(action) that is called each time tracked value changes in IntegerStorage
	 * if object that implements it is registered observer.
	 * 
	 * @param istorage	IntegerStorage instance where change was noticed.
	 * 					Does certain action using this.
	 */
	public void valueChanged(IntegerStorageChange istorageChange);
}