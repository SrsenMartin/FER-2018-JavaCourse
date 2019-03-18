package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.crypto.Crypto;
import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Servlet representing blog main page.
 * Processes request to draw form where user can log in,
 * ability to create new account and to show all registered users
 * as hyperlinks.
 * 
 * @author Martin Sršen
 *
 */
public class MainPageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		loadUsers(req);
		req.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String nick = req.getParameter("nick");
		String password = req.getParameter("password");
		
		req.setAttribute("nick", nick);
		loadUsers(req);
		
		if(password.isEmpty() || nick.isEmpty()) {
			if(nick.isEmpty()) req.setAttribute("nickError", "You didn't provide nickname.");
			if(password.isEmpty()) req.setAttribute("passwordError", "You didn't provide password");
			
			req.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(req, resp);
			return;
		}
		
		BlogUser user = DAOProvider.getDAO().getBlogUser(nick);
		if(user == null) {
			req.setAttribute("nickError", "Invalid nickname given.");
			
			req.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(req, resp);
			return;
		}
		
		String passHash = Crypto.getEncodedHashValue(password);
		if(!passHash.equals(user.getPasswordHash())) {
			req.setAttribute("passwordError", "Invalid password given.");
		
			req.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(req, resp);
			return;
		}

		req.getSession().setAttribute("currentId", user.getId());
		req.getSession().setAttribute("currentNick", user.getNick());
		req.getSession().setAttribute("currentFn", user.getFirstName());
		req.getSession().setAttribute("currentLn", user.getLastName());
		
		req.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(req, resp);
	}
	
	/**
	 * Helper method that will load users list and set it to attributes with key users.
	 * 
	 * @param req	an HttpServletRequest object that contains the request the client has made of the servlet.
	 */
	public static void loadUsers(HttpServletRequest req){
		List<BlogUser> users = DAOProvider.getDAO().getUsers();
		
		req.setAttribute("users", users);
	}
}
