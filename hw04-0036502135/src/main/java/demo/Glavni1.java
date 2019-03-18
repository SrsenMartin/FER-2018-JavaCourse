package demo;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilderProvider;
import hr.fer.zemris.lsystems.gui.LSystemViewer;
import hr.fer.zemris.lsystems.impl.LSystemBuilderImpl;

/**
 * Simple program that creates koch curve with LSystem based on prouduction level.
 * 
 * @author Martin Sršen
 *
 */
public class Glavni1 {

	/**
	 * Called when program is started.
	 * Creates GUI used to show koch curve.
	 * 
	 * @param args	Arguments from command prompt.Not used in this example.
	 */
	public static void main(String[] args) {
		LSystemViewer.showLSystem(createKochCurve(LSystemBuilderImpl::new));
	}

	/**
	 * Creates LSystem using LSystemBuilderProvider that knows how to create it
	 * by calling method that will initialise its state.
	 * 
	 * @param provider	LSystem builder that know how to create LSystem based on input.
	 * @return	LSystem used to build koch curve.
	 */
	private static LSystem createKochCurve(LSystemBuilderProvider provider) {
		return provider.createLSystemBuilder()
		.registerCommand('F', "draw 1")
		.registerCommand('+', "rotate 60")
		.registerCommand('-', "rotate -60")
		.setOrigin(0.05, 0.4)
		.setAngle(0)
		.setUnitLength(0.9)
		.setUnitLengthDegreeScaler(1.0/3.0)
		.registerProduction('F', "F+F--F+F")
		.setAxiom("F")
		.build();
	}

}
