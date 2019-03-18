package hr.fer.zemris.java.hw06.observer2;

/**
 * Implementation of IntegerStorageObserver interface.
 * Tracks number of changes that occurs while this class is registered into observers.
 * After each change prints number of changes so far.
 * Observer for IntegerStorage class.
 * 
 * @author Martin Sršen
 *
 */
public class ChangeCounter implements IntegerStorageObserver {

	/**
	 * Counts number of changes of tracked values.
	 */
	private int counter;
	
	/**
	 * Method(action) that is called each time tracked value changes in IntegerStorage
	 * if this object is registered observer.
	 * Increases counter value by 1 and prints it.
	 * 
	 * @param istorage	IntegerStorageChange class where change was stored.
	 * 					Does certain action using this.
	 */
	@Override
	public void valueChanged(IntegerStorageChange istorage) {
		counter++;
		System.out.println("Number of value changes since tracking: " + counter);
	}

}
