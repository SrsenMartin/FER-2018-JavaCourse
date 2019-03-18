package hr.fer.zemris.math;

import java.util.Objects;

/**
 * Class that represents complex numbers rooted polynomial.
 * Takes n roots through constructor which represents nullpoints of polynomial.
 * Provides methods to calculate polynomial for given z,
 * to turn it into ComplexPolynomial and to find index of closest root for given complex number
 * that is within treshold, or -1 if there is no such root.
 * 
 * @author Martin Sršen
 *
 */
public class ComplexRootedPolynomial {

	/**
	 * Roots of given polynomial.
	 */
	private Complex roots[];
	
	/**
	 * Constructor that accepts variable number of roots through arguments.
	 * 
	 * @param factors	polynomial roots.
	 * @throws NullPointerException	if given roots are null.
	 * @throws	IllegalArgumentException	if 0 roots are provided.
	 */
	public ComplexRootedPolynomial(Complex ...roots) {
		Objects.requireNonNull(roots, "Given roots can't be null.");
		
		if(roots.length == 0) {
			throw new IllegalArgumentException("Must provide at least 1 root.");
		}
		
		this.roots = roots;
	}
	
	/**
	 * Calculates polynomial for given complex number z.
	 * Returns new Complex number as result of calculation.
	 * 
	 * @param z	Polynomial unknown element used to calculate result.
	 * @return	new Complex as result of calculating equation.
	 * @throws 	NullPointerException if given Complex is null.
	 */
	public Complex apply(Complex z) {
		Objects.requireNonNull(z, "Complex can't be null.");
		
		Complex result = Complex.ONE;
		
		for(Complex root: roots) {
			Complex nullpoint = z.sub(root);
			
			result = result.multiply(nullpoint);
		}
		
		return result;
	}
	
	/**
	 * Method that creates ComplexPolynomial created from this rooted polynomial.
	 * 
	 * @return	ComplexPolynomial created from this rooted polynomial.
	 */
	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial result = new ComplexPolynomial(Complex.ONE);
		
		for(Complex root: roots) {
			result = result.multiply(new ComplexPolynomial(root.negate(), Complex.ONE));
		}
		
		return result;
	}

	/**
	 * Method that finds index of closest root for given complex number z that is within
	 * given treshold or, if there is no such root, returns -1.
	 * 
	 * @param z	Complex number used to find closest index for it.
	 * @param treshold	given treshold.
	 * @return	index of closest root for given complex number that is within given treshold.
	 * @throws NullPointerException if given Complex is null.
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		Objects.requireNonNull(z, "Complex can't be null.");
		
		double closest = Double.MAX_VALUE;
		int index = -2;
		
		for(int i = 0; i < roots.length; i++) {
			double distance = roots[i].sub(z).module();
			
			if(distance < closest && distance <= treshold) {
				index = i;
				closest = distance;
			}
		}
		
		return index + 1;
	}

	/**
	 * String representation of ComplexRootedPolynomail.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for(Complex root: roots) {
			sb.append("(").append("z-").append("(").append(root.toString()).append("))");
		}
		
		return sb.toString();
	}
}
