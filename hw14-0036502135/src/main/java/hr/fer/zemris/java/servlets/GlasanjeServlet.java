package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.PollData;
import hr.fer.zemris.java.p12.model.Poll;

/**
 * Servlet that will load all available poll data into list of poll data.
 * Pushes that list as attribute with key pollData.
 * Dispatches processing to /WEB-INF/pages/glasanjeIndex.jsp ,
 * jsp file that will show list of bands and by clicking it
 * add voting to results file.
 * 
 * @author Martin Sršen
 *
 */
public class GlasanjeServlet extends HttpServlet {

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
		
		List<PollData> pollData = DAOProvider.getDao().getPollDataList(id);
		
		Poll pollDesc = DAOProvider.getDao().getPoll(id);
		if(pollDesc == null) {
			req.getRequestDispatcher("/WEB-INF/pages/invalidParameters.jsp").forward(req, resp);
		}
		
		req.setAttribute("pollData", pollData);
		req.setAttribute("poll", pollDesc);
		
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}
}
