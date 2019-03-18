package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class that implements IWebWorker.
 * Loads parameters, if they are null, puts default value for them,
 * 1 for a and 2 for b.
 * Delegates calculation and output to /private/calc.smscr.
 * Can be accessed by /ext/SumWorker or /calc.
 * Takes 2 parameters, a and b values to calculate sum.
 * 
 * @author Martin Sršen
 *
 */
public class SumWorker implements IWebWorker {

	/**
	 * Constant that determines path to file that will calculaet and write result.
	 */
	private static final String DELEGATE_TO = "/private/calc.smscr";
	
	/**
	 * Method that worker implements and determines the way request will be processed.
	 * 
	 * @param context	RequestContext used to read data from and write data to.
	 * @throws Exception	If exception happens during processing request.
	 */
	@Override
	public void processRequest(RequestContext context) throws Exception {
		String aParam = context.getParameter("a");
		String bParam = context.getParameter("b");
		
		int a = getNumber(aParam, 1);
		int b = getNumber(bParam, 2);
		
		context.setTemporaryParameter("a", Integer.toString(a));
		context.setTemporaryParameter("b", Integer.toString(b));
		context.setTemporaryParameter("zbroj", Integer.toString(a+b));
		
		context.getDispatcher().dispatchRequest(DELEGATE_TO);
	}
	
	/**
	 * Method that returns number that will be written into temporary parameters.
	 * 
	 * @param tempParam	Provided parameter.
	 * @param defaultValue	If invalid parameter is given, returns this value.
	 * @return	number that will be written into temporary parameters.
	 */
	private int getNumber(String tempParam, int defaultValue) {
		if(tempParam == null)	return defaultValue;
		
		try {
			return Integer.parseInt(tempParam);
		}catch(NumberFormatException ex) {
			return defaultValue;
		}
	}
}
