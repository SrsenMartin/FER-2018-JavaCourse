package hr.fer.zemris.java.hw16.jvdraw.editor;

import java.awt.BorderLayout;
import java.awt.Point;

import javax.swing.JTextField;

import hr.fer.zemris.java.hw16.jvdraw.components.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.FilledCircle;

/**
 * Class used to allow filled circle editing.
 * Panel used to input new filled circle info
 * to apply to selected filled circle.
 * 
 * @author Martin Sršen
 */
public class FilledCircleEditor extends GeometricalObjectEditor {

	private static final long serialVersionUID = 1L;

	/**
	 * Editing filled circle.
	 */
	private FilledCircle fCircle;
	
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
	 * Current background color.
	 */
	private JColorArea bColor;
	
	/**
	 * Default constructor that takes editing filled circle.
	 * 
	 * @param fCircle	FilledCircle to edit.
	 */
	public FilledCircleEditor(FilledCircle fCircle) {
		this.fCircle = fCircle;
		
		centerX = new JTextField(fCircle.getCenter().x + "", 6);
		centerY = new JTextField(fCircle.getCenter().y + "", 6);
		radius = new JTextField(fCircle.getRadius() + "", 6);
		
		fColor = new JColorArea(fCircle.getFColor());
		bColor = new JColorArea(fCircle.getBColor());
		
		createPanel();
	}
	
	@Override
	public void checkEditing() {
		CircleEditor.checkCirclePosition(centerX.getText(), centerY.getText(), radius.getText());
	}

	@Override
	public void acceptEditing() {
		int cX = Integer.parseInt(centerX.getText());
		int cY = Integer.parseInt(centerY.getText());
		int r = Integer.parseInt(radius.getText());
		
		fCircle.setCenter(new Point(cX, cY));
		fCircle.setRadius(r);
		fCircle.setfColor(fColor.getCurrentColor());
		fCircle.setbColor(bColor.getCurrentColor());
	}

	/**
	 *	Method that will create editor look.
	 */
	private void createPanel() {
		setLayout(new BorderLayout());

		add(CircleEditor.circleFullSpec(centerX, centerY, radius, "Coordinates:  "), BorderLayout.WEST);
		add(LineEditor.colorPanel(fColor, "Foreground color: "), BorderLayout.CENTER);
		add(LineEditor.colorPanel(bColor, "Background color: "), BorderLayout.EAST);
	}
}
