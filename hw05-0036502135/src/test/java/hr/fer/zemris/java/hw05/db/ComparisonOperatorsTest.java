package hr.fer.zemris.java.hw05.db;

import org.junit.Test;

import org.junit.Assert;

public class ComparisonOperatorsTest {

	@Test
	public void lessTest() {
		String jmbag1 = "0000000005";
		String jmbag2 = "0000000010";
		
		boolean less = ComparisonOperators.LESS.satisfied(jmbag1, jmbag2);
		boolean greater = ComparisonOperators.LESS.satisfied(jmbag2, jmbag1);
		boolean equal = ComparisonOperators.LESS.satisfied(jmbag1, jmbag1);
		
		Assert.assertEquals(true, less);
		Assert.assertEquals(false, greater);
		Assert.assertEquals(false, equal);
	}
	
	@Test
	public void lessOrEqualTest() {
		String jmbag1 = "0000000005";
		String jmbag2 = "0000000010";
		
		boolean less = ComparisonOperators.LESS_OR_EQUALS.satisfied(jmbag1, jmbag2);
		boolean greater = ComparisonOperators.LESS_OR_EQUALS.satisfied(jmbag2, jmbag1);
		boolean equal = ComparisonOperators.LESS_OR_EQUALS.satisfied(jmbag1, jmbag1);
		
		Assert.assertEquals(true, less);
		Assert.assertEquals(false, greater);
		Assert.assertEquals(true, equal);
	}
	
	@Test
	public void greaterTest() {
		String jmbag1 = "0000000005";
		String jmbag2 = "0000000010";
		
		boolean less = ComparisonOperators.GREATER.satisfied(jmbag1, jmbag2);
		boolean greater = ComparisonOperators.GREATER.satisfied(jmbag2, jmbag1);
		boolean equal = ComparisonOperators.GREATER.satisfied(jmbag1, jmbag1);
		
		Assert.assertEquals(false, less);
		Assert.assertEquals(true, greater);
		Assert.assertEquals(false, equal);
	}
	
	@Test
	public void greaterOrEqualTest() {
		String jmbag1 = "0000000005";
		String jmbag2 = "0000000010";
		
		boolean less = ComparisonOperators.GREATER_OR_EQUALS.satisfied(jmbag1, jmbag2);
		boolean greater = ComparisonOperators.GREATER_OR_EQUALS.satisfied(jmbag2, jmbag1);
		boolean equal = ComparisonOperators.GREATER_OR_EQUALS.satisfied(jmbag1, jmbag1);
		
		Assert.assertEquals(false, less);
		Assert.assertEquals(true, greater);
		Assert.assertEquals(true, equal);
	}
	
	@Test
	public void equalTest() {
		String jmbag1 = "0000000005";
		String jmbag2 = "0000000010";
		
		boolean less = ComparisonOperators.EQUALS.satisfied(jmbag1, jmbag2);
		boolean greater = ComparisonOperators.EQUALS.satisfied(jmbag2, jmbag1);
		boolean equal = ComparisonOperators.EQUALS.satisfied(jmbag1, jmbag1);
		
		Assert.assertEquals(false, less);
		Assert.assertEquals(false, greater);
		Assert.assertEquals(true, equal);
	}
	
	@Test
	public void notEqualTest() {
		String jmbag1 = "0000000005";
		String jmbag2 = "0000000010";
		
		boolean less = ComparisonOperators.NOT_EQUALS.satisfied(jmbag1, jmbag2);
		boolean greater = ComparisonOperators.NOT_EQUALS.satisfied(jmbag2, jmbag1);
		boolean equal = ComparisonOperators.NOT_EQUALS.satisfied(jmbag1, jmbag1);
		
		Assert.assertEquals(true, less);
		Assert.assertEquals(true, greater);
		Assert.assertEquals(false, equal);
	}
	
	@Test
	public void likeTest() {
		String value = "Ivana";
		
		String validLike1 = "Iv*a";
		String validLike2 = "*ana";
		String validLike3 = "Iva*";
		String validLike4 = "Iv*ana";
		
		String invalidLike1 = "Iv*n";
		String invalidLike2 = "Rand*om";
		
		IComparisonOperator like = ComparisonOperators.LIKE;
		
		Assert.assertEquals(true, like.satisfied(value, validLike1));
		Assert.assertEquals(true, like.satisfied(value, validLike2));
		Assert.assertEquals(true, like.satisfied(value, validLike3));
		Assert.assertEquals(true, like.satisfied(value, validLike4));
		
		Assert.assertEquals(false, like.satisfied(value, invalidLike1));
		Assert.assertEquals(false, like.satisfied(value, invalidLike2));
	}
}
