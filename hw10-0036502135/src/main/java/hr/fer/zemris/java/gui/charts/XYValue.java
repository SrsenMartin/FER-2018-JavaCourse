package hr.fer.zemris.java.gui.charts;

/**
 * Class that represents x,y value of chartBar coordinate system.
 * X value must be positive int, y can be either positive or negative.
 * Contains real-only attributes x and y.
 * 
 * @author Martin Sršen
 *
 */
public class XYValue {

	/**
	 * X value of barChart value.
	 */
	private final int x;
	/**
	 * Y value of barChart value.
	 */
	private final int y;
	
	/**
	 * Constructor that creates and initializes xy value.
	 * 
	 * @param x	x value of barChart value.
	 * @param y	y value of barChart value.
	 */
	public XYValue(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Getter method for x value of barChart value.
	 * 
	 * @return	x value of barChart value.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Getter method for y value of barChart value.
	 * 
	 * @return	y value of barChart value.
	 */
	public int getY() {
		return y;
	}
}
