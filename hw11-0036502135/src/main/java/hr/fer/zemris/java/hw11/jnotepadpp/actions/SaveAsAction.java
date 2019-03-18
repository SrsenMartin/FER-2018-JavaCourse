package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.nio.file.Path;

import hr.fer.zemris.java.hw11.jnotepadpp.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Action that extends from LocalizableAction.
 * Selects file where document content will be saved,
 * and saves content into it.
 * Mnemonic key set to letter A.
 * Accelerator key: control + alt + S.
 * 
 * @author Martin Sršen
 *
 */
public class SaveAsAction extends LocalizableAction {

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
	public SaveAsAction(MultipleDocumentModel documentModel, String nameKey, String descKey, ILocalizationProvider lp) {
		super(nameKey, descKey, lp);
		provider = lp;
		this.documentModel = documentModel;
		Util.createSingleAction(this, "control alt S", KeyEvent.VK_A);
	}

	 /**
	  * Called every time actions is called.
	  * Selects file where document content will be saved,
	  * and saves content into it.
	  * 
	  * @param event	Details about occurred event.
	  */
	@Override
	public void actionPerformed(ActionEvent event) {
		Path path = Util.getFileToSave(provider);
		if(path == null)	return;
		
		documentModel.saveDocument(documentModel.getCurrentDocument(), path);
	}
}
