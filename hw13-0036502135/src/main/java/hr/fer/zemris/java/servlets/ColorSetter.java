package hr.fer.zemris.java.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet used to set background color to all session pages.
 * Takes color through parameter with key background
 * and if valid color is given sets session attribute 
 * with key pickedBgCol and value of color, else 
 * sets default color white.
 * Dispatches processing to /index.jsp
 * 
 * @author Martin Sršen
 *
 */
public class ColorSetter extends HttpServlet {

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
		String hexColor = req.getParameter("background");
		if(!isValidColor(hexColor)) {
			hexColor = "FFFFFF";
		}
		
		req.getSession().setAttribute("pickedBgCol", "#" + hexColor);
		req.getRequestDispatcher("/index.jsp").forward(req, resp);
	}
	
	/**
	 * Helper method that check whether given string is valid color hexcode.
	 * 
	 * @param color	String to check.
	 * @return	true if given string is valid color hexcode, false otherwise.
	 */
	private boolean isValidColor(String color) {
		if(color == null)	return false;
		
		boolean validHex = color.matches("^[0-9a-fA-F]+$");
		boolean validLength = color.length() == 6;
		
		if(validHex && validLength)	return true;
		return false;
	}
}