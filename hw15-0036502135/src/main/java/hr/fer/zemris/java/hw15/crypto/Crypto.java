package hr.fer.zemris.java.hw15.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * Class containing methods to get encoded hash value from given string/password
 * using SHA-1 algorithm.
 * 
 * @author Martin Sršen
 *
 */
public class Crypto {
	
/**
 * Takes password/string, and encodes it using SHA-1 algorithm
 * into hash value.
 * 
 * @param password	string to encode.
 * @return	encoded hash string value.
 */
	public static String getEncodedHashValue(String password) {
			MessageDigest msgDigest = null;
			try {
				msgDigest = MessageDigest.getInstance("SHA-1");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			
			byte[] passBytes = password.getBytes();
			
			msgDigest.update(passBytes);
			byte[] hash = msgDigest.digest();
			
			return bytetohex(hash);
	}
	
	/**
	 * Used to convert from byte array to valid hex string.
	 * 
	 * @param bytearray	Used to create hex string out of it.
	 * @return	Hex String created from byte array.
	 */
	private static String bytetohex(byte[] bytearray) {	
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
}
