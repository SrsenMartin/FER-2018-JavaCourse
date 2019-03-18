package hr.fer.zemris.java.gui.calc.buttons;

import javax.swing.JButton;

import hr.fer.zemris.java.gui.calc.CalcModel;

/**
 * Class that represents calculator digit buttons.
 * Each digit button takes its name,number, and CalcModel.
 * When digit button is pressed, digit is append to current calculator value.
 * 
 * @author Martin Sršen
 *
 */
public class DigitButton extends JButton {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor that takes digit button name 
	 * and model used to store calculator data at.
	 * 
	 * @param name	Digit button name.
	 * @param model CalcModel used to store calculator data.
	 */
	public DigitButton(String name, CalcModel model) {
		super(name);
		
		addActionListener(event -> model.insertDigit(Integer.parseInt(getActionCommand())));
	}
	
}
