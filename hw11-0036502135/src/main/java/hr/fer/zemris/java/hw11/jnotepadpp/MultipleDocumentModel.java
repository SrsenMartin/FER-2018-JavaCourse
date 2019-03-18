package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;

/**
 * Class that represents model of multiple documents.
 * Contains methods for altering state of model
 * and manipulating text files.
 * 
 * @author Martin Sršen
 *
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
	/**
	 * Method that creates new document, adds it into list of documents,
	 * and sets it as current document.
	 * Appends needed listeners to it.
	 */
	SingleDocumentModel createNewDocument();

	/**
	 * Getter method for currentDocument
	 * 
	 * @return	currentDocument
	 */
	SingleDocumentModel getCurrentDocument();

	/**
	 * Method that loads content from given path into current document.
	 * If file is already opened does nothing.
	 * 
	 * @param path	file path to load.
	 * @return	created document or null if not loaded.
	 */
	SingleDocumentModel loadDocument(Path path);

	/**
	 * Saves given document content into newPath if provided,
	 * else in models saved path.
	 * If none is provided than user chooses from file chooser.
	 * 
	 * @param model	model whose data is beeing saved.
	 * @param	newPath used at saveAs action.
	 */
	void saveDocument(SingleDocumentModel model, Path newPath);

	/**
	 * Used to close currently opened document.
	 * There always will be at least 1 document.
	 * 
	 * @param model	document model to remove.
	 */
	void closeDocument(SingleDocumentModel model);

	/**
	 * Used to register MultipleDocumentListener.
	 * 
	 * @param l listener to add.
	 */
	void addMultipleDocumentListener(MultipleDocumentListener l);

	/**
	 * Used to remove MultipleDocumentListener.
	 * 
	 * @param l listener to remove.
	 */
	void removeMultipleDocumentListener(MultipleDocumentListener l);

	/**
	 * Returns number of documents contained in notepad.
	 * 
	 * @return	number of documents contained in notepad.
	 */
	int getNumberOfDocuments();

	/**
	 * Returns document at specified index in tabbed pane.
	 * 
	 * @return	document at specified index in tabbed pane.
	 */
	SingleDocumentModel getDocument(int index);
}