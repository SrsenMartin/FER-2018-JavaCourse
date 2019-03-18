package hr.fer.zemris.java.gui.calc;

import static java.lang.Math.E;
import static java.lang.Math.acos;
import static java.lang.Math.asin;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.log;
import static java.lang.Math.log10;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.tan;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.calc.buttons.CommandButton;
import hr.fer.zemris.java.gui.calc.buttons.DigitButton;
import hr.fer.zemris.java.gui.calc.buttons.InvertibleButton;
import hr.fer.zemris.java.gui.calc.buttons.InvertibleOperatorButton;
import hr.fer.zemris.java.gui.calc.buttons.OperatorButton;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

/**
 * Program that represents Calculator that works as standard Windows XP calculator.
 * Numbers are inserted by pressing buttons with numbers.
 * Component at position 1,1 is screen where current number is displayed.
 * Input through keyboard is not supported.
 * Calculator does not support brackets.
 * 
 * @author Martin Sršen
 *
 */
public class Calculator extends JFrame {

	private static final long serialVersionUID = 1L;
	
	/**
	 * CalcModel used to store calculator data.
	 */
	private CalcModel model;
	/**
	 * Stack where pushed numbers are saved.
	 */
	private Stack<Double> stack;
	
	/**
	 * Default constructor that initializes window size,
	 * sets title and location.
	 * Calls method to initialize whole calculator GUI.
	 */
	public Calculator() {
		setLocation(500, 200);
		setSize(600, 370);
		setTitle("Calculator");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		model = new CalcModelImpl();
		stack = new Stack<>();
		
		initGUI();
	}

	/**
	 * Method that adds all components into window.
	 * Creates all GUI.
	 * Gap between buttons is set to 10.
	 */
	private void initGUI() {
		Container container = getContentPane();
		container.setLayout(new CalcLayout(10));
		
		addScreen(container);
		addDigits(container);
		addOperators(container);
		addComands(container);
		JCheckBox box = addCheckBox(container);
		addInvertible(container, box);
	}

	/**
	 * Creates and adds screen into container at location 1,1.
	 * Screen text font set to serif, bold, size 32.
	 * Background color set to orange.
	 * Used to show current number.
	 * 
	 * @param container	Component to add calculator components into.
	 */
	private void addScreen(Container container) {
		JLabel screen = new JLabel("0");
		screen.setHorizontalAlignment(SwingConstants.RIGHT);
		screen.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		screen.setOpaque(true);
		screen.setBackground(Color.ORANGE);
		screen.setFont(new Font("serif", Font.BOLD, 32));
		
		model.addCalcValueListener(model -> screen.setText(model.toString()));
		
		container.add(screen, "1,1");
	}
	
	/**
	 * Creates and adds invertible buttons
	 * that do operation only on currently displayed value into container.
	 * Each invertible button has 2 states and actions, when given JCheckBox is selected,
	 * and when it is not selected.
	 * 
	 * @param container	Component to add calculator components into.
	 * @param box	JCheckBox used to determine current button state and action.
	 */
	private void addInvertible(Container container, JCheckBox box) {
		container.add(new InvertibleButton("sin", "asin", x -> sin(x), x -> asin(x), box, model), "2,2");
		container.add(new InvertibleButton("cos", "acos", x -> cos(x), x -> acos(x), box, model), "3,2");
		
		container.add(new InvertibleButton("tan", "atan", x -> tan(x), x -> atan(x), box, model), "4,2");
		container.add(new InvertibleButton("ctg", "actg", x -> 1./tan(x), x -> 1./atan(x), box, model), "5,2");
		
		container.add(new InvertibleButton("log", "10^", x -> log10(x), x -> pow(10, x), box, model), "3,1");
		container.add(new InvertibleButton("ln", "e^", x -> log(x), x -> pow(E, x), box, model), "4,1");
		
		container.add(new InvertibleOperatorButton("x^n", "nsqrtx", (x, n) -> pow(x, n), (x, n) -> pow(x, 1./n), box, model), "5,1");
	}
	
	/**
	 * Creates and adds JCheckBox into container.
	 * It is used to determine invertible buttons states.
	 * Background color set to light blue.
	 * 
	 * @param container	Component to add calculator components into.
	 * @return	created and added JCheckBox.
	 */
	private JCheckBox addCheckBox(Container container) {
		JCheckBox box = new JCheckBox("Inv");
		box.setHorizontalAlignment(SwingConstants.CENTER);
		box.setBackground(new Color(180, 210, 255));
		container.add(box, "5,7");
		
		return box;
	}
	
	/**
	 * Creates and adds buttons that do commands onto calculator state into container.
	 * 
	 * @param container	Component to add calculator components into.
	 */
	private void addComands(Container container) {
		container.add(new CommandButton("=", event -> {
			if(!model.isActiveOperandSet())	return;
			
			model.setValue(model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue()));
			model.clearActiveOperand();
		}), "1,6");
		
		container.add(new CommandButton("clr", event -> model.clear()), "1,7");
		container.add(new CommandButton("res", event -> model.clearAll()), "2,7");
		container.add(new CommandButton("push", event -> stack.push(model.getValue())), "3,7");
		container.add(new CommandButton("+/-", event -> model.swapSign()), "5,4");
		container.add(new CommandButton(".", Event -> model.insertDecimalPoint()), "5,5");
		
		container.add(new CommandButton("pop", event -> {
			if(stack.isEmpty())	{
				JOptionPane.showMessageDialog(this, "Stack is empty.", "Error" , JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			model.setValue(stack.pop());
		}), "4,7");
		
		container.add(new CommandButton("1/x", event -> model.setValue(1/model.getValue())), "2,1");
	}
	
	/**
	 * Creates and adds buttons that are used to insert operators
	 * between 2 given number values into container.
	 * 
	 * @param container	Component to add calculator components into.
	 */
	private void addOperators(Container container) {
		container.add(new OperatorButton("/", (a, b) -> a/b, model), "2,6");
		container.add(new OperatorButton("*", (a, b) -> a*b, model), "3,6");
		container.add(new OperatorButton("-", (a, b) -> a-b, model), "4,6");
		container.add(new OperatorButton("+", (a, b) -> a+b, model), "5,6");
	}
	
	/**
	 * Creates and adds buttons that represents digits used to apply
	 * numbers at the end of current value showed on screen into container.
	 * 
	 * @param container	Component to add calculator components into.
	 */
	private void addDigits(Container container) {
		int counter = 0;
		
		for(int row = 5; row > 1; row--) {
			for(int column = 3; column < 6; column++) {
				if(row == 5 && column == 4)	break;
				
				container.add(new DigitButton(Integer.toString(counter), model), new RCPosition(row, column));
				
				counter++;
			}
		}
	}
	
	/**
	 * Called when program is started.
	 * 
	 * @param args	Arguments from command prompt.Not used in this example.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Calculator().setVisible(true));
	}
}
