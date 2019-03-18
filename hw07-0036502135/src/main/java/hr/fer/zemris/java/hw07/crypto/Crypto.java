package hr.fer.zemris.java.hw07.crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Program that takes 2 or 3 arguments through command line based on wished command.
 * First argument is wished command, can be checksha - to check if file had changed recently,
 * encrypt - to encrypt given file and decrypt - to decrypt encrypted file.
 * Second argument is input file, and third argument is output file if we are encrypting or decrypting.
 * After program starts, in checksha mode user will need to provide expected digest through system in,
 * in encrypt and decrypt mode will need to provide keyText and ivText as 32 number hex-digit.
 * 
 * @author Martin Sršen
 *
 */
public class Crypto {
	
	/**
	 * String representation of directory where all files will be found and saved.
	 */
	private static final String FILES_DIRECTORY = "src/main/resources/";
	
	/**
	 * Called when program is started.
	 * 
	 * @param args Arguments from command prompt.
	 * Used in this example.
	 */
	public static void main(String[] args) {
		if(args.length != 2 && args.length != 3) {
			throw new IllegalArgumentException("Number of arguments must be 2 or 3. Was " + args.length);
		}
		
		String keyword = args[0].toLowerCase().trim();
		String inputFile = args[1];
		String outputFile = null;
		
		if(args.length == 3) {
			outputFile = args[2];
		}else if(!keyword.equals("checksha")) {
			throw new IllegalArgumentException("Expected output file for encription/decription not given.");
		}
		
		if(Files.notExists(Paths.get(FILES_DIRECTORY + inputFile))) {
			throw new IllegalArgumentException("Given input file doesn't exist: " + inputFile);
		}
		
		switch(keyword) {
			case "checksha":
				checksha(inputFile);
				break;
			case "encrypt":
				encryptDecrypt(true, inputFile, outputFile);
				break;
			case "decrypt":
				encryptDecrypt(false, inputFile, outputFile);
				break;
			default:
				throw new IllegalArgumentException(keyword + " is invalid keyword.");
		}
	}
	
	/**
	 * Used to check if file had changed recently using digest that was calculated by given file.
	 * Digest is calculated using SHA-256 algorithm, which means that digest will always be 256-bits long.
	 * If file hasn't changed recently, expected digest will be equal to calculated one.
	 * 
	 * @param inputFile	File used to check if it had changed.
	 */
	private static void checksha(String inputFile) {
		System.out.print("Please provide expected sha-256 digest for " + inputFile + ": \n> ");
		
		Scanner scanner = new Scanner(System.in);
		String expectedDigest = scanner.nextLine();
		scanner.close();
		
		try(InputStream reader = new BufferedInputStream(Files.newInputStream(Paths.get(FILES_DIRECTORY + inputFile)))) {
			MessageDigest msgDigest = MessageDigest.getInstance("SHA-256");
			
			byte[] loaded = new byte[1024];
			int numRead;
			
			while ((numRead = reader.read(loaded)) != -1) {
				msgDigest.update(loaded, 0, numRead);
			}
			
			byte[] hash = msgDigest.digest();
			String generatedDigest = Util.bytetohex(hash);
			
			System.out.print("Digesting completed. ");
			if(generatedDigest.equals(expectedDigest.toLowerCase().trim())) {
				System.out.println("Digest of " + inputFile + " matches expected digest.");
			}else {
				System.out.println("Digest of " + inputFile + " does not match the expected digest.\nDigest was: " + generatedDigest);
			}
		} catch (Exception ex) {
			System.out.println("InputStream/MessageDigest error: " + ex.getMessage());
			System.exit(-1);
		}
	}

	/**
	 * Used to encrypt or decrypt given input based on encrypt variable from
	 * input file into output file,
	 * if output file doesn't exist new will be created.
	 * 
	 * @param encrypt	Used to determine between encryption and decryption.
	 * @param inputFile	File to encrypt/decrypt.
	 * @param outputFile	Where to store encrypted/decrypted file.
	 * @throws IllegalArgumentException if inputFile doesn't exits.
	 */
	private static void encryptDecrypt(boolean encrypt, String inputFile, String outputFile) {
		Cipher cipher = getCipher(encrypt);
		
		try(InputStream reader = new BufferedInputStream(Files.newInputStream(Paths.get(FILES_DIRECTORY + inputFile)));
			OutputStream writer = new BufferedOutputStream(Files.newOutputStream(Paths.get(FILES_DIRECTORY + outputFile)))) {
			byte[] loaded = new byte[1024];
			int numRead;

			while ((numRead = reader.read(loaded)) != -1) {
				byte[] updated = cipher.update(loaded, 0, numRead);
				writer.write(updated);
			}
			writer.write(cipher.doFinal());
			writer.flush();
			
		} catch (Exception ex) {
			System.out.println("InputStream/OutputStream error: " + ex.getMessage());
			System.exit(-1);
		}
		
		System.out.println((encrypt ? "Encryption" : "Decryption") + " completed. "
				+ "Generated file " + outputFile + " based on file " + inputFile + ".");
	}

	/**
	 * Helper method that is used to create and return Cipher object.
	 * Cipher is used to encrypt or decrypt given file.
	 * 
	 * @param encrypt	Used to determine between encryption and decryption.
	 * @return	new Cipher created.
	 */
	private static Cipher getCipher(boolean encrypt) {
		Scanner scanner = new Scanner(System.in);
		String keyText = getKeyText(scanner);
		String ivText = getInitializationVector(scanner);
		scanner.close();
		
		SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(keyText), "AES");
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hextobyte(ivText));
		Cipher cipher = null;
		
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
		}catch(Exception ex) {
			System.out.println("Cipher error: " + ex.getMessage());
			System.exit(-1);
		}
		
		return cipher;
	}
	
	/**
	 * Helper method that returns KeyText got by user input.
	 * Checks whether it is valid key,if not throws exception.
	 * 
	 * @param scanner	Used to read user input.
	 * @return	KeyText as String.
	 * @throws IllegalArgumentException if initializationVector length is different that 32.
	 */
	private static String getKeyText(Scanner scanner) {
		System.out.print("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):\n>  ");
		
		String keyText = scanner.nextLine().trim();
		if(keyText.length() != 32) {
			throw new IllegalArgumentException("Password must contain 32 hex-digits.");
		}
		
		return keyText;
	}
	
	/**
	 * Helper method that returns InitializationVector got by user input.
	 * Checks whether it is valid initialization vector,if not throws exception.
	 * 
	 * @param scanner	Used to read user input.
	 * @return	InitializationVector as String.
	 * @throws IllegalArgumentException if initializationVector length is different that 32.
	 */
	private static String getInitializationVector(Scanner scanner) {
		System.out.print("Please provide initialization vector as hex-encoded text (32 hex-digits):\n>  ");
		
		String initializationVector = scanner.nextLine().trim();
		if(initializationVector.length() != 32) {
			throw new IllegalArgumentException("Initialization vector must contain 32 hex-digits.");
		}
		
		return initializationVector;
	}
}
