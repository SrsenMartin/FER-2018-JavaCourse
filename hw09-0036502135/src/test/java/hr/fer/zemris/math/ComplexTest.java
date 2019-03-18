package hr.fer.zemris.math;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ComplexTest {

	private static final double TRESHOLD = 1E-4;
	
	@Test
	public void construstorTest() {
		Complex num = new Complex(-3, 2);

		double expectedReal = -3;
		double actualReal = num.getReal();
		Assert.assertEquals(expectedReal, actualReal, TRESHOLD);

		double expectedImaginary = 2;
		double actualImaginary = num.getImaginary();
		Assert.assertEquals(expectedImaginary, actualImaginary, TRESHOLD);
	}
	
	@Test
	public void moduleTest() {
		Complex num = new Complex(-4, -3);
		double expected = 5;
		double actual = num.module();

		Assert.assertEquals(expected, actual, TRESHOLD);

		num = new Complex(-1, 0);
		double expected2 = 1;
		double actual2 = num.module();

		Assert.assertEquals(expected2, actual2, TRESHOLD);

		num = new Complex(0, 1);
		double expected3 = 1;
		double actual3 = num.module();

		Assert.assertEquals(expected3, actual3, TRESHOLD);
	}
	
	@Test (expected = NullPointerException.class)
	public void multiplyNullTest() {
		new Complex(3.1, -4).multiply(null);
	}
	
	@Test
	public void multiplyTest() {
		Complex num1 = new Complex(3.1, -4);
		Complex num2 = new Complex(-2.12, 6);
		
		Complex multiplied = num1.multiply(num2);

		double expectedReal = 17.428;
		double actualReal = multiplied.getReal();

		Assert.assertEquals(expectedReal, actualReal, TRESHOLD);

		double expectedImaginary = 27.08;
		double actualImaginary = multiplied.getImaginary();

		Assert.assertEquals(expectedImaginary, actualImaginary, TRESHOLD);
	}
	
	@Test (expected = NullPointerException.class)
	public void divideNullTest() {
		new Complex(3.1, -4).divide(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void divWith0Test() {
		Complex num1 = new Complex(3.1, -4);
		Complex number = new Complex(0, 0);

		num1.divide(number);
	}
	
	@Test
	public void divideTest() {
		Complex num1 = new Complex(3.1, -4);
		Complex num2 = new Complex(-2.12, 6);
		
		Complex divided = num1.divide(num2);

		double expectedReal = -0.75497;
		double actualReal = divided.getReal();

		Assert.assertEquals(expectedReal, actualReal, TRESHOLD);

		double expectedImaginary = -0.2499;
		double actualImaginary = divided.getImaginary();

		Assert.assertEquals(expectedImaginary, actualImaginary, TRESHOLD);
	}
	
	@Test (expected = NullPointerException.class)
	public void addNullTest() {
		new Complex(3.1, -4).add(null);
	}
	
	@Test
	public void addTest() {
		Complex num1 = new Complex(3.1, -4);
		Complex num2 = new Complex(-2.12, 6);
		
		Complex added = num1.add(num2);

		double expectedReal = num1.getReal() + num2.getReal();
		double actualReal = added.getReal();

		Assert.assertEquals(expectedReal, actualReal, TRESHOLD);

		double expectedImaginary = num1.getImaginary() + num2.getImaginary();
		double actualImaginary = added.getImaginary();

		Assert.assertEquals(expectedImaginary, actualImaginary, TRESHOLD);
	}

	@Test (expected = NullPointerException.class)
	public void subNullTest() {
		new Complex(3.1, -4).sub(null);
	}
	
	@Test
	public void subTest() {
		Complex num1 = new Complex(3.1, -4);
		Complex num2 = new Complex(-2.12, 6);
		
		Complex subed = num1.sub(num2);

		double expectedReal = num1.getReal() - num2.getReal();
		double actualReal = subed.getReal();

		Assert.assertEquals(expectedReal, actualReal, TRESHOLD);

		double expectedImaginary = num1.getImaginary() - num2.getImaginary();
		double actualImaginary = subed.getImaginary();

		Assert.assertEquals(expectedImaginary, actualImaginary, TRESHOLD);
	}
	
	@Test
	public void negateTest() {
		Complex num1 = new Complex(3.1, -4);
		
		Complex negated = num1.negate();

		double expectedReal = -3.1;
		double actualReal = negated.getReal();

		Assert.assertEquals(expectedReal, actualReal, TRESHOLD);

		double expectedImaginary = 4;
		double actualImaginary = negated.getImaginary();

		Assert.assertEquals(expectedImaginary, actualImaginary, TRESHOLD);
	}
	
	@Test
	public void powerTest() {
		Complex num1 = new Complex(3.1, -4);
		
		Complex powered = num1.power(6);

		double expectedReal = 11529.3997;
		double actualReal = powered.getReal();

		Assert.assertEquals(expectedReal, actualReal, TRESHOLD);

		double expectedImaginary = 12215.0838;
		double actualImaginary = powered.getImaginary();

		Assert.assertEquals(expectedImaginary, actualImaginary, TRESHOLD);
	}

	@Test
	public void power0Test() {
		Complex num1 = new Complex(3.1, -4);
		
		Complex powered = num1.power(0);

		double expectedReal = 1;
		double actualReal = powered.getReal();

		Assert.assertEquals(expectedReal, actualReal, TRESHOLD);

		double expectedImaginary = 0;
		double actualImaginary = powered.getImaginary();

		Assert.assertEquals(expectedImaginary, actualImaginary, TRESHOLD);
	}

	@Test(expected = IllegalArgumentException.class)
	public void powerNegativeTest() {
		new Complex(1, 2).power(-1);
	}
	
	@Test
	public void root() {
		Complex num1 = new Complex(3.1, -4);
		
		List<Complex> roots = num1.root(3);

		Assert.assertEquals(3, roots.size());
		
		// root1
		double expectedReal3 = 1.63822;
		double actualReal3 = roots.get(0).getReal();

		Assert.assertEquals(expectedReal3, actualReal3, TRESHOLD);

		double expectedImaginary3 = -0.513643;
		double actualImaginary3 = roots.get(0).getImaginary();

		Assert.assertEquals(expectedImaginary3, actualImaginary3, TRESHOLD);
		
		// root 2
		double expectedReal1 = -0.374284;
		double actualReal1 = roots.get(1).getReal();

		Assert.assertEquals(expectedReal1, actualReal1, TRESHOLD);

		double expectedImaginary1 = 1.67557;
		double actualImaginary1 = roots.get(1).getImaginary();

		Assert.assertEquals(expectedImaginary1, actualImaginary1, TRESHOLD);
		
		// root 3
		double expectedReal2 = -1.26394;
		double actualReal2 = roots.get(2).getReal();

		Assert.assertEquals(expectedReal2, actualReal2, TRESHOLD);

		double expectedImaginary2 = -1.16192;
		double actualImaginary2 = roots.get(2).getImaginary();

		Assert.assertEquals(expectedImaginary2, actualImaginary2, TRESHOLD);
	}

	@Test(expected = IllegalArgumentException.class)
	public void root0Test() {
		new Complex(1, 2).root(0);
	}
}
