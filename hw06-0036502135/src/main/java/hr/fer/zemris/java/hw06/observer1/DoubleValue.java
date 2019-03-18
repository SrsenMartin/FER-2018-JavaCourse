package hr.fer.zemris.java.hw06.observer1;

/**
 * Implementation of IntegerStorageObserver interface.
 * Takes one variable to constructor that decides how many changes will class track.
 * After variable comes to 0, it removes itself from observers.
 * Prints double value of current value in IntegerStorage after each change
 *  while this class is registered into observers.
 * Observer for IntegerStorage class.
 * 
 * @author Martin Sršen
 *
 */
public class DoubleValue implements IntegerStorageObserver {

	/**
	 * Decides how many times will class observe changes.
	 */
	private int times;
	
	/**
	 * Constructor that takes times value as argument. 
	 */
	public DoubleValue(int times) {
		this.times = times;
	}
	
	/**
	 * Method(action) that is called each time tracked value changes in IntegerStorage
	 * if this object is registered observer.
	 * Decreases times value by 1 and prints double value of changed IntegerStorage value.
	 * 
	 * @param istorage	IntegerStorage instance where change was noticed.
	 * 					Does certain action using this.
	 */
	@Override
	public void valueChanged(IntegerStorage istorage) {
		if(times == 0) {
			istorage.removeObserver(this);
			return;
		}
		
		System.out.println("Double value: " + istorage.getValue()*2);
		times--;
	}
}
