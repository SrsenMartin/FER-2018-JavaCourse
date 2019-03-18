package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Class representing bridge between localization changed listeners and
 * localization provider object.
 * Only this class will listen to localizatioProvider and will fire notify as
 * soon as it gets it from LocalizationProvider.
 * Used so program can end when all threads die.
 * No memory leak.
 * 
 * @author Martin Sršen
 *
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {

	/**
	 * stores LocalizationProvider object.
	 */
	private ILocalizationProvider provider;
	/**
	 * Listener on LocalizationProvider object.
	 */
	private ILocalizationListener listener;
	/**
	 * Checks whether bridge is connected.
	 */
	private boolean connected;
	/**
	 * Last set language.
	 */
	private String language;
	
	/**
	 * Constructor that takes provider through constructor.
	 * Creates listener.
	 * 
	 * @param provider	LocalizationProvider object.
	 */
	public LocalizationProviderBridge(ILocalizationProvider provider) {
		this.provider = provider;
		language = getCurrentLanguage();
		
		listener = () -> {
			language = getCurrentLanguage();
			fire();
		};
	}
	
	/**
	 * Connects itself to LocalizationProvider list of listeners.
	 * Creates bridge.
	 */
	public void connect() {
		if(connected)	return;
		if(language != getCurrentLanguage()) {
			language = getCurrentLanguage();
			fire();
		}
		
		provider.addLocalizationListener(listener);
		connected = true;
	}
	
	/**
	 * Disconnects itself from LocalizationProvider list of listeners.
	 * Destroys bridge.
	 */
	public void disconnect() {
		if(!connected)	return;
		
		provider.removeLocalizationListener(listener);
		connected = false;
	}
	
	/**
	 * Returns string word on current language
	 * saved by given key.
	 * 
	 * @param key	Given key.
	 * @return	String saved under given key on current language.
	 */
	public String getString(String key) {
		return provider.getString(key);
	}
	
	/**
	 * Getter for current language set in LocalizationProvider.
	 * 
	 * @return	current language set in LocalizationProvider.
	 */
	private String getCurrentLanguage() {
		return LocalizationProvider.getInstance().getLanguage();
	}
}
