package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * Intarface representing listener on
 * document changing, adding, removing.
 * 
 * @author Martin Sršen
 *
 */
public interface MultipleDocumentListener {
	/**
	 * Called when current document changes.
	 * 
	 * @param previousModel	old currentDocument.
	 * @param currentModel	 new currentDocument.
	 */
	void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);

	/**
	 * Called when new document is added.
	 * 
	 * @param added	added document model.
	 */
	void documentAdded(SingleDocumentModel model);

	/**
	 * Called when current document is removed.
	 * 
	 * @param added	removed document model.
	 */
	void documentRemoved(SingleDocumentModel model);
}
