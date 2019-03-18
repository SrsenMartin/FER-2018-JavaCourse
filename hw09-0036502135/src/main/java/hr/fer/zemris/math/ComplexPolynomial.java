package hr.fer.zemris.math;

import java.util.Objects;

/**
 * Class that represents complex numbers polynomial.
 * Takes n factors through constructor which represents coefficients
 * of nth potency.
 * If n elements are given with constructor,than polynomial order will be n-1.
 * Has methods to multiply 2 polynomials, derive it or to calculate for given z.
 * 
 * @author Martin Sršen
 *
 */
public class ComplexPolynomial {

	/**
	 * Factors of given polynomial.
	 */
	private Complex[] factors;
	
	/**
	 * Constructor that accepts variable number of factors through arguments.
	 * 
	 * @param factors	polynomial factors,from lowest potency to highest factors.
	 * @throws NullPointerException	if given factors are null.
	 * @throws	IllegalArgumentException	if 0 factors are provided.
	 */
	public ComplexPolynomial(Complex ...factors) {
		Objects.requireNonNull(factors, "Given factors can't be null.");
		
		if(factors.length == 0) {
			throw new IllegalArgumentException("Must provide at least 1 factor.");
		}
		
		this.factors = factors;
	}
	
	/**
	 * Calculates and returns order of this polynomial.
	 * 
	 * @return	polynomial order.
	 */
	public short order() {
		return (short) (factors.length - 1);
	}
	
	/**
	 * Creates new ComplexPolynomial object with factors calculated
	 * by multiplication of this ComplexPolynomial object and given one.
	 * 
	 * @param p	ComplexPolynomial to multiply with.
	 * @return	new ComplexPolynomial,result of multiplication.
	 * @throws NullPointerException	if given ComplexPolynomial is null.
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		Objects.requireNonNull(p, "Given ComplexPolynomial can't be null.");
		
		Complex[] newFactors = new Complex[this.order() + p.order() + 1];
		
		for(int i = 0; i < factors.length; i++) {
			for(int j = 0; j < p.factors.length; j++) {
				newFactors[i+j] = newFactors[i+j] == null ? 
						factors[i].multiply(p.factors[j]) : newFactors[i+j].add(factors[i].multiply(p.factors[j]));
			}
		}
		
		return new ComplexPolynomial(newFactors);
	}
	
	/**
	 * Returns new ComplexPolynomial got by derivation of
	 * this ComplexPolynomial object.
	 * 
	 * @return	new ComplexPolynomial, result of derivation.
	 */
	public ComplexPolynomial derive() {
		if(order() == 0) {
			return new ComplexPolynomial(Complex.ZERO);
		}
		
		Complex[] newFactors = new Complex[order()];
		
		for(int i = 1; i < factors.length; i++) {
			newFactors[i-1] = factors[i].multiply(new Complex(i, 0));
		}
		
		return new ComplexPolynomial(newFactors);
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
		
		Complex result = Complex.ZERO;
		Complex currentZ = Complex.ONE;
		
		for(Complex factor: factors) {
			Complex forFactor = factor.multiply(currentZ);
			result = result.add(forFactor);
			
			currentZ = currentZ.multiply(z);
		}
		
		return result;
	}
	
	/**
	 * String representation of ComplexPolynomail.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for(int i = order(); i >= 0; i--) {
			if(i != factors.length - 1) {
				sb.append("+");
			}

			sb.append("(").append(factors[i].toString()).append(")");
			sb.append("z^").append(i);
		}
		
		return sb.toString();
	}
}
