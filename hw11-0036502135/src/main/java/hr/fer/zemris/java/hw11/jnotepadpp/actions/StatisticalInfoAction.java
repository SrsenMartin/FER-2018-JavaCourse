package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import hr.fer.zemris.java.hw11.jnotepadpp.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Action that extends from LocalizableAction.
 * Writes statistical info to user onto JOptionPane.
 * Statistical info contains from character length, non blank character length and
 * number of lines.
 * Mnemonic key set to letter I.
 * Accelerator key: control + I.
 * 
 * @author Martin Sršen
 *
 */
public class StatisticalInfoAction extends LocalizableAction {

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
	public StatisticalInfoAction(MultipleDocumentModel documentModel, String nameKey, String descKey, ILocalizationProvider lp) {
		super(nameKey, descKey, lp);
		provider = lp;
		this.documentModel = documentModel;
		Util.createSingleAction(this, "control I", KeyEvent.VK_I);
	}

	 /**
	  * Called every time actions is called.
	  * Writes statistical info to user onto JOptionPane.
	  * Statistical info contains from character length, non blank character length and
	  * number of lines.
	  * 
	  * @param event	Details about occurred event.
	  */
	@Override
	public void actionPerformed(ActionEvent event) {
		JTextArea editor = documentModel.getCurrentDocument().getTextComponent();
		String text = editor.getText();
		if(text == null)	return;
		
		int charLength = text.length();
		int nonBlankLength = text.replaceAll("\n|\t| ", "").length();
		int lines = editor.getLineCount();
		
		String msg = provider.getString("docHas") + " " + charLength + " " + provider.getString("characters") + ", " +
				nonBlankLength + " " + provider.getString("nonBlankAnd") + " " + lines + " " + provider.getString("lines") + ".";
		JOptionPane.showMessageDialog(null, msg, provider.getString("statisticalInfo"), JOptionPane.INFORMATION_MESSAGE);
	}
}
