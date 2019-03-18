package hr.fer.zemris.java.custom.scripting.lexer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class SmartScriptLexerTest {

	@Test(expected = SmartScriptLexerException.class)
	public void documentNull() {
		new SmartScriptLexer(null);
	}

	@Test
	public void testEmpty() {
		SmartScriptLexer lexer = new SmartScriptLexer("");

		assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
	}

	@Test
	public void testGetReturnsLastNext() {
		// Calling getToken once or several times after calling nextToken must return
		// each time what nextToken returned...
		SmartScriptLexer lexer = new SmartScriptLexer("");

		SmartScriptToken token = lexer.nextToken();
		assertEquals(token, lexer.getToken());
		assertEquals(token, lexer.getToken());
	}

	@Test(expected = SmartScriptLexerException.class)
	public void testRadAfterEOF() {
		SmartScriptLexer lexer = new SmartScriptLexer("");

		// will obtain EOF
		lexer.nextToken();
		// will throw!
		lexer.nextToken();
	}

	@Test(expected = SmartScriptLexerException.class)
	public void testNullState() {
		new SmartScriptLexer("").setState(null);
	}

	@Test
	public void testNotNullInTag() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		lexer.setState(SmartScriptLexerState.TAG);

		assertNotNull(lexer.nextToken());
	}

	@Test
	public void testEmptyInTag() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		lexer.setState(SmartScriptLexerState.TAG);

		assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
	}

	@Test
	public void testGetReturnsLastNextInTag() {
		// Calling getToken once or several times after calling nextToken must return
		// each time what nextToken returned...
		SmartScriptLexer lexer = new SmartScriptLexer("");
		lexer.setState(SmartScriptLexerState.TAG);

		SmartScriptToken token = lexer.nextToken();
		assertEquals(token, lexer.getToken());
		assertEquals(token, lexer.getToken());
	}

	@Test(expected = SmartScriptLexerException.class)
	public void testRadAfterEOFInTag() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		lexer.setState(SmartScriptLexerState.TAG);

		// will obtain EOF
		lexer.nextToken();
		// will throw!
		lexer.nextToken();
	}

	@Test
	public void testFOR() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$  foR  $}");

		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TAG, "open"));
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TAG, "FOR"));
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TAG, "close"));
	}

	@Test
	public void testEND() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$  enD  $}");

		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TAG, "open"));
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TAG, "END"));
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TAG, "close"));
	}

	@Test
	public void testECHO() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$=$}");

		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TAG, "open"));
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TAG, "="));
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TAG, "close"));
	}

	@Test
	public void testText() {
		SmartScriptLexer lexer = new SmartScriptLexer("dsaad 3213 \n \r \t 43{ } @ fds3.1 {$=$}");

		checkToken(lexer.nextToken(),
				new SmartScriptToken(SmartScriptTokenType.TEXT, "dsaad 3213 \n \r \t 43{ } @ fds3.1 "));
	}

	@Test
	public void testTextEscape() {
		SmartScriptLexer lexer = new SmartScriptLexer("\\\\ \\{$=$}");

		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TEXT, "\\ {$=$}"));
	}

	@Test(expected = SmartScriptLexerException.class)
	public void testTextEscapeInvalid() {
		SmartScriptLexer lexer = new SmartScriptLexer("\\ {$=$}");
		lexer.nextToken();
	}

	@Test
	public void testValidKeyWords() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$fOr EnD =$}");

		lexer.nextToken();
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TAG, "FOR"));
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TAG, "END"));
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.TAG, "="));
	}

	@Test
	public void testValidVariables() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$k32_5 A528 zzzlfds___ k_2_k_3$}");

		lexer.nextToken();
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.VARIABLE, "k32_5"));
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.VARIABLE, "A528"));
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.VARIABLE, "zzzlfds___"));
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.VARIABLE, "k_2_k_3"));
	}

	@Test(expected = SmartScriptLexerException.class)
	public void testInvalidVariable1() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$ 3kz$}");

		lexer.nextToken();
		lexer.nextToken();
	}

	@Test(expected = SmartScriptLexerException.class)
	public void testInvalidVariable2() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$ _kz$}");

		lexer.nextToken();
		lexer.nextToken();
	}

	@Test
	public void testString() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$= \" \\\\ \\\" fdsf 32.2 \\n \\t \\r \" $}");

		lexer.nextToken();
		lexer.nextToken();
		checkToken(lexer.nextToken(),
				new SmartScriptToken(SmartScriptTokenType.ELEMENT_STRING, " \\ \" fdsf 32.2 \n \t \r "));
	}

	@Test(expected = SmartScriptLexerException.class)
	public void invalidTestString() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$= \" \\ \" $}");

		lexer.nextToken();
		lexer.nextToken();
		lexer.nextToken();
	}

	@Test
	public void testNumberConstant() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$ 41 -12 - 12 5.2 -6.8 - 4.4$}");

		lexer.nextToken();
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.CONSTANT_INTEGER, 41));
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.CONSTANT_INTEGER, -12));
		lexer.nextToken();
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.CONSTANT_INTEGER, 12));
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.CONSTANT_DOUBLE, 5.2));
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.CONSTANT_DOUBLE, -6.8));
		lexer.nextToken();
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.CONSTANT_DOUBLE, 4.4));
	}

	@Test(expected = SmartScriptLexerException.class)
	public void testInvalidNumber() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$ 5.21.2 $}");

		lexer.nextToken();
		lexer.nextToken();
	}

	@Test(expected = SmartScriptLexerException.class)
	public void testInvalidNumber2() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$ 5. $}");

		lexer.nextToken();
		lexer.nextToken();
	}

	@Test(expected = SmartScriptLexerException.class)
	public void testInvalidNumber3() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$ 532k $}");

		lexer.nextToken();
		lexer.nextToken();
	}

	@Test
	public void testValidFunctions() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$@k32_5 @A528 @zzzlfds___ @k_2_k_3$}");

		lexer.nextToken();
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.FUNCTION, "k32_5"));
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.FUNCTION, "A528"));
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.FUNCTION, "zzzlfds___"));
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.FUNCTION, "k_2_k_3"));
	}

	@Test(expected = SmartScriptLexerException.class)
	public void testInvalidFunction1() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$ @3kz$}");

		lexer.nextToken();
		lexer.nextToken();
	}

	@Test(expected = SmartScriptLexerException.class)
	public void testInvalidFunction2() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$ @_3kz$}");

		lexer.nextToken();
		lexer.nextToken();
	}

	@Test
	public void testValidOperators() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$ + - / * ^ $}");

		lexer.nextToken();
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.OPERATOR, '+'));
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.OPERATOR, '-'));
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.OPERATOR, '/'));
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.OPERATOR, '*'));
		checkToken(lexer.nextToken(), new SmartScriptToken(SmartScriptTokenType.OPERATOR, '^'));
	}

	@Test(expected = SmartScriptLexerException.class)
	public void testInvalidOperator() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$ % $}");

		lexer.nextToken();
		lexer.nextToken();
	}

	private void checkToken(SmartScriptToken actual, SmartScriptToken expected) {
		String msg = "Token are not equal.";
		assertEquals(msg, expected.getType(), actual.getType());
		assertEquals(msg, expected.getValue(), actual.getValue());
	}

}
