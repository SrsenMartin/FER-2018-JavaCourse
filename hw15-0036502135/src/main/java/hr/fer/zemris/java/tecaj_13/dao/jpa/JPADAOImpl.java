package hr.fer.zemris.java.tecaj_13.dao.jpa;

import java.util.List;

import javax.persistence.NoResultException;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOException;
import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * DAO implementation whose source of data/informations is
 * database.
 * JPA is used to easily store objects to database.
 * 
 *
 */
public class JPADAOImpl implements DAO {

	@Override
	public BlogUser getBlogUser(String nickname) throws DAOException {
		BlogUser user = null;
		try {
			user = JPAEMProvider.getEntityManager()
				.createNamedQuery("BlogUser.userByNick", BlogUser.class).setParameter("be", nickname).getSingleResult();
		}catch(NoResultException ex) {}
		
		JPAEMProvider.close();
		
		return user;
	}

	@Override
	public List<BlogUser> getUsers() throws DAOException {
		List<BlogUser> users = JPAEMProvider.getEntityManager().createNamedQuery("BlogUser.users", BlogUser.class).getResultList();
		
		JPAEMProvider.close();
		
		return users;
	}

	@Override
	public void addUser(BlogUser user) throws DAOException {
		JPAEMProvider.getEntityManager().persist(user);
		JPAEMProvider.close();
	}

	@Override
	public List<BlogEntry> getUserEntries(BlogUser user) throws DAOException {
		List<BlogEntry> userEntries = JPAEMProvider.getEntityManager()
				.createNamedQuery("BlogEntry.userEntries", BlogEntry.class).setParameter("user", user).getResultList();
		
		JPAEMProvider.close();
		
		return userEntries;
	}

	@Override
	public BlogEntry getEntry(Long entryId) throws DAOException {
		BlogEntry entry = null;
		try {
			entry = loadEntry(entryId);
		}catch(NoResultException ex) {}
		
		JPAEMProvider.close();
		
		return entry;
	}

	@Override
	public void saveEntry(BlogEntry newEntry) throws DAOException {
		if(newEntry.getId() == null) {
			JPAEMProvider.getEntityManager().persist(newEntry);
		}else {
			BlogEntry entry = loadEntry(newEntry.getId());
			
			entry.setLastModifiedAt(newEntry.getLastModifiedAt());
			entry.setText(newEntry.getText());
			entry.setTitle(newEntry.getTitle());
		}
		
		JPAEMProvider.close();
	}
	
	/**
	 * Helper method that loads BlodEntry with given entryId without closing connection.
	 * 
	 * @param entryId	Id of BlogEntry to look for.
	 * @return	found BlogEntry.
	 */
	private BlogEntry loadEntry(long entryId) {
		BlogEntry blogEntry = JPAEMProvider.getEntityManager().find(BlogEntry.class, entryId);
		return blogEntry;
	}

	@Override
	public List<BlogComment> getEntryComments(BlogEntry entry) throws DAOException {
		List<BlogComment> entryComments = JPAEMProvider.getEntityManager()
				.createNamedQuery("BlogComment.entryComments", BlogComment.class).setParameter("entry", entry).getResultList();
		
		JPAEMProvider.close();
		
		return entryComments;
	}

	@Override
	public void addComment(BlogComment comment) throws DAOException {
		JPAEMProvider.getEntityManager().persist(comment);
		JPAEMProvider.close();
	}
}