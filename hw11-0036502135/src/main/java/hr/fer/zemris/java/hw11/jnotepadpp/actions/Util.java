package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.nio.file.Path;
import java.text.Collator;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Locale;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

/**
 * Class that contains helper methods used to save,load, and do
 * all kinds of things with selected text, sort, remove duplicates,
 * get system clipboard, cut, copy ,paste,...
 * 
 * @author Martin Sršen
 *
 */
public class Util {

	/**
	 * Static method that creates action accelerator key and mnemonic key.
	 * 
	 * @param action	Action used to put accelerator and mnemonic key into.
	 * @param accelerator	Accelerator key.
	 * @param mnemonicKey	Mnemonic key.
	 */
	public static void createSingleAction(Action action, String accelerator, int mnemonicKey) {
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator));
		action.putValue(Action.MNEMONIC_KEY, mnemonicKey);
	}
	
	/**
	 * Static method used to return selected file from JFileChooser.
	 * 
	 * @param fileChooser	JFileChooser used to get file.
	 * @param chooseOption	Choose option to get file.
	 * @param provider	used to set language based on selected language.
	 * @return	file path got from JFileChooser.
	 */
	public static Path getSelectedFile(JFileChooser fileChooser, int chooseOption, ILocalizationProvider provider) {	
		if(chooseOption != JFileChooser.APPROVE_OPTION) {
			JOptionPane.showMessageDialog(null, provider.getString("nonSelected"), provider.getString("information"), JOptionPane.INFORMATION_MESSAGE);
			return null;
		}
		
		return fileChooser.getSelectedFile().toPath();
	}
	
	/**
	 * Static method used to get file to save at.
	 * 
	 * @param provider	used to set language based on selected language.
	 * @return	file path to save document content into.
	 */
	public static Path getFileToSave(ILocalizationProvider provider) {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle(provider.getString("save"));
		return Util.getSelectedFile(chooser, chooser.showSaveDialog(null), provider);
	}
	
	/**
	 * Static method used to check if document is modified and if it is asks
	 * user if he wants to save modified file.
	 * If yes opens JFileChooser to save document at.
	 * 
	 * @param model	SingleDocumentModel representing one document, used to get data from.
	 * @param documentModel	document model used to get input and write output.
	 * @param provider	used to set language based on selected language.
	 * @return	if user gave valid answer.
	 */
	public static boolean checkToSave(SingleDocumentModel model, MultipleDocumentModel documentModel, ILocalizationProvider provider) {		
		if(!model.isModified())	return true;
		
		String fileName = model.getFilePath() == null ? provider.getString("new") : model.getFilePath().getFileName().toString();

		int input = JOptionPane.showConfirmDialog(null, provider.getString("save") + " \"" + fileName + "\"?", provider.getString("save"),
				JOptionPane.YES_NO_CANCEL_OPTION);
		if (input == 0) {
			Path saveTo = null;
			
			if(model.getFilePath() == null) {
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle(provider.getString("save"));
				saveTo = getSelectedFile(chooser, chooser.showSaveDialog(null), provider);
				
				if(saveTo == null)	return false;
			}
			
			documentModel.saveDocument(model, saveTo);
		}else if(input == 2) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Defines action that can be performed on selected text.
	 */
	public static enum SelectedTextAction{
		COPY, CUT, PASTE, TO_UPPERCASE, TO_LOWERCASE, INVERT, SORT_ASCENDING, SORT_DESCENDING, UNIQUE
	}
	
	/**
	 * Does certain action with selected text based on SelectedTextAction.
	 * 
	 * @param documentModel	document model used to get input and write output.
	 * @param action	action that will be performed.
	 */
	public static void executeTextAction(MultipleDocumentModel documentModel, SelectedTextAction action) {
		JTextArea editor = documentModel.getCurrentDocument().getTextComponent();
		
		int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
		int offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
		
		callAction(action, editor, offset, len);
	}
	
	/**
	 * Helper method that will actually call action on selected text.
	 * 
	 * @param action	action that will be performed.
	 * @param editor	JTextArea to get text from.
	 * @param offset	offset of selected text.
	 * @param len		length of selected text.
	 */
	private static void callAction(SelectedTextAction action, JTextArea editor, int offset, int len) {
		try {
			switch(action) {
				case CUT:
					cut(editor.getDocument(), offset, len);
					break;
				case COPY:
					copy(editor.getDocument(), offset, len);
					break;
				case PASTE:
					paste(editor.getDocument(), offset, len);
					break;
				case TO_UPPERCASE:
					toUpperCase(editor.getDocument(), offset, len);
					break;
				case TO_LOWERCASE:
					toLowerCase(editor.getDocument(), offset, len);
					break;
				case INVERT:
					invertText(editor.getDocument(), offset, len);
					break;
				case SORT_ASCENDING:
					sort(true, editor, offset, len);
					break;
				case SORT_DESCENDING:
					sort(false, editor, offset, len);
					break;
				case UNIQUE:
					removeDuplicates(editor, offset, len);
					break;
		}
		}catch(Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Method that will sort selected lines of text.
	 * 
	 * @param ascending	whether it is needed to sort in ascending order.
	 * @param editor	JTextArea to get text from.
	 * @param offset	offset of selected text.
	 * @param len	length of selected text.
	 * @throws BadLocationException	thrown if invalid localization occurs.
	 */
	private static void sort(boolean ascending, JTextArea editor, int offset, int len) throws BadLocationException {
		int lineStart = editor.getLineOfOffset(offset);
		int lineEnd = editor.getLineOfOffset(offset + len);
		int offsetStart = editor.getLineStartOffset(lineStart);
		int offsetEnd = editor.getLineEndOffset(lineEnd);
		
		String txt = editor.getDocument().getText(offsetStart, offsetEnd - offsetStart);
		
		Locale locale = new Locale(LocalizationProvider.getInstance().getLanguage());
		Collator collator = Collator.getInstance(locale);
		
		String[] pts = txt.split("\n");
		Arrays.sort(pts, (a, b) -> ascending ? collator.compare(a.trim(), b.trim()) : collator.compare(b.trim(), a.trim()));
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < pts.length; i++) {
			if(pts[i].replaceAll(" ", "").length() == 0)	continue;
			sb.append(pts[i] + "\n");
		}
		
		editor.getDocument().remove(offsetStart, offsetEnd - offsetStart);
		editor.getDocument().insertString(offsetStart, sb.toString(), null);
	}

	/**
	 * Method that will invert case of selected text letters.
	 * 
	 * @param doc	editor document used to get text from.
	 * @param offset	offset of selected text.
	 * @param len	length of selected text.
	 * @throws BadLocationException	thrown if invalid localization occurs.
	 */
	private static void invertText(Document doc, int offset, int len) throws BadLocationException {
		String text = doc.getText(offset, len);
		
		doc.remove(offset, len);
		doc.insertString(offset, invertCase(text), null);
	}

	/**
	 * Helper method that inverts case of given text.
	 * 
	 * @param text	Text to invert case.
	 */
	private static String invertCase(String text) {
		char[] chars = text.toCharArray();
		
		for(int i = 0; i < chars.length; i++) {
			char c = chars[i];
			
			if(Character.isLowerCase(c)) {
				chars[i] = Character.toUpperCase(c);
			}else if(Character.isUpperCase(c)) {
				chars[i] = Character.toLowerCase(c);
			}
		}
		
		return new String(chars);
	}

	/**
	 * Method that will lower case of selected text letters.
	 * 
	 * @param doc	editor document used to get text from.
	 * @param offset	offset of selected text.
	 * @param len	length of selected text.
	 * @throws BadLocationException	thrown if invalid localization occurs.
	 */
	private static void toLowerCase(Document doc, int offset, int len) throws BadLocationException {
		String text = doc.getText(offset, len);
		
		doc.remove(offset, len);
		doc.insertString(offset, text.toLowerCase(), null);
	}

	/**
	 * Method that will caps case of selected text letters.
	 * 
	 * @param doc	editor document used to get text from.
	 * @param offset	offset of selected text.
	 * @param len	length of selected text.
	 * @throws BadLocationException	thrown if invalid localization occurs.
	 */
	private static void toUpperCase(Document doc, int offset, int len) throws BadLocationException {
		String text = doc.getText(offset, len);
		
		doc.remove(offset, len);
		doc.insertString(offset, text.toUpperCase(), null);
	}

	/**
	 * Method that will paste text from clipboard into gievn document.
	 * 
	 * @param doc	editor document used to get text from.
	 * @param offset	offset of selected text.
	 * @param len	length of selected text.
	 * @throws BadLocationException	thrown if invalid localization occurs.
	 */
	private static void paste(Document doc, int offset, int len) throws Exception {
		doc.remove(offset, len);
		
	    String text =(String) getSystemClipboard().getData(DataFlavor.stringFlavor);
	    if (text == null)	return;
	   
	    doc.insertString(offset, text, null);
	}

	/**
	 * Method that will copy selected text into clipboard.
	 * 
	 * @param doc	editor document used to get text from.
	 * @param offset	offset of selected text.
	 * @param len	length of selected text.
	 * @throws BadLocationException	thrown if invalid localization occurs.
	 */
	private static void copy(Document doc, int offset, int len) throws BadLocationException {
		String text = doc.getText(offset, len);
		
		getSystemClipboard().setContents(new StringSelection(text), null);
	}

	/**
	 * Method that will cut selected text into clipboard.
	 * 
	 * @param doc	editor document used to get text from.
	 * @param offset	offset of selected text.
	 * @param len	length of selected text.
	 * @throws BadLocationException	thrown if invalid localization occurs.
	 */
	private static void cut(Document doc, int offset, int len) throws BadLocationException {
		String text = doc.getText(offset, len);
		
		getSystemClipboard().setContents(new StringSelection(text), null);
		doc.remove(offset, len);
	}

	/**
	 * Helper method that returns system clipboard.
	 * 
	 * @return	system clipboard.
	 */
	private static Clipboard getSystemClipboard() {
		return Toolkit.getDefaultToolkit().getSystemClipboard();
	}
	
	/**
	 * Method that will remove duplicate lines of selected text.
	 * 
	 * @param editor	editor used to get text from.
	 * @param offset	offset of selected text.
	 * @param len	length of selected text.
	 * @throws BadLocationException	thrown if invalid localization occurs.
	 */
	private static void removeDuplicates(JTextArea editor, int offset, int len) throws BadLocationException {
		int lineStart = editor.getLineOfOffset(offset);
		int lineEnd = editor.getLineOfOffset(offset + len);
		int offsetStart = editor.getLineStartOffset(lineStart);
		int offsetEnd = editor.getLineEndOffset(lineEnd);
		
		String txt = editor.getDocument().getText(offsetStart, offsetEnd - offsetStart);
		String[] pts = txt.split("\n");
		
		LinkedHashSet<String> dupl = new LinkedHashSet<>();
		for(String pt : pts) {
			dupl.add(pt);
		}
		
		StringBuilder sb = new StringBuilder();
		for(String pt: dupl) {
			if(pt.replaceAll(" ", "").length() == 0)	continue;
			
			sb.append(pt + "\n");
		}
		
		editor.getDocument().remove(offsetStart, offsetEnd - offsetStart);
		editor.getDocument().insertString(offsetStart, sb.toString(), null);
	}
	
}
