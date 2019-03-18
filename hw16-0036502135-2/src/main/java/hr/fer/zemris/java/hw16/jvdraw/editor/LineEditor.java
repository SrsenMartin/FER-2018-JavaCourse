package hr.fer.zemris.java.hw16.jvdraw.editor;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import hr.fer.zemris.java.hw16.jvdraw.components.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.Line;

/**
 * Class used to allow line editing.
 * Panel used to input new line info
 * to apply to selected line.
 * 
 * @author Martin Sršen
 */
public class LineEditor extends GeometricalObjectEditor {

	private static final long serialVersionUID = 1L;

	/**
	 * Editing line.
	 */
	private Line line;
	
	/**
	 * JTextField where start x is shown.
	 */
	private JTextField startX;
	/**
	 * JTextField where start y is shown.
	 */
	private JTextField startY;
	/**
	 * JTextField where end x is shown.
	 */
	private JTextField endX;
	/**
	 * JTextField where end y is shown.
	 */
	private JTextField endY;
	/**
	 * Current foreground color.
	 */
	private JColorArea fColor;
	
	/**
	 * Default constructor that takes editing line.
	 * 
	 * @param line	Line to edit.
	 */
	public LineEditor(Line line) {
		this.line = line;
		
		startX = new JTextField(line.getStart().x + "", 6);
		startY = new JTextField(line.getStart().y + "", 6);
		endX = new JTextField(line.getEnd().x + "", 6);
		endY = new JTextField(line.getEnd().y + "", 6);
		fColor = new JColorArea(line.getColor());
		
		createPanel();
	}

	@Override
	public void checkEditing() {
		checkGivenCoordinates(startX.getText(), startY.getText(), endX.getText(), endY.getText());
	}
	
	@Override
	public void acceptEditing() {
		int sX = Integer.parseInt(startX.getText());
		int sY = Integer.parseInt(startY.getText());
		int eX = Integer.parseInt(endX.getText());
		int eY = Integer.parseInt(endY.getText());
		
		line.setStart(new Point(sX, sY));
		line.setEnd(new Point(eX, eY));
		line.setColor(fColor.getCurrentColor());
	}
	
	/**
	 *	Method that will create editor look.
	 */
	private void createPanel() {
		setLayout(new BorderLayout());
		
		add(coordinatesPanel(startX, startY, "Start coordinates:"), BorderLayout.WEST);
		add(coordinatesPanel(endX, endY, "End coordinates:"), BorderLayout.CENTER);
		add(colorPanel(fColor, "Color:"), BorderLayout.EAST);
	}
	
	/**
	 * Method that creates JPanel for editing line start and end coordinates.
	 * 
	 * @param x	JTextField where start or end x is shown.
	 * @param y	JTextField where start or end y is shown.
	 * @param name	Column name.
	 * @return	JPanel for editing start or end of line.
	 */
	public static JPanel coordinatesPanel(JTextField x, JTextField y, String name) {
		JPanel p1 = new JPanel(new GridLayout(4,0));
		p1.add(new JLabel(name + "      "));
		JPanel s = new JPanel();
		s.add(new JLabel("X: "));
		s.add(x);
		p1.add(s);
		JPanel s2 = new JPanel();
		s2.add(new JLabel("Y: "));
		s2.add(y);
		p1.add(s2);
		
		return p1;
	}
	
	/**
	 * Method that creates JPanel for editing color.
	 * 
	 * @param colorArea	Component used to change color.
	 * @param name	Column name.
	 * @return	JPanel for editing color.
	 */
	public static JPanel colorPanel(JColorArea colorArea, String name) {
		JPanel p3 = new JPanel(new GridLayout(2, 0));
		
		p3.add(new JLabel(name + "      "));
		p3.add(colorArea);
		
		return p3;
	}
	
	/**
	 * Method that checks given coordinates validation.
	 * 
	 * @param startX	Start point x.
	 * @param startY	Start point y.
	 * @param endX	End point x.
	 * @param endY	End point y.
	 * @throws IllegalArgumentException	if invalid coordinates are given.
	 */
	public static void checkGivenCoordinates(String startX, String startY, String endX, String endY) {
		int sX = 0;
		int sY = 0;
		int eX = 0;
		int eY = 0;
		try {
			sX = Integer.parseInt(startX);
			sY = Integer.parseInt(startY);
			eX = Integer.parseInt(endX);
			eY = Integer.parseInt(endY);
		}catch(Exception ex) {
			throw new IllegalArgumentException("Coordinates must be numbers.");
		}
		
		if(sX <= -10000 || sY <= -10000 || eX <= -10000 || eY <= -10000 ||
				sX >= 10000 || sY >= 10000 || eX >= 10000 || eY >= 10000) {
			throw new IllegalArgumentException("Width and height must be in range <-10000, 10000>");
		}
	}
}
