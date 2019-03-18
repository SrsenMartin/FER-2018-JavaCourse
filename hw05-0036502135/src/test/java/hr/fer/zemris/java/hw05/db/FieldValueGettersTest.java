package hr.fer.zemris.java.hw05.db;

import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;

public class FieldValueGettersTest {

	StudentRecord record;
	
	@Before
	public void init() {
		record = new StudentRecord("0000000008", "Horvat", "Ivan", 5);
	}
	
	@Test
	public void firstNameGetter() {
		IFieldValueGetter getter = FieldValueGetters.FIRST_NAME;
		
		Assert.assertEquals("Ivan", getter.get(record));
	}
	
	@Test
	public void lastNameGetter() {
		IFieldValueGetter getter = FieldValueGetters.LAST_NAME;
		
		Assert.assertEquals("Horvat", getter.get(record));
	}
	
	@Test
	public void jmbagGetter() {
		IFieldValueGetter getter = FieldValueGetters.JMBAG;
		
		Assert.assertEquals("0000000008", getter.get(record));
	}
}
