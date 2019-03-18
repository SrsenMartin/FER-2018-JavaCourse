package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Class that represents model of single document.
 * Containes filePath to loaded file,
 * modified flag to check whether file is changed
 * and editor used to show and add text.
 * 
 * @author Martin Sršen
 *
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {

	/**
	 * path to loaded file.
	 */
	private Path filePath;
	/**
	 * flag to check whether file is changed
	 */
	private boolean modified;
	/**
	 * used to show and add text.
	 */
	private JTextArea editor;
	
	/**
	 * Set of listeners on this object.
	 */
	private Set<SingleDocumentListener> listeners;
	
	/**
	 * Constructor initializing single document.
	 * 
	 * @param filePath	path to loaded file.
	 * @param text	text to append to editor.
	 */
	public DefaultSingleDocumentModel(Path filePath, String text) {
		listeners = new HashSet<>();
		this.filePath = filePath;
		
		createEditor(text);
	}
	
	/**
	 * Returns editor.
	 * 
	 * @return editor.
	 */
	@Override
	public JTextArea getTextComponent() {
		return editor;
	}

	/**
	 * Returns path to loaded file.
	 * 
	 * @return path to loaded file or null if no file is loaded.
	 */
	@Override
	public Path getFilePath() {
		return filePath;
	}

	/**
	 * Sets path to loaded file.
	 * 
	 * @param path path to loaded file.
	 */
	@Override
	public void setFilePath(Path path) {
		if(path == null)	return;
		if(path.equals(filePath))	return;
		
		filePath = path;
		
		notifyPathChanged();
	}

	/**
	 * Returns whether document is modified or not.
	 * 
	 * @return true if document is modified, false otherwise.
	 */
	@Override
	public boolean isModified() {
		return modified;
	}

	/**
	 * Sets modification flag to given value.
	 * 
	 * @param modified	used to set modified flag to.
	 */
	@Override
	public void setModified(boolean modified) {
		if(this.modified == modified)	return;
		
		this.modified = modified;
		
		notifyModified();
	}

	/**
	 * Used to register SingleDocumentListener.
	 * 
	 * @param l	listener to add.
	 */
	@Override
	public void addSingleDocumentListener(SingleDocumentListener l) {
		if(l == null)	return;
		
		listeners.add(l);
	}

	/**
	 * Used to remove SingleDocumentListener.
	 * 
	 * @param l	listener to remove.
	 */
	@Override
	public void removeSingleDocumentListener(SingleDocumentListener l) {
		listeners.remove(l);
	}

	/**
	 * Helper method used to notify all modified listeners.
	 */
	private void notifyModified() {
		List<SingleDocumentListener> copy = new ArrayList<>(listeners);
		copy.forEach(listener -> listener.documentModifyStatusUpdated(this));
	}
	
	/**
	 * Helper method used to notify all path changed listeners.
	 */
	private void notifyPathChanged() {
		List<SingleDocumentListener> copy = new ArrayList<>(listeners);
		copy.forEach(listener -> listener.documentFilePathUpdated(this));
	}
	
	/**
	 * Helper method used to create editor
	 * and add document listener to check when it is modified.
	 * 
	 * @param text	Initial text of editor.
	 */
	private void createEditor(String text) {
		editor = new JTextArea(text);
		
		editor.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent event) {
			}

			@Override
			public void insertUpdate(DocumentEvent event) {
				change();
			}

			@Override
			public void removeUpdate(DocumentEvent event) {
				change();
			}
			
			private void change(){
				if(!isModified()) {
					setModified(true);
				}else if(filePath == null && editor.getText().equals("")) {
					setModified(false);
				}
			}
		});
	}
}
