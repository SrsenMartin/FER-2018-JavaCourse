package hr.fer.zemris.java.p12.dao;

import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletContext;

import hr.fer.zemris.java.p12.model.PollData;
import hr.fer.zemris.java.p12.model.Poll;

/**
 * Interface to subsystem for persistance of data.
 * Contains methods to initialize data, 
 * and to get needed data from created connection
 * and to update votes.
 * 
 * @author Martin Sršen
 *
 */
public interface DAO {
	/**
	 * Method that will initialize starting data for voting web application.
	 * 
	 * @param connection	Connection used to access data source.
	 * @param context	ServletContext used to get real file path.
	 * @throws DAOException	If error happens initializing data.
	 */
	void initializeData(Connection connection, ServletContext context) throws DAOException;
	
	/**
	 * Method used to get all available active polls as list.
	 * 
	 * @return	list of polls data.
	 * @throws DAOException	If error happens initializing data.
	 */
	List<Poll> getPollsList() throws DAOException;
	
	/**
	 * Method used to get list of poll data results from given poll id.
	 * 
	 * @param pollID	Id of pool to get its data.
	 * @return	list of poll data results.
	 * @throws DAOException	If error happens initializing data.
	 */
	List<PollData> getPollDataList(int pollID) throws DAOException;
	
	/**
	 * Method used to get poll data with given id.
	 * 
	 * @param poolDataID	Id of pool data saved in data source.
	 * @return	poll data with given id.
	 * @throws DAOException	If error happens initializing data.
	 */
	PollData getPollData(int poolDataID) throws DAOException;
	
	/**
	 * Method used to get poll description with given id.
	 * 
	 * @param ID	Id of poll description saved in data source.
	 * @return	poll description data at given id.
	 * @throws DAOException	If error happens initializing data.
	 */
	Poll getPoll(int ID) throws DAOException;
	
	/**
	 * Method used to add vote to poll data entry with given id.
	 * 
	 * @param ID	Id of poll data saved in data source.
	 * @throws DAOException	If error happens initializing data.
	 */
	void addVote(int ID) throws DAOException;
}