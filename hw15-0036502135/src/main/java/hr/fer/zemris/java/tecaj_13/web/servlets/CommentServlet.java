package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;

/**
 * Servlet that will process one comment adding to database.
 * If given data for text and email are invalid,error message is shown.
 * Else saves comment to database.
 * 
 * @author Martin Sršen
 *
 */
public class CommentServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String email = req.getParameter("email");
		String text = req.getParameter("text");
		Long entryId = Long.parseLong(req.getParameter("entryId"));
		
		BlogEntry entry = DAOProvider.getDAO().getEntry(entryId);
		if(email == null) {
			String currentUserNick = (String) req.getSession().getAttribute("currentNick");
			email = DAOProvider.getDAO().getBlogUser(currentUserNick).getEmail();
		}
		
		if(email.trim().isEmpty() || text.trim().isEmpty()) {
			if(text.trim().isEmpty())  sendError("You can't add empty comment.", req, resp);
			if(email.trim().isEmpty()) sendError("Not registered users must provide email.", req, resp);
			
			return;
		}
		
		saveComment(entry, text, email);
		resp.sendRedirect("author/" + entry.getCreator().getNick() + "/" + entryId);
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
	 * Helper method that will crate comment object and save comment into database.
	 * 
	 * @param entry	Entry whose comment it is.
	 * @param text	Comment text.
	 * @param email	Comment email.
	 */
	private void saveComment(BlogEntry entry, String text, String email) {
		BlogComment comment = new BlogComment();
		comment.setBlogEntry(entry);
		comment.setId(null);
		comment.setMessage(text);
		comment.setPostedOn(new Date());
		comment.setUsersEMail(email);
		
		DAOProvider.getDAO().addComment(comment);
	}
}
