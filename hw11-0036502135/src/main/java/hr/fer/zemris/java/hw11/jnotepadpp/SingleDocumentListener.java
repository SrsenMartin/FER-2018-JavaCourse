package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * Intarface representing listener on
 * document modify status update and path update.
 * 
 * @author Martin Sršen
 *
 */
public interface SingleDocumentListener {
	/**
	 * Called when current document modify status changes.
	 * 
	 * @param model	document model where modify status changed.
	 */
	void documentModifyStatusUpdated(SingleDocumentModel model);

	/**
	 * Called when current document path updates.
	 * 
	 * @param model	document model where path updates.
	 */
	void documentFilePathUpdated(SingleDocumentModel model);
}
