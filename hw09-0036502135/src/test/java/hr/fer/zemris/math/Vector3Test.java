package hr.fer.zemris.math;

import org.junit.Assert;
import org.junit.Test;

public class Vector3Test {

	private static final double TRESHOLD = 1E-4;
	
	@Test
	public void makeNormalVector() {
		Vector3 vector = new Vector3(10.2, -2.1, 3.3);
		
		double expectedX = 10.2;
		double expectedY = -2.1;
		double expectedZ = 3.3;
		double actualX = vector.getX();
		double actualY = vector.getY();
		double actualZ = vector.getZ();
		
		Assert.assertEquals(expectedX, actualX, TRESHOLD);
		Assert.assertEquals(expectedY, actualY, TRESHOLD);
		Assert.assertEquals(expectedZ, actualZ, TRESHOLD);
	}
	
	@Test
	public void normTest() {
		Vector3 vector = new Vector3(3, 2, -1);
		
		double expected = 3.74165738;
		double actual = vector.norm();
		
		Assert.assertEquals(expected, actual, TRESHOLD);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void normalizedTestNullVector() {
		Vector3 vector = new Vector3(0, 0, 0);
		vector.normalized();
	}
	
	@Test
	public void normalizedTest() {
		Vector3 vector = new Vector3(2, -2, 1);
		Vector3 normalized = vector.normalized();
		
		double expectedX = 2./3;
		double expectedY = -2./3;
		double expectedZ = 1./3;
		double actualX = normalized.getX();
		double actualY = normalized.getY();
		double actualZ = normalized.getZ();
		
		Assert.assertEquals(expectedX, actualX, TRESHOLD);
		Assert.assertEquals(expectedY, actualY, TRESHOLD);
		Assert.assertEquals(expectedZ, actualZ, TRESHOLD);
		
		double originalX = 2;
		double originalY = -2;
		double originalZ = 1;
		double actualOriginalX = vector.getX();
		double actualOriginalY = vector.getY();
		double actualOriginalZ = vector.getZ();
		
		Assert.assertEquals(originalX, actualOriginalX, TRESHOLD);
		Assert.assertEquals(originalY, actualOriginalY, TRESHOLD);
		Assert.assertEquals(originalZ, actualOriginalZ, TRESHOLD);
	}
	
	@Test (expected = NullPointerException.class)
	public void addNull() {
		new Vector3(1, 2, 3).add(null);
	}
	
	@Test
	public void addTest() {
		Vector3 vector = new Vector3(2, -2, 1);
		Vector3 added = vector.add(new Vector3(1, 2, 3));
		
		double expectedX = 3;
		double expectedY = 0;
		double expectedZ = 4;
		double actualX = added.getX();
		double actualY = added.getY();
		double actualZ = added.getZ();
		
		Assert.assertEquals(expectedX, actualX, TRESHOLD);
		Assert.assertEquals(expectedY, actualY, TRESHOLD);
		Assert.assertEquals(expectedZ, actualZ, TRESHOLD);
		
		double originalX = 2;
		double originalY = -2;
		double originalZ = 1;
		double actualOriginalX = vector.getX();
		double actualOriginalY = vector.getY();
		double actualOriginalZ = vector.getZ();
		
		Assert.assertEquals(originalX, actualOriginalX, TRESHOLD);
		Assert.assertEquals(originalY, actualOriginalY, TRESHOLD);
		Assert.assertEquals(originalZ, actualOriginalZ, TRESHOLD);
	}
	
	@Test (expected = NullPointerException.class)
	public void subNull() {
		new Vector3(1, 2, 3).sub(null);
	}
	
	@Test
	public void subTest() {
		Vector3 vector = new Vector3(2, -2, 1);
		Vector3 subbed = vector.sub(new Vector3(1, 2, 3));
		
		double expectedX = 1;
		double expectedY = -4;
		double expectedZ = -2;
		double actualX = subbed.getX();
		double actualY = subbed.getY();
		double actualZ = subbed.getZ();
		
		Assert.assertEquals(expectedX, actualX, TRESHOLD);
		Assert.assertEquals(expectedY, actualY, TRESHOLD);
		Assert.assertEquals(expectedZ, actualZ, TRESHOLD);
		
		double originalX = 2;
		double originalY = -2;
		double originalZ = 1;
		double actualOriginalX = vector.getX();
		double actualOriginalY = vector.getY();
		double actualOriginalZ = vector.getZ();
		
		Assert.assertEquals(originalX, actualOriginalX, TRESHOLD);
		Assert.assertEquals(originalY, actualOriginalY, TRESHOLD);
		Assert.assertEquals(originalZ, actualOriginalZ, TRESHOLD);
	}
	
	@Test (expected = NullPointerException.class)
	public void dotNull() {
		new Vector3(1, 2, 3).dot(null);
	}
	
	@Test
	public void dotTest() {
		Vector3 vector = new Vector3(2, -2, 1);
		
		double expected = 1;
		double actual = vector.dot(new Vector3(1, 2, 3));
		
		Assert.assertEquals(expected, actual, TRESHOLD);
	}
	
	@Test (expected = NullPointerException.class)
	public void crossNull() {
		new Vector3(1, 2, 3).cross(null);
	}
	
	@Test
	public void crossTest() {
		Vector3 vector = new Vector3(2, -2, 1);
		Vector3 cross = vector.cross(new Vector3(1, 2, 3));
		
		double expectedX = -8;
		double expectedY = -5;
		double expectedZ = 6;
		double actualX = cross.getX();
		double actualY = cross.getY();
		double actualZ = cross.getZ();
		
		Assert.assertEquals(expectedX, actualX, TRESHOLD);
		Assert.assertEquals(expectedY, actualY, TRESHOLD);
		Assert.assertEquals(expectedZ, actualZ, TRESHOLD);
		
		double originalX = 2;
		double originalY = -2;
		double originalZ = 1;
		double actualOriginalX = vector.getX();
		double actualOriginalY = vector.getY();
		double actualOriginalZ = vector.getZ();
		
		Assert.assertEquals(originalX, actualOriginalX, TRESHOLD);
		Assert.assertEquals(originalY, actualOriginalY, TRESHOLD);
		Assert.assertEquals(originalZ, actualOriginalZ, TRESHOLD);
	}
	
	@Test
	public void scaleTest() {
		Vector3 vector = new Vector3(2, -2, 1);
		Vector3 scaled = vector.scale(-3);
				
		double expectedX = -6;
		double expectedY = 6;
		double expectedZ = -3;
		double actualX = scaled.getX();
		double actualY = scaled.getY();
		double actualZ = scaled.getZ();
		
		Assert.assertEquals(expectedX, actualX, TRESHOLD);
		Assert.assertEquals(expectedY, actualY, TRESHOLD);
		Assert.assertEquals(expectedZ, actualZ, TRESHOLD);
		
		double originalX = 2;
		double originalY = -2;
		double originalZ = 1;
		double actualOriginalX = vector.getX();
		double actualOriginalY = vector.getY();
		double actualOriginalZ = vector.getZ();
		
		Assert.assertEquals(originalX, actualOriginalX, TRESHOLD);
		Assert.assertEquals(originalY, actualOriginalY, TRESHOLD);
		Assert.assertEquals(originalZ, actualOriginalZ, TRESHOLD);
	}
	
	@Test (expected = NullPointerException.class)
	public void cosAngleNull() {
		new Vector3(1, 1, 1).cosAngle(null);
	}
	
	@Test
	public void cosAngleTest() {
		Vector3 vector = new Vector3(2, -2, 1);
		Vector3 vector2 = new Vector3(1, 2, -3);
		
		double expected = -0.4454354;
		double actual = vector.cosAngle(vector2);
		
		Assert.assertEquals(expected, actual, TRESHOLD);
		Assert.assertEquals(vector2.cosAngle(vector), actual, TRESHOLD);
		Assert.assertEquals(1, vector.cosAngle(vector), TRESHOLD);
	}
	
	@Test
	public void toArrayTest() {
		Vector3 vector = new Vector3(2, -2, 1);
		double[] vArray = vector.toArray();
		
		Assert.assertEquals(3, vArray.length);
		Assert.assertEquals(vector.getX(), vArray[0], TRESHOLD);
		Assert.assertEquals(vector.getY(), vArray[1], TRESHOLD);
		Assert.assertEquals(vector.getZ(), vArray[2], TRESHOLD);
	}
}
