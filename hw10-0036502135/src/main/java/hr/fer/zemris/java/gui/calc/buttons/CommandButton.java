package hr.fer.zemris.java.gui.calc.buttons;

import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * Class that represents calculator command buttons.
 * Each command button takes its name and action to execute when pressed.
 * 
 * @author Martin Sršen
 *
 */
public class CommandButton extends JButton {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that takes command button name 
	 * and listener that represents action to perform when
	 * command button is clicked.
	 * 
	 * @param name	Command button name.
	 * @param listener	Command button action when button is clicked.
	 */
	public CommandButton(String name, ActionListener listener) {
		super(name);
		
		addActionListener(listener);
	}
	
}
