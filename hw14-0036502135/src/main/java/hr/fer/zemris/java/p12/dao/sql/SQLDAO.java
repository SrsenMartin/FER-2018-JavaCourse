package hr.fer.zemris.java.p12.dao.sql;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import hr.fer.zemris.java.p12.dao.DAO;
import hr.fer.zemris.java.p12.dao.DAOException;
import hr.fer.zemris.java.p12.model.PollData;
import hr.fer.zemris.java.p12.model.Poll;

/**
 * Implementation of DAO interface that knows how to
 * connect and talk to database.
 * Contains methods to initialize data, 
 * and to get needed data from created connection
 * and to update votes.
 * 
 * @author Martin Sršen
 */
public class SQLDAO implements DAO {
	
	/**
	 * Sql command used to create polls database.
	 */
	private static final String POLLS_SQL = "CREATE TABLE Polls" + 
			" (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," + 
			" title VARCHAR(150) NOT NULL," + 
			" message CLOB(2048) NOT NULL" + ")";
	/**
	 * Sql command used to create polloptions database.
	 */
	private static final String POLL_OPTIONS_SQL = "CREATE TABLE PollOptions" + 
			" (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," + 
			" optionTitle VARCHAR(100) NOT NULL," + 
			" optionLink VARCHAR(150) NOT NULL," + 
			" pollID BIGINT," + 
			" votesCount BIGINT," + 
			" FOREIGN KEY (pollID) REFERENCES Polls(id)" + ")";
	
	/**
	 * File where default initialization polls database data is contained.
	 */
	private static final String POLLS_DEFAULT_FILE = "/WEB-INF/polls-default.txt";

	/**
	 * Method used to get poll data with given id from database.
	 * 
	 * @param poolDataID	Id of pool data saved in database.
	 * @return	poll data with given id.
	 * @throws DAOException	If error happens initializing data.
	 */
	@Override
	public PollData getPollData(int poolDataID) throws DAOException {
		Connection connection = SQLConnectionProvider.getConnection();
		
		try(PreparedStatement pst = connection.prepareStatement("select * from polloptions where id=?")) {
			pst.setInt(1, poolDataID);
			
			try(ResultSet rs = pst.executeQuery()){
				if(rs != null && rs.next()) {
					return createPollData(rs);
				}
			}
		} catch (SQLException e) {
			throw new DAOException("Error getting polls list from database.");
		}
		
		return null;
	}
	
	/**
	 * Method used to add vote to poll data entry with given id in database.
	 * 
	 * @param ID	Id of poll data saved in database.
	 * @throws DAOException	If error happens initializing data.
	 */
	@Override
	public void addVote(int ID) throws DAOException {
		Connection connection = SQLConnectionProvider.getConnection();
		
		try(PreparedStatement pst = connection.prepareStatement("update polloptions set votescount = votescount + 1 where ID = ?")) {
			pst.setInt(1, ID);
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException("Error getting polls list from database.");
		}
	}
	
	/**
	 * Method used to get poll description with given id in database.
	 * 
	 * @param ID	Id of poll description saved in database.
	 * @return	poll description data at given id.
	 * @throws DAOException	If error happens initializing data.
	 */
	@Override
	public Poll getPoll(int ID) throws DAOException {
		Connection connection = SQLConnectionProvider.getConnection();
		
		try(PreparedStatement pst = connection.prepareStatement("select * from polls where id = ?")) {
			pst.setInt(1, ID);
			
			try(ResultSet rs = pst.executeQuery()) {
				if(rs != null && rs.next()) {
					return createPoll(rs);
				}
			}
		} catch (SQLException e) {
			throw new DAOException("Error getting polls list from database.");
		}
		
		return null;
	}
	
	/**
	 * Method used to get list of poll data results from given poll id in database.
	 * 
	 * @param pollID	Id of pool to get its data.
	 * @return	list of poll data results.
	 * @throws DAOException	If error happens initializing data.
	 */
	@Override
	public List<PollData> getPollDataList(int pollID) throws DAOException {
		List<PollData> pollDataList = new ArrayList<>();
		Connection connection = SQLConnectionProvider.getConnection();
		
		try(PreparedStatement pst = connection.prepareStatement(
				"select * from polloptions where pollid = ? order by votescount desc")) {
			pst.setInt(1, pollID);
			
			try(ResultSet rs = pst.executeQuery()) {
				while(rs != null && rs.next()) {
					pollDataList.add(createPollData(rs));
				}
			}
		} catch (SQLException e) {
			throw new DAOException("Error getting bands list from database.");
		}
		
		return pollDataList;
	}
	
	/**
	 * Helper method used to create one PollData object
	 * from given ResultSet ,set to current element.
	 * 
	 * @param rs	ResultSet used to gather informations from database.
	 * @return	created PollData object.
	 * @throws SQLException	if error happens reading from ResultSet.
	 */
	private PollData createPollData(ResultSet rs) throws SQLException {
		PollData data = new PollData();
		
		data.setId(rs.getInt(1));
		data.setName(rs.getString(2));
		data.setLink(rs.getString(3));
		data.setPollId(rs.getInt(4));
		data.setNoVotes(rs.getInt(5));
		
		return data;
	}
	
	/**
	 * Method used to get all available active polls as list.
	 * 
	 * @return	list of polls data.
	 * @throws DAOException	If error happens initializing data.
	 */
	@Override
	public List<Poll> getPollsList() throws DAOException {
		List<Poll> pollsList = new ArrayList<>();
		Connection connection = SQLConnectionProvider.getConnection();
		
		try(PreparedStatement pst = connection.prepareStatement("select * from polls")) {
			try(ResultSet rs = pst.executeQuery()) {
				while(rs != null && rs.next()) {
					pollsList.add(createPoll(rs));
				}
			}
		} catch (SQLException e) {
			throw new DAOException("Error getting polls list from database.");
		}
		
		return pollsList;
	}
	
