package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import javax.swing.AbstractAction;
import javax.swing.Action;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Class that extends from AbstractAction.
 * Class is itself abstract.
 * Used to make it easier to allow multi-language action names.
 * 
 * @author Martin Sršen
 *
 */
public abstract class LocalizableAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that creates action name and description with given key and provider.
	 * Adds localizationListener onto action that will change name if language changes.
	 * 
	 * @param nameKey	key containing action name.
	 * @param descKey	key containing action description.
	 * @param lp	ILocalizationProvider used to set language based on selected language.
	 */
	public LocalizableAction(String nameKey, String descKey, ILocalizationProvider lp) {
		putValue(Action.NAME, lp.getString(nameKey));
		putValue(Action.SHORT_DESCRIPTION, lp.getString(descKey));
		
		lp.addLocalizationListener(() -> {
			putValue(Action.NAME, lp.getString(nameKey));
			putValue(Action.SHORT_DESCRIPTION, lp.getString(descKey));
		});
	}
}
