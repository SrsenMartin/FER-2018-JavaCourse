package hr.fer.zemris.java.custom.collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class DictionaryTest {

	Dictionary dictionary;
	
	@Before
	public void init() {
		dictionary = new Dictionary();
	}
	
	@Test
	public void size0() {
		int expected = 0;
		int actual = dictionary.size();
		
		Assert.assertEquals(expected, actual);
		Assert.assertEquals(true, dictionary.isEmpty());
	}
	
	@Test
	public void normalSize() {
		dictionary.put(11, "Value 1");
		dictionary.put("hkm", 2);
		
		int expected = 2;
		int actual = dictionary.size();
		
		Assert.assertEquals(expected, actual);
		Assert.assertEquals(false, dictionary.isEmpty());
	}
	
	@Test
	public void clear() {
		dictionary.put(11, "Value 1");
		dictionary.put("hkm", 2);
		
		int expected = 2;
		int actual = dictionary.size();
		
		Assert.assertEquals(expected, actual);
		
		dictionary.clear();
		
		Assert.assertEquals(true, dictionary.isEmpty());
	}
	
	@Test
	public void putNormal() {
		dictionary.put(11, "Value 1");
		dictionary.put("hkm", 2);
		dictionary.put("nullvalue", null);
		
		int expected = 3;
		int actual = dictionary.size();
		
		Assert.assertEquals(expected, actual);
		
		Object value1 = dictionary.get(11);
		Object value2 = dictionary.get("hkm");
		Object value3 = dictionary.get("nullvalue");
		
		Object expected1 = "Value 1";
		Object expected2 = 2;
		Object expected3 = null;
		
		Assert.assertEquals(expected1, value1);
		Assert.assertEquals(expected2, value2);
		Assert.assertEquals(expected3, value3);
	}
	
	@Test
	public void putDuplicateKey() {
		dictionary.put(11, "Value 1");
		dictionary.put("hkm", 2);
		
		dictionary.put(11, "Replaced value");
		dictionary.put("hkm", -2);
		
		int expected = 2;
		int actual = dictionary.size();
		
		Assert.assertEquals(expected, actual);
		
		Object value1 = dictionary.get(11);
		Object value2 = dictionary.get("hkm");
		
		Object expected1 = "Replaced value";
		Object expected2 = -2;
		
		Assert.assertEquals(expected1, value1);
		Assert.assertEquals(expected2, value2);
	}
	
	@Test (expected = NullPointerException.class)
	public void putKeyAsNull() {
		dictionary.put(null, "value");
	}
	
	@Test
	public void getNormal() {
		dictionary.put(11, "Value 1");
		dictionary.put("hkm", 2);
		dictionary.put("nullvalue", null);
		
		Object value1 = dictionary.get(11);
		Object value2 = dictionary.get("hkm");
		Object value3 = dictionary.get("nullvalue");
		
		Object expected1 = "Value 1";
		Object expected2 = 2;
		Object expected3 = null;
		
		Assert.assertEquals(expected1, value1);
		Assert.assertEquals(expected2, value2);
		Assert.assertEquals(expected3, value3);
	}
	
	@Test
	public void getNotContained() {
		dictionary.put(11, "Value 1");
		dictionary.put("hkm", 2);
		
		Object notContainedValue = dictionary.get("random key");
		Object expected = null;
		
		Assert.assertEquals(expected, notContainedValue);
	}
	
	@Test
	public void getKeyNull() {
		dictionary.put(11, "Value 1");
		dictionary.put("hkm", 2);
		
		Object keyNullValue = dictionary.get(null);
		Object expected = null;
		
		Assert.assertEquals(expected, keyNullValue);
	}
}
