package hr.fer.zemris.java.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.lang.Math.pow;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Servlet that takes 3 parameters: a with domain [-100, 100],
 * b with domain [-100, 100] and n with domain[1, 5]
 * If invalid domain is given or parameter is not provided,
 * page will be redirected to error page,
 * else creates xml file that contains n pages.
 * Each page will contain 2 columns:
 * 1. column are numbers from a to b, and second column
 * that rows i-th potention.
 * 
 * @author Martin Sršen
 *
 */
public class PowersServlet extends HttpServlet {

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
		Integer a = null;
		Integer b = null;
		Integer n = null;
		
		try {
			a = Integer.valueOf(req.getParameter("a"));
			b = Integer.parseInt(req.getParameter("b"));
			n = Integer.parseInt(req.getParameter("n"));
		}catch(Exception ex) {}
		
		if(!areValidParameters(a, b, n)) {
			req.getRequestDispatcher("/WEB-INF/pages/invalidParameters.jsp").forward(req, resp);
			return;
		}
		
		if(a > b) {
			a = a+b;
			b = a-b;
			a = a-b;
		}
		
		writeToXML(resp, a, b, n);
	}
	
	/**
	 * Method that will write data into xml file.
	 * Calls fillPage method n times.
	 * 
	 * @param resp	an HttpServletResponse object that contains the response the servlet sends to the client
	 * @param a	what number do numbers start from.
	 * @param b	at what number numbers end.
	 * @param n	how many pages there would be.
	 * @throws IOException	if error happens writing to output stream.
	 */
	private void writeToXML(HttpServletResponse resp, int a, int b, int n) throws IOException {
		HSSFWorkbook hwb=new HSSFWorkbook();
		for(int i = 1; i <= n; i++) {
			fillPage(i, hwb, a, b);
		}
		
		resp.setContentType("application/vnd.ms-excel");
		hwb.write(resp.getOutputStream());
		hwb.close();
	}
	
	/**
	 * Method that will fill page with given index in given HSSFWokbook(xml file)
	 * with given Result data from list.
	 * Adds 2 rows, 1st numbers from a to b,
	 * and second row i-th potention of current number,
	 * where i is given pageIndex.
	 * 
	 * @param results	page index that is beeing filled.
	 * @param hwb	Object representing xml file.
	 * @param a	what number do numbers start from.
	 * @param b	at what number numbers end.
	 */
	private void fillPage(int pageIndex, HSSFWorkbook hwb, int a, int b) {
		HSSFSheet sheet = hwb.createSheet("Page " + pageIndex);
		
		HSSFRow rowhead = sheet.createRow(0);
		rowhead.createCell(0).setCellValue("number");
		rowhead.createCell(1).setCellValue("i-th power");
		
		for(int rowIndex = 1;a <= b;a++, rowIndex++) {
			HSSFRow row = sheet.createRow(rowIndex);
			row.createCell(0).setCellValue(a);
			row.createCell(1).setCellValue(pow(a, pageIndex));
		}
	}

	/**
	 * Helper method that checks whether given parameters are valid.
	 * 
	 * @param a	what number do numbers start from.
	 * @param b	at what number numbers end.
	 * @param n	how many pages there would be.
	 * @return	true if parameters are valid, false otherwise.
	 */
	private boolean areValidParameters(Integer a, Integer b, Integer n) {
		if(a == null || b == null || n == null)	return false;
		if(a < -100 || a > 100)	return false;
		if(b < -100 || b > 100)	return false;
		if(n < 1 || n > 5)	return false;
		
		return true;
	}
}
