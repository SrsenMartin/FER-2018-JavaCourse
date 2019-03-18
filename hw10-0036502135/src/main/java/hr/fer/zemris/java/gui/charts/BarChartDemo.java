package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Program that presents use of BarChart component.
 * Takes 1 argument from command prompt, path to file,
 * which contains informations used to draw BarChart.
 * If file and data are valid, program draws bar chart,
 * else writes message to user and shuts program.
 * 
 * @author Martin Sršen
 *
 */
public class BarChartDemo extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor that creates window size,title and
	 * closing operation.
	 */
	public BarChartDemo() {
		setLocation(500, 200);
		setSize(600, 370);
		setTitle("BarChartDemo");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	/**
	 * Helper method that creates frame, puts 1 label that writes
	 * given path to file north and adds drawn BarChartComponent in center.
	 * 
	 * @param chart	BarChart object that contains informations to draw bar chart.
	 * @param path	Path to file where informations are stored.
	 */
	private static void createAndShow(BarChart chart, Path path) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new BarChartDemo();
			Container container = frame.getContentPane();
			
			JLabel pathShower = new JLabel(path.toAbsolutePath().toString());
			pathShower.setHorizontalAlignment(SwingConstants.CENTER);
			container.add(pathShower, BorderLayout.NORTH);
			
			container.add(new BarChartComponent(chart), BorderLayout.CENTER);
			
			frame.setVisible(true);
		});
	}

	/**
	 * Helper method that reads from file, parses its content
	 * and if valid content was provided creates and returns BarChart
	 * containing informations BarChartComponent needs to draw itself.
	 * If invalid input was given, program shuts down.
	 * 
	 * @param path	Path to file where informations are stored.
	 * @return	BarChart object that contains informations to draw bar chart.
	 * @throws IOException	If something wrong happens reading data from file.
	 */
	private static BarChart getBarChart(Path path) throws IOException {
		List<String> lines = Files.readAllLines(path);
		if(lines.size() != 6) {
			System.out.println("Invalid file content given.");
			System.exit(-1);
		}
		
		String xAxisDescription = lines.get(0);
		String yAxisDescription = lines.get(1);
		List<XYValue> values = getXYValues(lines.get(2));
		int minY = checkInt(lines.get(3));
		int maxY = checkInt(lines.get(4));
		int yGap = checkInt(lines.get(5));
		
		return new BarChart(values, xAxisDescription, yAxisDescription, minY, maxY, yGap);
	}

	/**
	 * Helper method that parses line where xy values were provided.
	 * Adds these parsed values into list and returns list.
	 * If invalid xy value is given, program shuts down.
	 * 
	 * @param input	Line where xy values were provided.
	 * @return	list of XYValue objects.
	 */
	private static List<XYValue> getXYValues(String input) {
		List<XYValue> values = new ArrayList<>();
		
		String[] points = input.trim().split("\\s+");
		for(String point : points) {
			String dots[] = point.trim().split(",");
			
			if(dots.length != 2) {
				System.out.println("Only 2 dots per point are accepted.");
				System.exit(-1);
			}
			
			values.add(new XYValue(checkInt(dots[0]), checkInt(dots[1])));
		}
		
		return values;
	}
	
	/**
	 * Helper method that checks if given String is number
	 * as was expected.
	 * If yes, returns that number as int, else program shuts down.
	 * 
	 * @param input	String that should represent number.
	 * @return	int parsed from string.
	 */
	private static int checkInt(String input) {
		try {
			return Integer.parseInt(input.trim());
		}catch(NumberFormatException ex) {
			System.out.println("Invalid file row given: " + input);
			System.exit(-1);
		}
		
		return 0;
	}
	
	/**
	 * Called when program is started.
	 * Program takes command prompt input that represents
	 * path to file where BarChart informations are provided.
	 * 
	 * @param args	Arguments from command prompt.
	 */
	public static void main(String[] args) throws IOException {
		if(args.length != 1) {
			System.out.println("Program exprects 1 argument, file path.");
			System.exit(-1);
		}
		
		Path path = Paths.get(args[0]);
		if(!Files.isRegularFile(path)) {
			System.out.println("Invalid file path given.");
			System.exit(-1);
		}
		
		BarChart chart = getBarChart(path);
		createAndShow(chart, path);
	}
}
