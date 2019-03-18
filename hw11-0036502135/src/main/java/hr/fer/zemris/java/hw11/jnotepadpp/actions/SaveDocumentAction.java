package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.nio.file.Path;

import hr.fer.zemris.java.hw11.jnotepadpp.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Action that extends from LocalizableAction.
 * Saves altered content into file that was loaded previously.
 * If no file was loaded it behaves like save As.
 * Mnemonic key set to letter S.
 * Accelerator key: control + S.
 * 
 * @author Martin Sršen
 *
 */
public class SaveDocumentAction extends LocalizableAction {

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
	public SaveDocumentAction(MultipleDocumentModel documentModel, String nameKey, String descKey, ILocalizationProvider lp) {
		super(nameKey, descKey, lp);
		provider = lp;
		this.documentModel = documentModel;
		Util.createSingleAction(this, "control S", KeyEvent.VK_S);
	}

	 /**
	  * Called every time actions is called.
	  * Saves altered content into file that was loaded previously.
	  * If no file was loaded it behaves like save As.
	  * 
	  * @param event	Details about occurred event.
	  */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Path pathToSave = null;
		if(documentModel.getCurrentDocument().getFilePath() == null) {
			pathToSave = Util.getFileToSave(provider);
			if(pathToSave == null)	return;
		}
		
		documentModel.saveDocument(documentModel.getCurrentDocument(), pathToSave);
	}
}
