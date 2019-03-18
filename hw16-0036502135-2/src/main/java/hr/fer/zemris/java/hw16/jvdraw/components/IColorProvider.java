package hr.fer.zemris.java.hw16.jvdraw.components;

import java.awt.Color;

/**
 * Provider interface used to
 * get current color and add and remove listeners.
 * 
 * @author Martin Sršen
 *
 */
public interface IColorProvider {
	/**
	 * Getter for current color in color provider.
	 * 
	 * @return	current color.
	 */
	 public Color getCurrentColor();
	 /**
	  * Used to add color change listener.
	  * 
	  * @param l	ColorChangeListener to add.
	  */
	 public void addColorChangeListener(ColorChangeListener l);
	 /**
	  * Used to remove color change listener.
	  * 
	  * @param l	ColorChangeListener to remove.
	  */
	 public void removeColorChangeListener(ColorChangeListener l);
}
