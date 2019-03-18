package hr.fer.zemris.java.hw16.jvdraw.editor;

import java.awt.BorderLayout;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import hr.fer.zemris.java.hw16.jvdraw.components.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.Circle;

/**
 * Class used to allow circle editing.
 * Panel used to input new circle info
 * to apply to selected circle.
 * 
 * @author Martin Sršen
 */
public class CircleEditor extends GeometricalObjectEditor {

	private static final long serialVersionUID = 1L;

	/**
	 * Editing circle.
	 */
	private Circle circle;
	
	/**
	 * JTextField where center x is shown.
	 */
	private JTextField centerX;
	/**
	 * JTextField where center y is shown.
	 */
	private JTextField centerY;
	/**
	 * JTextField where radius is shown.
	 */
	private JTextField radius;
	/**
	 * Current foreground color.
	 */
	private JColorArea fColor;
	
	/**
	 * Default constructor that takes editing circle.
	 * 
	 * @param circle	Circle to edit.
	 */
	public CircleEditor(Circle circle) {
		this.circle = circle;
		
		centerX = new JTextField(circle.getCenter().x + "", 6);
		centerY = new JTextField(circle.getCenter().y + "", 6);
		radius = new JTextField(circle.getRadius() + "", 6);
		
		fColor = new JColorArea(circle.getFColor());
		
		createPanel();
	}
	
	@Override
	public void checkEditing() {
		checkCirclePosition(centerX.getText(), centerY.getText(), radius.getText());
	}

	@Override
	public void acceptEditing() {
		int cX = Integer.parseInt(centerX.getText());
		int cY = Integer.parseInt(centerY.getText());
		int r = Integer.parseInt(radius.getText());
		
		circle.setCenter(new Point(cX, cY));
		circle.setRadius(r);
		circle.setfColor(fColor.getCurrentColor());
	}
	
	/**
	 *	Method that will create editor look.
	 */
	private void createPanel() {
		setLayout(new BorderLayout());
		
		add(circleFullSpec(centerX, centerY, radius, "Coordinates:  "), BorderLayout.WEST);
		add(LineEditor.colorPanel(fColor, "Foreground color: "), BorderLayout.EAST);
	}
	
	/**
	 * Method that creates JPanel for editing center and radius of circle.
	 * 
	 * @param x	JTextField where center x is shown.
	 * @param y	JTextField where center y is shown.
	 * @param radius	JTextField where radius is shown.
	 * @param name	Column name.
	 * @return	JPanel for editing center and radius of circle.
	 */
	public static JPanel circleFullSpec(JTextField x, JTextField y,JTextField radius, String name) {
		JPanel coordsPanel = LineEditor.coordinatesPanel(x, y, name);
		
		JPanel rad = new JPanel();
		rad.add(new JLabel("Radius: "));
		rad.add(radius);
		coordsPanel.add(rad);
		
		return coordsPanel;
	}
	
	/**
	 * Method that checks if valid circle data is inserted.
	 * 
	 * @param centerX	Given circle x.
	 * @param centerY	Given circle y.
	 * @param radius	Given circle radius.
	 */
	public static void checkCirclePosition(String centerX, String centerY, String radius) {
		int cX = 0;
		int cY = 0;
		int r = 0;
		try {
			cX = Integer.parseInt(centerX);
			cY = Integer.parseInt(centerY);
			r = Integer.parseInt(radius);
		}catch(Exception ex) {
			throw new IllegalArgumentException("Coordinates must be numbers.");
		}
		
		if(cX <= -10000 || cY <= -10000 || r <= 0 ||
				cX >= 10000 || cY >= 10000 || r >= 1000) {
			throw new IllegalArgumentException("Width must be in range <-10000, 10000>, height <-10000, 10000> and radius <0, 1000>");
		}
	}
}
