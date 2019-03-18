package hr.fer.zemris.java.p12;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import hr.fer.zemris.java.p12.dao.DAOException;
import hr.fer.zemris.java.p12.dao.DAOProvider;

/**
 * Context listener that will listen for context intialization/app start.
 * Used to initialize combo pool data source.
 * If database tables are not created, they will be created
 * and filled with default starting data.
 * 
 * @author Martin Sršen
 *
 */
@WebListener
public class Inicijalizacija implements ServletContextListener {

	private static final String PROPERTIES_FILE = "/WEB-INF/dbsettings.properties";
	
	/**
	 * Receives notification that the web application initialization process is starting.
	 * All ServletContextListeners are notified of context initialization before any filters or
	 * servlets in the web application are initialized.
	 * 
	 * @param event	the ServletContextEvent containing the ServletContext that is being initialized
	 * @throws	RuntimeException	if error happens initializing database state.
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		ComboPooledDataSource cpds = initComboPool(sce);
		
		try(Connection connection = cpds.getConnection()) {
			DAOProvider.getDao().initializeData(connection, sce.getServletContext());
		} catch (DAOException e) {
			throw new RuntimeException(e.getMessage());
		} catch (SQLException e1) {
			throw new RuntimeException("Error creating connection.");
		}
		
		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);
	}

	/**
	 * Receives notification that the ServletContext is about to be shut down.
	 * All servlets and filters will have been destroyed before any ServletContextListeners
	 * are notified of context destruction.
	 * 
	 * @param event	the ServletContextEvent containing the ServletContext that is being destroyed
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource)sce.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		if(cpds!=null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Helper method that will create and initialize combo pooled data source.
	 * Makes connection to database.
	 * 
	 * @param sce	Triggered event object.
	 * @return	created ComboPooledDataSource
	 * @throws	RuntimeException	if error happens initializing it.
	 */
	private ComboPooledDataSource initComboPool(ServletContextEvent sce) {
		Properties properties = null;
		try {
			properties = readProperties(sce.getServletContext());
		}catch(IOException ex) {
			throw new RuntimeException("Error reading properties file.");
		}
		
		String connectionURL = "jdbc:derby://" + properties.getProperty("host") + ":" + properties.getProperty("port") + "/" + properties.getProperty("name");

		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("Error initialising pool.", e1);
		}
		
		cpds.setJdbcUrl(connectionURL);
		cpds.setUser(properties.getProperty("user"));
		cpds.setPassword(properties.getProperty("password"));
		
		return cpds;
	}
	
	/**
	 * Helper method that loads and checks validation
	 * of database location and valid user.
	 * 
	 * @param context	Used to get real file path.
	 * @return	Properties file containing valid database information.
	 * @throws IOException	if error happens reading file.
	 * @throws RuntimeException	if invalid database settings are given.
	 */
	private Properties readProperties(ServletContext context) throws IOException {
		Properties properties = new Properties();
		
		Path path = Paths.get(context.getRealPath(PROPERTIES_FILE));
		if(!Files.isRegularFile(path)) {
			throw new RuntimeException("File with database settings not found.");
		}
		
		try(InputStream stream = Files.newInputStream(path)) {
			properties.load(stream);
			
			if(!properties.containsKey("host") ||
					!properties.containsKey("port") ||
					!properties.containsKey("name") ||
					!properties.containsKey("user") ||
					!properties.containsKey("password")) {
				throw new RuntimeException("Invalid database settings content given.");
			}
		}
		
		return properties;
	}
}