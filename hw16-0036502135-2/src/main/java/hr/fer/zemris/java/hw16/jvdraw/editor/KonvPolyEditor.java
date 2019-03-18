package hr.fer.zemris.java.hw16.jvdraw.editor;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTextField;

import hr.fer.zemris.java.hw16.jvdraw.components.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.KonvPoly;
import hr.fer.zemris.java.vector.Vector3;

public class KonvPolyEditor extends GeometricalObjectEditor {

	private static final long serialVersionUID = 1L;
	
	private KonvPoly konvPoly;
	private List<JTextField> edits;
	
	private JColorArea fArea;
	private JColorArea bArea;
	
	public KonvPolyEditor(KonvPoly konvPoly) {
		this.konvPoly = konvPoly;
		edits = new ArrayList<>();
		
		fArea = new JColorArea(konvPoly.getFColor());
		bArea = new JColorArea(konvPoly.getBColor());

		
		List<Point> points = konvPoly.getPoints();
		for(int i = 0; i < points.size(); i++) {
			edits.add(new JTextField(points.get(i).x + "", 6));
			edits.add(new JTextField(points.get(i).y + "", 6));
		}
		
		createPanel();
	}
	
	private void createPanel() {
		setLayout(new GridLayout(0, 3));
		
		for(int i = 0; i< edits.size(); i++) {
			if(i % 2 == 0) {
				add(new JLabel("Point" + (i/2 + 1) + ": "));
			}
			
			add(edits.get(i));
		}
		
		add(LineEditor.colorPanel(fArea, "Foreground color: "), BorderLayout.CENTER);
		add(LineEditor.colorPanel(bArea, "Background color: "), BorderLayout.EAST);
	}

	@Override
	public void checkEditing() {
		if(!validDot()) {
			throw new IllegalArgumentException("Sa danim koordinatama ne konvergira.");
		}
		validDistance();
		
		for(int i = 0; i< edits.size(); i++) {
			try {
				Integer.parseInt(edits.get(i).getText());
			}catch(Exception ex) {
				throw new IllegalArgumentException("Trebas dati integere ne nesto trece.");
			}
		}
	}
	
	private void validDistance() {
		List<Point> points = konvPoly.getPoints();
		for(int i = 0;i < points.size(); i++) {
			if(getDistance(points.get(i%points.size()), points.get((i+1)%points.size())) < 3) {
				throw new IllegalArgumentException("Invalid distance now.");
			}
		}
	}
	
	private double getDistance(Point a, Point b) {
		int one = a.x - b.x;
		int other = a.y - b.y;
		
		double distance = Math.sqrt(one*one + other*other);
		return distance;
	}

	public boolean validDot() {
		List<Point> points = konvPoly.getPoints();
		if(points.size() < 4)	return true;
		
		List<Vector3> vectors = new ArrayList<>();
		for(Point p: points) {
			vectors.add(new Vector3(p.x, p.y, 0));
		}
		
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

	@Override
	public void acceptEditing() {
		for(int i = 0; i < edits.size(); i+=2) {
			int x = Integer.parseInt(edits.get(i).getText());
			int y = Integer.parseInt(edits.get(i+1).getText());
			List<Point> pp = konvPoly.getPoints();
			Point org = pp.get(i/2);
			org.x = x;
			org.y = y;
		}
		
		konvPoly.setFColor(fArea.getCurrentColor());
		konvPoly.setBColor(bArea.getCurrentColor());
	}

}
