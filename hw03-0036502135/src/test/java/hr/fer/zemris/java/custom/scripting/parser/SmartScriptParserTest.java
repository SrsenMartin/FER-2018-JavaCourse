package hr.fer.zemris.java.custom.scripting.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.hw03.SmartScriptTester;

public class SmartScriptParserTest {

	@Test(expected = SmartScriptParserException.class)
	public void documentNull() {
		new SmartScriptParser(null);
	}

	@Test
	public void forValids() {
		SmartScriptParserTest pars = new SmartScriptParserTest();
		String docBody = pars.loader("FORvalidElements.txt");

		SmartScriptParser parser = new SmartScriptParser(docBody);
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = SmartScriptTester.createOriginalDocumentBody(document);

		SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
		DocumentNode document2 = parser2.getDocumentNode();
		String secondParse = SmartScriptTester.createOriginalDocumentBody(document2);

		Assert.assertEquals(originalDocumentBody, secondParse);
	}

	@Test(expected = SmartScriptParserException.class)
	public void forInvalidVariable() {
		String docBody = "{$ FOR 3 1 10 1 $}";
		new SmartScriptParser(docBody);
	}

	@Test(expected = SmartScriptParserException.class)
	public void forInvalidVariable2() {
		String docBody = "{$ FOR * \"1\" -10 \"1\" $}";
		new SmartScriptParser(docBody);
	}

	@Test(expected = SmartScriptParserException.class)
	public void forInvalidElement() {
		String docBody = "{$ FOR year @sin 10 $}";
		new SmartScriptParser(docBody);
	}

	@Test(expected = SmartScriptParserException.class)
	public void forTooManyArguments() {
		String docBody = "{$ FOR year 1 10 \"1\" \"10\" $}";
		new SmartScriptParser(docBody);
	}

	@Test(expected = SmartScriptParserException.class)
	public void forFewArguments() {
		String docBody = "{$ FOR year $}";
		new SmartScriptParser(docBody);
	}

	@Test(expected = SmartScriptParserException.class)
	public void echoNotClosed() {
		String docBody = "{$= ";
		new SmartScriptParser(docBody);
	}

	@Test(expected = SmartScriptParserException.class)
	public void forNotClosed() {
		String docBody = "{$ FOR aa 1 10 1 {$END 32$}";
		new SmartScriptParser(docBody);
	}

	@Test(expected = SmartScriptParserException.class)
	public void endNotClosed() {
		String docBody = "{$ FOR aa 1 10 1 $} {$END 32";
		new SmartScriptParser(docBody);
	}

	@Test(expected = SmartScriptParserException.class)
	public void invalidTagKeyWord() {
		String docBody = "{$$ $}";
		new SmartScriptParser(docBody);
	}

	@Test(expected = SmartScriptParserException.class)
	public void testNotAllClosedTags() {
		SmartScriptParserTest pars = new SmartScriptParserTest();
		String docBody = pars.loader("AllNotClosed.txt");
		new SmartScriptParser(docBody);
	}

	@Test(expected = SmartScriptParserException.class)
	public void testTooManyEnds() {
		SmartScriptParserTest pars = new SmartScriptParserTest();
		String docBody = pars.loader("TooManyEnds.txt");
		new SmartScriptParser(docBody);
	}

	@Test(expected = SmartScriptParserException.class)
	public void elementInEnd() {
		String docBody = "{$ FOR aa 1 10 1 $} {$END 32$}";
		new SmartScriptParser(docBody);
	}

	@Test
	public void testdoc1Example() {
		SmartScriptParserTest pars = new SmartScriptParserTest();
		String docBody = pars.loader("doc1.txt");

		SmartScriptParser parser = new SmartScriptParser(docBody);
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = SmartScriptTester.createOriginalDocumentBody(document);

		SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
		DocumentNode document2 = parser2.getDocumentNode();
		String secondParse = SmartScriptTester.createOriginalDocumentBody(document2);

		Assert.assertEquals(originalDocumentBody, secondParse);
	}

	@Test
	public void testNormal() {
		SmartScriptParserTest pars = new SmartScriptParserTest();
		String docBody = pars.loader("Normal.txt");

		SmartScriptParser parser = new SmartScriptParser(docBody);
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = SmartScriptTester.createOriginalDocumentBody(document);

		SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
		DocumentNode document2 = parser2.getDocumentNode();
		String secondParse = SmartScriptTester.createOriginalDocumentBody(document2);

		Assert.assertEquals(originalDocumentBody, secondParse);
	}

	private String loader(String filename) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename)) {
			byte[] buffer = new byte[1024];
			while (true) {
				int read = is.read(buffer);
				if (read < 1)
					break;
				bos.write(buffer, 0, read);
			}
			return new String(bos.toByteArray(), StandardCharsets.UTF_8);
		} catch (IOException ex) {
			return null;
		}
	}

}
