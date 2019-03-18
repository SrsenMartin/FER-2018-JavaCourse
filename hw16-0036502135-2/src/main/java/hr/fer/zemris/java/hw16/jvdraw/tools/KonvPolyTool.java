package hr.fer.zemris.java.hw16.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import hr.fer.zemris.java.hw16.jvdraw.components.IColorProvider;
import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.KonvPoly;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.visitor.GeometricalObjectPainter;
import hr.fer.zemris.java.vector.Vector3;

public class KonvPolyTool implements Tool {

	/**
	 * Whether mouse was pressed once.
	 */
	protected boolean pressed;
	
	private List<Point> points = new ArrayList<>();
	private Point moving;
	
	/**
	 * Model used to store saved geometrical objects.
	 */
	protected DrawingModel model;
	/**
	 * Foreground color.
	 */
	protected IColorProvider fColor;
	/**
	 * Canvas used to draw on.
	 */
	protected JComponent canvas;
	/**
	 * Background filled circle color.
	 */
	private IColorProvider bColor;
	
	public KonvPolyTool(DrawingModel model, IColorProvider fColor, IColorProvider bColor, JComponent canvas) {
		this.model = model;
		this.fColor = fColor;
		this.bColor = bColor;
		this.canvas = canvas;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(SwingUtilities.isRightMouseButton(e)) {
			pressed = false;
			points = new ArrayList<>();
			canvas.repaint();
			return;
		}
		
		if(!pressed) {
			points.add(e.getPoint());
			moving = e.getPoint();
			pressed = true;
		}else {
			if(points.size() >= 3 && (getDistance(e.getPoint(), points.get(points.size() - 1)) <= 3 || 
					getDistance(e.getPoint(), points.get(0)) <= 3)) {
				model.add(new KonvPoly(points, fColor.getCurrentColor(), bColor.getCurrentColor()));
				pressed = false;
				points = new ArrayList<>();
			}else if(points.size() >= 3 && !validDot(e.getPoint())) {
				JOptionPane.showMessageDialog(null, "Ne konvergira,ne prihvaca se", "Error", JOptionPane.ERROR_MESSAGE);
			}else if(getDistance(e.getPoint(), points.get(points.size() - 1)) > 3 &&
			
					getDistance(e.getPoint(), points.get(0)) > 3) {
				points.add(e.getPoint());
			}
		}
	}
	
	public boolean validDot(Point point) {		
		List<Vector3> vectors = new ArrayList<>();
		for(Point p: points) {
			vectors.add(new Vector3(p.x, p.y, 0));
		}
		vectors.add(new Vector3(point.x, point.y, 0));
		
		boolean valid = true;
		boolean lessThanZero = false;
		for(int i = 0;i < vectors.size(); i++) {
			Vector3 ri1 = vectors.get(i%vectors.size()).sub(vectors.get((i+1)%vectors.size()));
			Vector3 ri2 = vectors.get(i%vectors.size()).sub(vectors.get((i+2)%vectors.size()));
			
			Vector3 res = ri1.cross(ri2);
			if(i == 0) {
				lessThanZero = res.getZ() < 0.;
			}
			
			if(res.getZ() < 0. && lessThanZero == false) {
				valid = false;
				break;
			}else if(res.getZ() > 0. && lessThanZero == true) {
				valid = false;
				break;
			}
		}
		
		return valid;
	}

	private double getDistance(Point a, Point b) {
		int one = a.x - b.x;
		int other = a.y - b.y;
		
		double distance = Math.sqrt(one*one + other*other);
		return distance;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(!pressed)	return;

		moving = e.getPoint();
		canvas.repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void paint(Graphics2D g2d) {
		if(!pressed)	return;
		
		GeometricalObjectPainter.createPoly(g2d, bColor.getCurrentColor(), points, moving, true);
		GeometricalObjectPainter.createPoly(g2d, fColor.getCurrentColor(), points, moving, false);
	}

}
