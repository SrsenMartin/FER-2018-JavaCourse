package hr.fer.zemris.java.p12.dao;

import hr.fer.zemris.java.p12.dao.sql.SQLDAO;

/**
 * Singleton class who knows who should return as a service provider to access the data subsystem.
 * 
 * @author Martin Sršen
 *
 */
public class DAOProvider {

	/**
	 * DAO that provider returns when called.
	 */
	private static DAO dao = new SQLDAO();
	
	/**
	 * Getter for DAO field.
	 * 
	 * @return	DAO field.
	 */
	public static DAO getDao() {
		return dao;
	}
	
}