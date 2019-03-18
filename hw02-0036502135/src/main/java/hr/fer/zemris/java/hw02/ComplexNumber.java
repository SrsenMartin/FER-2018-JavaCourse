package hr.fer.zemris.java.hw02;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * ComplexNumber immutable implementation for working with complex numbers.
 * Complex numbers represented as double precision numbers.
 * Allows various calculations on complex numbers like adding, subtraction,
 * 	 division, multiplication, power and root.
 * Has multiple factory methods for making complex number:fromImaginary, 
 * 	fromMagnitudeAndAngle and parse so it can accept Strings.
 * Every time operation is used new complex number is returned,
 * 	numbers used in calculation are unchanged.
 * 
 * @author Martin Sršen
 *
 */
public class ComplexNumber {

	/**
	 * Real part of complex number.
	 */
	private double real;
	/**
	 * Imaginary part of complex number.
	 */
	private double imaginary;

	/**
	 * Constructor that takes real and imaginary part.
	 * 
	 * @param real Real part of complex number.
	 * @param imaginary Imaginary part of complex number.
	 */
	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	/**
	 * Factory method used to return complex number from real part.
	 * 
	 * @param real	Real part of complex number.
	 * @return	new complex number.
	 */
	public static ComplexNumber fromReal(double real) {
		return new ComplexNumber(real, 0);
	}

	/**
	 * Factory method used to return complex number from imaginary part.
	 * 
	 * @param imaginary	Imaginary part of complex number.
	 * @return	new complex number.
	 */
	public static ComplexNumber fromImaginary(double imaginary) {
		return new ComplexNumber(0, imaginary);
	}

