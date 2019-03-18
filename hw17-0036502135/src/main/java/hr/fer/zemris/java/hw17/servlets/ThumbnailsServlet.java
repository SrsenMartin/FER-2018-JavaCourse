package hr.fer.zemris.java.hw17.servlets;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet whose job is to return image of size 150x150(thumbnail) when required through get request.
 * Takes one parameter, image url.
 * If thumbnails is not yet created, creates it.
 * 
 * @author Martin Srsen
 *
 */
public class ThumbnailsServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final String THUMBNAILS_FOLDER = "/WEB-INF/thumbnails";
	
	private static final int TH_WIDTH = 150;
	private static final int TH_HEIGHT = 150;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getParameter("url");
		
		Path thFolder = Paths.get(req.getServletContext().getRealPath(THUMBNAILS_FOLDER));
		checkFolder(thFolder);
		
		Path thPath = Paths.get(thFolder.toAbsolutePath().toString(), url);
		
		if(!Files.exists(thPath)) {
			Path imgPath = Paths.get(req.getServletContext().getRealPath(GetImageServlet.IMAGES_FOLDER), url);
			createThumbnail(imgPath, thPath);
		}
		
		BufferedImage img = ImageIO.read(thPath.toFile());
		
		resp.setContentType("image/jpg");
		ImageIO.write(img, "jpg", resp.getOutputStream());
	}

	/**
	 * Method that creates thumbnail if not yet created.
	 * 
	 * @param imgPath	Path to original image.
	 * @param thPath	Path where thumbnail will be saved.
	 * @throws IOException	if error happens accessing file.
	 */
	private void createThumbnail(Path imgPath, Path thPath) throws IOException {		
		BufferedImage img = ImageIO.read(imgPath.toFile());
		Image image = img.getScaledInstance(TH_WIDTH, TH_HEIGHT, Image.SCALE_SMOOTH);
		
		BufferedImage thImg = new BufferedImage(TH_WIDTH, TH_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = thImg.createGraphics();
		AffineTransform tr = new AffineTransform();
		g2d.drawImage(image, tr, null);
		g2d.dispose();
		
		ImageIO.write(thImg, "jpg", thPath.toFile());
	}

	/**
	 * Method that checks whether directory exists at given path,
	 * if not creates one.
	 * 
	 * @param folder	Path of directory to check.
	 * @throws IOException	If error happens creating file.
	 */
	private void checkFolder(Path folder) throws IOException {
		if(Files.exists(folder))	return;
		
		Files.createDirectory(folder);
	}
}
