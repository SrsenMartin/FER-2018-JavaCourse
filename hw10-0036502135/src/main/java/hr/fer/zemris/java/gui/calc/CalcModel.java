package hr.fer.zemris.java.gui.calc;

import java.util.function.DoubleBinaryOperator;

/**
 * Interface representing calculator model of data.
 * It is used to store calculator data and get them when needed.
 * 
 * @author Martin Sršen
 *
 */
public interface CalcModel {
	/**
	 * Method used to add listener to CalcModel current value.
	 * 
	 * @param l	Listener that represents action to perform when current value changes.
	 */
	void addCalcValueListener(CalcValueListener l);
	/**
	 * Method used to remove listener from CalcModel current value.
	 * 
	 * @param l	Listener that represents action to perform when value changes.
	 */
	void removeCalcValueListener(CalcValueListener l);
	/**
	 * Returns value as string representation of CalcModel.
	 * 
	 * @return	value as String representing CalcModel.
	 */
	String toString();
	/**
	 * Used to return current value of CalcModel.
	 * 
	 * @return	current value.
	 */
	double getValue();
	/**
	 * Used to set current value of CalcModel.
	 * 
	 * @param value	current value to set.
	 */
	void setValue(double value);
	/**
	 * Sets current value to null.
	 */
	void clear();
	/**
	 * Sets current value, active value and pending operation to null.
	 */
	void clearAll();
	/**
	 * Used to swap sign of current value.
	 */
	void swapSign();
	/**
	 * Used to insert decimal point if not inserted already.
	 */
	void insertDecimalPoint();
	/**
	 * Used to insert digit at last index of current value.
	 * 
	 * @param digit	digit to append at last index of current value.
	 */
	void insertDigit(int digit);
	/**
	 * Returns whether active operand is different than null.
	 * 
	 * @return	whether active operand is different than null.
	 */
	boolean isActiveOperandSet();
	/**
	 * Returns active operand value.
	 * 
	 * @return	active operand value.
	 */
	double getActiveOperand();
	/**
	 * Sets active operand to given value.
	 * 
	 * @param activeOperand	used to set operand value to.
	 */
	void setActiveOperand(double activeOperand);
	/**
	 * Used to set active operand to null.
	 */
	void clearActiveOperand();
	/**
	 * Getter for pending binary operation value.
	 * 
	 * @return	pending binary operation value.
	 */
	DoubleBinaryOperator getPendingBinaryOperation();
	/**
	 * Setter for pending binary operation value.
	 * 
	 * @param op DoubleBinaryOperator used to set pending binary operation value.
	 */
	void setPendingBinaryOperation(DoubleBinaryOperator op);
}