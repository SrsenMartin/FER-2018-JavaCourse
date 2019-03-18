package hr.fer.zemris.java.hw02.demo;

import hr.fer.zemris.java.hw02.ComplexNumber;

/**
 * Simple program that demonstrates working with complex numbers.
 * 
 * @author Martin Sršen
 *
 */
public class ComplexDemo {

	/**
	 * Called when program is started.
	 * Does few operations with complex numbers and prints
	 * result to standard output.
	 * 
	 * @param args Arguments from command prompt.Not used in this program.
	 */
	public static void main(String[] args) {
		ComplexNumber c1 = new ComplexNumber(2, 3);
		ComplexNumber c2 = ComplexNumber.parse("2.5-3i");
		ComplexNumber c3 = c1.add(ComplexNumber.fromMagnitudeAndAngle(2, 1.57)).div(c2).power(3).root(2)[1];
		System.out.println(c3);
	}
}