	/**
	 * Helper method used to create one Poll object
	 * from given ResultSet ,set to current element.
	 * 
	 * @param rs	ResultSet used to gather informations from database.
	 * @return	created Poll object.
	 * @throws SQLException	if error happens reading from ResultSet.
	 */
	private Poll createPoll(ResultSet rs) throws SQLException {
		Poll poll = new Poll();
		
		poll.setID(rs.getInt(1));
		poll.setPollTitle(rs.getString(2));
		poll.setPollMessage(rs.getString(3));
		
		return poll;
	}
	
	/**
	 * Method that will initialize starting data for voting web application.
	 * 
	 * @param connection	Connection used to access database.
	 * @param context	ServletContext used to get real file path.
	 * @throws DAOException	If error happens initializing data.
	 */
	@Override
	public void initializeData(Connection connection, ServletContext context) throws DAOException {
		try {
			DatabaseMetaData dbmd = connection.getMetaData();
			
			//samo jednu treba provjeriti jer se obe stvaraju istovremenu, jedne nema -> niti jedne nema.
			try(ResultSet rs = dbmd.getTables(null, null, "POLLS", null)) {
				if(!rs.next()) {
					createTables(connection);
				}
			}
			
			fillTables(connection, context);
		} catch (SQLException | IOException e) {
			throw new DAOException("Error creating and initialising tables.");
		}
	}
	
	/**
	 * Method that will create polls and polloptions tables in database.
	 * 
	 * @param connection	Connection used to access database.
	 * @throws SQLException	If error happens creating table.
	 */
	private void createTables(Connection connection) throws SQLException {
		try(PreparedStatement pst = connection.prepareStatement(POLLS_SQL)) {pst.executeUpdate();}
		try(PreparedStatement pst = connection.prepareStatement(POLL_OPTIONS_SQL)) {pst.executeUpdate();}
	}
	
	/**
	 * Checks whether poll and polloptions tables are empty.
	 * 
	 * @param connection	Connection used to access database.
	 * @return	true if tables are empty, false otherwise.
	 * @throws SQLException	if error happens accessing database.
	 */
	private boolean areTablesEmpty(Connection connection) throws SQLException {
		try(PreparedStatement pst = connection.prepareStatement("select count(*) from Polls"); ResultSet rs = pst.executeQuery()) {
			if (rs != null && rs.next()) {
				int numberOfRows = rs.getInt(1);
				if (numberOfRows == 0) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Method that will fill tables with default data
	 * if tables are empty,otherwise does nothing.
	 * 
	 * @param connection	Connection used to access database.
	 * @param context	Used to get real path to file with default data.
	 * @throws IOException	If error happens accessing file.
	 * @throws SQLException	If error happens accessing database.
	 */
	private void fillTables(Connection connection, ServletContext context) throws IOException, SQLException {
		if(!areTablesEmpty(connection))	return;
		
		Path path = Paths.get(context.getRealPath(POLLS_DEFAULT_FILE));
		checkFile(path);
		
		List<String> lines = Files.readAllLines(path);
		for(String line: lines) {
			if(line.trim().isEmpty())	continue;
			
			String pts[] = line.split("\t");
			try(PreparedStatement pst = connection.prepareStatement("insert into polls (title, message) values (?, ?)",
					Statement.RETURN_GENERATED_KEYS)) {
				pst.setString(1, pts[0]);
				pst.setString(2, pts[1]);
				
				pst.executeUpdate();
				
				String poolOptionsPath = path.toString().substring(0, path.toString().lastIndexOf(path.getFileName().toString())) + pts[2];
				try(ResultSet gen = pst.getGeneratedKeys()) {
					if(gen != null && gen.next()) {
						long poolID = gen.getLong(1);
						fillPoolOptions(connection, Paths.get(poolOptionsPath), poolID);
					}
				}
				
			}
		}
	}
	
	/**
	 * Method that will fill poolOptions table in database.
	 * Reads default data from file and adds it into table.
	 * 
	 * @param connection	Connection used to access database.
	 * @param path	Path to file containing default content.
	 * @param poolID	Id of pool whose data to look for.
	 * @throws SQLException	If error happens accessing database.
	 * @throws IOException	If error happens accessing file.
	 */
	private void fillPoolOptions(Connection connection, Path path, long poolID) throws SQLException, IOException {
		checkFile(path);
		
		List<String> lines = Files.readAllLines(path);
		for(String line: lines) {
			if(line.trim().isEmpty())	continue;
			
			String pts[] = line.split("\t");
			try(PreparedStatement pst = connection.prepareStatement(
					"insert into pollOptions (optionTitle, optionLink, pollID, votesCount) values (?, ?, ?, ?)")) {
				pst.setString(1, pts[0]);
				pst.setString(2, pts[1]);
				pst.setLong(3, poolID);
				pst.setInt(4, Integer.parseInt(pts[2]));
				
				pst.executeUpdate();
			}
		}
	}
	
	/**
	 * Helper method that checks whether given file is
	 * valid file.
	 * If not, throws exception, else does nothing.
	 * 
	 * @param path	Path to file to check its validation.
	 * @throws RuntimeException	if given path to given file is invalid.
	 */
	private void checkFile(Path path) {
		if(!Files.isRegularFile(path) || !Files.isReadable(path)) {
			throw new RuntimeException("Invalid content file given: " + path.toAbsolutePath().toString());
		}
	}
	
}
