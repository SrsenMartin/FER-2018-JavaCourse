package hr.fer.zemris.java.gui.calc.buttons;

import java.util.function.DoubleBinaryOperator;

import javax.swing.JButton;
import javax.swing.JCheckBox;

import hr.fer.zemris.java.gui.calc.CalcModel;

/**
 * Class that represents calculator invertible buttons that operates on 2 inputs.
 * Buttons name is determined by checkBox,whether its selected or not,
 * and its action too.
 * When pressed, command executes operation on current calculator value and sets pending binary operation
 * if active operand is set, else initializes active operand and pending binary operation.
 * Button takes name and DoubleBinaryOperation to use when JCheckBox is selected,
 * and other name and DoubleBinaryoperation when it is not selected.
 * 
 * @author Martin Sršen
 *
 */
public class InvertibleOperatorButton extends JButton {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that takes name and DoubleBinaryOperation to use when JCheckBox is selected,
	 * and other name and DoubleBinaryoperation when it is not selected, JCheckBox that is used to
	 * append listeners to it and CalcModel used to store calculation data at.
	 * 
	 * @param nameNormal	Button name when JCheckBox is not selected.
	 * @param nameInverted	Button name when JCheckBox is selected.
	 * @param normal	Button action when JCheckBox is not selected.
	 * @param inverted	Button action when JCheckBox is selected.
	 * @param box	JCheckBox used to listen for its change.
	 * @param model	CalcModel used to store calculator data.
	 */
	public InvertibleOperatorButton(String nameNormal, String nameInverted, DoubleBinaryOperator normal,
			DoubleBinaryOperator inverted, JCheckBox box, CalcModel model) {
		super(nameNormal);
		
		box.addItemListener(event -> setText(box.isSelected() ? nameInverted : nameNormal));
		
		addActionListener(event -> {
			double newActiveOperand = model.getValue();
			
			if(model.isActiveOperandSet()) {
				newActiveOperand = model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue());
			}
			
			model.setPendingBinaryOperation(box.isSelected() ? inverted : normal);
			model.setActiveOperand(newActiveOperand);
			model.clear();
		});
	}
	
}
