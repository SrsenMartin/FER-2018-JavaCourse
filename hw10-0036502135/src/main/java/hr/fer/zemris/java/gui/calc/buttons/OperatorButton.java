package hr.fer.zemris.java.gui.calc.buttons;

import java.util.function.DoubleBinaryOperator;

import javax.swing.JButton;

import hr.fer.zemris.java.gui.calc.CalcModel;

/**
 * Class that represents calculator operator buttons.
 * Each operator button takes its name, DoubleBinaryOperator
 * and model used to store calculator data at.
 * Buttons used to perform calculations on data when clicked,
 * or store current value in active value if active value is
 * not initialized.
 * 
 * @author Martin Sršen
 *
 */
public class OperatorButton extends JButton {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that takes operator button name, DoubleBinaryOperator used to perform operation
	 * on numbers and model used to store calculation data at.
	 * 
	 * @param name	Digit button name.
	 * @param operator	DoubleBinaryOperator used to perform operation on numbers.
	 * @param model	CalcModel used to store calculator data.
	 */
	public OperatorButton(String name, DoubleBinaryOperator operator, CalcModel model) {
		super(name);
		
		addActionListener(event -> {
			double newActiveOperand = model.getValue();
			
			if(model.isActiveOperandSet()) {
				newActiveOperand = model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue());
			}
			
			model.setPendingBinaryOperation(operator);
			model.setActiveOperand(newActiveOperand);
			model.clear();
		});
	}
}
