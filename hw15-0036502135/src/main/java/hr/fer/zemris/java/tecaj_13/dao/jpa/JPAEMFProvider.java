package hr.fer.zemris.java.tecaj_13.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * Class used as getter for EntityManagerFactory.
 * Used to connect to database and do operations with it easily.
 * 
 *
 */
public class JPAEMFProvider {

	/**
	 * factory object that will be returned on request.
	 */
	public static EntityManagerFactory emf;
	
	/**
	 * Getter for emf.
	 */
	public static EntityManagerFactory getEmf() {
		return emf;
	}
	
	/**
	 * Setter of emf.
	 * 
	 * @param emf	EntityManagertFactory object.
	 */
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}