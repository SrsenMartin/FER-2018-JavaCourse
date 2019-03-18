package hr.fer.zemris.java.hw17.servlets;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet whose job is to return image when required through get request.
 * Takes one parameter, image url.
 * 
 * @author Martin Srsen
 *
 */
public class GetImageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static final String IMAGES_FOLDER = "/WEB-INF/slike";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getParameter("url");
		
		Path imgPath = Paths.get(req.getServletContext().getRealPath(GetImageServlet.IMAGES_FOLDER), url);
		BufferedImage img = ImageIO.read(imgPath.toFile());
		
		resp.setContentType("image/jpg");
		ImageIO.write(img, "jpg", resp.getOutputStream());
	}
}
