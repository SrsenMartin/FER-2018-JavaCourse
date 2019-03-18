package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;

import hr.fer.zemris.java.hw11.jnotepadpp.actions.*;
import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

/**
 * Program that represents simple Notepad++ written in java.
 * Has various actions to do with current document and for
 * loading and saving.
 * Provides implementation for translation on 3 languages:
 * English, Croatian and German.
 * 
 * @author Martin Sršen
 *
 */
public class JNotepadPP extends JFrame {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Document model used to get input and write output.
	 */
	private MultipleDocumentModel documentModel;
	/**
	 * Status bar instance.
	 */
	private StatusBar statusBar;
	/**
	 * Used to set language based on selected language
	 */
	private ILocalizationProvider provider;
	
	/**
	 * action that creates new document.
	 */
	private Action newDocumentAction;
	/**
	 * action that loads file and adds to document.
	 */
	private Action openDocumentAction;
	/**
	 * action that saves document content into same loaded file.
	 */
	private Action saveDocumentAction;
	/**
	 * action that saves document content into chosen file.
	 */
	private Action saveAsAction;
	/**
	 * action that closes current document.
	 */
	private Action closeDocumentAction;
	/**
	 * action that copies into clipboard.
	 */
	private Action copyTextAction;
	/**
	 * action that pastes from clipboard.
	 */
	private Action pasteTextAction;
	/**
	 * action that cuts into clipboard.
	 */
	private Action cutTextAction;
	/**
	 * action that shows statistical info.
	 */
	private Action statisticalInfoAction;
	/**
	 * action that caps selected text.
	 */
	private Action toUpperCaseAction;
	/**
	 * action that lowers case of selected text.
	 */
	private Action toLowerCaseAction;
	/**
	 * action that inverts case of selected text.
	 */
	private Action invertCaseAction;
	/**
	 * action that exits application.
	 */
	private Action exitAction;
	/**
	 * action that sorts selected text in ascending order.
	 */
	private Action ascendingSortAction;
	/**
	 * action that sorts selected text in descending order.
	 */
	private Action descendingSortAction;
	/**
	 * action that removes duplicate lines from selected lines.
	 */
	private Action uniqueAction;
	
	/**
	 * Default constructor to initialize bacis state of frame.
	 */
	public JNotepadPP() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setLocation(400, 150);
		setSize(760, 470);
		
