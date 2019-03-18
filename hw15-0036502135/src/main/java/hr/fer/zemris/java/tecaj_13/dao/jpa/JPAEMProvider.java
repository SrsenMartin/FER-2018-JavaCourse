package hr.fer.zemris.java.tecaj_13.dao.jpa;

import hr.fer.zemris.java.tecaj_13.dao.DAOException;

import javax.persistence.EntityManager;

/**
 * Store the connection to the database in the ThreadLocal object.
 * ThreadLocal is actually a folder whose keys are a tree identifier that operates mapping.
 * 
 *
 */
public class JPAEMProvider {

	/**
	 * folder whose keys are a tree identifier that operates mapping.
	 */
	private static ThreadLocal<EntityManager> locals = new ThreadLocal<>();

	/**
	 * Creates entity manager using factory,
	 * starts transaction, and returns created entity manager.
	 * 
	 * @return	created EntityManager.
	 */
	public static EntityManager getEntityManager() {
		EntityManager em = locals.get();
		if(em==null) {
			em = JPAEMFProvider.getEmf().createEntityManager();
			em.getTransaction().begin();
			locals.set(em);
		}
		return em;
	}

	/**
	 * Commits transactions and closes connection to entity manager.
	 * 
	 * @throws DAOException	If error happens commiting trensaction or closing EM.
	 */
	public static void close() throws DAOException {
		EntityManager em = locals.get();
		if(em==null) {
			return;
		}
		DAOException dex = null;
		try {
			em.getTransaction().commit();
		} catch(Exception ex) {
			dex = new DAOException("Unable to commit transaction.", ex);
		}
		try {
			em.close();
		} catch(Exception ex) {
			if(dex!=null) {
				dex = new DAOException("Unable to close entity manager.", ex);
			}
		}
		locals.remove();
		if(dex!=null) throw dex;
	}
	
}