package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import hr.fer.zemris.java.hw11.jnotepadpp.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.Util.SelectedTextAction;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Action that extends from LocalizableAction.
 * Removes duplicate lines from selected text.
 * Mnemonic key set to letter P.
 * Accelerator key: control + P.
 * 
 * @author Martin Sršen
 *
 */
public class UniqueAction extends LocalizableAction {

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
	public UniqueAction(MultipleDocumentModel documentModel, String nameKey, String descKey, ILocalizationProvider lp) {
		super(nameKey, descKey, lp);
		this.documentModel = documentModel;
		Util.createSingleAction(this, "control P", KeyEvent.VK_P);
	}

	 /**
	  * Called every time actions is called.
	  * Calls method that will remove duplicate lines from selected text.
	  * 
	  * @param event	Details about occurred event.
	  */
	@Override
	public void actionPerformed(ActionEvent event) {
		Util.executeTextAction(documentModel, SelectedTextAction.UNIQUE);
	}
}
