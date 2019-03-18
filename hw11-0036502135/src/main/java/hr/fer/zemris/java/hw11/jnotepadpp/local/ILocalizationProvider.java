package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * ILocalizationProvider interface that defines methods
 * that adds and removes localization listeners.
 * Has method getString that returns string word on current language
 * saved by given key.
 * 
 * @author Martin Sršen
 *
 */
public interface ILocalizationProvider {
	/**
	 * Adds localization listener.
	 * 
	 * @param listener	listener to add.
	 */
	void addLocalizationListener(ILocalizationListener listener);
	/**
	 * Removes localization listener.
	 * 
	 * @param listener	listener to remove.
	 */
	void removeLocalizationListener(ILocalizationListener listener);
	/**
	 * Returns string word on current language
	 * saved by given key.
	 * 
	 * @param key	Given key.
	 * @return	String saved under given key on current language.
	 */
	String getString(String key);
}
