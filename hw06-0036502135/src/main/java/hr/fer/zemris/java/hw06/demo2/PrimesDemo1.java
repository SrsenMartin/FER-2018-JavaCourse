package hr.fer.zemris.java.hw06.demo2;

/**
 * Program that illustrates working of PrimeCollection class.
 * Program iterates and prints first 5 prime numbers.
 * 
 * @author Martin Sršen
 *
 */
public class PrimesDemo1 {

	/**
	 * Called when program is started.
	 * 
	 * @param args Arguments from command prompt.Not used in this example.
	 */
	public static void main(String[] args) {
		PrimesCollection primesCollection = new PrimesCollection(5); // 5: how many of them

		for (Integer prime : primesCollection) {
			System.out.println("Got prime: " + prime);
		}
	}
}
