package hr.fer.zemris.java.custom.scripting.exec;

import java.util.function.BinaryOperator;

/**
 * Class that represents simple Wrapper for all different objects and null value.
 * It implements methods that can perform operations with other given object,
 *  allowing only few object to do them. String, Double, Integer and null are allowed.
 *  Implements method that will compare wrapper with given object, only 4 types that
 *  we mentioned are allowed.
 *  Null values in operations are transformed into Integers with value 0.
 * 
 * @author Martin Sršen
 *
 */
public class ValueWrapper {

	/**
	 * Object that ValueWrapper wraps or null.
	 */
	private Object value;
	
	/**
	 * Constructor that takes Object to wrap.
	 * Null alowed.
	 * 
	 * @param value	Object to wrap or null value.
	 */
	public ValueWrapper(Object value) {	
		this.value = value;
	}

	/**
	 * Getter method for wrapped value.
	 * 
	 * @return	value.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Setter method for wrapped value.
	 * 
	 * @param value	Object or null value to wrap.
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	/**
	 * Adds currently wrapped value to given Object value
	 * and stores result into wrapped value.
	 * Allowed values are null, String, Double and Integer.
	 * Null values are represented with Integer with value 0.
	 * 
	 * @param incValue	Value to add to currently wrapped value.
	 * @throws	RuntimeException if any value is invalid Object type or String type isn't number representation.
	 */
	public void add(Object incValue) {
		Number number1 = convertToNumber(this.getValue());
		Number number2 = convertToNumber(incValue);
		
		this.setValue(operate(number1, number2, (v1, v2) -> v1+v2));
	}

	/**
	 * Subtract currently wrapped value to given Object value
	 * and stores result into wrapped value.
	 * Allowed values are null, String, Double and Integer.
	 * Null values are represented with Integer with value 0.
	 * 
	 * @param decValue	Value to subtract from currently wrapped value.
	 * @throws	RuntimeException if any value is invalid Object type or String type isn't number representation.
	 */
	public void subtract(Object decValue) {
		Number number1 = convertToNumber(this.getValue());
		Number number2 = convertToNumber(decValue);
		
		this.setValue(operate(number1, number2, (v1, v2) -> v1-v2));
	}
	
	/**
	 * Multiplies currently wrapped value to given Object value
	 * and stores result into wrapped value.
	 * Allowed values are null, String, Double and Integer.
	 * Null values are represented with Integer with value 0.
	 * 
	 * @param mulValue	Value to multiply with currently wrapped value.
	 * @throws	RuntimeException if any value is invalid Object type or String type isn't number representation.
	 */
	public void multiply(Object mulValue) {
		Number number1 = convertToNumber(this.getValue());
		Number number2 = convertToNumber(mulValue);
		
		this.setValue(operate(number1, number2, (v1, v2) -> v1*v2));
	}
	
	/**
	 * Divide currently wrapped value to given Object value
	 * and stores result into wrapped value.
	 * Allowed values are null, String, Double and Integer.
	 * Null values are represented with Integer with value 0.
	 * 
	 * @param divValue	Value to divide from currently wrapped value.
	 * @throws	RuntimeException if any value is invalid Object type or String type isn't number representation.
	 * @throws IllegalArgumentException if division by 0 is attempted.
	 */
	public void divide(Object divValue) {
		Number number1 = convertToNumber(this.getValue());
		Number number2 = convertToNumber(divValue);
		
		if(number2.doubleValue() < 1E-5) {
			throw new IllegalArgumentException("Divide by 0 attempt: " + number1 + "/" + number2);
		}
		
		this.setValue(operate(number1, number2, (v1, v2) -> v1/v2));
	}
	
	/**
	 * Compares currently wrapped value to given Object value.
	 * Allowed values are null, String, Double and Integer.
	 * Null values are represented with Integer with value 0.
	 * 
	 * @param withValue	Value to compare wrapped value with.
	 * @return	1 if wrapped value is bigger, 0 if they are equal, -1 if wrapped value is less that given.
	 */
	public int numCompare(Object withValue) {
		Double number1 = (Double) convertToNumber(this.getValue()).doubleValue();
		Double number2 = (Double) convertToNumber(withValue).doubleValue();
		
		return number1.compareTo(number2);
	}
	
	@Override
	public String toString() {
		return value == null ? "0" : value.toString();
	}
	
	/**
	 * Helper method that calculates operation between 2 given numbers.
	 * Operation is determined by BinaryOperator functional interface.
	 * 
	 * @param originalNumber	Wrapped Object number value.
	 * @param givenNumber	Given Object number value.
	 * @param operator	BinaryOperator functional interface that calculates operation and returns result.
	 * @return	result of operation between 2 given numbers.
	 */
	private static Number operate(Number originalNumber, Number givenNumber, BinaryOperator<Double> operator) {
		Double result = operator.apply(originalNumber.doubleValue(), givenNumber.doubleValue());
		
		if(originalNumber instanceof Integer && givenNumber instanceof Integer) {
			return (Integer) result.intValue();
		}

		return result;
	}
	
	/**
	 * Takes Object through argument and check whether given Object is number representation.
	 * Null value is represented as Integer with value of 0.
	 * Valid objects are String, Double, Integer and null value.
	 * 
	 * @param obj	Object to check if it represents number.
	 * @return	Number that was converted from Object.
	 * @throws	RuntimeException if given String isn't number representation or
	 * 			if invalid Object type was given.
	 */
	private static Number convertToNumber(Object obj) {
		checkObjectValidation(obj);
		
		if(obj == null) {
			return Integer.valueOf(0);
		}else if(obj instanceof Integer || obj instanceof Double) {
			return (Number) obj;
		}else{
			String num = (String) obj;
			
			if(num.contains(".") || num.toUpperCase().contains("E")) {
				try {
					return Double.parseDouble(num.trim());
				}catch(NumberFormatException exc) {}
			}else {
				try {
					return Integer.parseInt(num.trim());
				}catch(NumberFormatException ex) {}
			}
			
			throw new RuntimeException("Invalid string representation of number: " + num);
		}
	}
	
	/**
	 * Helper method that checks whether given Object is allowed Object type.
	 * Allowed types are String, Double, Integer and null value.
	 * 
	 * @param value	Value to check.
	 * @throws	RuntimeException if given Object isn't allowed Object type.
	 */
	private static void checkObjectValidation(Object value) {
		boolean valid = value == null ||
						value instanceof Integer ||
						value instanceof Double ||
						value instanceof String;
		
		if(!valid) {
			throw new RuntimeException("Invalid object type for ValueWrapper operation." + value.getClass());
		}
	}
}
