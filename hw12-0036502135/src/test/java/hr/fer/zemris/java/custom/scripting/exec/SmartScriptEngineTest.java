package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

public class SmartScriptEngineTest {

	private static final String FILE_LOC = "webroot/scripts/";
	
	//Ako zelis pokreniti test,vidjeti rezultat, desni klik na ime testa -> runAs -> JUnit Test
	
	@Test
	public void osnovniTest() throws IOException {
		String documentBody = readFromDisk(FILE_LOC + "osnovni.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		
		// create engine and execute it
		new SmartScriptEngine(new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters, cookies)).execute();
	}
	
	@Test
	public void zbrajanjeTest() {
		String documentBody = readFromDisk(FILE_LOC + "zbrajanje.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		parameters.put("a", "4");
		parameters.put("b", "2");
		
		// create engine and execute it
		new SmartScriptEngine(new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters, cookies)).execute();

	}
	
	@Test
	public void brojPozivaTest() {
		String documentBody = readFromDisk(FILE_LOC + "brojPoziva.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		persistentParameters.put("brojPoziva", "3");
		RequestContext rc = new RequestContext(System.out, parameters, persistentParameters, cookies);
		new SmartScriptEngine(new SmartScriptParser(documentBody).getDocumentNode(), rc).execute();
		System.out.println("Vrijednost u mapi: " + rc.getPersistentParameter("brojPoziva"));
	}
	
	@Test
	public void fibonacciTest() {
		String documentBody = readFromDisk(FILE_LOC + "fibonacci.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		
		// create engine and execute it
		new SmartScriptEngine(new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters, cookies)).execute();
	}
	
	@Test
	public void fibonaccihTest() {
		String documentBody = readFromDisk(FILE_LOC + "fibonacci.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		
		// create engine and execute it
		try {
			new SmartScriptEngine(new SmartScriptParser(documentBody).getDocumentNode(),
					new RequestContext(Files.newOutputStream(Paths.get("fib.html")), parameters, persistentParameters, cookies)).execute();
		} catch (IOException e) {
		}
	}
	
	private String readFromDisk(String filePath) {
		try {
			return new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
		} catch (IOException e) {
		}
		
		return null;
	}
}
