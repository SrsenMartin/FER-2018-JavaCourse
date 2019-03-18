package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toRadians;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.abs;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that takes 2 parameters, a and b.
 * If parameter is not provised sets a to default value 0,
 * and b to default value b.
 * 
 * Calculates sin and cos for each number from a to b
 * and saves them into list of degreeValue objects.
 * Sets list as attribute with key trigonometricValues.
 * Dispatches processing to /WEB-INF/pages/trigonomtric.jsp
 * that will create table and enter sin and cos values into it.
 * 
 * @author Martin Sršen
 *
 */
public class TrigonometricServlet extends HttpServlet {

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
		String from = req.getParameter("a");
		String to = req.getParameter("b");
		
		int a = getDegree(from, 0);
		int b = getDegree(to, 360);
		if(a > b) {
			a = a+b;
			b = a-b;
			a = a-b;
		}
		if(b > a+720) {
			b = a+720;
		}
		
		List<DegreeValue> values = new ArrayList<>();
		for(;a <= b;a++) {
			DegreeValue value = new DegreeValue();
			value.setDegree(a);
			value.setSin(sin(toRadians(a)));
			value.setCos(cos(toRadians(a)));
			values.add(value);
		}
		
		req.setAttribute("trigonometricValues", values);
		req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);
	}
	
	/**
	 * Method that will check given param,
	 * and if it is not valid,
	 * returns defaultValue,
	 * else returns given value.
	 * 
	 * @param param	given parameter.
	 * @param defaultValue	value to set if given parameter is invalid.
	 * @return	value that will be used.
	 */
	private int getDegree(String param, int defaultValue) {
		if(param == null)	return defaultValue;
		
		try {
			return Integer.parseInt(param);
		}catch(NumberFormatException ex) {
			return defaultValue;
		}
	}
	
	/**
	 * Class used to save sin and cos value for each degree from a to b.
	 * Provides getters and setters for these values.
	 */
	public static class DegreeValue {
		/**
		 * degree used for calculation.
		 */
		private int degree;
		/**
		 * degree sin value.
		 */
		private double sin;
		/**
		 * degree cos value.
		 */
		private double cos;
		
		/**
		 * Default constructor.
		 */
		public DegreeValue() {}

		/**
		 * Getter method for degree.
		 * 
		 * @return	degree.
		 */
		public int getDegree() {
			return degree;
		}

		/**
		 * Setter method for degree.
		 * 
		 * @param degree	degree to set.
		 */
		public void setDegree(int degree) {
			this.degree = degree;
		}

		/**
		 * Getter method for sin(degree).
		 * 
		 * @return	sin(degree)
		 */
		public double getSin() {
			return sin;
		}

		/**
		 * Setter method for sin(degree)
		 * 
		 * @param sin	sin(degree) to set.
		 */
		public void setSin(double sin) {
			if(abs(sin) < 10E-10)	sin = 0;
			this.sin = sin;
		}

		/**
		 * Getter method for cos(degree).
		 * 
		 * @return	cos(degree)
		 */
		public double getCos() {
			return cos;
		}

		/**
		 * Setter method for cos(degree)
		 * 
		 * @param sin	cos(degree) to set.
		 */
		public void setCos(double cos) {
			if(abs(cos) < 10E-10)	cos = 0;
			this.cos = cos;
		}
		
		
	}
}
