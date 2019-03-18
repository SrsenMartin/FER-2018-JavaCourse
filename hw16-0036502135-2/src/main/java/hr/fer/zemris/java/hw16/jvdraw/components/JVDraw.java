package hr.fer.zemris.java.hw16.jvdraw.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import hr.fer.zemris.java.hw16.jvdraw.editor.GeometricalObjectEditor;
import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.KonvPoly;
import hr.fer.zemris.java.hw16.jvdraw.geometricalObjects.Line;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingObjectListModel;
import hr.fer.zemris.java.hw16.jvdraw.model.MyDrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.tools.CircleTool;
import hr.fer.zemris.java.hw16.jvdraw.tools.FilledCircleTool;
import hr.fer.zemris.java.hw16.jvdraw.tools.KonvPolyTool;
import hr.fer.zemris.java.hw16.jvdraw.tools.LineTool;
import hr.fer.zemris.java.hw16.jvdraw.tools.Tool;
import hr.fer.zemris.java.hw16.jvdraw.visitor.GeometricalObjectBBCalculator;
import hr.fer.zemris.java.hw16.jvdraw.visitor.GeometricalObjectPainter;

/**
 * Application that represents simple paint.
 * Enables user to draw lines, circles and filled circles
 * with chosen color for foreground and background.
 * Enabled saving picture to different image types.
 * Enabled saving and loading picture specs to file.
 * All objects are show in the list where objects can
 * be deleted and replaced.
 * 
 *  @author Martin Sršen
 */
public class JVDraw extends JFrame {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Current selected tool state.
	 */
	private Tool currentState;
	/**
	 * Drawing model used to store all geometrical objects.
	 */
	private DrawingModel model;
	
	/**
	 * Saved savePath used for saving.
	 */
	private Path savePath;
	/**
	 * Whether image was saved recently(not changed).
	 */
	private boolean saved = true;

	/**
	 * Default constructor used to create frame.
	 */
	public JVDraw() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setLocation(100, 50);
		setSize(1131, 700);
	
		model = new MyDrawingModel();
		
