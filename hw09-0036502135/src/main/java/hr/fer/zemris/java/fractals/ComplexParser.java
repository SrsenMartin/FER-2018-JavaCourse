package hr.fer.zemris.java.fractals;

import hr.fer.zemris.math.Complex;

/**
 * Class that represents parser for complex number.
 * Takes string as input through constructor.
 * When getComplex method is called, input is beeing parsed
 * and if it is valid complex number returns Complex, 
 * else throws appropriate exception.
 * 
 * @author Martin Sršen
 *
 */
public class ComplexParser {
	/**
	 * Input as char array.
	 */
	char[] pts;
	/**
	 * Index of current elements in pts array.
	 */
	int index;
	
	/**
	 * Constructor that takes String input that is assumed to be valid complex number.
	 * 
	 * @param input	String representation of complex number.
	 * @throws IllegalArgumentException if given input length is 0.
	 */
	public ComplexParser(String input){
		input = input.trim().toLowerCase();
		
		if(input.length() == 0) {
			throw new IllegalArgumentException("You must provide some input.");
		}
		
		pts = input.toCharArray();
	}
	
	/**
	 * Method when called parses input String and returns Complex
	 * if input is valid complex number,
	 * else throws IllegalArgumentException.
	 * 
	 * @return	Complex number created from input.
	 * @throws	IllegalArgumentException if given input is invalid String.
	 */
	public Complex getComplex() {
		double real = getRealPart();
		removeSpaces();
		double imaginary = getImaginaryPart();
		removeSpaces();
		
		if(index < pts.length) {
			throw new IllegalArgumentException("Invalid complex number given.");
		}
	
		return new Complex(real, imaginary);
	}

	/**
	 * Helper method that parses imaginary part from input.
	 * If imaginary part is not provided ,method returns 0.
	 * 
	 * @return	imaginary part from input, or 0 if not provided.
	 * @throws IllegalArgumentException	if invalid input is given.
	 */
	private double getImaginaryPart() {
		StringBuilder sb = new StringBuilder();
		boolean iCont = false;
		
		if(index < pts.length && (pts[index] == '-' || pts[index] == '+'))	sb.append(pts[index++]);
		else if(index < pts.length && index != 0) {
			throw new IllegalArgumentException("There must be + or - between numbers.");
		}
		removeSpaces();
		if(index < pts.length && pts[index] == 'i')	iCont = true;
		if(index < pts.length && pts[index] != 'i') {
			throw new IllegalArgumentException("Expected i, but was " + pts[index]);
		}
		index++;
		
		while(index < pts.length && (Character.isDigit(pts[index]) || pts[index] == '.')) {
			sb.append(pts[index++]);
		}
		
		if((sb.length() == 0 || (sb.length() == 1 && sb.toString().equals("+"))) && iCont)	return 1;
		else if(sb.length() == 0)	return 0;
		else if(sb.length() == 1 && sb.toString().equals("-") && iCont)	return -1;
		
		return Double.parseDouble(sb.toString());
	}

	/**
	 * Helper method that parses real part from input.
	 * If real part is not provided ,method returns 0.
	 * 
	 * @return	real part from input, or 0 if not provided.
	 * @throws IllegalArgumentException	if invalid input is given.
	 */
	private double getRealPart() {
		StringBuilder sb = new StringBuilder();
		int start = index;
		
		if(pts[index] == '-' || pts[index] == '+')	sb.append(pts[index++]);
		removeSpaces();
		if(index < pts.length && pts[index] == 'i') {
			index = start;
			return 0;
		}
		
		while(index < pts.length && (Character.isDigit(pts[index]) || pts[index] == '.')) {
			sb.append(pts[index++]);
		}
		
		if(sb.length() == 0)	return 0;
		return Double.parseDouble(sb.toString());
	}
	
	/**
	 * Helper method used to remove spaces in char array.
	 */
	private void removeSpaces() {
		while (index < pts.length) {
			char current = pts[index];

			if (current == ' ' || current == '\t') {
				index++;
				continue;
			}

			break;
		}
	}
}
