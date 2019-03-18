package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import hr.fer.zemris.java.hw11.jnotepadpp.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.Util.SelectedTextAction;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Action that extends from LocalizableAction.
 * Caps selected text letter case.
 * Action disabled if no text is selected.
 * Mnemonic key set to letter U.
 * Accelerator key: control + U.
 * 
 * @author Martin Sršen
 *
 */
public class ToUpperCaseAction extends LocalizableAction {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Document model used to get input and write output.
	 */
	private MultipleDocumentModel documentModel;

	/**
	 * Constructor that creates action.
	 * 
	 * @param documentModel	used to get input and write output.
	 * @param nameKey	key containing action name.
	 * @param descKey	key containing action description.
	 * @param lp	ILocalizationProvider used to set language based on selected language.
	 */
	public ToUpperCaseAction(MultipleDocumentModel documentModel, String nameKey, String descKey, ILocalizationProvider lp) {
		super(nameKey, descKey, lp);
		this.documentModel = documentModel;
		Util.createSingleAction(this, "control U", KeyEvent.VK_U);
	}

	 /**
	  * Called every time actions is called.
	  * Calls method that will caps selected text letter case.
	  * 
	  * @param event	Details about occurred event.
	  */
	@Override
	public void actionPerformed(ActionEvent event) {
		Util.executeTextAction(documentModel, SelectedTextAction.TO_UPPERCASE);
	}
}
