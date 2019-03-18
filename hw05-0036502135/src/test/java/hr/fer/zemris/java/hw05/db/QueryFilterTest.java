package hr.fer.zemris.java.hw05.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.junit.Assert;

public class QueryFilterTest {

	private StudentDatabase db;
	
	@Before
	public void init() throws IOException {
		List<String> lines = Files.readAllLines(
				 Paths.get("src/main/resources/database.txt"),
				 StandardCharsets.UTF_8
				);
		
		db = new StudentDatabase(lines);
	}
	
	@Test (expected = NullPointerException.class)
	public void queryFilterListNull() {
		new QueryFilter(null);
	}
	
	@Test
	public void testQueryFilter() {
		String query = "jmbag <= \"0000000020\" AND jmbag >= \"0000000010\" AND lastName LIKE \"G*c\" AND firstName LIKE \"Mil*n\"";
		
		QueryFilter filter = new QueryFilter(new QueryParser(query).getQuery());
		
		List<StudentRecord> list = db.filter(filter);
		
		Assert.assertEquals(1, list.size());
		Assert.assertEquals("Milan", list.get(0).getFirstName());
		Assert.assertEquals("0000000016", list.get(0).getJmbag());
	}
}
