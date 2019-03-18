package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class that implements IWebWorker.
 * Takes color hexcode through parameters with key bgcolor.
 * Checks if its valid color, and if is, changes background
 * color of index2.html site.
 * If color is chages writes appropriate message.
 * Switches screen to index2.html.
 * Can be accessed by /setbgcolor or /ext/BgColorWorker
 * 
 * @author Martin Sršen
 *
 */
public class BgColorWorker implements IWebWorker {

	/**
	 * Method that worker implements and determines the way request will be processed.
	 * 
	 * @param context	RequestContext used to read data from and write data to.
	 * @throws Exception	If exception happens during processing request.
	 */
	@Override
	public void processRequest(RequestContext context) throws Exception {
		String background = context.getParameter("bgcolor");

		if(isValidColor(background)) {
			String oldColor = context.getPersistentParameter("bgcolor");
			
			if(oldColor != null && oldColor.equals(background)) {
				context.setPersistentParameter("message", "Color wasn't updated.");
			}else {
				context.setPersistentParameter("bgcolor", background);
				context.setPersistentParameter("message", "Color updated.");
			}
		}else {
			context.setPersistentParameter("message", "Color wasn't updated.");
		}
		
		context.getDispatcher().dispatchRequest("/index2.html");
	}
	
	/**
	 * Helper method that check whether given string is valid color hexcode.
	 * 
	 * @param color	String to check.
	 * @return	true if given string is valid color hexcode, false otherwise.
	 */
	private boolean isValidColor(String color) {
		if(color == null)	return false;
		
		boolean validHex = color.matches("^[0-9a-fA-F]+$");
		boolean validLength = color.length() == 6;
		
		if(validHex && validLength)	return true;
		return false;
	}
}
