package hr.fer.zemris.java.servlets;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Context listener that will listen for context intialization/app start.
 * When context is initialized puts current time in miliseconds
 * into application map(servlet context attributes)
 * with key timeStarted.
 * 
 * @author Martin Sršen
 *
 */
public class TimeStarter implements ServletContextListener {

	/**
	 * Receives notification that the ServletContext is about to be shut down.
	 * All servlets and filters will have been destroyed before any ServletContextListeners
	 * are notified of context destruction.
	 * 
	 * @param event	the ServletContextEvent containing the ServletContext that is being destroyed
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}

	/**
	 * Receives notification that the web application initialization process is starting.
	 * All ServletContextListeners are notified of context initialization before any filters or
	 * servlets in the web application are initialized.
	 * 
	 * @param event	the ServletContextEvent containing the ServletContext that is being initialized
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		event.getServletContext().setAttribute("timeStarted", System.currentTimeMillis());
	}
}
