package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class that implements IWebWorker.
 * Creates table with 2 columns(name and value).
 * Writes all given parameters into this table.
 * Can be accessed by /ext/EchoParams.
 * Takes unlimited number of parameters.
 * 
 * @author Martin Sršen
 *
 */
public class EchoParams implements IWebWorker {

	/**
	 * Method that worker implements and determines the way request will be processed.
	 * 
	 * @param context	RequestContext used to read data from and write data to.
	 * @throws Exception	If exception happens during processing request.
	 */
	@Override
	public void processRequest(RequestContext context) throws Exception {
		context.setMimeType("text/html");
		context.setContentLength(null);
		
		try {
			context.write("<html><body>");
			context.write("<h1 align=\"center\">Given parameters</h1><table align=\"center\" border=1 cellpadding=7>");
			
			context.write("<thead><tr><th scope=\"col\"><h2>Name</h2></th><th scope=\"col\"><h2>Value</h2></th></tr></thread><tbody>");
			for(String name : context.getParameterNames()) {
				context.write("<tr><td>" + name + "</td><td>" + context.getParameter(name) + "</td></tr>");
			}
			
			context.write("</tbody></table></body></html>");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
