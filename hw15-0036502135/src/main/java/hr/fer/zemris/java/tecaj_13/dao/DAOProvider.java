package hr.fer.zemris.java.tecaj_13.dao;

import hr.fer.zemris.java.tecaj_13.dao.jpa.JPADAOImpl;

/**
 * Singleton class who knows who should return as a service provider to access the data subsystem.
 * 
 */
public class DAOProvider {

	/**
	 * DAO that provider returns when called.
	 */
	private static DAO dao = new JPADAOImpl();
	
	/**
	 * Getter for DAO field.
	 * 
	 * @return	DAO field.
	 */
	public static DAO getDAO() {
		return dao;
	}
	
}