package hr.fer.zemris.java.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.PollData;

/**
 * Servlet used to update number of votes in database,
 * adds +1 vote.
 * Id to update is given through parameter with key id.
 * If used doesn't provide id, or id is not integer value,
 * processing will be dispatched to /invalidParameters.jsp
 * If valid id is given, updates file and dispatches processing
 * to /glasanje-rezultati that will show the results of voting.
 * 
 * 
 * @author Martin Sršen
 *
 */
public class GlasanjeGlasajServlet extends HttpServlet {

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
		String idValue = req.getParameter("id");
		
		if(idValue == null) {
			req.getRequestDispatcher("/WEB-INF/pages/invalidParameters.jsp").forward(req, resp);
			return;
		}
		
		int id;
		try {
			id = Integer.valueOf(idValue);
		}catch(Exception ex) {
			req.getRequestDispatcher("/WEB-INF/pages/invalidParameters.jsp").forward(req, resp);
			return;
		}
		
		DAOProvider.getDao().addVote(id);
		PollData data = DAOProvider.getDao().getPollData(id);
		
		if(data == null) {
			req.getRequestDispatcher("/WEB-INF/pages/invalidParameters.jsp").forward(req, resp);
			return;
		}
		
		resp.sendRedirect(req.getContextPath() + "/servleti/glasanje-rezultati?pollID=" + data.getPollId());
	}
}
