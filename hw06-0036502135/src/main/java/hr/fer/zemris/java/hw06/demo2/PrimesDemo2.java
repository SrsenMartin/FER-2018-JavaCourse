package hr.fer.zemris.java.hw06.demo2;

/**
 * Program that illustrates working of PrimeCollection class.
 * Program iterates 2 times through collection and prints their Cartesian product.
 * 
 * @author Martin Sršen
 *
 */
public class PrimesDemo2 {

	/**
	 * Called when program is started.
	 * 
	 * @param args Arguments from command prompt.Not used in this example.
	 */
	public static void main(String[] args) {
		PrimesCollection primesCollection = new PrimesCollection(2);

		for (Integer prime : primesCollection) {
			for (Integer prime2 : primesCollection) {
				System.out.println("Got prime pair: " + prime + ", " + prime2);
			}
		}
	}
}
