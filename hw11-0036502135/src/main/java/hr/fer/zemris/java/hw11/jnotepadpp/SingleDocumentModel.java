package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;

import javax.swing.JTextArea;

public interface SingleDocumentModel {
	/**
	 * Returns JTextArea editor.
	 * 
	 * @return JTextArea editor.
	 */
	JTextArea getTextComponent();

	/**
	 * Returns path to loaded file.
	 * 
	 * @return path to loaded file or null if no file is loaded.
	 */
	Path getFilePath();

	/**
	 * Sets path to loaded file.
	 * 
	 * @param path path to loaded file.
	 */
	void setFilePath(Path path);

	/**
	 * Returns whether document is modified or not.
	 * 
	 * @return true if document is modified, false otherwise.
	 */
	boolean isModified();

	/**
	 * Sets modification flag to given value.
	 * 
	 * @param modified	used to set modified flag to.
	 */
	void setModified(boolean modified);

	/**
	 * Used to register SingleDocumentListener.
	 * 
	 * @param l	listener to add.
	 */
	void addSingleDocumentListener(SingleDocumentListener l);

	/**
	 * Used to remove SingleDocumentListener.
	 * 
	 * @param l	listener to remove.
	 */
	void removeSingleDocumentListener(SingleDocumentListener l);
}
