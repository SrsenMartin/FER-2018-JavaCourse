package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet used to update number of votes in result file,
 * adds +1 vote.
 * If file doesn't exist creates one.
 * Id to update is given through parameter with key id.
 * If used doesn't provide id, or id is not integer value,
 * processing will be dispatched to /invalidParameters.jsp
 * If valid id is given, updates file and dispatches processing
 * to /glasanje-rezultati that will show the results of voting.
 * 
 * 
 * @author Martin Sršen
 *
 */
public class GlasanjeGlasajServlet extends HttpServlet {

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
		Path filePath = ResultsUtils.getFilePath(req, ResultsUtils.RESULTS_FILE, true);
		
		String idValue = req.getParameter("id");
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
		
		updateData(filePath, id);
		
		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
	}
	
	/**
	 * Method that updates entry with given id.
	 * Increases number of votes by 1.
	 * Entries into file are sorted by id.
	 * 
	 * @param filePath	file path where results are stored.
	 * @param id	entry id into file.
	 * @throws IOException	if error happends reading file.
	 */
	private void updateData(Path filePath, int id) throws IOException {
		List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
		
		boolean added = false;
		for(int i = 0; i < lines.size(); i++) {
			int idNum = Integer.parseInt(lines.get(i).split("\t")[0]);
			int count = Integer.parseInt(lines.get(i).split("\t")[1]);
			
			if(idNum == id) {
				lines.remove(i);
				lines.add(i, id + "\t" + (count+1));
				added = true;
				break;
			}
			if(idNum > id) {
				lines.add(i, id + "\t1");
				added = true;
				break;
			}
		}
		
		if(!added)	lines.add(id + "\t1");
		
		Files.write(filePath, lines, StandardCharsets.UTF_8);
	}
}
