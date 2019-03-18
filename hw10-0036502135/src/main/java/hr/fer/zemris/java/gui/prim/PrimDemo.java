package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Program that has 2 lists and 1 button.
 * Lists use created PrimListModel as data model.
 * After button is clicked. PrimListModels next() method is called.
 * After that calculated prime number adds into both lists end.
 * 
 * @author Martin Sršen
 *
 */
public class PrimDemo extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor that creates window size,title and
	 * closing operation and calls method that will initialize GUI.
	 */
	public PrimDemo() {
		setLocation(500, 200);
		setSize(600, 370);
		setTitle("PrimDemo");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		initGui();
	}

	/**
	 * Method called to initialize PrimDemo GUI.
	 * Adds 2 JList object that are wrapped in JSCrollPane so they are scrollable.
	 * At frame South , button is places which when pressed generates next prime number.
	 */
	private void initGui() {
		Container container = getContentPane();
		
		PrimListModel<Integer> listModel = new PrimListModel<>();
		
		JPanel listPanel = new JPanel(new GridLayout(1, 0));
		listPanel.add(new JScrollPane(new JList<Integer>(listModel)));
		listPanel.add(new JScrollPane(new JList<Integer>(listModel)));
		
		container.add(listPanel, BorderLayout.CENTER);
		
		JButton nextButton = new JButton("Next");
		nextButton.addActionListener(event -> listModel.next());
		container.add(nextButton, BorderLayout.SOUTH);
		
		setVisible(true);
	}
	
	/**
	 * Called when program is started.
	 * 
	 * @param args	Arguments from command prompt.Not used in this example.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new PrimDemo().setVisible(true));
	}
}
