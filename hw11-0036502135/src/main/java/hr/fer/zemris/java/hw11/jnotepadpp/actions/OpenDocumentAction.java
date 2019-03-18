package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.nio.file.Path;

import javax.swing.JFileChooser;

import hr.fer.zemris.java.hw11.jnotepadpp.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Action that extends from LocalizableAction.
 * Opens selected text from file into new tab.
 * Mnemonic key set to letter O.
 * Accelerator key: control + O.
 * 
 * @author Martin Sršen
 *
 */
public class OpenDocumentAction extends LocalizableAction {

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
	public OpenDocumentAction(MultipleDocumentModel documentModel, String nameKey, String descKey, ILocalizationProvider lp) {
		super(nameKey, descKey, lp);
		provider = lp;
		this.documentModel = documentModel;
		Util.createSingleAction(this, "control O", KeyEvent.VK_O);
	}

	 /**
	  * Called every time actions is called.
	  * Opens JFileChooser to select file to open, and after that
	  * calls method that will load text from document.
	  * 
	  * @param event	Details about occurred event.
	  */
	@Override
	public void actionPerformed(ActionEvent event) {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle(provider.getString("open"));
		Path path = Util.getSelectedFile(chooser, chooser.showOpenDialog(null), provider);
		if(path == null)	return;
		
		documentModel.loadDocument(path);
	}
}