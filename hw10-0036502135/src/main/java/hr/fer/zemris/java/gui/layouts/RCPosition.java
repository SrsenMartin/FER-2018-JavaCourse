package hr.fer.zemris.java.gui.layouts;

/**
 * Class that represents index position for CalcLayout.
 * Each RCPosition is represented as row and column index.
 * RCPosition width must be between 1 and 7, 
 * and height between 1 and 5.
 * If row is 1, columns must be 1, 6 or 7.
 * 
 * @author Martin Sršen
 *
 */
public class RCPosition {

	/**
	 * Row index.
	 */
	private final int row;
	/**
	 * Column index.
	 */
	private final int column;
	
	/**
	 * Constructor to initialize RCPosition.
	 * Takes row and column indexes.
	 * 
	 * @param row	Row index.
	 * @param column	Column index.
	 * @throws CalcLayoutException if invalid row, index numbers are given.
	 */
	public RCPosition(int row, int column) {
		if(row < 1 || row > 5 ||
				column < 1 || column > 7 ||
				(row == 1 && column > 1 && column < 6)) {
			throw new CalcLayoutException("Invalid position given: (" + row + ", " + column + ").");
		}
		
		this.row = row;
		this.column = column;
	}
	
	/**
	 * Getter for row index.
	 * 
	 * @return	row index.
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * Getter for column index.
	 * 
	 * @return	column index.
	 */
	public int getColumn() {
		return column;
	}
}
