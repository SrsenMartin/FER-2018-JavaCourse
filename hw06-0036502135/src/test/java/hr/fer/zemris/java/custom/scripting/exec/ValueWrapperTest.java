package hr.fer.zemris.java.custom.scripting.exec;

import org.junit.Test;

import org.junit.Assert;

public class ValueWrapperTest {

	//setValue radi identicno kao konstruktor.
	@Test
	public void testValidConstructors() {
		new ValueWrapper(null);
		new ValueWrapper(Integer.valueOf(10));
		new ValueWrapper(Double.valueOf(1E-4));
		new ValueWrapper("1.2E1");
		new ValueWrapper(Long.valueOf(12));
		new ValueWrapper(new StringBuilder());
	}
	
	@Test
	public void validAdds() {
		ValueWrapper intWrapper = new ValueWrapper(Integer.valueOf(10));
		ValueWrapper doubleWrapper = new ValueWrapper(Double.valueOf(1.3E1));
		ValueWrapper stringWrapper  = new ValueWrapper("3.14");
		ValueWrapper nullWrapper = new ValueWrapper(null);
		
		intWrapper.add(Integer.valueOf(12));
		Assert.assertEquals(intWrapper.getValue(), Integer.valueOf(22));
		intWrapper.add(null);
		Assert.assertEquals(intWrapper.getValue(), Integer.valueOf(22));
		intWrapper.add(Double.valueOf(1.3E1));
		Assert.assertEquals(intWrapper.getValue(), Double.valueOf(35));
		intWrapper.add("3.14");
		Assert.assertEquals(intWrapper.getValue(), Double.valueOf(38.14));
		
		doubleWrapper.add(Integer.valueOf(9));
		Assert.assertEquals(doubleWrapper.getValue(), Double.valueOf(22));
		doubleWrapper.add(null);
		Assert.assertEquals(doubleWrapper.getValue(), Double.valueOf(22));
		doubleWrapper.add(Double.valueOf(1.3E1));
		Assert.assertEquals(doubleWrapper.getValue(), Double.valueOf(35));
		doubleWrapper.add("3.14");
		Assert.assertEquals(doubleWrapper.getValue(), Double.valueOf(38.14));
		
		stringWrapper.add(Integer.valueOf(12));
		Assert.assertEquals(stringWrapper.getValue(), Double.valueOf(15.14));
		stringWrapper.add(null);
		Assert.assertEquals(stringWrapper.getValue(), Double.valueOf(15.14));
		stringWrapper.add(Double.valueOf(1.3E1));
		Assert.assertEquals(stringWrapper.getValue(), Double.valueOf(28.14));
		stringWrapper.add("3.14");
		Assert.assertEquals(stringWrapper.getValue(), Double.valueOf(31.28));
		
		nullWrapper.add(Integer.valueOf(22));
		Assert.assertEquals(nullWrapper.getValue(), Integer.valueOf(22));
		nullWrapper.add(null);
		Assert.assertEquals(nullWrapper.getValue(), Integer.valueOf(22));
		nullWrapper.add(Double.valueOf(1.3E1));
		Assert.assertEquals(nullWrapper.getValue(), Double.valueOf(35));
		nullWrapper.add("3.14");
		Assert.assertEquals(nullWrapper.getValue(), Double.valueOf(38.14));
	}
	
	@Test (expected = RuntimeException.class)
	public void invalidAdd() {
		ValueWrapper intWrapper = new ValueWrapper(Integer.valueOf(10));
		
		intWrapper.add(Long.valueOf(12));
	}
	