		initGui();
	}

	/**
	 * Helper method that initializes frame gui.
	 * Adds all components into it.
	 */
	private void initGui() {
		provider = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
		documentModel = new DefaultMultipleDocumentModel(provider);
		statusBar = new StatusBar();
		addDocumentAndStatusBarListeners();
		
		initActions();
		createMenuBar();
		createToolbar();
		
		documentModel.createNewDocument();
		
		JPanel modelStatus = new JPanel(new BorderLayout());
		modelStatus.add((JTabbedPane) documentModel, BorderLayout.CENTER);
		modelStatus.add(statusBar, BorderLayout.SOUTH);
		add(modelStatus, BorderLayout.CENTER);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				exitAction.actionPerformed(null);
			}
			
			@Override
			public void windowClosed(WindowEvent event) {
				statusBar.stopClock();
			}
		});
	}

	/**
	 * Helper method used to initialize actions.
	 */
	private void initActions() {
		newDocumentAction = new NewDocumentAction(documentModel, "new", "newDesc", provider);
		openDocumentAction = new OpenDocumentAction(documentModel, "open", "openDesc", provider);
		saveDocumentAction = new SaveDocumentAction(documentModel, "save", "saveDesc", provider);
		saveAsAction = new SaveAsAction(documentModel, "saveAs", "saveAsDesc", provider);
		closeDocumentAction = new CloseDocumentAction(documentModel, "close", "closeDesc", provider);
		copyTextAction = new CopyTextAction(documentModel, "copy", "copyDesc", provider);
		pasteTextAction = new PasteTextAction(documentModel, "paste", "pasteDesc", provider);
		cutTextAction = new CutTextAction(documentModel, "cut", "cutDesc", provider);
		statisticalInfoAction = new StatisticalInfoAction(documentModel, "statisticalInfo", "statisticalInfoDesc", provider);
		toUpperCaseAction = new ToUpperCaseAction(documentModel, "toUpperCase", "toUpperCaseDesc", provider);
		toLowerCaseAction = new ToLowerCaseAction(documentModel, "toLowerCase", "toLowerCaseDesc", provider);
		invertCaseAction = new InvertCaseAction(documentModel, "invertCase", "invertDesc", provider);
		exitAction = new ExitAction(documentModel, this, "exit", "exitDesc", provider);
		ascendingSortAction = new AscendingSortAction(documentModel, "ascendingSort", "ascendingDesc", provider);
		descendingSortAction = new DescendingSortAction(documentModel, "descendingSort", "descendingSort", provider);
		uniqueAction = new UniqueAction(documentModel, "unique", "uniqueDesc", provider);
	}
	
	/**
	 * Helper method used to add multiple document listener on documentModel
	 * and to update status bar and frame synchronously.
	 */
	private void addDocumentAndStatusBarListeners() {
		documentModel.addMultipleDocumentListener(new MultipleDocumentListener() {
			@Override
			public void documentRemoved(SingleDocumentModel model) {
			}
			
			@Override
			public void documentAdded(SingleDocumentModel model) {
				model.getTextComponent().addCaretListener(event -> {
					updateDocument();
					updateStatusBar();
				});
			}

			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				setTitle((currentModel.getFilePath() == null ? "" : currentModel.getFilePath().toAbsolutePath().toString() + " - ") + "JNotepad++");
				updateDocument();
				updateStatusBar();
			}
			
			/**
			 * Helper method that updates status bar.
			 */
			private void updateStatusBar() {
				try {
					JTextArea editor = documentModel.getCurrentDocument().getTextComponent();
					Caret caret = editor.getCaret();
					
					int length = editor.getDocument().getLength();
					int line = editor.getLineOfOffset(caret.getDot());
					int col = caret.getDot() - editor.getLineStartOffset(line) + 1;
					int selectedLen = Math.abs(caret.getDot() - caret.getMark());

					setStatusText(line, length, col, selectedLen);
					provider.addLocalizationListener(() -> updateStatusBar());
				} catch(BadLocationException ex) {
					ex.printStackTrace();
				}
			}
			
			/**
			 * Helper method that sets statuBar text.
			 * 
			 * @param line	Current line where caret currently is.
			 * @param length	Current length of document.
			 * @param col	Current column where caret currently is.
			 * @param selectedLen	Length of selected part.
			 */
			private void setStatusText(int line, int length, int col, int selectedLen) {
				statusBar.getLeft().setText(provider.getString("length") + " : " + length);
				statusBar.getCenter().setText(provider.getString("line") + " : " + (line + 1) + "   " +
						provider.getString("column") + " : " + col + "   " + provider.getString("selected") + " : " + selectedLen);
			}
			
			/**
			 * Helper method used to update gui.
			 */
			public void updateDocument() {
				Caret caret = documentModel.getCurrentDocument().getTextComponent().getCaret();
				boolean marked = caret.getDot() != caret.getMark();
				
				getJMenuBar().getComponent(2).setEnabled(marked);
				copyTextAction.setEnabled(marked);
				cutTextAction.setEnabled(marked);
				toUpperCaseAction.setEnabled(marked);
				toLowerCaseAction.setEnabled(marked);
				invertCaseAction.setEnabled(marked);
				ascendingSortAction.setEnabled(marked);
				descendingSortAction.setEnabled(marked);
				uniqueAction.setEnabled(marked);
			}
		});
	}
	
	/**
	 * Helper method used to create and add toolbar to gui.
	 */
	private void createToolbar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(true);
		
		toolBar.add(new JButton(newDocumentAction));
		toolBar.add(new JButton(openDocumentAction));
		toolBar.add(new JButton(saveDocumentAction));
		toolBar.add(new JButton(saveAsAction));
		toolBar.add(new JButton(closeDocumentAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(cutTextAction));
		toolBar.add(new JButton(copyTextAction));
		toolBar.add(new JButton(pasteTextAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(statisticalInfoAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(exitAction));
		
		getContentPane().add(toolBar, BorderLayout.NORTH);
	}

	/**
	 * Helper method that creates menuBar.
	 */
	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		createFileMenu(menuBar);
		createEditMenu(menuBar);
		createToolsMenu(menuBar);
		createLanguageMenu(menuBar);
		
		setJMenuBar(menuBar);
	}
	
	/**
	 * Helper method used to create language menu.
	 * 
	 * @param menuBar	JMenuBar where menu will be added.
	 */
	private void createLanguageMenu(JMenuBar menuBar) {
		JMenu languageMenu = new JMenu();
		addJMenuName(languageMenu, "languages");
		languageMenu.setMnemonic(KeyEvent.VK_L);
		
		createLanguageMenuItem(languageMenu, "croatian", "hr");
		createLanguageMenuItem(languageMenu, "english", "en");
		createLanguageMenuItem(languageMenu, "german", "de");
		
		menuBar.add(languageMenu);
	}
	
	/**
	 * Helper method that creates JMenuItem and sets text
	 * based on currently selected language.
	 * 
	 * @param menu	JMenu where item will be added.
	 * @param langKey	item name key.
	 * @param localeName	Locale name.
	 */
	private void createLanguageMenuItem(JMenu menu, String langKey, String localeName) {
		JMenuItem langItem = new JMenuItem(provider.getString(langKey));
		provider.addLocalizationListener(() -> langItem.setText(provider.getString(langKey)));
		langItem.addActionListener(event -> LocalizationProvider.getInstance().setLanguage(localeName));
		
		menu.add(langItem);
	}

	/**
	 * Helper method that crates tools menu.
	 * 
	 * @param menuBar	JMenuBar where menu will be added.
	 */
	private void createToolsMenu(JMenuBar menuBar) {
		JMenu toolsMenu = new JMenu();
		addJMenuName(toolsMenu, "tools");
		toolsMenu.setEnabled(false);
		toolsMenu.setMnemonic(KeyEvent.VK_T);
		
		JMenu changeCaseMenu = new JMenu("Change case");
		addJMenuName(changeCaseMenu, "changeCase");
		changeCaseMenu.setMnemonic(KeyEvent.VK_C);
		changeCaseMenu.add(new JMenuItem(toUpperCaseAction));
		changeCaseMenu.add(new JMenuItem(toLowerCaseAction));
		changeCaseMenu.add(new JMenuItem(invertCaseAction));
		toolsMenu.add(changeCaseMenu);
		
		JMenu sortMenu = new JMenu("Sort");
		addJMenuName(sortMenu, "sort");
		sortMenu.setMnemonic(KeyEvent.VK_S);
		sortMenu.add(new JMenuItem(ascendingSortAction));
		sortMenu.add(new JMenuItem(descendingSortAction));
		toolsMenu.add(sortMenu);
		
		toolsMenu.addSeparator();
		toolsMenu.add(new JMenuItem(uniqueAction));
		
		menuBar.add(toolsMenu);
	}
	
	/**
	 * Helper method that crates edit menu.
	 * 
	 * @param menuBar	JMenuBar where menu will be added.
	 */
	private void createEditMenu(JMenuBar menuBar) {
		JMenu editMenu = new JMenu();
		addJMenuName(editMenu, "edit");
		editMenu.setMnemonic(KeyEvent.VK_E);
		
		editMenu.add(new JMenuItem(cutTextAction));
		editMenu.add(new JMenuItem(copyTextAction));
		editMenu.add(new JMenuItem(pasteTextAction));
		
		menuBar.add(editMenu);
	}
	
	/**
	 * Helper method that crates file menu.
	 * 
	 * @param menuBar	JMenuBar where menu will be added.
	 */
	private void createFileMenu(JMenuBar menuBar) {
		JMenu fileMenu = new JMenu();
		addJMenuName(fileMenu, "file");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		
		fileMenu.add(new JMenuItem(newDocumentAction));
		fileMenu.add(new JMenuItem(openDocumentAction));
		fileMenu.add(new JMenuItem(saveDocumentAction));
		fileMenu.add(new JMenuItem(saveAsAction));
		fileMenu.add(new JMenuItem(closeDocumentAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(statisticalInfoAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(exitAction));
		
		menuBar.add(fileMenu);
	}
	
	/**
	 * Helper method that sets JMenu name based on currently selected language
	 * and adds listener to listen for language changes.
	 * 
	 * @param menu	JMenu to set its name.
	 * @param key	name key.
	 */
	private void addJMenuName(JMenu menu, String key) {
		menu.setText(provider.getString(key));
		provider.addLocalizationListener(() -> menu.setText(provider.getString(key)));
	}

	/**
	 * Called when program is started.
	 * 
	 * @param args	Arguments from command prompt.Not used in this example.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JNotepadPP().setVisible(true));
	}
}
