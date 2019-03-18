package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class that implements IWebWorker.
 * Writes hello message to user, and if name was provided
 * writes length of name, else writes name not provided.
 * Can be accessed by /hello or /ext/HelloWorker.
 * Takes 1 parameter with key name and value given name.
 * 
 * @author Martin Sršen
 *
 */
public class HelloWorker implements IWebWorker {

	/**
	 * Method that worker implements and determines the way request will be processed.
	 * 
	 * @param context	RequestContext used to read data from and write data to.
	 * @throws Exception	If exception happens during processing request.
	 */
	@Override
	public void processRequest(RequestContext context) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		
		context.setMimeType("text/html");
		context.setContentLength(null);
		
		String name = context.getParameter("name");
		try {
			context.write("<html><body>");
			context.write("<h1>Hello!!!</h1>");
			context.write("<p>Now is: " + sdf.format(now) + "</p>");
			
			if (name == null || name.trim().isEmpty()) {
				context.write("<p>You did not send me your name!</p>");
			} else {
				context.write("<p>Your name has " + name.trim().length() + " letters.</p>");
			}
			
			context.write("</body></html>");
		} catch (IOException ex) {
			// Log exception to servers log...
			ex.printStackTrace();
		}
	}

}