	/**
	 * Factory method used to return complex number based on
	 * 	magnitude and angle.
	 * 
	 * @param magnitude	Magnitude of complex number.
	 * @param angle	Angle of complex number.
	 * @return	new complex number.
	 */
	public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
		return new ComplexNumber(magnitude * cos(angle), magnitude * sin(angle));
	}

	/**
	 * Factory method used to return complex number from
	 * 	a string representation.
	 * 
	 * @param s	String representation of complex number.
	 * @return	new complex number.
	 */
	public static ComplexNumber parse(String s) {
		if (s == null) {
			throw new IllegalArgumentException("String to parse can't be null.");
		}

		double real = parseReal(s);
		double imaginary = parseImaginary(s);

		return new ComplexNumber(real, imaginary);
	}

	/**
	 * Helper method that extracts real part of complex number
	 * 	from a string representation.
	 * 
	 * @param s	String representation of complex number.
	 * @return	real part of complex number.
	 */
	private static double parseReal(String s) {
		if (!s.contains("i")) {
			return checkAndParse(s);
		}

		String[] parts;
		boolean splitOnPlus = false;

		if (s.contains("-") && s.lastIndexOf("-") != 0) {
			parts = s.split("-");
		} else if (s.contains("+") && s.indexOf("+") != 0) {
			parts = s.split("\\+");
			splitOnPlus = true;
		} else {
			return 0;
		}

		if (!splitOnPlus && parts[0].equals("")) {
			return checkAndParse("-" + parts[1]);
		} else {
			return checkAndParse(parts[0]);
		}
	}

	/**
	 * 	Helper method that extracts imaginary part of complex number
	 * 	from a string representation.
	 * 
	 * @param String representation of complex number.
	 * @return	imaginary part of complex number.
	 */
	private static double parseImaginary(String s) {
		if (s.contains("i")) {
			String[] parts;
			boolean splitOnPlus = false;

			if(s.equals("i")) {
				return 1;
			}else if(s.equals("-i")) {
				return -1;
			}
			
			//ako postoje 2 slova i,ne valja
			if(s.indexOf("i") != s.lastIndexOf("i")) {
				throw new IllegalArgumentException("There can't be 2 or more i's");
			}
			
			//gleda gdje ce splitat
			//te ako je samo imaginarni dio i duljina dijelova veca od 1,ima nesto iza i,baci exc
			if (s.contains("-") && s.lastIndexOf("-") != 0) {
				parts = s.split("-");
			} else if (s.contains("+") && s.indexOf("+") != 0) {
				parts = s.split("\\+");
				splitOnPlus = true;
			} else if(s.split("i").length == 1){
				return checkAndParse(s.split("i")[0]);
			} else {
				throw new IllegalArgumentException("There can't be anything after i");
			}
			
			//vadi imaginarni dio i salje ga na provjeru
			//takodjer ako je nesto iza i u normalnom broju baca exc
			if (splitOnPlus && parts[1].split("i").length == 1) {
				return checkAndParse(parts[1].split("i")[0]);
			} else if (parts[0].equals("") && parts[2].split("i").length == 1) {
				return checkAndParse("-" + parts[2].split("i")[0]);
			} else if(parts[1].split("i").length == 1){
				return checkAndParse("-" + parts[1].split("i")[0]);
			} else {
				throw new IllegalArgumentException("There can't be anything after i");
			}
		}

		return 0;
	}

	/**
	 * Checks whether String that represents number is valid double number.
	 * If false,throws exception.
	 * 
	 * @param numberAsString	String representation of double.
	 * @return	parsed real or imaginary part of complex number.
	 */
	private static double checkAndParse(String numberAsString) {
		try {
			double number = Double.parseDouble(numberAsString);
			return number;
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("Invalid complex number");
		}
	}

	/**
	 * Getter for real part of complex number.
	 * 
	 * @return	Real part of complex number.
	 */
	public double getReal() {
		return real;
	}

	/**
	 * Getter for imaginary part of complex number.
	 * 
	 * @return	Imaginary part of complex number.
	 */
	public double getImaginary() {
		return imaginary;
	}

	/**
	 * Calculates and returns magnitude of complex number.
	 * 
	 * @return	Magnitude of complex number.
	 */
	public double getMagnitude() {
		return sqrt(real * real + imaginary * imaginary);
	}

	/**
	 * Calculates and returns angle of complex number.
	 * 
	 * @return	Angle of complex number.
	 */
	public double getAngle() {
		double result = atan2(imaginary, real);
		if (result < 0.0) {
			result += 2 * PI;
		}

		return result;
	}

	/**
	 * Used to add two complex numbers and return new one.
	 * 
	 * @param c	Complex number to add to current one.
	 * @return	Result of adding two complex numbers as new complex number.
	 */
	public ComplexNumber add(ComplexNumber c) {
		return new ComplexNumber(this.real + c.real, this.imaginary + c.imaginary);
	}

	/**
	 * Used to subtract two complex numbers and return new one.
	 * 
	 * @param c	Complex number to subtract from current one.
	 * @return	Result of subtracting two complex numbers as new complex number.
	 */
	public ComplexNumber sub(ComplexNumber c) {
		return new ComplexNumber(this.real - c.real, this.imaginary - c.imaginary);
	}

	/**
	 * Used to multiply two complex numbers and return new one.
	 * 
	 * @param c	Complex number to multiply current one.
	 * @return	Result of multiplication two complex numbers as new complex number.
	 */
	public ComplexNumber mul(ComplexNumber c) {
		return new ComplexNumber(this.real * c.real - this.imaginary * c.imaginary,
				this.imaginary * c.real + this.real * c.imaginary);
	}

	/**
	 * Used to divide two complex numbers and return new one.
	 * 
	 * @param c	Complex number to divide from current one.
	 * @return	Result of dividing two complex numbers as new complex number.
	 */
	public ComplexNumber div(ComplexNumber c) {
		if (c.getReal() == 0. && c.getImaginary() == 0.) {
			throw new IllegalArgumentException("Can't divide by 0.");
		}

		double divisor = (c.real * c.real + c.imaginary * c.imaginary);

		return new ComplexNumber((this.real * c.real + this.imaginary * c.imaginary) / divisor,
				(this.imaginary * c.real - this.real * c.imaginary) / divisor);
	}

	/**
	 * Used to calculate power of complex number and return new one.
	 * 
	 * @param n	Exponent of powering complex number.
	 * @return	Result of powering complex number as new complex number.
	 */
	public ComplexNumber power(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("Exponent must be higher or equal to 0. Entered " + n + ".");
		}

		double magnitude = getMagnitude();
		double angle = getAngle();

		double r = pow(magnitude, (double) n);

		return new ComplexNumber(r * cos(n * angle), r * sin(n * angle));
	}

	/**
	 * Used to calculate all roots of given complex number
	 * and returns array of solutions as complex numbers.
	 * 
	 * @param n	Index of rooting
	 * @return	Array of complex numbers that represent solutions.
	 */
	public ComplexNumber[] root(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("Exponent must be higher then 0. Entered" + n + ".");
		}

		double magnitude = getMagnitude();
		double angle = getAngle();

		ComplexNumber[] solutions = new ComplexNumber[n];

		double r = pow(magnitude, (double) 1 / n);
		for (int k = 0; k < n; k++) {
			solutions[k] = new ComplexNumber(r * cos((angle + 2 * PI * k) / n), 
											r * sin((angle + 2 * PI * k) / n));
		}

		return solutions;
	}

	/**
	 * Returns complex number as String.
	 * 
	 * @return complex number as String.
	 */
	@Override
	public String toString() {
		final double THRESHOLD = 9.99999999E-6;

		if (abs(imaginary) < THRESHOLD) {
			return String.format("%.5f", real);
		} else if (abs(real) < THRESHOLD) {
			return String.format("%.5fi", imaginary);
		} else if (imaginary >= 0) {
			return String.format("%.5f+%.5fi", real, imaginary);
		} else {
			return String.format("%.5f%.5fi", real, imaginary);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(ComplexNumber.parse("-i"));
	}
}
