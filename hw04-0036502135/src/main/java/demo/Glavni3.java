package demo;

import hr.fer.zemris.lsystems.gui.LSystemViewer;
import hr.fer.zemris.lsystems.impl.LSystemBuilderImpl;

/**
 * Simple program that creates koch curve with LSystem based on prouduction level.
 * 
 * @author Martin Sršen
 *
 */
public class Glavni3 {

	/**
	 * Called when program is started.
	 * Creates GUI used to show any LSystem.
	 * Takes input from text file and outputs picture of curve.
	 * 
	 * @param args	Arguments from command prompt.Not used in this example.
	 */
	public static void main(String[] args) {
		LSystemViewer.showLSystem(LSystemBuilderImpl::new);
	}
	
}
