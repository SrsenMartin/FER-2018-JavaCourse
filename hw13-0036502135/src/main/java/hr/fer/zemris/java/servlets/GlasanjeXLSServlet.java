package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import hr.fer.zemris.java.servlets.ResultsUtils.Result;

/**
 * Servlet that will create xml file containing
 * results of voting with 2 columns:
 * 1. coulmn is bands names and 2. votes that each got.
 * Has only one page called results.
 * 
 * @author Martin Sršen
 *
 */
public class GlasanjeXLSServlet extends HttpServlet {

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
		List<Result> results = ResultsUtils.getResults(req);
		
		HSSFWorkbook hwb=new HSSFWorkbook();
		fillXLS(results, hwb);
		
		resp.setContentType("application/vnd.ms-excel");
		hwb.write(resp.getOutputStream());
		hwb.close();
	}
	
	/**
	 * Method that will fill given HSSFWokbook(xml file)
	 * with given Result data from list.
	 * Adds 2 rows, 1st row band names,
	 * and second row number of votes that band got.
	 * 
	 * @param results	list of result objects used to fill xml page.
	 * @param hwb	Object representing xml file.
	 */
	private void fillXLS(List<Result> results, HSSFWorkbook hwb) {
		HSSFSheet sheet = hwb.createSheet("Results");
		
		HSSFRow rowhead = sheet.createRow(0);
		rowhead.createCell(0).setCellValue("Band");
		rowhead.createCell(1).setCellValue("Number of votes");
		
		for(int rowIndex = 1; rowIndex <= results.size(); rowIndex++) {
			Result rowResult = results.get(rowIndex - 1);
			
			HSSFRow row = sheet.createRow(rowIndex);
			row.createCell(0).setCellValue(rowResult.getName());
			row.createCell(1).setCellValue(rowResult.getNumberOfVotes());
		}
		
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
	}
}
