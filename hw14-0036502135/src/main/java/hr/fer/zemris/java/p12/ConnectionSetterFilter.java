package hr.fer.zemris.java.p12;

import hr.fer.zemris.java.p12.dao.sql.SQLConnectionProvider;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.sql.DataSource;

/**
 * Filter that opens connection to database every time
 * web location is called with path /servleti/*, 
 * and destroys it after servlet does its work.
 * 
 * @author Martin Sršen
 *
 */
@WebFilter(filterName="f1",urlPatterns={"/servleti/*"})
public class ConnectionSetterFilter implements Filter {
	
	/**
	 * Called by the web container to indicate to a filter that it is being placed into service.
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	
	/**
	 * Called by the web container to indicate to a filter that it is being taken out of service.
	 */
	@Override
	public void destroy() {
	}
	
	/**
	 * The doFilter method of the Filter is called by the container each time a
	 * request/response pair is passed through the chain due to a client request for a resource at the end of the chain.
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		DataSource ds = (DataSource)request.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		Connection con = null;
		try {
			con = ds.getConnection();
		} catch (SQLException e) {
			throw new IOException("Database not available", e);
		}
		SQLConnectionProvider.setConnection(con);
		try {
			chain.doFilter(request, response);
		} finally {
			SQLConnectionProvider.setConnection(null);
			try { con.close(); } catch(SQLException ignorable) {}
		}
	}
	
}