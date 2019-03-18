package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class that extends from AbstractLocalizationProvider.
 * Default language is set to English.
 * Class that saves ResourceBungle used to get translted strings.
 * Containes reference to current language.
 * 
 * @author Martin Sršen
 *
 */
public class LocalizationProvider extends AbstractLocalizationProvider {

	private static final String DEFAULT_LANGUAGE = "en";
	
	/**
	 * Resource bundle used to get translated words.
	 */
	private ResourceBundle bundle;
	/**
	 * Current language.
	 */
	private String language;
	
	/**
	 * Instance of this class. Only one can and will exist.
	 */
	private static LocalizationProvider provider = new LocalizationProvider();
	
	/**
	 * Private constructor used so noone can create instance from outside.
	 * Sets language to default.
	 */
	private LocalizationProvider() {
		setLanguage(DEFAULT_LANGUAGE);
	}
	
	/**
	 * Returns provider.
	 * 
	 * @return	provider.
	 */
	public static LocalizationProvider getInstance() {
		return provider;
	}
	
	/**
	 * Sets language to given language
	 * and notifies all listeners.
	 * 
	 * @param language	new language to translate to.
	 */
	public void setLanguage(String language) {
		if(language == null || language.equals(this.language))	return;
		
		this.language = language;
		bundle = ResourceBundle.getBundle("hr.fer.zemris.java.hw11.jnotepadpp.local.translations", new Locale(language));
		fire();
	}
	
	/**
	 * Returns string word on current language
	 * saved by given key.
	 * 
	 * @param key	Given key.
	 * @return	String saved under given key on current language.
	 */
	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}

	/**
	 * Getter method for current language.
	 * 
	 * @return	Current language.
	 */
	public String getLanguage() {
		return language;
	}
}
