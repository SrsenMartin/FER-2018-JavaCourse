package hr.fer.zemris.java.p12.dao.sql;

import java.sql.Connection;

/**
 * Store the connection to the database in the ThreadLocal object.
 * ThreadLocal is actually a folder whose keys are a tree identifier that operates mapping.
 * 
 * @author Martin Sršen
 *
 */
public class SQLConnectionProvider {

	/**
	 * folder whose keys are a tree identifier that operates mapping.
	 */
	private static ThreadLocal<Connection> connections = new ThreadLocal<>();
	
	/**
	 * Set a link to the current tree (or delete the entry from the folder if the argument is <code> null </ code>).
	 * 
	 * @param con Connection to database.
	 */
	public static void setConnection(Connection con) {
		if(con==null) {
			connections.remove();
		} else {
			connections.set(con);
		}
	}
	
	/**
	 * Get the connection that the current caller (caller) can use.
	 * 
	 * @return connection to database.
	 */
	public static Connection getConnection() {
		return connections.get();
	}
	
}