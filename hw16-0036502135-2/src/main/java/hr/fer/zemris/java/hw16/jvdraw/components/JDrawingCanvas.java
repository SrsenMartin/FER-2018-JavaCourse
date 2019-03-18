package hr.fer.zemris.java.hw16.jvdraw.components;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModelListener;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.visitor.GeometricalObjectPainter;
import hr.fer.zemris.java.hw16.jvdraw.visitor.GeometricalObjectVisitor;

/**
 * Component used to show all objects using
 * given drawing model that stores them.
 * 
 * @author Martin Sršen
 *
 */
public class JDrawingCanvas extends JComponent {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Model where objects are stored.
	 */
	private DrawingModel model;
	/**
	 * JFrame where canvas is contained.
	 */
	private JVDraw parent;

	/**
	 * Constructor that takes DrawingModel and paren JFrame,
	 * initializes its state.
	 * 
	 * @param model	Model where objects are stored.
	 * @param parent	JFrame where canvas is contained.
	 */
	public JDrawingCanvas(DrawingModel model, JVDraw parent) {
		this.model = model;
		this.parent = parent;
		model.addDrawingModelListener(new DrawingModelListener() {

			@Override
			public void objectsAdded(DrawingModel source, int index0, int index1) {
				repaint();
				parent.setSaved(false);
			}

			@Override
			public void objectsRemoved(DrawingModel source, int index0, int index1) {
				repaint();
				parent.setSaved(false);
			}

			@Override
			public void objectsChanged(DrawingModel source, int index0, int index1) {
				repaint();
				parent.setSaved(false);
			}
		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		GeometricalObjectVisitor visitor = new GeometricalObjectPainter((Graphics2D) g);
	
		for(int i = 0; i < model.getSize(); i++) {
			model.getObject(i).accept(visitor);
		}
		
		parent.getCurrentState().paint((Graphics2D) g);
		g.dispose();
	}
}
