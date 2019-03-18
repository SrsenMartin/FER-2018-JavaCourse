package hr.fer.zemris.java.hw07.crypto;

import org.junit.Assert;
import org.junit.Test;

import hr.fer.zemris.java.hw07.crypto.Util;

public class UtilTest {

	@Test
	public void hextobyteGivenExample() {
		String input = "01aE22";
		
		byte[] expected = new byte[] {1, -82, 34};
		byte[] actual = Util.hextobyte(input);
		
		Assert.assertEquals(expected.length, actual.length);
		for(int i = 0; i < actual.length; i++) {
			Assert.assertEquals(expected[i], actual[i]);
		}
	}
	
	@Test
	public void bytetohexGivenExample() {
		byte[] input = new byte[] {1, -82, 34};
		
		String actual = Util.bytetohex(input);
		String expected = "01ae22";
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void hextobyteOddSized() {
		String input = "01aE223";
		Util.hextobyte(input);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void hextobyteInvalidCharacter() {
		String input = "01aE22FG";
		Util.hextobyte(input);
	}
	
	@Test
	public void hextobyteHigestHex() {
		String input = "FF";
		
		Assert.assertEquals(-1, Util.hextobyte(input)[0]);
	}
	
	@Test
	public void hextobyteLowestHex() {
		String input = "00";
		
		Assert.assertEquals(0, Util.hextobyte(input)[0]);
	}
	
	@Test
	public void hextobyteHigestByteFromHex() {
		String input = "7F";
		
		Assert.assertEquals(127, Util.hextobyte(input)[0]);
	}
	
	@Test
	public void hextobyteLowestByteFromHex() {
		String input = "80";
		
		Assert.assertEquals(-128, Util.hextobyte(input)[0]);
	}
	
	@Test
	public void bytetohexHighestByte() {
		byte[] input = new byte[] {127};
		
		Assert.assertEquals("7f", Util.bytetohex(input));
	}
	
	@Test
	public void bytetohexLowestByte() {
		byte[] input = new byte[] {-128};
		
		Assert.assertEquals("80", Util.bytetohex(input));
	}
	
	@Test
	public void bytetohexHighestHexFromByte() {
		byte[] input = new byte[] {-1};
		
		Assert.assertEquals("ff", Util.bytetohex(input));
	}
	
	@Test
	public void bytetohexLowestHexFromByte() {
		byte[] input = new byte[] {0};
		
		Assert.assertEquals("00", Util.bytetohex(input));
	}
}
