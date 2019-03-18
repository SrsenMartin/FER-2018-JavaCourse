package hr.fer.zemris.java.gui.calc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

import javax.swing.JOptionPane;

/**
 * Class implementation of CalcModel interface used as calculator model of data.
 * It is used to store calculator data and get them when needed.
 * 
 * @author Martin Sršen
 *
 */
public class CalcModelImpl implements CalcModel {

	/**
	 * Calculator currentValue,displayed onto screen.
	 */
	private String currentValue;
	/**
	 * Calculator active operand,stored waiting for next operand to perform operation.
	 */
	private Double activeOperand;
	/**
	 * Used to perform operation on currentValue and activeOperand.
	 */
	private DoubleBinaryOperator pendingOperation;
	
	/**
	 * List of CalcValueListener listeners.
	 */
	private List<CalcValueListener> listeners;
	
	/**
	 * Method used to add listener to CalcModelImpl current value.
	 * 
	 * @param l	Listener that represents action to perform when currentValue changes.
	 */
	@Override
	public void addCalcValueListener(CalcValueListener l) {
		if(l == null)	return;
		
		if(listeners == null) {
			listeners = new ArrayList<>();
		}
		
		listeners.add(l);
	}

	/**
	 * Method used to remove listener from CalcModel current value.
	 * 
	 * @param l	Listener that represents action to perform when value changes.
	 */
	@Override
	public void removeCalcValueListener(CalcValueListener l) {
		listeners.remove(l);
	}

	/**
	 * Used to return currentValue of CalcModelImpl.
	 * 
	 * @return	currentValue.
	 */
	@Override
	public double getValue() {
		if(currentValue == null)	return 0;
		
		return Double.parseDouble(currentValue);
	}

	/**
	 * Used to set currentValue of CalcModelImpl.
	 * 
	 * @param value	number to set currentValue to.
	 */
	@Override
	public void setValue(double value) {
		if(Double.isInfinite(value)) {
			JOptionPane.showMessageDialog(null, "Cannot accept infinite result.", "Infinite", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		if(Double.isNaN(value)) {
			JOptionPane.showMessageDialog(null, "Invalid fuction domain given.", "Domain", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		currentValue = Double.toString(value);
		
		notifyAllListeners();
	}

	/**
	 * Sets currentValue to null.
	 */
	@Override
	public void clear() {
		currentValue = null;
		
		notifyAllListeners();
	}

	/**
	 * Sets currentValue, activeOperand and pendingOperation to null.
	 */
	@Override
	public void clearAll() {
		currentValue = null;
		activeOperand = null;
		pendingOperation = null;
		
		notifyAllListeners();
	}

	/**
	 * Used to swap sign of currentValue.
	 */
	@Override
	public void swapSign() {
		if(currentValue == null)	return;
		
		if(currentValue.contains("-")) {
			currentValue = currentValue.replaceFirst("-", "");
		}else {
			currentValue = "-" + currentValue;
		}
		
		notifyAllListeners();
	}

	/**
	 * Used to insert decimal point if not inserted already.
	 */
	@Override
	public void insertDecimalPoint() {
		if(currentValue == null) {
			currentValue = "0.";
			
			notifyAllListeners();
			return;
		}
		
		if(currentValue.contains("."))	return;
		
		currentValue += ".";
		
		notifyAllListeners();
	}

	/**
	 * Used to insert digit at last index of currentValue.
	 * 
	 * @param digit	digit to append at last index of currentValue.
	 */
	@Override
	public void insertDigit(int digit) {
		if(currentValue == null || currentValue.equals("0")) {
			currentValue = Integer.toString(digit);
			
			notifyAllListeners();
			return;
		}
		
		String toSave = currentValue + Integer.toString(digit);
		if(Double.isInfinite(Double.parseDouble(toSave)))	return;
		
		currentValue = toSave;
		
		notifyAllListeners();
	}

	/**
	 * Returns whether activeOperand is different than null.
	 * 
	 * @return	whether activeOperand is different than null.
	 */
	@Override
	public boolean isActiveOperandSet() {
		return activeOperand != null;
	}

	/**
	 * Returns activeOperand value.
	 * 
	 * @return	activeOperand value.
	 * @throws IllegalStateException if activeOperand is null.
	 */
	@Override
	public double getActiveOperand() {
		if(activeOperand == null) {
			throw new IllegalStateException("Active operand is not set.");
		}
		
		return activeOperand;
	}

	/**
	 * Sets activeOperand to given value.
	 * 
	 * @param activeOperand	double value used to set activeOperand value to.
	 */
	@Override
	public void setActiveOperand(double activeOperand) {
		this.activeOperand = Double.valueOf(activeOperand);
	}

	/**
	 * Used to set activeOperand to null.
	 */
	@Override
	public void clearActiveOperand() {
		activeOperand = null;
	}

	/**
	 * Getter for pendingOperation value.
	 * 
	 * @return	pendingOperation value.
	 * @throws IllegalStateException if activeOperand is null.
	 */
	@Override
	public DoubleBinaryOperator getPendingBinaryOperation() {
		if(pendingOperation == null) {
			throw new IllegalStateException("Pending operation is not set.");
		}
		
		return pendingOperation;
	}

	/**
	 * Setter for pendingOperation value.
	 * 
	 * @param op DoubleBinaryOperator used to set pendingOperation value.
	 * @throws NullPointerException if given DoubleBinaryOperator is null.
	 */
	@Override
	public void setPendingBinaryOperation(DoubleBinaryOperator op) {
		Objects.requireNonNull(op);
		
		this.pendingOperation = op;
	}
	
	/**
	 * Returns currentValue as string representation of CalcModelImpl.
	 * 
	 * @return	currentValue as String representing CalcModelImpl.
	 */
	@Override
	public String toString() {
		if(currentValue == null)	return "0";
		
		return currentValue;
	}
	
	private void notifyAllListeners() {
		if(listeners == null)	return;
		
		listeners.forEach(listener -> listener.valueChanged(this));
	}
}
