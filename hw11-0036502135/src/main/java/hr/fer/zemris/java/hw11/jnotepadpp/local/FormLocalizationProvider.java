package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * Class which is used to talk with out gui.
 * In its constructor it sets connecting when window opens,
 * and destroys it as soon as it closes.
 * 
 * @author Martin Sršen
 *
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

	/**
	 * Constructor that initializes object state and adds window listener
	 * that sets connecting when window opens,
	 * and destroys it as soon as it closes.
	 * 
	 * @param provider	LocalizationProvider object.
	 * @param frame	JFrame used to listen for its window.
	 */
	public FormLocalizationProvider(ILocalizationProvider provider, JFrame frame) {
		super(provider);
		
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowOpened(WindowEvent event) {
				connect();
			}
			
			@Override
			public void windowClosed(WindowEvent event) {
				disconnect();
			}
		});
	}

}
