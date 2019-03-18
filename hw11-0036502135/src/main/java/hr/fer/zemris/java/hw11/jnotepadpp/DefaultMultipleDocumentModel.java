package hr.fer.zemris.java.hw11.jnotepadpp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Class that represents model of multiple documents.
 * Extends from JTabbedPane and implements MultipleDocumentModel.
 * Has list of SingleDocumentModel object where each represents single
 * tab in tabbed pane.
 * 
 * @author Martin Sršen
 *
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {

	private static final long serialVersionUID = 1L;
	
	/**
	 * list of objects each representing one opened document.
	 */
	private List<SingleDocumentModel> documents;
	/**
	 * Currently opened document.
	 */
	private SingleDocumentModel currentDocument;
	
	/**
	 * Used to set language based on selected language
	 */
	private ILocalizationProvider provider;
	
	/**
	 * represents red disk icon.
	 */
	private ImageIcon redDisk;
	/**
	 * represents green disk icon.
	 */
	private ImageIcon greenDisk;
	
	/**
	 * Set of registered listeners on this object.
	 */
	private Set<MultipleDocumentListener> listeners;

	/**
	 * Constructor that initializes state of this object.
	 * Adds change listener on itself to track when to change current document.
	 * 
	 * @param provider	ILocalizationProvider used to set language based on selected language.
	 */
	public DefaultMultipleDocumentModel(ILocalizationProvider provider) {
		redDisk = getImage("icons/redDisk.png");
		redDisk = new ImageIcon(redDisk.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
		greenDisk = getImage("icons/greenDisk.png");
		greenDisk = new ImageIcon(greenDisk.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
		
		documents = new ArrayList<>();
		listeners = new HashSet<>();
		this.provider = provider;
		
		this.addChangeListener(event -> {
			currentDocument = documents.get(getSelectedIndex());
			notifyDocumentChanged(null, documents.get(getSelectedIndex()));
		});
	}
	
	/**
	 * Returns iterator over list of SingleDocumentModel objects.
	 * 
	 * @return	iterator over list of SingleDocumentModel objects.
	 */
	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return documents.iterator();
	}

	/**
	 * Method that creates new document, adds it into list of documents,
	 * and sets it as current document.
	 * Appends needed listeners to it.
	 */
	@Override
	public SingleDocumentModel createNewDocument() {
		SingleDocumentModel old = currentDocument;
		
		SingleDocumentModel document = new DefaultSingleDocumentModel(null, "");
		currentDocument = document;
		documents.add(document);
		document.addSingleDocumentListener(new SingleDocumentListener() {
			@Override
			public void documentModifyStatusUpdated(SingleDocumentModel model) {
				DefaultMultipleDocumentModel.this.setIconAt(documents.indexOf(document),
						document.isModified() ? redDisk : greenDisk);
			}

			@Override
			public void documentFilePathUpdated(SingleDocumentModel model) {
				setTitleAt(documents.indexOf(document), document.getFilePath().getFileName().toString());
				setToolTipTextAt(documents.indexOf(document), document.getFilePath().toString());
				notifyDocumentChanged(document, document);
			}
		});
		
		add("", new JScrollPane(document.getTextComponent()));
		setTitleAt(documents.indexOf(document), provider.getString("new"));
		provider.addLocalizationListener(() -> {
			if(document.getFilePath() == null) {
				int index = documents.indexOf(document);
				if(index == -1)	return;
				setTitleAt(index, provider.getString("new"));
			}
		});
		
		setIconAt(documents.indexOf(document), greenDisk);
		setSelectedIndex(documents.size() - 1);
		
		notifyDocumentChanged(old, document);
		notifyDocumentAdded(document);
		
		return document;
	}

	/**
	 * Getter method for currentDocument
	 * 
	 * @return	currentDocument
	 */
	@Override
	public SingleDocumentModel getCurrentDocument() {
		return currentDocument;
	}

	/**
	 * Method that loads content from given path into current document.
	 * If file is already opened does nothing.
	 * 
	 * @param path	file path to load.
	 * @return	created document or null if not created.
	 */
	@Override
	public SingleDocumentModel loadDocument(Path path) {
		if(path == null)	return null;
		
		for(SingleDocumentModel doc: documents) {
			if(!path.equals(doc.getFilePath()))	continue;
			
			currentDocument = doc;
			setSelectedIndex(documents.indexOf(currentDocument));

			notifyDocumentChanged(currentDocument, doc);
			JOptionPane.showMessageDialog(null, provider.getString("file") + " " + path.toAbsolutePath() + " " +
					provider.getString("alreadyOpened"), provider.getString("information"), JOptionPane.INFORMATION_MESSAGE);

			return null;
		}
		
		return loadAndAdd(path);
	}

	/**
	 * Helper method that loads content from file with given path.
	 * 
	 * @param path	file path to load.
	 * @return	created document or null if not created.
	 */
	private SingleDocumentModel loadAndAdd(Path path) {
		byte[] okteti;
		
		if(!Files.isReadable(path)) {
			JOptionPane.showMessageDialog(null, provider.getString("file") + " " + path.toAbsolutePath() + " " + 
					provider.getString("notReadable"), provider.getString("error"), JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		try {
			okteti = Files.readAllBytes(path);
		}catch(IOException ex) {
			JOptionPane.showMessageDialog(null,provider.getString("ioexcRead") +  " " +
					path.toAbsolutePath(), provider.getString("error"), JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		String text = new String(okteti, StandardCharsets.UTF_8);
		SingleDocumentModel document = createNewDocument();
		document.setFilePath(path);
		document.getTextComponent().setText(text);
		document.setModified(false);
		
		return document;
	}

	/**
	 * Saves given document content into newPath if provided,
	 * else in models saved path.
	 * If none is provided than user chooses from file chooser.
	 * 
	 * @param model	model whose data is beeing saved.
	 * @param	newPath used at saveAs action.
	 */
	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		Path toWrite = getFileToSave(model.getFilePath(), newPath);
		if(toWrite == null)	return;
		
		byte[] data = model.getTextComponent().getText().getBytes(StandardCharsets.UTF_8);
		try {
			Files.write(toWrite, data);
		}catch(IOException ex) {
			JOptionPane.showMessageDialog(null,provider.getString("ioexcWrite") + " " +
					toWrite, provider.getString("error"), JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		model.setFilePath(toWrite);
		model.setModified(false);
		JOptionPane.showMessageDialog(null,provider.getString("saveMsg") + " " +
				toWrite.toAbsolutePath(), provider.getString("information"), JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Helper method that transforms input paths into
	 * actual path where data will be saved.
	 * 
	 * @param currentPath	models saved path.
	 * @param newPath	Given path to save at.
	 * @return	Path where content will be saved, or null if failed to get.
	 */
	private Path getFileToSave(Path currentPath, Path newPath) {
		Path startPath = currentPath;
		if(newPath != null) {
			if(isNewPathContained(newPath))	return null;
			currentPath = newPath;
		}
		
		if(!Files.isRegularFile(currentPath)) {
			try {
				Files.createFile(currentPath);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,provider.getString("ioexcCreate") + ": " + currentPath.toAbsolutePath().toString(),
						provider.getString("error"), JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}else if(!currentPath.equals(startPath)) {
			int input = JOptionPane.showConfirmDialog(null, provider.getString("overwriteConfirm"), provider.getString("overwrite"),
					JOptionPane.YES_NO_OPTION);
			
			if(input == 1)	return null;
		}
		
		return currentPath;
	}
	
	/**
	 * Checks whether given path is already contained in list of document paths.
	 * 
	 * @param newPath	Used to check its existance.
	 * @return	true if path is contained, false otherwise.
	 */
	private boolean isNewPathContained(Path newPath) {
		for(SingleDocumentModel doc: documents) {
			if(newPath.equals(doc.getFilePath()) && (currentDocument.getFilePath() == null || !newPath.equals(currentDocument.getFilePath()))) {
				JOptionPane.showMessageDialog(null,provider.getString("file") + " " +
						newPath.toAbsolutePath() + " " + provider.getString("opened"), provider.getString("error"), JOptionPane.ERROR_MESSAGE);
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Used to close currently opened document.
	 * There always will be at least 1 document.
	 * 
	 * @param model	document model to remove.
	 */
	@Override
	public void closeDocument(SingleDocumentModel model) {
		int index = documents.indexOf(model);
		if(index == -1)	return;
		
		if (documents.size() == 1) {
			createNewDocument();
		}
		
		documents.remove(model);
		remove(index);
		notifyDocumentRemoved(model);

		if (index >= documents.size())	index--;

		SingleDocumentModel newDocument = documents.get(index);
		notifyDocumentChanged(currentDocument, newDocument);
		currentDocument = newDocument;
	}

	/**
	 * Used to register MultipleDocumentListener.
	 * 
	 * @param l listener to add.
	 */
	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		if(l == null)	return;
		
		listeners.add(l);
	}

	/**
	 * Used to remove MultipleDocumentListener.
	 * 
	 * @param l listener to remove.
	 */
	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.remove(l);
	}

	/**
	 * Returns number of documents contained in notepad.
	 * 
	 * @return	number of documents contained in notepad.
	 */
	@Override
	public int getNumberOfDocuments() {
		return documents.size();
	}

	/**
	 * Returns document at specified index in tabbed pane.
	 * 
	 * @return	document at specified index in tabbed pane.
	 */
	@Override
	public SingleDocumentModel getDocument(int index) {
		return documents.get(index);
	}

	/**
	 * Helper method used to notify all document changed listeners.
	 * 
	 * @param prev	old currentDocument.
	 * @param curr	new currentDocument.
	 */
	private void notifyDocumentChanged(SingleDocumentModel prev, SingleDocumentModel curr) {
		List<MultipleDocumentListener> copy = new ArrayList<>(listeners);
		copy.forEach(listener -> listener.currentDocumentChanged(prev, curr));
	}
	
	/**
	 * Helper method used to notify all document added listeners.
	 * 
	 * @param added	added document model.
	 */
	private void notifyDocumentAdded(SingleDocumentModel added) {
		List<MultipleDocumentListener> copy = new ArrayList<>(listeners);
		copy.forEach(listener -> listener.documentAdded(added));
	}
	
	/**
	 * Helper method used to notify all document removed listeners.
	 * 
	 * @param removed	removed document model.
	 */
	private void notifyDocumentRemoved(SingleDocumentModel removed) {
		List<MultipleDocumentListener> copy = new ArrayList<>(listeners);
		copy.forEach(listener -> listener.documentRemoved(removed));
	}
	
	/**
	 * Helper method used to load ImageIcon from given relazivePath string.
	 * 
	 * @param relativePath	String representing relative path to icon.
	 * @return	loaded ImageIcon.
	 */
	private ImageIcon getImage(String relativePath) {
		try(InputStream is = this.getClass().getResourceAsStream(relativePath)) {
			if (is == null) {
				JOptionPane.showMessageDialog(null,provider.getString("imgLoadExc") + " " + relativePath, provider.getString("error"),
						JOptionPane.ERROR_MESSAGE);
				System.exit(-1);
			}

			byte[] bytes = is.readAllBytes();
			return new ImageIcon(bytes);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,provider.getString("ioexcRead"), provider.getString("error"), JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
}
