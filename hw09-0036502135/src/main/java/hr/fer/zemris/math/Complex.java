package hr.fer.zemris.math;

import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ComplexNumber immutable implementation for working with complex numbers.
 * Complex numbers represented as double precision numbers.
 * Allows various methods that will return new Complex number
 * calculated by operation on this and given complex number.
 * 
 * @author Martin Sršen
 *
 */
public class Complex {

	/**
	 * Real part of complex number.
	 */
	private final double real;
	/**
	 * Imaginary part of complex number.
	 */
	private final double imaginary;
	
	/**
	 * Constant that represents Complex number with value 0.
	 */
	public static final Complex ZERO = new Complex(0,0);
	/**
	 * Constant that represents Complex number with value 1;
	 */
	public static final Complex ONE = new Complex(1,0);
	/**
	 * Constant that represents Complex number with value -1.
	 */
	public static final Complex ONE_NEG = new Complex(-1,0);
	/**
	 * Constant that represents Complex number with value i.
	 */
	public static final Complex IM = new Complex(0,1);
	/**
	 * Constant that represents Complex number with value -i.
	 */
	public static final Complex IM_NEG = new Complex(0,-1);
	
	public Complex() {
		real = 0.;
		imaginary = 0.;
	}
	
	/**
	 * Constructor that takes real and imaginary part.
	 * 
	 * @param re	Real part of complex number.
	 * @param im	Imaginary part of complex number.
	 */
	public Complex(double re, double im) {
		real = re;
		imaginary = im;
	}
	
	/**
	 * Calculates and returns module of complex number.
	 * 
	 * @return	Module of complex number.
	 */
	public double module() {
		return sqrt(real * real + imaginary * imaginary);
	}
	
	/**
	 * Used to multiply two complex numbers and return new one.
	 * 
	 * @param c	Complex number to multiply current one.
	 * @return	Result of multiplication two complex numbers as new complex number.
	 * @throws NullPointerException	if given Complex is null.
	 */
	public Complex multiply(Complex c) {
		Objects.requireNonNull(c, "Complex can't be null.");
		
		double newReal = this.real * c.real - this.imaginary * c.imaginary;
		double newImaginary = this.imaginary * c.real + this.real * c.imaginary;
		
		return new Complex(newReal, newImaginary);
	}
	
	/**
	 * Used to divide two complex numbers and return new one.
	 * 
	 * @param c	Complex number to divide from current one.
	 * @return	Result of dividing two complex numbers as new complex number.
	 * @throws NullPointerException	if given Complex is null.
	 */
	public Complex divide(Complex c) {
		Objects.requireNonNull(c, "Complex can't be null.");
		
		double divisor = (c.real * c.real + c.imaginary * c.imaginary);
		if(divisor == 0.) {
			throw new IllegalArgumentException("Can't divide by 0.");
		}
		
		double newReal = (this.real * c.real + this.imaginary * c.imaginary) / divisor;
		double newImaginary = (this.imaginary * c.real - this.real * c.imaginary) / divisor;
		
		return new Complex(newReal, newImaginary);
	}
	
	/**
	 * Used to add two complex numbers and return new one.
	 * 
	 * @param c	Complex number to add to current one.
	 * @return	Result of adding two complex numbers as new complex number.
	 * @throws NullPointerException	if given Complex is null.
	 */
	public Complex add(Complex c) {
		Objects.requireNonNull(c, "Complex can't be null.");
		
		return new Complex(this.real + c.real, this.imaginary + c.imaginary);
	}
	
	/**
	 * Used to subtract two complex numbers and return new one.
	 * 
	 * @param c	Complex number to subtract from current one.
	 * @return	Result of subtracting two complex numbers as new complex number.
	 * @throws NullPointerException	if given Complex is null.
	 */
	public Complex sub(Complex c) {
		Objects.requireNonNull(c, "Complex can't be null.");
		
		return new Complex(this.real - c.real, this.imaginary - c.imaginary);
	}
	
	/**
	 * Used to return new Complex number as negation of this one.
	 * 
	 * @return	new Complex number, value is negation of this one.
	 */
	public Complex negate() {
		return new Complex(-1*real, -1*imaginary);
	}
	
	/**
	 * Used to calculate power of complex number and return new one.
	 * 
	 * @param n	Exponent of powering complex number.
	 * @return	Result of powering complex number as new complex number.
	 * @throws	IllegalArgumentException if given exponent is negative integer.
	 */
	public Complex power(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("Exponent must be non-negative integer.");
		}
		
		double angle = atan2(imaginary, real);
		double r = pow(module(), (double) n);
		
		return new Complex(r * cos(n * angle), r * sin(n * angle));
	}
	
	/**
	 * Used to calculate all roots of given complex number
	 * and returns list of solutions as complex numbers.
	 * 
	 * @param n	Index of rooting
	 * @return	List of complex numbers that represent solutions.
	 * @throws	IllegalArgumentException if given exponent is not positive integer.
	 */
	public List<Complex> root(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("Exponent must be positive integer.");
		}
		
		List<Complex> roots = new ArrayList<>(n);
		
		double angle = atan2(imaginary, real);
		double r = pow(module(), (double) 1 / n);
		
		for (int k = 0; k < n; k++) {
			Complex root = new Complex(r * cos((angle + 2 * PI * k) / n), 
											r * sin((angle + 2 * PI * k) / n));
			
			roots.add(root);
		}
		
		return roots;
	}
	
	/**
	 * Returns complex number as String.
	 * Written using 6 decimal places.
	 * 
	 * @return complex number as String.
	 */
	@Override
	public String toString() {
		if(imaginary < 0) {
			return String.format("%.6f%.6fi", real, imaginary);
		}
		
		return String.format("%.6f+%.6fi", real, imaginary);
	}
	
	/**
	 * Getter method for real part of complex number.
	 * 
	 * @return	real part of complex number.
	 */
	public double getReal() {
		return real;
	}
	
	/**
	 * Getter method for imaginary part of complex number.
	 * 
	 * @return	imaginary part of complex number.
	 */
	public double getImaginary() {
		return imaginary;
	}
}
