package hr.fer.zemris.java.hw16.jvdraw.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JColorChooser;
import javax.swing.JComponent;

/**
 * Component that is used as color provider.
 * When clicked opens JColorChooser
 * where user can choose color to use.
 * 
 * @author Martin Sršen
 *
 */
public class JColorArea extends JComponent implements IColorProvider {

	private static final long serialVersionUID = 1L;
	
	/**
	 * currently selected color.
	 */
	private Color selectedColor;
	/**
	 * List of ColorChangeListener objects.
	 */
	private List<ColorChangeListener> colorListeners;

	/**
	 * Default constructor that initializes component
	 * to default color.
	 * 
	 * @param selectedColor	Color to set as default.
	 */
	public JColorArea(Color selectedColor) {
		Objects.requireNonNull(selectedColor, "Initial color can't be null value.");
		this.selectedColor = selectedColor;
		
		colorListeners = new ArrayList<>();
		changeColor(selectedColor);
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				Color newColor = JColorChooser.showDialog(JColorArea.this, "Choose colour", selectedColor);
				changeColor(newColor);
			}
		});
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(23, 23);
	}
	
	@Override
	public Dimension getMaximumSize() {
		return new Dimension(23, 23);
	}

	@Override
	public Color getCurrentColor() {
		return selectedColor;
	}

	@Override
	public void addColorChangeListener(ColorChangeListener listener) {
		if(listener == null)	return;
		colorListeners.add(listener);
	}

	@Override
	public void removeColorChangeListener(ColorChangeListener listener) {
		colorListeners.remove(listener);
	}
	
	/**
	 * Method that changes current color
	 * to given new color.
	 * 
	 * @param newColor	Color used to set current color to.
	 */
	private void changeColor(Color newColor) {
		if(newColor == null || newColor.equals(selectedColor))	return;
		
		Color old = selectedColor;
		selectedColor = newColor;
		this.setBackground(newColor);
		
		List<ColorChangeListener> copy = new ArrayList<>(colorListeners);
		copy.forEach(listener -> listener.newColorSelected(this, old, newColor));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(selectedColor);
		g.fillRect((int) getAlignmentX(), (int) getAlignmentY(), getPreferredSize().width, getPreferredSize().height);
		g.dispose();
	}
}
