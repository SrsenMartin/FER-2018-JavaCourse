package hr.fer.zemris.java.sevleti;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.KonvPoly;
import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.Line;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.model.MyDrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.visitor.GeometricalObjectBBCalculator;
import hr.fer.zemris.java.hw16.jvdraw.visitor.GeometricalObjectPainter;

@WebServlet("/crtaj")
public class JediniServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private DrawingModel model = new MyDrawingModel();
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String text = req.getParameter("jvdFile");
		for(String line: text.split("\n")) {
			try {
				addToModel(line);
			} catch(Exception ex) {
				resp.getWriter().write("<html><head><title>Pogreška</title></head><body>Neispravan format primljenih podataka</body>");
			}
		}
		BufferedImage image = getImage();
		ImageIO.write(image, "png", resp.getOutputStream());
	}
	
	public void addToModel(String data) {
		String[] pts = data.trim().split(" ");
		if(pts[0].equals("LINE")) {
			Point start = new Point(Integer.parseInt(pts[1]), Integer.parseInt(pts[2]));
			Point end = new Point(Integer.parseInt(pts[3]), Integer.parseInt(pts[4]));
			Color color = new Color(Integer.parseInt(pts[5]), Integer.parseInt(pts[6]), Integer.parseInt(pts[7]));
			
			model.add(new Line(start, end, color));
		}else if(pts[0].equals("CIRCLE")) {
			Point center = new Point(Integer.parseInt(pts[1]), Integer.parseInt(pts[2]));
			int radius = Integer.parseInt(pts[3]);
			Color color = new Color(Integer.parseInt(pts[4]), Integer.parseInt(pts[5]), Integer.parseInt(pts[6]));
			
			model.add(new Circle(center, radius, color));
		}else if(pts[0].equals("FCIRCLE")) {
			Point center = new Point(Integer.parseInt(pts[1]), Integer.parseInt(pts[2]));
			int radius = Integer.parseInt(pts[3]);
			Color fColor = new Color(Integer.parseInt(pts[4]), Integer.parseInt(pts[5]), Integer.parseInt(pts[6]));
			Color bColor = new Color(Integer.parseInt(pts[7]), Integer.parseInt(pts[8]), Integer.parseInt(pts[9]));
			
			model.add(new FilledCircle(center, radius, fColor, bColor));
		}else if(pts[0].equals("FPOLY")) {
			List<Point> points = new ArrayList<>();
			for(int i = 1; i < pts.length - 6;i+=2) {
				points.add(new Point(Integer.parseInt(pts[i]), Integer.parseInt(pts[i+1])));
			}
			
			Color fColor = new Color(Integer.parseInt(pts[pts.length - 6]), Integer.parseInt(pts[pts.length-5]), Integer.parseInt(pts[pts.length-4]));
			Color bColor = new Color(Integer.parseInt(pts[pts.length - 3]), Integer.parseInt(pts[pts.length-2]), Integer.parseInt(pts[pts.length-1]));
			
			model.add(new KonvPoly(points, fColor, bColor));
		}
	}
	
	private BufferedImage getImage() {
		GeometricalObjectBBCalculator bbcalc = new GeometricalObjectBBCalculator();
		for(int i = 0; i < model.getSize(); i++) {
			model.getObject(i).accept(bbcalc);
		}
		
		Rectangle box = bbcalc.getBoundingBox();
				
		BufferedImage image = new BufferedImage(box.width, box.height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = image.createGraphics();
		AffineTransform trans = new AffineTransform();
		trans.translate(-box.x, -box.y);
		g.transform(trans);

		GeometricalObjectPainter painter = new GeometricalObjectPainter((Graphics2D) g);
		for(int i = 0; i < model.getSize(); i++) {
			model.getObject(i).accept(painter);
		}
		g.dispose();
		
		return image;
	}
}