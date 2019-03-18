package hr.fer.zemris.java.tecaj_13.dao;

import java.util.List;

import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

public interface DAO {

	/**
	 * Dohvaća entry sa zadanim <code>id</code>-em. Ako takav entry ne postoji,
	 * vraća <code>null</code>.
	 * 
	 * @param id ključ zapisa
	 * @return entry ili <code>null</code> ako entry ne postoji
	 * @throws DAOException ako dođe do pogreške pri dohvatu podataka
	 */
	BlogUser getBlogUser(String nickname) throws DAOException;
	/**
	 * Returns all BlogUsers from implemented source as
	 * list of BlogUser objects.
	 * 
	 * @return	list of all BlogUser objects.
	 * @throws DAOException	If error happens loading data.
	 */
	List<BlogUser> getUsers() throws DAOException;
	/**
	 * Adds/updates BlogUser in given data source/destination.
	 * 
	 * @param user	BlogUser to add/update.
	 * @throws DAOException	If error happens loading data.
	 */
	void addUser(BlogUser user) throws DAOException;
	/**
	 * Returns all BlogUser BlogEntry objects from implemented source as
	 * list of BlogEntry objects.
	 * 
	 * @param user	BlogUser to get its entries.
	 * @return	list of all BlogEntry objects for given user..
	 * @throws DAOException	If error happens loading data.
	 */
	List<BlogEntry> getUserEntries(BlogUser user) throws DAOException;
	/**
	 * Takes entry id and returns BlogEntry object containing that id,
	 * or returns null if no match was found.
	 * 
	 * @param entryId	Id of BlogEntry to get.
	 * @return	BlogEntry with given id.
	 * @throws DAOException	If error happens loading data.
	 */
	BlogEntry getEntry(Long entryId)	throws DAOException;
	/**
	 * Saves entry into data destination.
	 * 
	 * @param entry	BlogEntry to save to data source.
	 * @throws DAOException	If error happens loading data.
	 */
	void saveEntry(BlogEntry entry) throws DAOException;
	/**
	 * Returns all BlogEntrys BlogComment objects from implemented source as
	 * list of BlogComment objects.
	 * 
	 * @param entry	BlogEntry to get its comments.
	 * @return	list of all BlogComment objects for given user..
	 * @throws DAOException	If error happens loading data.
	 */
	List<BlogComment> getEntryComments(BlogEntry entry)	throws DAOException;
	/**
	 * Saves entry comment into data source.
	 * 
	 * @param comment	Entry comment to save.
	 * @throws DAOException	If error happens loading data.
	 */
	void addComment(BlogComment comment) throws DAOException;
}