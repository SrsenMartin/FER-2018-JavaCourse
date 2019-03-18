package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;
import hr.fer.zemris.java.tecaj_13.model.BlogUserForm;

/**
 * Servlet used to create, register and save new users to database.
 * Each user must have different nick.
 * After created , user is redirected to main page.
 * 
 * @author Martin Sršen
 *
 */
public class RegisterServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BlogUserForm userForm = new BlogUserForm();
		userForm.fill(req);
		userForm.validate();
		
		if(userForm.hasMistakes()) {
			req.setAttribute("form", userForm);
			req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
			return;
		}
		
		BlogUser user = userForm.getUser();
		DAOProvider.getDAO().addUser(user);
		
		resp.sendRedirect("main");
	}
}
