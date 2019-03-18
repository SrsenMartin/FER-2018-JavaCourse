package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.Poll;

/**
 * Servlet that will load all poll descriptions into list of polls.
 * Sets list as attribute with key polls.
 * Dispatches request to pollChooser.jsp to create view for user.
 * 
 * @author Martin Sršen
 *
 */
public class PollsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Called by the server (via the service method) to allow a servlet to handle a GET request.
	 * If the request is incorrectly formatted, doGet returns an HTTP "Bad Request" message.
	 * 
	 * @param req	an HttpServletRequest object that contains the request the client has made of the servlet.
	 * @param resp	an HttpServletResponse object that contains the response the servlet sends to the client
	 * @throws ServletException	if the request for the GET could not be handled
	 * @throws IOException	if an input or output error is detected when the servlet handles the GET request
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Poll> pollsList = DAOProvider.getDao().getPollsList();
		req.setAttribute("polls", pollsList);
		
		req.getRequestDispatcher("/WEB-INF/pages/pollChooser.jsp").forward(req, resp);
	}
}
