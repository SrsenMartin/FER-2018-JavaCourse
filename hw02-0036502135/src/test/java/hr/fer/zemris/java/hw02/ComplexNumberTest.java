package hr.fer.zemris.java.hw02;

import static java.lang.Math.PI;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ComplexNumberTest {

	public static final double THRESHOLD = 1E-4;

	@Test
	public void construstor() {
		ComplexNumber num = new ComplexNumber(-3, 2);

		double expectedReal = -3;
		double actualReal = num.getReal();
		Assert.assertEquals(expectedReal, actualReal, THRESHOLD);

		double expectedImaginary = 2;
		double actualImaginary = num.getImaginary();
		Assert.assertEquals(expectedImaginary, actualImaginary, THRESHOLD);
	}

	@Test
	public void fromReal() {
		ComplexNumber num = ComplexNumber.fromReal(10.2);

		double expectedReal = 10.2;
		double actualReal = num.getReal();
		Assert.assertEquals(expectedReal, actualReal, THRESHOLD);

		double expectedImaginary = 0;
		double actualImaginary = num.getImaginary();
		Assert.assertEquals(expectedImaginary, actualImaginary, THRESHOLD);
	}

	@Test
	public void fromImaginary() {
		ComplexNumber num = ComplexNumber.fromImaginary(-5.55);

		double expectedReal = 0;
		double actualReal = num.getReal();
		Assert.assertEquals(expectedReal, actualReal, THRESHOLD);

		double expectedImaginary = -5.55;
		double actualImaginary = num.getImaginary();
		Assert.assertEquals(expectedImaginary, actualImaginary, THRESHOLD);
	}

	@Test
	public void fromMagnitudeAndAngle() {
		ComplexNumber sampleNumber = new ComplexNumber(3.12, -5.15);
		double magnitude = sampleNumber.getMagnitude();
		double angle = sampleNumber.getAngle();

		ComplexNumber num = ComplexNumber.fromMagnitudeAndAngle(magnitude, angle);

		double expectedReal = sampleNumber.getReal();
		double actualReal = num.getReal();
		Assert.assertEquals(expectedReal, actualReal, THRESHOLD);

		double expectedImaginary = sampleNumber.getImaginary();
		double actualImaginary = num.getImaginary();
		Assert.assertEquals(expectedImaginary, actualImaginary, THRESHOLD);
	}

	@Test
	public void parse() {
		ComplexNumber num = ComplexNumber.parse("5.1+2.2i");

		double expectedReal = 5.1;
		double actualReal = num.getReal();
		Assert.assertEquals(expectedReal, actualReal, THRESHOLD);

		double expectedImaginary = 2.2;
		double actualImaginary = num.getImaginary();
		Assert.assertEquals(expectedImaginary, actualImaginary, THRESHOLD);

		num = ComplexNumber.parse("6.8-8.2i");

		double expectedReal2 = 6.8;
		double actualReal2 = num.getReal();
		Assert.assertEquals(expectedReal2, actualReal2, THRESHOLD);

		double expectedImaginary2 = -8.2;
		double actualImaginary2 = num.getImaginary();
		Assert.assertEquals(expectedImaginary2, actualImaginary2, THRESHOLD);

		num = ComplexNumber.parse("-6.8+8.2i");

		double expectedReal3 = -6.8;
		double actualReal3 = num.getReal();
		Assert.assertEquals(expectedReal3, actualReal3, THRESHOLD);

		double expectedImaginary3 = 8.2;
		double actualImaginary3 = num.getImaginary();
		Assert.assertEquals(expectedImaginary3, actualImaginary3, THRESHOLD);

		num = ComplexNumber.parse("-6.8-8.2i");

		double expectedReal4 = -6.8;
		double actualReal4 = num.getReal();
		Assert.assertEquals(expectedReal4, actualReal4, THRESHOLD);

		double expectedImaginary4 = -8.2;
		double actualImaginary4 = num.getImaginary();
		Assert.assertEquals(expectedImaginary4, actualImaginary4, THRESHOLD);
	}

	@Test(expected = IllegalArgumentException.class)
	public void parseNull() {
		ComplexNumber.parse(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void parseIllegal() {
		ComplexNumber.parse("3k+3i");
	}

	@Test
	public void getMagnitude() {
		ComplexNumber num = new ComplexNumber(-4, -3);
		double expected = 5;
		double actual = num.getMagnitude();

		Assert.assertEquals(expected, actual, THRESHOLD);

		num = new ComplexNumber(-1, 0);
		double expected2 = 1;
		double actual2 = num.getMagnitude();

		Assert.assertEquals(expected2, actual2, THRESHOLD);

		num = new ComplexNumber(0, 1);
		double expected3 = 1;
		double actual3 = num.getMagnitude();

		Assert.assertEquals(expected3, actual3, THRESHOLD);
	}

	@Test
	public void getAngle() {
		ComplexNumber num = new ComplexNumber(3, -4);
		double expected = 1.704832 * PI;
		double actual = num.getAngle();

		Assert.assertEquals(expected, actual, THRESHOLD);

		num = new ComplexNumber(1, 0);
		double expected2 = 0;
		double actual2 = num.getAngle();

		Assert.assertEquals(expected2, actual2, THRESHOLD);

		num = new ComplexNumber(-1, 0);
		double expected3 = PI;
		double actual3 = num.getAngle();

		Assert.assertEquals(expected3, actual3, THRESHOLD);

		num = new ComplexNumber(0, -1);
		double expected4 = 3. / 2. * PI;
		double actual4 = num.getAngle();

		Assert.assertEquals(expected4, actual4, THRESHOLD);
	}

	private ComplexNumber num1;
	private ComplexNumber num2;

	@Before
	public void init() {
		num1 = new ComplexNumber(3.1, -4);
		num2 = new ComplexNumber(-2.12, 6);
	}

	@Test
	public void add() {
		ComplexNumber added = num1.add(num2);

		double expectedReal = num1.getReal() + num2.getReal();
		double actualReal = added.getReal();

		Assert.assertEquals(expectedReal, actualReal, THRESHOLD);

		double expectedImaginary = num1.getImaginary() + num2.getImaginary();
		double actualImaginary = added.getImaginary();

		Assert.assertEquals(expectedImaginary, actualImaginary, THRESHOLD);
	}

	@Test
	public void sub() {
		ComplexNumber subed = num1.sub(num2);

		double expectedReal = num1.getReal() - num2.getReal();
		double actualReal = subed.getReal();

		Assert.assertEquals(expectedReal, actualReal, THRESHOLD);

		double expectedImaginary = num1.getImaginary() - num2.getImaginary();
		double actualImaginary = subed.getImaginary();

		Assert.assertEquals(expectedImaginary, actualImaginary, THRESHOLD);
	}

	@Test
	public void mul() {
		ComplexNumber multiplied = num1.mul(num2);

		double expectedReal = 17.428;
		double actualReal = multiplied.getReal();

		Assert.assertEquals(expectedReal, actualReal, THRESHOLD);

		double expectedImaginary = 27.08;
		double actualImaginary = multiplied.getImaginary();

		Assert.assertEquals(expectedImaginary, actualImaginary, THRESHOLD);
	}

	@Test
	public void div() {
		ComplexNumber divided = num1.div(num2);

		double expectedReal = -0.75497;
		double actualReal = divided.getReal();

		Assert.assertEquals(expectedReal, actualReal, THRESHOLD);

		double expectedImaginary = -0.2499;
		double actualImaginary = divided.getImaginary();

		Assert.assertEquals(expectedImaginary, actualImaginary, THRESHOLD);
	}

	@Test(expected = IllegalArgumentException.class)
	public void divWith0() {
		ComplexNumber number = new ComplexNumber(0, 0);

		num1.div(number);
	}

	@Test
	public void power() {
		ComplexNumber powered = num1.power(6);

		double expectedReal = 11529.3997;
		double actualReal = powered.getReal();

		Assert.assertEquals(expectedReal, actualReal, THRESHOLD);

		double expectedImaginary = 12215.0838;
		double actualImaginary = powered.getImaginary();

		Assert.assertEquals(expectedImaginary, actualImaginary, THRESHOLD);
	}

	@Test
	public void power0() {
		ComplexNumber powered = num1.power(0);

		double expectedReal = 1;
		double actualReal = powered.getReal();

		Assert.assertEquals(expectedReal, actualReal, THRESHOLD);

		double expectedImaginary = 0;
		double actualImaginary = powered.getImaginary();

		Assert.assertEquals(expectedImaginary, actualImaginary, THRESHOLD);
	}

	@Test(expected = IllegalArgumentException.class)
	public void powerIllegal() {
		num1.power(-1);
	}

	@Test
	public void root() {
		ComplexNumber[] roots = num1.root(3);

		// root 1
		double expectedReal1 = -0.374284;
		double actualReal1 = roots[0].getReal();

		Assert.assertEquals(expectedReal1, actualReal1, THRESHOLD);

		double expectedImaginary1 = 1.67557;
		double actualImaginary1 = roots[0].getImaginary();

		Assert.assertEquals(expectedImaginary1, actualImaginary1, THRESHOLD);

		// root2
		double expectedReal2 = -1.26394;
		double actualReal2 = roots[1].getReal();

		Assert.assertEquals(expectedReal2, actualReal2, THRESHOLD);

		double expectedImaginary2 = -1.16192;
		double actualImaginary2 = roots[1].getImaginary();

		Assert.assertEquals(expectedImaginary2, actualImaginary2, THRESHOLD);

		// root3
		double expectedReal3 = 1.63822;
		double actualReal3 = roots[2].getReal();

		Assert.assertEquals(expectedReal3, actualReal3, THRESHOLD);

		double expectedImaginary3 = -0.513643;
		double actualImaginary3 = roots[2].getImaginary();

		Assert.assertEquals(expectedImaginary3, actualImaginary3, THRESHOLD);
	}

	@Test(expected = IllegalArgumentException.class)
	public void rootIllegal() {
		num1.root(0);
	}

}
