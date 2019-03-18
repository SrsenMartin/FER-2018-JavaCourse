package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Abstract class that implements from ILocalizationProvider.
 * Defines methods that are shared by all classes that implement ILocalizationProvider.
 * Defnes method fire that will notify all registered listeners that change of 
 * language occurred.
 * 
 * @author Martin Sršen
 *
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

	/**
	 * Set of registered listeners on this object.
	 */
	private Set<ILocalizationListener> listeners = new HashSet<>();
	
	/**
	 * Adds localization listener.
	 * 
	 * @param listener	listener to add.
	 */
	@Override
	public void addLocalizationListener(ILocalizationListener listener) {
		if(listener == null)	return;
		
		listeners.add(listener);
	}
	
	/**
	 * Removes localization listener.
	 * 
	 * @param listener	listener to remove.
	 */
	@Override
	public void removeLocalizationListener(ILocalizationListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Method used to notify registered listeners that language change occurred.
	 */
	public void fire() {
		List<ILocalizationListener> copy = new ArrayList<>(listeners);
		copy.forEach(listener -> listener.localizationChanged());
	}
}