	@Test
	public void validSubstract() {
		ValueWrapper intWrapper = new ValueWrapper(Integer.valueOf(10));
		ValueWrapper doubleWrapper = new ValueWrapper(Double.valueOf(1.3E1));
		ValueWrapper stringWrapper  = new ValueWrapper("3.14");
		ValueWrapper nullWrapper = new ValueWrapper(null);
		
		intWrapper.subtract(Integer.valueOf(12));
		Assert.assertEquals(intWrapper.getValue(), Integer.valueOf(-2));
		intWrapper.subtract(null);
		Assert.assertEquals(intWrapper.getValue(), Integer.valueOf(-2));
		intWrapper.subtract(Double.valueOf(1.3E1));
		Assert.assertEquals(intWrapper.getValue(), Double.valueOf(-15));
		intWrapper.subtract("3.14");
		Assert.assertEquals(intWrapper.getValue(), Double.valueOf(-18.14));
		
		doubleWrapper.subtract(Integer.valueOf(9));
		Assert.assertEquals(doubleWrapper.getValue(), Double.valueOf(4));
		doubleWrapper.subtract(null);
		Assert.assertEquals(doubleWrapper.getValue(), Double.valueOf(4));
		doubleWrapper.subtract(Double.valueOf(1.3E1));
		Assert.assertEquals(doubleWrapper.getValue(), Double.valueOf(-9));
		doubleWrapper.subtract("3.14");
		Assert.assertEquals(doubleWrapper.getValue(), Double.valueOf(-12.14));
		
		stringWrapper.subtract(Integer.valueOf(12));
		Assert.assertEquals(stringWrapper.getValue(), Double.valueOf(-8.86));
		stringWrapper.subtract(null);
		Assert.assertEquals(stringWrapper.getValue(), Double.valueOf(-8.86));
		stringWrapper.subtract(Double.valueOf(1.3E1));
		Assert.assertEquals(stringWrapper.getValue(), Double.valueOf(-21.86));
		stringWrapper.subtract("3.14");
		Assert.assertEquals(stringWrapper.getValue(), Double.valueOf(-25));
		
		nullWrapper.subtract(Integer.valueOf(22));
		Assert.assertEquals(nullWrapper.getValue(), Integer.valueOf(-22));
		nullWrapper.subtract(null);
		Assert.assertEquals(nullWrapper.getValue(), Integer.valueOf(-22));
		nullWrapper.subtract(Double.valueOf(1.3E1));
		Assert.assertEquals(nullWrapper.getValue(), Double.valueOf(-35));
		nullWrapper.subtract("3.14");
		Assert.assertEquals(nullWrapper.getValue(), Double.valueOf(-38.14));
	}
	
	@Test (expected = RuntimeException.class)
	public void invalidSubstract() {
		ValueWrapper longWrapper = new ValueWrapper(Long.valueOf(11));
		
		longWrapper.subtract("11");
	}
	
	@Test
	public void validMultiply() {
		ValueWrapper intWrapper = new ValueWrapper(Integer.valueOf(10));
		ValueWrapper doubleWrapper = new ValueWrapper(Double.valueOf(0.4E1));
		ValueWrapper stringWrapper  = new ValueWrapper("3");
		ValueWrapper nullWrapper = new ValueWrapper(null);
		
		intWrapper.multiply(Integer.valueOf(12));
		Assert.assertEquals(intWrapper.getValue(), Integer.valueOf(120));
		intWrapper.multiply(Double.valueOf(1.3E1));
		Assert.assertEquals(intWrapper.getValue(), Double.valueOf(1560));
		intWrapper.multiply("2");
		Assert.assertEquals(intWrapper.getValue(), Double.valueOf(3120));
		intWrapper.multiply(null);
		Assert.assertEquals(intWrapper.getValue(), Double.valueOf(0));
		
		doubleWrapper.multiply(Integer.valueOf(9));
		Assert.assertEquals(doubleWrapper.getValue(), Double.valueOf(36));
		doubleWrapper.multiply(Double.valueOf(0.5E1));
		Assert.assertEquals(doubleWrapper.getValue(), Double.valueOf(180));
		doubleWrapper.multiply("2");
		Assert.assertEquals(doubleWrapper.getValue(), Double.valueOf(360));
		doubleWrapper.multiply(null);
		Assert.assertEquals(doubleWrapper.getValue(), Double.valueOf(0));
		
		stringWrapper.multiply(Integer.valueOf(12));
		Assert.assertEquals(stringWrapper.getValue(), Integer.valueOf(36));
		stringWrapper.multiply(Double.valueOf(0.5E1));
		Assert.assertEquals(stringWrapper.getValue(), Double.valueOf(180));
		stringWrapper.multiply("2");
		Assert.assertEquals(stringWrapper.getValue(), Double.valueOf(360));
		doubleWrapper.multiply(null);
		Assert.assertEquals(doubleWrapper.getValue(), Double.valueOf(0));
		
		nullWrapper.multiply(Integer.valueOf(22));
		Assert.assertEquals(nullWrapper.getValue(), Integer.valueOf(0));
		nullWrapper.multiply(Double.valueOf(1.3E1));
		Assert.assertEquals(nullWrapper.getValue(), Double.valueOf(0));
		nullWrapper.multiply("3.14");
		Assert.assertEquals(nullWrapper.getValue(), Double.valueOf(0));
		doubleWrapper.multiply(null);
		Assert.assertEquals(doubleWrapper.getValue(), Double.valueOf(0));
	}
	
