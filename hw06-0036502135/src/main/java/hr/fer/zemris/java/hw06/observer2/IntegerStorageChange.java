package hr.fer.zemris.java.hw06.observer2;

/**
 * Class where IntegerStorage changes are stored.
 * Each time value changes new class is created and sent to observers.
 * Class stores IntegerStorage object, old value and new value.
 * 
 * @author Martin Sršen
 *
 */
public class IntegerStorageChange {
	
	/**
	 * IntegerStorage where change had occurred.
	 */
	private IntegerStorage storage;
	/**
	 * Value before change.
	 */
	private int beforeValue;
	/**
	 * Value after change.
	 */
	private int afterValue;
	
	/**
	 * Constructor that initializes values for InternetStorageChange object.
	 * 
	 * @param storage	IntegerStorage where change had occurred.
	 * @param afterValue	Value after change.
	 * @param beforeValue	Value before change.
	 */
	public IntegerStorageChange(IntegerStorage storage, int afterValue, int beforeValue) {
		this.storage = storage;
		this.afterValue = afterValue;
		this.beforeValue = beforeValue;
	}
	
	/**
	 * Getter method for stored IntegerStorage object.
	 * 
	 * @return	storage
	 */
	public IntegerStorage getStorage() {
		return storage;
	}
	
	/**
	 * Getter method for value before change.
	 * 
	 * @return beforeValue
	 */
	public int getBeforeValue() {
		return beforeValue;
	}
	
	/**
	 * Getter method for value after change.
	 * 
	 * @return	afterValue
	 */
	public int getAfterValue() {
		return afterValue;
	}
	
}
