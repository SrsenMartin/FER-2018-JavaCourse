package hr.fer.zemris.java.hw05.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.junit.Assert;

public class StudentDatabaseTest {

	private StudentDatabase db;
	
	@Before
	public void init() throws IOException {
		List<String> lines = Files.readAllLines(
				 Paths.get("src/main/resources/database.txt"),
				 StandardCharsets.UTF_8
				);
		
		db = new StudentDatabase(lines);
	}
	
	@Test
	public void testForJmbagContained() {
		StudentRecord contained = db.forJMBAG("0000000005");
		
		Assert.assertEquals("Jusufadis", contained.getFirstName());
	}
	
	@Test
	public void testForJmbagNotContained() {
		StudentRecord notContained = db.forJMBAG("0000100100");
		
		Assert.assertEquals(null, notContained);
	}
	
	@Test
	public void testFilterAlwaysTrue() {
		IFilter truefilter = record -> true;
		
		List<StudentRecord> lista = db.filter(truefilter);
		
		Assert.assertEquals(63, lista.size());
	}
	
	@Test
	public void testFilterAlwaysFalse() {
		IFilter falsefilter = record -> false;
		
		List<StudentRecord> lista = db.filter(falsefilter);
		
		Assert.assertEquals(0, lista.size());
	}
}
