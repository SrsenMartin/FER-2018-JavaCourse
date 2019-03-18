package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import hr.fer.zemris.java.hw11.jnotepadpp.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Action that extends from LocalizableAction.
 * Closes current document and removes tab.
 * If not saved, checks if user wants to save.
 * Mnemonic key set to letter C.
 * Accelerator key: control + W.
 * 
 * @author Martin Sršen
 *
 */
public class CloseDocumentAction extends LocalizableAction {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Document model used to get input and write output.
	 */
	private MultipleDocumentModel documentModel;
	/**
	 * Used to set language based on selected language
	 */
	private ILocalizationProvider provider;
	
	/**
	 * Constructor that creates action.
	 * 
	 * @param documentModel	used to get input and write output.
	 * @param nameKey	key containing action name.
	 * @param descKey	key containing action description.
	 * @param lp	ILocalizationProvider used to set language based on selected language.
	 */
	public CloseDocumentAction(MultipleDocumentModel documentModel, String nameKey, String descKey, ILocalizationProvider lp) {
		super(nameKey, descKey, lp);
		provider = lp;
		this.documentModel = documentModel;
		Util.createSingleAction(this, "control W", KeyEvent.VK_C);
	}

	 /**
	  * Called every time actions is called.
	  * Calls method that will remove current document and tab.
	  * 
	  * @param event	Details about occurred event.
	  */
	@Override
	public void actionPerformed(ActionEvent event) {
		boolean execute = Util.checkToSave(documentModel.getCurrentDocument(), documentModel, provider);
		if(!execute)	return;
		
		documentModel.closeDocument(documentModel.getCurrentDocument());
	}
}
