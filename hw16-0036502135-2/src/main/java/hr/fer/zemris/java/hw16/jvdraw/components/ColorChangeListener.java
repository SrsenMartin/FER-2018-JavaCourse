package hr.fer.zemris.java.hw16.jvdraw.components;

import java.awt.Color;

/**
 * Listener for color change on JColorArea.
 * 
 * @author Martin Sršen
 *
 */
public interface ColorChangeListener {
	/**
	 * Method called whether color changes on JColorArea.
	 * 
	 * @param source	color source.
	 * @param oldColor	color before change.
	 * @param newColor	color after change.
	 */
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor);
}
