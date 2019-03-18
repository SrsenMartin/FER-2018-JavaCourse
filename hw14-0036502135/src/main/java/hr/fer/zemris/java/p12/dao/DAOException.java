package hr.fer.zemris.java.p12.dao;

/**
 * Exception throws when error happens when
 * reading data from data source.
 * 
 * @author Martin Sršen
 *
 */
public class DAOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public DAOException() {
	}

	/**
	 * Constructor that takes
	 * arguments to create exception.
	 * 
	 * @param message	Message of error.
	 * @param cause	Cause of error.
	 * @param enableSuppression	enable suppression or not.
	 * @param writableStackTrace	writable stackTrace or not.
	 */
	public DAOException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

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

	/**
	 * Constructor that takes
	 * arguments to create exception.
	 * 
	 * @param cause	Cause of error.
	 */
	public DAOException(Throwable cause) {
		super(cause);
	}
}
