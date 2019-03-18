package hr.fer.zemris.java.hw05.db;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class QueryParserTest {

	@Test (expected = NullPointerException.class)
	public void queryNull() {
		new QueryParser(null);
	}
	
	@Test
	public void isDirectQueryTest() {
		String directQuery = "  jmbag=  \"0000000011\" ";
		String notDirectQuery = "jmbag < \"0000000010\" ";
		String notDirectQuery2 = "jmbag=\\\"0000000011\\\" AND firstName = \"Ivan\" ";
		String notDirectQuery3 = "lastName = \"Horvat\" ";
		
		Assert.assertEquals(true, new QueryParser(directQuery).isDirectQuery());
		Assert.assertEquals(false, new QueryParser(notDirectQuery).isDirectQuery());
		Assert.assertEquals(false, new QueryParser(notDirectQuery2).isDirectQuery());
		Assert.assertEquals(false, new QueryParser(notDirectQuery3).isDirectQuery());
	}
	
	@Test (expected = IllegalStateException.class)
	public void getQueriedJMBAGnotDirect() {
		QueryParser pa = new QueryParser("lastName = \"Horvat\" ");
		pa.getQueriedJMBAG();
	}
	
	@Test
	public void getQueriedJMBAG() {
		QueryParser pa = new QueryParser("  jmbag=  \"0000000011\" ");
		
		Assert.assertEquals("0000000011", pa.getQueriedJMBAG());
	}
	
	@Test
	public void getQueryTest() {
		String query = " jmbag <= \"0000000001\" AND firstName LIKE \"I*n\" AND lastName > \"Horvat\"";
		
		QueryParser pa = new QueryParser(query);
		
		List<ConditionalExpression> list = pa.getQuery();
		
		Assert.assertEquals(3, list.size());
	}
	
}
