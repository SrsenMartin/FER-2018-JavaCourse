package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Intarface representing listener on
 * localization change, language change.
 * 
 * @author Martin Sršen
 *
 */
public interface ILocalizationListener {
	/**
	 * Called when localization,language changes.
	 */
	void localizationChanged();
}
