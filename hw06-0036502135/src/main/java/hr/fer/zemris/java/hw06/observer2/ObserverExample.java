package hr.fer.zemris.java.hw06.observer2;

/**
 * Program that illustrates simple example
 *  for IntegerStorage and its different types of observers.
 * 
 * @author Martin Sršen
 *
 */
public class ObserverExample {
	
	/**
	 * Called when program is started.
	 * 
	 * @param args Arguments from command prompt.Not used in this example.
	 */
	public static void main(String[] args) {
		
		IntegerStorage istorage = new IntegerStorage(20);
		
		IntegerStorageObserver observer = new SquareValue();
		
		istorage.addObserver(observer);
		istorage.removeObserver(observer);
		istorage.addObserver(new ChangeCounter());
		istorage.addObserver(new DoubleValue(1));
		istorage.addObserver(new DoubleValue(2));
		istorage.addObserver(new DoubleValue(2));
		
		istorage.setValue(5);
		istorage.setValue(2);
		istorage.setValue(25);
		istorage.setValue(13);
		istorage.setValue(22);
		istorage.setValue(15);
	}
	
}
