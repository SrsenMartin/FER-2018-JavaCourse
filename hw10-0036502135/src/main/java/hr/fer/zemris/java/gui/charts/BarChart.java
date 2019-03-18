package hr.fer.zemris.java.gui.charts;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Class that contains all informations needed to draw BarChart.
 * Contains list of xy values, x and y axis descriptions,
 * minimum y, maximum y and gap between 2 y grid values.
 * 
 * @author Martin Sršen
 *
 */
public class BarChart {

	/**
	 * List of xy values.
	 */
	private final List<XYValue> values;
	/**
	 * X axis description.
	 */
	private final String xAxisDescription;
	/**
	 * Y axis description.
	 */
	private final String yAxisDescription;
	/**
	 * Minimum y on coordinate system.
	 */
	private final int minY;
	/**
	 * Maximum y on coordinate system.
	 */
	private final int maxY;
	/**
	 * Gap between 2 y grid values.
	 */
	private final int yGap;
	
	/**
	 * Constructor that creates and initializes BarChart object.
	 * 
	 * @param values	List of xy values.
	 * @param xAxisDescription	X axis description.
	 * @param yAxisDescription	Y axis description.
	 * @param minY	Minimum y on coordinate system.
	 * @param maxY	Maximum y on coordinate system.
	 * @param yGap	Gap between 2 y grid values.
	 */
	public BarChart(List<XYValue> values, String xAxisDescription, String yAxisDescription, int minY, int maxY, int yGap) {
		Objects.requireNonNull(values, "Values list can't be null.");
		Objects.requireNonNull(xAxisDescription, "xAxis name can't be null.");
		Objects.requireNonNull(yAxisDescription, "yAxis name can't be null.");
		if(values.size() == 0)	throw new IllegalArgumentException("There must be atleast 1 value to draw.");
		if(minY >= maxY)	throw new IllegalArgumentException("Minimum y can't be bigger than maximum y.");
		if(yGap <= 0)	throw new IllegalArgumentException("Y gap must be positive integer.");
		
		Collections.sort(values, (a,b) -> Integer.valueOf(a.getX()).compareTo(Integer.valueOf(b.getX())));
		this.values = values;
		
		this.xAxisDescription = xAxisDescription;
		this.yAxisDescription = yAxisDescription;
		this.minY = minY;
		this.maxY = maxY;
		
		while((maxY-minY) % yGap != 0) {
			yGap++;
		}
		this.yGap = yGap;
	}

	/**
	 * Getter for list of xy values.
	 * 
	 * @return	list of xy values.
	 */
	public List<XYValue> getValues() {
		return values;
	}

	/**
	 * Getter for x axis description.
	 * 
	 * @return	x axis description.
	 */
	public String getxAxisDescription() {
		return xAxisDescription;
	}

	/**
	 * Getter for y axis description.
	 * 
	 * @return	y axis description.
	 */
	public String getyAxisDescription() {
		return yAxisDescription;
	}

	/**
	 * Getter for minimum y on coordinate system.
	 * 
	 * @return	minimum y on coordinate system.
	 */
	public int getMinY() {
		return minY;
	}

	/**
	 * Getter for maximum y on coordinate system.
	 * 
	 * @return	maximum y on coordinate system.
	 */
	public int getMaxY() {
		return maxY;
	}

	/**
	 * Getter for gap between 2 y grid values.
	 * 
	 * @return	gap between 2 y grid values.
	 */
	public int getyGap() {
		return yGap;
	}
}
