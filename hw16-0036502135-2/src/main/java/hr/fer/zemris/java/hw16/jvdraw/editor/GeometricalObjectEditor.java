package hr.fer.zemris.java.hw16.jvdraw.editor;

import javax.swing.JPanel;

/**
 * Base class for each geometrical object editor.
 * 
 * @author Martin Sršen
 *
 */
public abstract class GeometricalObjectEditor extends JPanel {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Checks whether given data is valid.
	 */
	public abstract void checkEditing();
	/**
	 * Saves changes into geometrical object.
	 */
	public abstract void acceptEditing();
}
