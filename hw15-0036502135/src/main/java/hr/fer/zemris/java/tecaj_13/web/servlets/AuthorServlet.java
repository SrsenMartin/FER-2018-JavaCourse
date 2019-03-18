package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogEntryForm;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Servlet that will map all urls with /servleti/author/*.
 * Provides complete functionality for showing, adding and
 * changing blog data.
 * 
 * @author Martin Sršen
 *
 */
public class AuthorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInf = req.getPathInfo() == null ? "" : req.getPathInfo().replaceFirst("/", "").trim();
		if(pathInf.isEmpty() || (pathInf.length() - pathInf.replaceAll("/", "").length()) > 1) {
			sendError("Invalid url provided.", req, resp);
			return;
		}
		
		if(pathInf.indexOf("/") == -1) {
			showEntriesListPage(req, resp, pathInf);
		}else if(pathInf.endsWith("/new") || pathInf.endsWith("/edit")) {
			newEdit(req, resp, pathInf);
		}else {
			showEntryPage(req, resp, pathInf);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String entryIdString = req.getParameter("entryId");
		Long entryId = (entryIdString == null || entryIdString.isEmpty()) ? null : Long.parseLong(entryIdString);
		BlogEntryForm userForm = new BlogEntryForm();
		userForm.fill(req);
		userForm.validate();
		
		if(userForm.hasMistakes()) {
			req.setAttribute("form", userForm);
			req.setAttribute("entryId", entryId);
			req.getRequestDispatcher("/WEB-INF/pages/editPage.jsp").forward(req, resp);
			return;
		}
		
		BlogEntry entry = entryId == null ? null : DAOProvider.getDAO().getEntry(entryId);
		if(entry == null)	entry = new BlogEntry();
		
		BlogUser user = DAOProvider.getDAO().getBlogUser((String) req.getSession().getAttribute("currentNick"));
		userForm.fillEntry(entry, user);
		
		DAOProvider.getDAO().saveEntry(entry);
		
		resp.sendRedirect("../" + user.getNick());
	}
	
	/**
	 * Used to proces requests for add or edit blog entries.
	 * 
	 * @param req	an HttpServletRequest object that contains the request the client has made of the servlet.
	 * @param resp	an HttpServletResponse object that contains the response the servlet sends to the client
	 * @param url	url after servleti/author/
	 * @throws ServletException	if the request could not be handled
	 * @throws IOException	if an input or output error is detected when the servlet handles request
	 */
	private void newEdit(HttpServletRequest req, HttpServletResponse resp, String url) throws ServletException, IOException {
		String nick = url.split("/")[0];
		String command = url.split("/")[1];
		
		if(DAOProvider.getDAO().getBlogUser(nick) == null) {
			sendError("User doesn't exist.", req, resp);
			return;
		}else if(!nick.equals(req.getSession().getAttribute("currentNick"))) {
			sendError("You are not allowed to create/edit blogs.", req, resp);
			return;
		}
		
		if(command.equals("edit")) {
			String entryIdString = req.getParameter("entryId");
			if(entryIdString == null || entryIdString.trim().isEmpty()) {
				sendError("Entry id not provided.", req, resp);
				return;
			}
			
			Long entryId = Long.parseLong(entryIdString);
			BlogEntry entry = DAOProvider.getDAO().getEntry(entryId);
			if(entry == null) {
				sendError("Entry with given id doesn't exist.", req, resp);
				return;
			}
			
			BlogEntryForm form = new BlogEntryForm();
			form.setText(entry.getText());
			form.setTitle(entry.getTitle());
			req.setAttribute("form", form);
			req.setAttribute("entryId", entryId);
		}
		
		req.getServletContext().getRequestDispatcher("/WEB-INF/pages/editPage.jsp").forward(req, resp);
	}
	
	/**
	 * Used to process request to show all user entries onto page.
	 * Loads list and pushes it as atrute.
	 * 
	 * @param req	an HttpServletRequest object that contains the request the client has made of the servlet.
	 * @param resp	an HttpServletResponse object that contains the response the servlet sends to the client
	 * @param nick	Nick of user to show its entries.
	 * @throws ServletException	if the request could not be handled
	 * @throws IOException	if an input or output error is detected when the servlet handles request
	 */
	private void showEntriesListPage(HttpServletRequest req, HttpServletResponse resp, String nick) throws ServletException, IOException {
		BlogUser user = DAOProvider.getDAO().getBlogUser(nick);
		if(user == null) {
			req.setAttribute("error", "Given user doesn't exist.");
			req.getServletContext().getRequestDispatcher("/WEB-INF/pages/invalidParameters.jsp").forward(req, resp);
			return;
		}
		
		req.setAttribute("userEntries", DAOProvider.getDAO().getUserEntries(user));
		req.setAttribute("nick", nick);
		req.getServletContext().getRequestDispatcher("/WEB-INF/pages/author.jsp").forward(req, resp);
	}
	
	/**
	 * Helper method that will send error and display it onto other site.
	 * 
	 * @param message	Message to be shown.
	 * @param req	an HttpServletRequest object that contains the request the client has made of the servlet.
	 * @param resp	an HttpServletResponse object that contains the response the servlet sends to the client
	 * @throws ServletException	if the request could not be handled
	 * @throws IOException	if an input or output error is detected when the servlet handles request
	 */
	private void sendError(String message, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("error", message);
		req.getServletContext().getRequestDispatcher("/WEB-INF/pages/invalidParameters.jsp").forward(req, resp);
	}
	
	/**
	 * Processes request to get informations to draw one entry page.
	 * Contained of title and text. Comments and ability to add new comments,
	 * and for user who wrote entry to edit it.
	 * 
	 * @param req	an HttpServletRequest object that contains the request the client has made of the servlet.
	 * @param resp	an HttpServletResponse object that contains the response the servlet sends to the client
	 * @param url	url after servleti/author/
	 * @throws ServletException	if the request could not be handled
	 * @throws IOException	if an input or output error is detected when the servlet handles request
	 */
	private void showEntryPage(HttpServletRequest req, HttpServletResponse resp, String url) throws ServletException, IOException {
		String nick = url.split("/")[0];
		String id = url.split("/")[1];
		
		if(DAOProvider.getDAO().getBlogUser(nick) == null) {
			sendError("User doesn't exist.", req, resp);
			return;
		}
		
		Long entryId = checkAndGetId(id, req, resp);
		if(entryId == null)	return;
		
		BlogEntry entry = DAOProvider.getDAO().getEntry(entryId);
		if(entry == null) {
			sendError("Entry with given id doesn't exist.", req, resp);
			return;
		}
		if(!entry.getCreator().getNick().equals(nick)) {
			sendError("User " + nick + " is not creator of that blog entry.", req, resp);
			return;
		}
		
		req.setAttribute("entry", entry);
		req.setAttribute("entryComments", DAOProvider.getDAO().getEntryComments(entry));
		req.setAttribute("nick", nick);
		req.setAttribute("textError", req.getAttribute("textError"));
		req.setAttribute("emailError", req.getAttribute("emailError"));
		req.getServletContext().getRequestDispatcher("/WEB-INF/pages/entryPage.jsp").forward(req, resp);
	}
	
	/**
	 * Helper method to check given attribute for id.
	 * Returns Long value if valid ,or null if invalid.
	 * 
	 * @param id	String representation of id.
	 * @param req	an HttpServletRequest object that contains the request the client has made of the servlet.
	 * @param resp	an HttpServletResponse object that contains the response the servlet sends to the client
	 * @return	Long value if valid ,or null if invalid.
	 * @throws ServletException	if the request could not be handled
	 * @throws IOException	if an input or output error is detected when the servlet handles request
	 */
	private Long checkAndGetId(String id, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			return Long.parseLong(id);
		}catch(Exception ex) {
			sendError("Id must be numeric integer value.", req, resp);
			return null;
		}
	}
}
