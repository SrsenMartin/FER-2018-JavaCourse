package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class that implements IWebWorker.
 * Takes color from persistentParameters with key bgcolor
 * and writes it into temporary parameters with key background
 * if color is not null, else writes default color 7F7F7F.
 * Calls dispatchRequest /private/home.smscr that will
 * write home screen.
 * Takes no parameters.
 * 
 * @author Martin Sršen
 *
 */
public class Home implements IWebWorker {

	/**
	 * Constant that determines path to file that will create home screen.
	 */
	private static final String DELEGATE_TO = "/private/home.smscr";
	
	/**
	 * Method that worker implements and determines the way request will be processed.
	 * 
	 * @param context	RequestContext used to read data from and write data to.
	 * @throws Exception	If exception happens during processing request.
	 */
	@Override
	public void processRequest(RequestContext context) throws Exception {
		String background = context.getPersistentParameter("bgcolor");
		context.setTemporaryParameter("background", background == null ? "7F7F7F" : background);
		
		context.getDispatcher().dispatchRequest(DELEGATE_TO);
	}

}
