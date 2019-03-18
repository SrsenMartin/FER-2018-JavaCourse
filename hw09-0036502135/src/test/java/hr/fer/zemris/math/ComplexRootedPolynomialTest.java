package hr.fer.zemris.math;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ComplexRootedPolynomialTest {
	
	private ComplexRootedPolynomial pol;
	
	@Before
	public void init() {
		pol = new ComplexRootedPolynomial(new Complex(2, 3), new Complex(1, -2), new Complex(1, 1));
	}
	
	@Test (expected = NullPointerException.class)
	public void rootsNullTest() {
		new ComplexRootedPolynomial((Complex[]) null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void roots0Test() {
		new ComplexRootedPolynomial();
	}
	
	@Test (expected = NullPointerException.class)
	public void applyNullTest() {
		pol.apply(null);
	}
	
	@Test
	public void applyTest() {
		Complex expected = new Complex(-10, 30);
		Complex actual = pol.apply(new Complex(-1, 2));
		
		Assert.assertEquals(expected.getReal(), actual.getReal(), 1E-4);
		Assert.assertEquals(expected.getImaginary(), actual.getImaginary(), 1E-4);
	}
	
	@Test
	public void toComplexPolynomTest() {
		String expected = new ComplexPolynomial(new Complex(-9, -7), new Complex(10, 3), new Complex(-4, -2), new Complex(1, 0)).toString();
		String actual = pol.toComplexPolynom().toString();
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test (expected = NullPointerException.class)
	public void indexOfClosestRootForNullTest() {
		pol.indexOfClosestRootFor(null, 0.);
	}
	
	@Test
	public void indexOfClosestRootForTest() {
		int expectedIndex = 2;
		int actualIndex = pol.indexOfClosestRootFor(new Complex(1, -1), 1.);
		
		Assert.assertEquals(expectedIndex, actualIndex);
		
		expectedIndex = -1;
		actualIndex = pol.indexOfClosestRootFor(new Complex(1.01, -1), 1.);
		
		Assert.assertEquals(expectedIndex, actualIndex);
	}
}
