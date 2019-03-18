package hr.fer.zemris.java.webserver;

/**
 * Interface representing contract that workers must apply.
 * Contains one method processRequest that each workers implements
 * on its own way of processing request.
 * 
 * @author Martin Sršen
 *
 */
public interface IWebWorker {

	/**
	 * Method that worker implements and determines the way request will be processed.
	 * 
	 * @param context	RequestContext used to read data from and write data to.
	 * @throws Exception	If exception happens during processing request.
	 */
	public void processRequest(RequestContext context) throws Exception;
	
}
