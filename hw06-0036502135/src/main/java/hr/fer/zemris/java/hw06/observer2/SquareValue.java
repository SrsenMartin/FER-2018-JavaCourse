package hr.fer.zemris.java.hw06.observer2;

/**
 * Implementation of IntegerStorageObserver interface.
 * After value in IntegerStorage changes prints changed value
 * and square of that value.
 * Observer for IntegerStorage class.
 * 
 * @author Martin Sršen
 *
 */
public class SquareValue implements IntegerStorageObserver {

	/**
	 * Method(action) that is called each time tracked value changes in IntegerStorage
	 * if this object is registered observer.
	 * Prints changed IntegerStorage value and its square value.
	 * 
	 * @param istorage	IntegerStorageChange instance where change was stored.
	 * 					Does certain action using this.
	 */
	@Override
	public void valueChanged(IntegerStorageChange istorage) {
		System.out.println("Provided new value: " + istorage.getStorage().getValue() +
				" square is " + istorage.getStorage().getValue()*istorage.getStorage().getValue());
	}

}
