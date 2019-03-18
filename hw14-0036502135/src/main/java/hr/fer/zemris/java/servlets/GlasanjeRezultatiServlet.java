package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.PollData;

/**
 * Servlet that calculates creates list of results
 * that contains all informations needed to show them.
 * List is than added as attribute with key results.
 * Processing is dispatched to /WEB-INF/pages/glasanjeRez.jsp
 * that will show these created informations.
 * 
 * @author Martin Sršen
 *
 */
public class GlasanjeRezultatiServlet extends HttpServlet {

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
		String idValue = req.getParameter("pollID");
		
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
		
		if(DAOProvider.getDao().getPoll(id) == null) {
			req.getRequestDispatcher("/WEB-INF/pages/invalidParameters.jsp").forward(req, resp);
			return;
		}
		
		List<PollData> resultData = DAOProvider.getDao().getPollDataList(id);
		
		req.setAttribute("results", resultData);
		
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}
}
