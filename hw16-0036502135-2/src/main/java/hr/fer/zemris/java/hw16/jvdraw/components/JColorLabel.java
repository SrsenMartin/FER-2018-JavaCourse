package hr.fer.zemris.java.hw16.jvdraw.components;

import java.awt.Color;

import javax.swing.JLabel;

/**
 * JLabel used to show currently selected
 * colors rgb values.
 * 
 * @author Martin Sršen
 *
 */
public class JColorLabel extends JLabel {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Foreground color text representation.
	 */
	private String fgText;
	/**
	 * Background color text representation.
	 */
	private String bgText;
	
	/**
	 * Default constructor that takes IColorProviders of current colors.
	 * 
	 * @param fgColorProvider	IColorProvider of foregroundColor.
	 * @param bgColorProvider	IColorProvider of backgroundColor.
	 */
	public JColorLabel(IColorProvider fgColorProvider, IColorProvider bgColorProvider) {
		fgColorProvider.addColorChangeListener((source, oldColor, newColor) -> {
			fgText = colorString(newColor);
			refreshText();
		});
		
		bgColorProvider.addColorChangeListener((source, oldColor, newColor) -> {
			bgText = colorString(newColor);
			refreshText();
		});
		
		fgText = colorString(fgColorProvider.getCurrentColor());
		bgText = colorString(bgColorProvider.getCurrentColor());
		refreshText();
	}
	
	/**
	 * Method called each time any color changes.
	 */
	private void refreshText() {
		this.setText("Foreground color: " + fgText + ", background color: " + bgText + ".");
	}
	
	/**
	 * Method that returns string representation of given color.
	 * 
	 * @param color	Color used to get string representation from.
	 * @return	String representation of given color.
	 */
	private String colorString(Color color) {
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();
		
		return String.format("(%d, %d, %d)", red, green, blue);
	}
}