	@Test (expected = RuntimeException.class)
	public void invalidMultiply() {
		ValueWrapper intWrapper = new ValueWrapper(Integer.valueOf(10));
		
		intWrapper.multiply(Long.valueOf(12));
	}
	
	@Test
	public void validDivide() {
		ValueWrapper intWrapper = new ValueWrapper(Integer.valueOf(10));
		ValueWrapper doubleWrapper = new ValueWrapper(Double.valueOf(2.1E1));
		ValueWrapper stringWrapper  = new ValueWrapper("30.2");
		ValueWrapper nullWrapper = new ValueWrapper(null);
		
		intWrapper.divide(Integer.valueOf(3));
		Assert.assertEquals(intWrapper.getValue(), Integer.valueOf(3));
		intWrapper.divide(Double.valueOf(0.2E1));
		Assert.assertEquals(intWrapper.getValue(), Double.valueOf(1.5));
		intWrapper.divide("2");
		Assert.assertEquals(intWrapper.getValue(), Double.valueOf(0.75));
		
		doubleWrapper.divide(Integer.valueOf(5));
		Assert.assertEquals(doubleWrapper.getValue(), Double.valueOf(4.2));
		doubleWrapper.divide(Double.valueOf(0.21E1));
		Assert.assertEquals(doubleWrapper.getValue(), Double.valueOf(2));
		doubleWrapper.divide("2");
		Assert.assertEquals(doubleWrapper.getValue(), Double.valueOf(1));
		
		stringWrapper.divide(Integer.valueOf(2));
		Assert.assertEquals(stringWrapper.getValue(), Double.valueOf(15.1));
		stringWrapper.divide(Double.valueOf(0.5E1));
		Assert.assertEquals(stringWrapper.getValue(), Double.valueOf(3.02));
		stringWrapper.divide("2");
		Assert.assertEquals(stringWrapper.getValue(), Double.valueOf(1.51));
		
		nullWrapper.divide(Integer.valueOf(1));
		Assert.assertEquals(nullWrapper.getValue(), Integer.valueOf(0));

	}
	
	@Test (expected = RuntimeException.class)
	public void invalidDivide() {
		ValueWrapper intWrapper = new ValueWrapper(Integer.valueOf(10));
		
		intWrapper.divide(Long.valueOf(12));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void divideBy0() {
		new ValueWrapper(Integer.valueOf(11)).divide(null);
	}
	
	@Test
	public void compareNulls() {
		Assert.assertEquals(0, new ValueWrapper(null).numCompare(null));
	}
	
	@Test
	public void compareWithNull() {
		ValueWrapper intWrapper = new ValueWrapper(Integer.valueOf(10));
		ValueWrapper doubleWrapper = new ValueWrapper(Double.valueOf(-2.1E1));
		ValueWrapper stringWrapper  = new ValueWrapper("0.");
		
		Assert.assertEquals(1, intWrapper.numCompare(null));
		Assert.assertEquals(-1, doubleWrapper.numCompare(null));
		Assert.assertEquals(0, stringWrapper.numCompare(null));
	}
	
	@Test
	public void compareNormal() {
		ValueWrapper intWrapper = new ValueWrapper(Integer.valueOf(10));
		ValueWrapper doubleWrapper = new ValueWrapper(Double.valueOf(-2.1E1));
		ValueWrapper stringWrapper  = new ValueWrapper("0.");
		
		Assert.assertEquals(1, intWrapper.numCompare(Integer.valueOf(3)));
		Assert.assertEquals(-1, intWrapper.numCompare(Double.valueOf(22.2)));
		Assert.assertEquals(0, intWrapper.numCompare("10"));
		
		Assert.assertEquals(1, doubleWrapper.numCompare(Integer.valueOf(-22)));
		Assert.assertEquals(-1, doubleWrapper.numCompare(Double.valueOf(22.2)));
		Assert.assertEquals(0, doubleWrapper.numCompare("-2.1E1"));
		
		Assert.assertEquals(1, stringWrapper.numCompare(Integer.valueOf(-1)));
		Assert.assertEquals(-1, stringWrapper.numCompare(Double.valueOf(22.2)));
		Assert.assertEquals(0, stringWrapper.numCompare("0"));
		
	}
	
	@Test (expected = RuntimeException.class)
	public void invalidNumCompareArgument() {
		ValueWrapper intWrapper = new ValueWrapper(Integer.valueOf(10));
		
		intWrapper.numCompare(Long.valueOf(12));
	}
}
