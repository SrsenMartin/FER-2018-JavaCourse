package hr.fer.zemris.java.gui.calc;

/**
 * The listener interface for CalcValue. 
 * The class that is interested in currentValue change listening implements this interface,
 * and the object created with that class is registered with a component, using the component's addCalcValueListener method.
 * When currentValue changes, that object's valueChanged method is invoked.
 *   
 * @author Martin Sršen
 *
 */
public interface CalcValueListener {
	
	/**
	 * Invoked when currentValue changes in given model.
	 * 
	 * @param model	CalcModel where change occurred.
	 */
	void valueChanged(CalcModel model);
}