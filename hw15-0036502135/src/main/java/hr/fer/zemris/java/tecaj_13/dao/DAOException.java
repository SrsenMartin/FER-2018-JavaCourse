package hr.fer.zemris.java.tecaj_13.dao;

/**
 * Exception thrown when error happens
 * getting information from data source.
 *
 */
public class DAOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that takes
	 * arguments to create exception.
	 * 
	 * @param message	Message of error.
	 * @param cause	Cause of error.
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor that takes
	 * arguments to create exception.
	 * 
	 * @param message	Message of error.
	 */
	public DAOException(String message) {
		super(message);
	}
}