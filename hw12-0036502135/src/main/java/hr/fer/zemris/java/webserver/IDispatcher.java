package hr.fer.zemris.java.webserver;

/**
 * Interface representing method that can dispatch request
 * to another file with given urlPath.
 * Used to call other file to do its implemented actions.
 * 
 * @author Martin Sršen
 *
 */
public interface IDispatcher {
	
	/**
	 * Method that dispatches request to given urlPath through argument.
	 * If exception happens during execution of request, method will
	 * throw it to caller method.
	 * 
	 * @param urlPath	To what file to dispatch request.
	 * @throws Exception	If exception happens executing request.
	 */
	void dispatchRequest(String urlPath) throws Exception;
}
