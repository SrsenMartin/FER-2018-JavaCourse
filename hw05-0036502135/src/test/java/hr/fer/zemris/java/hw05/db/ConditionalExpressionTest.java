package hr.fer.zemris.java.hw05.db;

import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;

public class ConditionalExpressionTest {

	@Test (expected = NullPointerException.class)
	public void givenValueNull() {
		new ConditionalExpression(null, "0000000001", ComparisonOperators.EQUALS);
	}
	
	ConditionalExpression exp;
	
	@Before
	public void init() {
		exp = new ConditionalExpression(FieldValueGetters.FIRST_NAME, "Ivan", ComparisonOperators.EQUALS);
	}
	
	@Test
	public void getFieldGetterTest() {
		Assert.assertEquals(FieldValueGetters.FIRST_NAME, exp.getFieldGetter());
	}
	
	@Test
	public void getStringLiteralTest() {
		Assert.assertEquals("Ivan", exp.getStringLiteral());
	}
	
	@Test
	public void getComparisonOperatorTest() {
		Assert.assertEquals(ComparisonOperators.EQUALS, exp.getComparisonOperator());
	}
}
