package hr.fer.zemris.java.hw07.crypto;

/**
 * Class that contains static util methods used to convert from hex string to byte array and vice versa.
 * 
 * @author Martin Sršen
 *
 */
public class Util {
	
	/**
	 * Used to convert from hex string to byte array.
	 * There must be even number of hex digits to create valid byte array.
	 * 
	 * @param keyText	Hex string used to make valid array of it.
	 * @return	byte array made from hex string.
	 * @throws IllegalArgumentException	if hex string has odd number of digits or
	 * 			it contains invalid digits.
	 */
	public static byte[] hextobyte(String keyText) {
		char[] digits = keyText.toCharArray();
		
		if(digits.length % 2 != 0) {
			throw new IllegalArgumentException("Invalid number of digits in hexString: " + keyText);
		}
		
		byte[] numberValues = new byte[digits.length/2];
		
		for(int i = 0; i < digits.length; i += 2) {
			byte bigPart =	(byte) (getDigitValue(digits[i]) << 4);
			byte smallPart = getDigitValue(digits[i+1]);
			
			numberValues[i/2] = (byte) (bigPart + smallPart);
		}
		
		return numberValues;
	}
	
	/**
	 * Used to convert from byte array to valid hex string.
	 * 
	 * @param bytearray	Used to create hex string out of it.
	 * @return	Hex String created from byte array.
	 */
	public static String bytetohex(byte[] bytearray) {	
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < bytearray.length; i++) {
			byte bigPart = (byte) (bytearray[i] >> 4 & 0xF) ;
			byte smallPart = (byte) (bytearray[i] & 0xF);
			
			sb.append(getHexNumber(bigPart)).append(getHexNumber(smallPart));
		}
		
		return sb.toString();
	}
	
	/**
	 * Helper method that converts given byte value into valid hex digit.
	 * 
	 * @param number	Byte number to convert into valid hex digit.
	 * @return	Hex digit created of given number.
	 */
	private static String getHexNumber(byte number) {
		if(number >= 10) {
			return (char) ('a' + (number - 10)) + "";
		}
		
			return number + "";
	}
	
	/**
	 * Helper method that converts hex digit into equivalent byte value.
	 * 
	 * @param c	Char representing hex digit.
	 * @return	Byte value of given hex digit.
	 * @throws IllegalArgumentException if given char is invalid hex digit.
	 */
	private static byte getDigitValue(char c) {
		c = Character.toLowerCase(c);
		
		if(Character.isDigit(c)) {
			return Byte.parseByte(c + "");
		}else if(isValidLetterDigit(c)) {
			return (byte) (c - 'a' + 10);
		}else {
			throw new IllegalArgumentException("Expected hex digit, found: " + c);
		}
	}

	/**
	 * Checks whether given char is valid hex digit that is written as letter.
	 * 
	 * @param c	Char representing hex digit.
	 * @return	true if given char is valid hex letter digit,otherwise false.
	 */
	private static boolean isValidLetterDigit(char c) {
		c = Character.toLowerCase(c);
		return  c == 'a' ||
				c == 'b' ||
				c == 'c' ||
				c == 'd' ||
				c == 'e' ||
				c == 'f';
	}
}
