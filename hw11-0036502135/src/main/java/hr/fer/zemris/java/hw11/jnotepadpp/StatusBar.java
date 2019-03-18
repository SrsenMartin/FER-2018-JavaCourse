package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;

/**
 * Class representing custom made component
 * that extends from JPanel.
 * Represents status bar containing
 * timestamp on right side and 2 JLabels
 * on left and in center with.
 * 
 * @author Martin Sršen
 *
 */
public class StatusBar extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * left side JLabel.
	 */
	private JLabel left;
	/**
	 * center JLabel.
	 */
	private JLabel center;
	/**
	 * right side JLabel, containing timestamp.
	 */
	private JLabel right;
	
	/**
	 * If clock should stop counting.
	 */
	private volatile boolean stopRequest;
	
	/**
	 * Default constructor that will make layout of status bar
	 * and call method that will add timestamp.
	 */
	public StatusBar() {
		this.setLayout(new GridLayout(1, 3));
		setBorder(new CompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED),
				BorderFactory.createEmptyBorder(2, 7, 2, 7)));
		
		left = new JLabel();
		center = new JLabel();
		right = new JLabel();
		right.setHorizontalAlignment(SwingConstants.RIGHT);
		
		add(left);
		
		JPanel centerComp = new JPanel(new BorderLayout());
		centerComp.add(center, BorderLayout.CENTER);
		centerComp.add(new JSeparator(JSeparator.VERTICAL), BorderLayout.WEST);
		add(centerComp);
		
		JPanel rightComp = new JPanel(new BorderLayout());
		rightComp.add(right, BorderLayout.CENTER);
		rightComp.add(new JSeparator(JSeparator.VERTICAL), BorderLayout.WEST);
		add(rightComp);
		
		createClock();
	}
	
	/**
	 * Returns left JLabel component.
	 * 
	 * @return	left JLabel component.
	 */
	public JLabel getLeft() {
		return left;
	}
	
	/**
	 * Returns center JLabel component.
	 * 
	 * @return	center JLabel component.
	 */
	public JLabel getCenter() {
		return center;
	}
	
	/**
	 * Helper method that creates new thread that will update timestamp each second.
	 */
	private void createClock(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		updateTime(format);
		
		Thread t = new Thread(()->{
			while(true) {
				try {
					Thread.sleep(1000);
				} catch(Exception ex) {}
				
				if(stopRequest)	break;
				
				SwingUtilities.invokeLater(()->{
					updateTime(format);
				});
			}
		});
		t.setDaemon(true);
		t.start();
	}

	/**
	 * Updates timestamp text.
	 * 
	 * @param format	SimpleDateFormat format.
	 */
	private void updateTime(SimpleDateFormat format) {
		String currentTime = format.format(Calendar.getInstance().getTime());
		
		right.setText(currentTime);
	}
	
	/**
	 * Method that stops clock.
	 */
	public void stopClock() {
		stopRequest = true;
	}
}
