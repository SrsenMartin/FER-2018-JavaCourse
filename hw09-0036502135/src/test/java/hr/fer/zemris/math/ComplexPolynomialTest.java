package hr.fer.zemris.math;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class ComplexPolynomialTest {

	private ComplexPolynomial pol;
	
	@Before
	public void init() {
		pol = new ComplexPolynomial(new Complex(1, 0), new Complex(5, 0), new Complex(2, 0), new Complex(7, 2));
	}
	
	@Test (expected = NullPointerException.class)
	public void factorsNullTest() {
		new ComplexPolynomial((Complex[]) null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void factors0Test() {
		new ComplexPolynomial();
	}
	
	@Test
	public void orderTest() {
		short expected = 3;
		short actual = pol.order();
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test (expected = NullPointerException.class)
	public void multiplyNull() {
		pol.multiply(null);
	}
	
	@Test
	public void multiplyTest() {
		String expected = new ComplexPolynomial(
				Complex.IM, new Complex(1, 3), new Complex(5, -8), new Complex(0, 3), new Complex(11, -12)).toString();
		String actual = pol.multiply(new ComplexPolynomial(new Complex(0, 1), new Complex(1, -2))).toString();
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void deriveTest() {
		String expected = new ComplexPolynomial(new Complex(5, 0), new Complex(4, 0), new Complex(21, 6)).toString();
		String actual = pol.derive().toString();
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test (expected = NullPointerException.class)
	public void applyNullTest() {
		pol.apply(null);
	}
	
	public void applyTest() {
		Complex expected = new Complex(71, 10);
		Complex actual = pol.apply(new Complex(-1, 2));
		
		Assert.assertEquals(expected.getReal(), actual.getReal(), 1E-4);
		Assert.assertEquals(expected.getImaginary(), actual.getImaginary(), 1E-4);
	}
}