		initGui();
	}
	
	/**
	 * Method used to initialize gui.
	 */
	private void initGui() {
		JColorArea fColorArea = new JColorArea(Color.RED);
		JColorArea bColorArea = new JColorArea(Color.BLUE);
		
		JDrawingCanvas canvas = new JDrawingCanvas(model, this);
		addList();
		
		ButtonGroup group = new ButtonGroup();
		JToggleButton lineButton = new JToggleButton("Line");
		lineButton.addActionListener(event -> {currentState = new LineTool(model, fColorArea, canvas); canvas.repaint();});
		JToggleButton circleButton = new JToggleButton("Circle");
		circleButton.addActionListener(event -> {currentState = new CircleTool(model, fColorArea, canvas); canvas.repaint();});
		JToggleButton fCircleButton = new JToggleButton("Filled circle");
		fCircleButton.addActionListener(event -> {currentState = new FilledCircleTool(model, fColorArea, bColorArea, canvas); canvas.repaint();});
		
		JToggleButton konvPoly = new JToggleButton("Konveksni poligon");
		konvPoly.addActionListener(event -> {currentState = new KonvPolyTool(model, fColorArea, bColorArea, canvas); canvas.repaint();});
		
		group.add(lineButton);
		group.add(circleButton);
		group.add(fCircleButton);
		group.add(konvPoly);
		
		addColorLabel(fColorArea, bColorArea);
		createToolBar(fColorArea, bColorArea, lineButton, circleButton, fCircleButton, konvPoly);
		
		getContentPane().add(canvas, BorderLayout.CENTER);
		addListeners(canvas);
		addMenu();
		setExitAction();
	}
	
	/**
	 * Method that adds JColorLabel to bottom of the screen.
	 * 
	 * @param fColorArea	foreground color provider.
	 * @param bColorArea	background color provider.
	 */
	private void addColorLabel(JColorArea fColorArea, JColorArea bColorArea) {
		JColorLabel colorLabel = new JColorLabel(fColorArea, bColorArea);
		colorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		colorLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		getContentPane().add(colorLabel, BorderLayout.SOUTH);
	}
	
	/**
	 * Method that sets exit action.
	 */
	private void setExitAction() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				if(!saved) {
					int input = JOptionPane.showConfirmDialog(JVDraw.this, "Save changes?", "Save",
							JOptionPane.YES_NO_CANCEL_OPTION);
					
					if(input == -1)	return;
					if(input == 2)	return;
					if(input == 0) {
						if(savePath == null) {
							savePath = getSaveFile(".jvd");
						}
						if(savePath == null)	return;
						
						save();
					}
				}
				
				dispose();
			}
		});
	}

	/**
	 * Method that adds Menu to frame.
	 */
	private void addMenu() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu itemsMenu = new JMenu("File");
		itemsMenu.setMnemonic(KeyEvent.VK_F);
		
		itemsMenu.add(createSaveItem());
		itemsMenu.add(createSaveAsItem());
		itemsMenu.add(createOpenItem());
		itemsMenu.add(createExportItem());
		
		JMenu toolsMenu = new JMenu("Tools");
		toolsMenu.setMnemonic(KeyEvent.VK_T);
		JMenuItem clear = new JMenuItem("Clear screen");
		clear.addActionListener(e -> clear());
		toolsMenu.add(clear);
		
		menuBar.add(itemsMenu);
		menuBar.add(toolsMenu);
		
		setJMenuBar(menuBar);
	}

	/**
	 * Method that adds list to frame at right side.
	 */
	private void addList() {
		DrawingObjectListModel listModel = new DrawingObjectListModel(model);
		JList<GeometricalObject> list = new JList<>(listModel);
		list.setFixedCellWidth(240);

		addListOnMouseClick(list, listModel);
		addListOnKeyPress(list, listModel);
		
		getContentPane().add(new JScrollPane(list), BorderLayout.EAST);
	}
	
	/**
	 * Adds on mouse click action to given list.
	 * 
	 * @param list	List to put mouse click action on.
	 * @param listModel	List model used to get elements.
	 */
	private void addListOnMouseClick(JList<GeometricalObject> list, DrawingObjectListModel listModel) {
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() < 2)	return;
			
				int index = list.locationToIndex(e.getPoint());
				if(index < 0)	return;
				
				GeometricalObject clicked = listModel.getElementAt(index);
				GeometricalObjectEditor editor = clicked.createGeometricalObjectEditor();
				if(JOptionPane.showConfirmDialog(JVDraw.this, editor,"Change object specs", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
					try {
						editor.checkEditing();
						editor.acceptEditing();
					} catch(Exception ex) {
						JOptionPane.showMessageDialog(JVDraw.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
	}
	
	/**
	 * Adds on key press action to given list.
	 * 
	 * @param list	List to put key press action on.
	 * @param listModel	List model used to get elements.
	 */
	private void addListOnKeyPress(JList<GeometricalObject> list, DrawingObjectListModel listModel) {
		list.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				int index = list.getSelectedIndex();
				if(index == -1)	return;
				
				GeometricalObject object = listModel.getElementAt(index);
				
				switch(event.getKeyCode()) {
					case KeyEvent.VK_DELETE:
						model.remove(object);
						break;
					case KeyEvent.VK_PLUS:
						model.changeOrder(object, -1);
						list.setSelectedIndex(index-1);
						break;
					case KeyEvent.VK_MINUS:
						model.changeOrder(object, 1);
						list.setSelectedIndex(index+1);
						break;
					default:
						return;
				}
			}
		});
	}

	/**
	 * Method used to add tool bar to frame.
	 * 
	 * @param fColorArea	foreground color provider.
	 * @param bColorArea	background color provider.
	 * @param lineButton	JToggleButton representing line.
	 * @param circleButton	JToggleButton representing circle.
	 * @param fCircleButton	JToggleButton representing filled circle.
	 */
	private void createToolBar(JColorArea fColorArea, JColorArea bColorArea, 
			JToggleButton lineButton, JToggleButton circleButton, JToggleButton fCircleButton, JToggleButton konvPoly) {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		
		toolBar.add(fColorArea);
		toolBar.addSeparator();
		toolBar.add(bColorArea);
		toolBar.addSeparator();
		
		toolBar.add(lineButton);
		toolBar.addSeparator();
		toolBar.add(circleButton);
		toolBar.addSeparator();
		toolBar.add(fCircleButton);
		toolBar.addSeparator();
		toolBar.add(konvPoly);
		
		lineButton.doClick();
		
		toolBar.setBorder(getDoubleBorder());
		
		getContentPane().add(toolBar, BorderLayout.NORTH);
	}

	/**
	 * Called when program is started.
	 * 
	 * @param args Arguments from command prompt.Not used in this example.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JVDraw().setVisible(true));
	}
	
	/**
	 * Method that will create double lined border.
	 * 
	 * @return	double lined border object.
	 */
	private Border getDoubleBorder() {
		return BorderFactory.createCompoundBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1), BorderFactory.createEmptyBorder(2, 2, 2, 2)), 
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1), BorderFactory.createEmptyBorder(3, 3, 3, 3)));
	}
	
	/**
	 * Method used to add listeners to JDrawingCanvas object.
	 * 
	 * @param canvas	JDrawingCanvas object used to display geometrical objects.
	 */
	private void addListeners(JDrawingCanvas canvas) {
		canvas.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				currentState.mouseClicked(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				currentState.mousePressed(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				currentState.mouseReleased(e);
			}
		});
		canvas.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				currentState.mouseDragged(e);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				currentState.mouseMoved(e);
			}
		});
	}

	/**
	 * Getter for current state.
	 * 
	 * @return	current state.
	 */
	public Tool getCurrentState() {
		return currentState;
	}
	
	/**
	 * Method that creates JMenuItem used to export
	 * image in png, jpg or gif format.
	 * 
	 * @return	JMenuItem used to export image.
	 */
	private JMenuItem createExportItem() {
		JMenuItem export = new JMenuItem("Export");
		
		export.setAccelerator(KeyStroke.getKeyStroke("control E"));
		export.setMnemonic(KeyEvent.VK_E);
		
		Object[] options = new Object[] {"JPG", "PNG", "GIF"};
		export.addActionListener(e -> {
			int index = JOptionPane.showOptionDialog(JVDraw.this, "Which export type do you want?", "Export type", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if(index == -1)	return;
			
			String type = (String) options[index];
			Path path = getSaveFile("." + type.toLowerCase());
			if(path == null)	return;
			
			saveImage(path, type);
		});
		
		return export;
	}
	
	/**
	 * Method that saves image of given type at given path.
	 * 
	 * @param path	Path to save Image at.
	 * @param type	Image type.
	 */
	private void saveImage(Path path, String type) {
		if(model.getSize() == 0) {
			JOptionPane.showMessageDialog(JVDraw.this, "You can't save empty image.", "No objects.", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		GeometricalObjectBBCalculator bbcalc = new GeometricalObjectBBCalculator();
		for(int i = 0; i < model.getSize(); i++) {
			model.getObject(i).accept(bbcalc);
		}
		
		Rectangle box = bbcalc.getBoundingBox();
				
		BufferedImage image = new BufferedImage(box.width, box.height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = image.createGraphics();
		AffineTransform trans = new AffineTransform();
		trans.translate(-box.x, -box.y);
		g.transform(trans);

		GeometricalObjectPainter painter = new GeometricalObjectPainter((Graphics2D) g);
		for(int i = 0; i < model.getSize(); i++) {
			model.getObject(i).accept(painter);
		}
		g.dispose();
		
		File file = path.toFile();
		try {
			ImageIO.write(image, type, file);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(JVDraw.this, "Error saving image.", "Error", JOptionPane.ERROR_MESSAGE);
		}

		JOptionPane.showMessageDialog(JVDraw.this, "Image saved to " + path, "Image saved", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Method that creates JMenuItem used to save paint model
	 * into file.
	 * 
	 * @return	JMenuItem used to save paint model into file.
	 */
	private JMenuItem createSaveItem() {
		JMenuItem save = new JMenuItem("Save");
		
		save.setAccelerator(KeyStroke.getKeyStroke("control S"));
		save.setMnemonic(KeyEvent.VK_S);
		
		save.addActionListener(e -> {
			if(savePath == null) {
				savePath = getSaveFile(".jvd");
			}
			if(savePath == null)	return;
			
			save();
		});
		
		return save;
	}
	
	/**
	 * Method used to create JMenuItem used
	 * to save paint model in chosen file.
	 * 
	 * @return	JMenuItem used to save paint model into chosen file.
	 */
	private JMenuItem createSaveAsItem() {
		JMenuItem saveAs = new JMenuItem("Save As");
		
		saveAs.setAccelerator(KeyStroke.getKeyStroke("control alt S"));
		saveAs.setMnemonic(KeyEvent.VK_A);
		
		saveAs.addActionListener(e -> {
			savePath = getSaveFile(".jvd");
			if(savePath == null)	return;
			
			save();
		});
		
		return saveAs;
	}
	
	/**
	 * Method used to create JMenuItem used
	 * to load paint model from chosen file.
	 * 
	 * @return	JMenuItem used to load paint model from chosen file.
	 */
	private JMenuItem createOpenItem() {
		JMenuItem open = new JMenuItem("Open");
		
		open.setAccelerator(KeyStroke.getKeyStroke("control O"));
		open.setMnemonic(KeyEvent.VK_O);
		
		open.addActionListener(e -> {
			Path loadPath = getLoadFile();
			if(loadPath == null)	return;
			
			clear();
			try {
				List<String> data = Files.readAllLines(loadPath);
				data.forEach(line -> addToModel(line));
				savePath = loadPath;
				saved = true;
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(JVDraw.this, "Error loading from file.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		return open;
	}
	
	/**
	 * Used to save current paint model into
	 * file saved in savePath variable.
	 */
	private void save() {
		if(savePath == null)	return;
		
		List<String> data = getDataToSave();
		try {
			Files.write(savePath, data, StandardCharsets.UTF_8);
			saved = true;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(JVDraw.this, "Error saving to file.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Method used to return path
	 * to chosen file in JFileChooser
	 * if it is valid file,
	 * else returns null.
	 * 
	 * @param extension	Required file extension.
	 * @return	chosen file path if valid, null otherwise.
	 */
	private Path getSaveFile(String extension) {
		JFileChooser chooser = new JFileChooser();
		
		if(chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		
		Path path = chooser.getSelectedFile().toPath();
		if(!Files.isRegularFile(path) && !path.toString().endsWith(extension)) {
			path = Paths.get(path.toString() + extension);
		}
		if(!path.toString().endsWith(extension)) {
			JOptionPane.showMessageDialog(JVDraw.this, "File must end with " + extension, "Information", JOptionPane.INFORMATION_MESSAGE);
			return null;
		}
		
		return path;
	}
	
	/**
	 * Method that returns path to
	 * chosen file with JFileChooser
	 * if valid file is selected,
	 * null otherwise.
	 * 
	 * @return	Path to valid jvd file if valid file,null otherwise.
	 */
	private Path getLoadFile() {
		JFileChooser chooser = new JFileChooser();
		
		if(chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		
		Path path = chooser.getSelectedFile().toPath();
		if(!Files.isRegularFile(path)) {
			JOptionPane.showMessageDialog(JVDraw.this, "File doesn't exist", "Information", JOptionPane.INFORMATION_MESSAGE);
			return null;
		}
		if(!path.toString().endsWith(".jvd")) {
			JOptionPane.showMessageDialog(JVDraw.this, "File must end with .jvd", "Information", JOptionPane.INFORMATION_MESSAGE);
			return null;
		}
		
		return path;
	}
	
	/**
	 * Creates List of String objects each row
	 * representing one geometrical object in model.
	 * 
	 * @return	List of String representation of geometrical objects.
	 */
	private List<String> getDataToSave(){
		List<String> data = new ArrayList<>();
		
		for(int i = 0;i < model.getSize(); i++) {
			data.add(model.getObject(i).getDataString());
		}
		
		return data;
	}
	
	/**
	 * Method that converts given string data
	 * into valid geometrical object and
	 * adds it into model.
	 * 
	 * @param data	String representation of geometrical object.
	 */
	public void addToModel(String data) {
		String[] pts = data.split(" ");
		if(pts[0].equals("LINE")) {
			Point start = new Point(Integer.parseInt(pts[1]), Integer.parseInt(pts[2]));
			Point end = new Point(Integer.parseInt(pts[3]), Integer.parseInt(pts[4]));
			Color color = new Color(Integer.parseInt(pts[5]), Integer.parseInt(pts[6]), Integer.parseInt(pts[7]));
			
			model.add(new Line(start, end, color));
		}else if(pts[0].equals("CIRCLE")) {
			Point center = new Point(Integer.parseInt(pts[1]), Integer.parseInt(pts[2]));
			int radius = Integer.parseInt(pts[3]);
			Color color = new Color(Integer.parseInt(pts[4]), Integer.parseInt(pts[5]), Integer.parseInt(pts[6]));
			
			model.add(new Circle(center, radius, color));
		}else if(pts[0].equals("FCIRCLE")) {
			Point center = new Point(Integer.parseInt(pts[1]), Integer.parseInt(pts[2]));
			int radius = Integer.parseInt(pts[3]);
			Color fColor = new Color(Integer.parseInt(pts[4]), Integer.parseInt(pts[5]), Integer.parseInt(pts[6]));
			Color bColor = new Color(Integer.parseInt(pts[7]), Integer.parseInt(pts[8]), Integer.parseInt(pts[9]));
			
			model.add(new FilledCircle(center, radius, fColor, bColor));
		}else if(pts[0].equals("FPOLY")) {
			List<Point> points = new ArrayList<>();
			for(int i = 1; i < pts.length - 6;i+=2) {
				points.add(new Point(Integer.parseInt(pts[i]), Integer.parseInt(pts[i+1])));
			}
			
			Color fColor = new Color(Integer.parseInt(pts[pts.length - 6]), Integer.parseInt(pts[pts.length-5]), Integer.parseInt(pts[pts.length-4]));
			Color bColor = new Color(Integer.parseInt(pts[pts.length - 3]), Integer.parseInt(pts[pts.length-2]), Integer.parseInt(pts[pts.length-1]));
			
			model.add(new KonvPoly(points, fColor, bColor));
		}
	}
	
	/**
	 * Method that removes all elements from model.
	 */
	private void clear() {
		while(model.getSize() != 0) {
			model.remove(model.getObject(0));
		}
	}
	
	/**
	 * Setter method for saved flag.
	 * 
	 * @param saved	saved flag.
	 */
	public void setSaved(boolean saved) {
		this.saved = saved;
	}
}
