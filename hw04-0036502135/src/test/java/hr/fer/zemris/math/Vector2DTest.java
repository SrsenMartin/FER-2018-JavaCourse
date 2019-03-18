package hr.fer.zemris.math;

import org.junit.Test;
import org.junit.Assert;

public class Vector2DTest {

	@Test
	public void makeNormalVector() {
		Vector2D vector = new Vector2D(10.2, -2.1);
		
		double expectedX = 10.2;
		double expectedY = -2.1;
		double actualX = vector.getX();
		double actualY = vector.getY();
		
		Assert.assertEquals(expectedX, actualX, 1E-4);
		Assert.assertEquals(expectedY, actualY, 1E-4);
	}
	
	@Test
	public void translateVector() {
		Vector2D vector = new Vector2D(10.2, -2.1);
		Vector2D translateBy = new Vector2D(-5, -2);
		
		vector.translate(translateBy);
		
		double expectedX = 5.2;
		double expectedY = -4.1;
		double actualX = vector.getX();
		double actualY = vector.getY();
		
		Assert.assertEquals(expectedX, actualX, 1E-4);
		Assert.assertEquals(expectedY, actualY, 1E-4);
	}
	
	@Test
	public void translatedVector() {
		Vector2D vector = new Vector2D(10.2, -2.1);
		Vector2D translateBy = new Vector2D(-5, -2);
		
		Vector2D translated = vector.translated(translateBy);
		
		double expectedX = 10.2;
		double expectedY = -2.1;
		double actualX = vector.getX();
		double actualY = vector.getY();
		
		Assert.assertEquals(expectedX, actualX, 1E-4);
		Assert.assertEquals(expectedY, actualY, 1E-4);
		
		double translatedX = 5.2;
		double translatedY = -4.1;
		double actualTransX = translated.getX();
		double actualTransY = translated.getY();
		
		Assert.assertEquals(translatedX, actualTransX, 1E-4);
		Assert.assertEquals(translatedY, actualTransY, 1E-4);
	}
	
	@Test
	public void rotateVector() {
		Vector2D vector = new Vector2D(6.2, -2.1);
		
		vector.rotate(222);
		
		double expectedX = -6.01267;
		double expectedY = -2.588;
		double actualX = vector.getX();
		double actualY = vector.getY();
		
		Assert.assertEquals(expectedX, actualX, 1E-4);
		Assert.assertEquals(expectedY, actualY, 1E-4);
		
		vector.rotate(-30);
		
		double expectedX2 = -6.501125;
		double expectedY2 = 0.765061;
		double actualX2 = vector.getX();
		double actualY2 = vector.getY();
		
		Assert.assertEquals(expectedX2, actualX2, 1E-4);
		Assert.assertEquals(expectedY2, actualY2, 1E-4);
	}
	
	@Test
	public void rotatedVector() {
		Vector2D vector = new Vector2D(6.2, -2.1);
		
		Vector2D rotated = vector.rotated(222);
		
		double expectedX = -6.01267;
		double expectedY = -2.588;
		double actualX = rotated.getX();
		double actualY = rotated.getY();
		
		Assert.assertEquals(expectedX, actualX, 1E-4);
		Assert.assertEquals(expectedY, actualY, 1E-4);
		
		double originalExpectedX = 6.2;
		double originalExpectedY = -2.1;
		double originalActualX = vector.getX();
		double originalActualY = vector.getY();
		
		Assert.assertEquals(originalExpectedX, originalActualX, 1E-4);
		Assert.assertEquals(originalExpectedY, originalActualY, 1E-4);
	}
	
	@Test
	public void scaleVector() {
		Vector2D vector = new Vector2D(10.2, -2.1);
		
		vector.scale(5);
		
		double expectedX = 10.2 * 5;
		double expectedY = -2.1 * 5;
		double actualX = vector.getX();
		double actualY = vector.getY();
		
		Assert.assertEquals(expectedX, actualX, 1E-4);
		Assert.assertEquals(expectedY, actualY, 1E-4);
	}
	
	@Test
	public void scaledVector() {
		Vector2D vector = new Vector2D(10.2, -2.1);
		
		Vector2D translated = vector.scaled(5);
		
		double expectedX = 10.2;
		double expectedY = -2.1;
		double actualX = vector.getX();
		double actualY = vector.getY();
		
		Assert.assertEquals(expectedX, actualX, 1E-4);
		Assert.assertEquals(expectedY, actualY, 1E-4);
		
		double translatedX = 10.2 * 5;
		double translatedY = -2.1 * 5;
		double actualTransX = translated.getX();
		double actualTransY = translated.getY();
		
		Assert.assertEquals(translatedX, actualTransX, 1E-4);
		Assert.assertEquals(translatedY, actualTransY, 1E-4);
	}
	
	@Test
	public void copyVector() {
		Vector2D vector = new Vector2D(10.2, -2.1);
		
		Vector2D copied = vector.copy();
		
		copied.scale(2);
		
		double expectedX = 10.2;
		double expectedY = -2.1;
		double actualX = vector.getX();
		double actualY = vector.getY();
		
		Assert.assertEquals(expectedX, actualX, 1E-4);
		Assert.assertEquals(expectedY, actualY, 1E-4);
		
		double copiedX = 10.2 * 2;
		double copiedY = -2.1 * 2;
		double actualCopiedX = copied.getX();
		double actualCopiedY = copied.getY();
		
		Assert.assertEquals(copiedX, actualCopiedX, 1E-4);
		Assert.assertEquals(copiedY, actualCopiedY, 1E-4);
	}
}
