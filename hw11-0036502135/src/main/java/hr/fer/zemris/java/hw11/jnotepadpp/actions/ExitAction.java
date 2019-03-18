package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import hr.fer.zemris.java.hw11.jnotepadpp.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Action that extends from LocalizableAction.
 * Closes main frame when called.
 * If not saved, checks if user wants to save unsaved documents.
 * Mnemonic key set to letter X.
 * Accelerator key: alt + F4.
 * 
 * @author Martin Sršen
 *
 */
public class ExitAction extends LocalizableAction {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Document model used to get input and write output.
	 */
	private MultipleDocumentModel documentModel;
	/**
	 * Frame to close when action is called.
	 */
	private JFrame window;
	/**
	 * Used to set language based on selected language
	 */
	private ILocalizationProvider provider;
	
	/**
	 * Constructor that creates action.
	 * 
	 * @param documentModel	used to get input and write output.
	 * @param window	frame to close when action is called.
	 * @param nameKey	key containing action name.
	 * @param descKey	key containing action description.
	 * @param lp	ILocalizationProvider used to set language based on selected language.
	 */
	public ExitAction(MultipleDocumentModel documentModel, JFrame window, String nameKey, String descKey, ILocalizationProvider lp) {
		super(nameKey, descKey, lp);
		provider = lp;
		this.documentModel = documentModel;
		this.window = window;
		Util.createSingleAction(this, "alt F4", KeyEvent.VK_X);
	}

	 /**
	  * Called every time actions is called.
	  * Calls method that will dispose given frame and ask if user
	  * wants to save unsaved documents.
	  * 
	  * @param event	Details about occurred event.
	  */
	@Override
	public void actionPerformed(ActionEvent event) {
		boolean on = true;
		
		for(SingleDocumentModel model : documentModel) {
			on = Util.checkToSave(model, documentModel, provider);
			
			if(!on)	break;
		}
		
		if(on)	window.dispose();
	}
}
