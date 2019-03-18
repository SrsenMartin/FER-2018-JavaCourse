package hr.fer.zemris.java.gui.layouts;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;

/**
 * The CalcLayout is a layout manager that lays out a container's components in
 * Calculator preferred grid.
 * The container is divided into 30 equal-sized rectangles and 1 screen rectangle at position 1,1
 * , and one component is placed in each rectangle.
 * Component position is determined by RCPosition class that contains width and height index to place at.
 * Container is sized as there are always all components added.
 * Has constant number of rows(5) and columns(7).
 * 
 * @author Martin Sršen
 *
 */
public class CalcLayout implements LayoutManager2 {

	/**
	 * Number of rows.
	 */
	public static final int ROW_COUNT = 5;
	/**
	 * Number of columns.
	 */
	public static final int COLUMN_COUNT = 7;
	/**
	 * Gap between components.
	 */
	private final int gap;
	/**
	 * Map of components and their positions.
	 */
	private Map<Component, RCPosition> components;
	
	/**
	 * Constructor that takes gap as argument.
	 * 
	 * @param gap	Gap between elements.
	 * @throws CalcLayoutException if given gap is negative.
	 */
	public CalcLayout(int gap) {
		if(gap < 0) {
			throw new CalcLayoutException("Gap can't be lower than 0.");
		}
		
		this.gap = gap;
		components = new HashMap<>();
	}
	
	/**
	 * Default constructor that initializes gap with 0.
	 */
	public CalcLayout() {
		this(0);
	}

	/**
	 * Lays out the specified container using this layout.
	 * This method reshapes the components in the specified target container in order to satisfy the constraints of the CalcLayout object.
	 * The CalcLayout manager determines the size of individual components by dividing the free space in the container into equal-sized 
	 * portions according to the number of rows and columns in the layout. The container's free space equals the container's size minus 
	 * any insets and any specified horizontal or vertical gap. All components in a CalcLayout are given the same size.
	 * 
	 * @param parent the container in which to do the layout.
	 */
	@Override
	public void layoutContainer(Container parent) {
		Dimension prefComponentSize = new Dimension(0, 0);
		Insets insets = parent.getInsets();
		
		getComponentDimension(prefComponentSize, comp -> comp.getPreferredSize(), (a, b) -> max(a, b));
		
		double wantedCompWidth = (double) (parent.getWidth()-insets.left-insets.right-6*gap) / COLUMN_COUNT;
		double wantedCompHeight = (double) (parent.getHeight()-insets.top-insets.bottom-4*gap) / ROW_COUNT;
		
		double widthScale = wantedCompWidth / prefComponentSize.width;
		double heightScale = wantedCompHeight / prefComponentSize.height;
		
		prefComponentSize.height *= heightScale;
		prefComponentSize.width *= widthScale;
		
		drawElements(prefComponentSize, insets);
	}

	/**
	 * Determines the maximum size of the container argument using this CalcLayout.
	 * The maximum width of a CalcLayout is the smallest maximum width of all of the components in the container times the number of columns,
	 *  plus the horizontal padding times the number of columns minus one, plus the left and right insets of the target container,
	 *  same for maximum height.
	 *  
	 *  @param parent the container in which to do the layout.
	 */
	@Override
	public Dimension maximumLayoutSize(Container parent) {
		Dimension maxDimension = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
		
		getLayoutDimension(parent, maxDimension, comp -> comp.getMaximumSize(), (a, b) -> min(a, b));

		return maxDimension;
	}
	
	/**
	 * Determines the minimum size of the container argument using this CalcLayout.
	 * The minimum width of a CalcLayout is the largest minimum width of all of the components in the container times the number of columns,
	 *  plus the horizontal padding times the number of columns minus one, plus the left and right insets of the target container,
	 *  same for minimum height.
	 *  
	 *  @param parent the container in which to do the layout.
	 */
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		Dimension minDimension = new Dimension(0, 0);
		
		getLayoutDimension(parent, minDimension, comp -> comp.getMinimumSize(), (a, b) -> max(a, b));

		return minDimension;
	}

	/**
	 * Determines the preferred size of the container argument using this CalcLayout.
	 * The preferred width of a CalcLayout is the largest preferred width of all of the components in the container times the number of columns,
	 *  plus the horizontal padding times the number of columns minus one, plus the left and right insets of the target container,
	 *  same for preferred height.
	 *  
	 *  @param parent the container in which to do the layout.
	 */
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		Dimension prefDimension = new Dimension(0, 0);
		
		getLayoutDimension(parent, prefDimension, comp -> comp.getPreferredSize(), (a, b) -> max(a, b));

		return prefDimension;
	}

	/**
	 * Removes the specified component from the layout.
	 * 
	 * @param comp the component to be removed.
	 */
	@Override
	public void removeLayoutComponent(Component comp) {
		components.remove(comp);
	}

	/**
	 * Adds the specified component with the specified position to the layout.
	 * 
	 * @param comp the component to be added.
	 * @param position RCPosition object, or String representing in form (row,column).
	 * @throws CalcLayoutException if given component is null, invalid position is given,
	 * 								or component already exists at given position.
	 */
	@Override
	public void addLayoutComponent(Component comp, Object position) {
		if(comp == null) {
			throw new CalcLayoutException("Components as null values cannot be added.");
		}
		
		RCPosition given = null;
		if(position instanceof RCPosition) {
			given = (RCPosition) position;
		}else if(position instanceof String) {
			given = parsePosition((String) position);
		}else {
			throw new CalcLayoutException("Invalid RCPoisition provided.");
		}
		
		for(RCPosition pos: components.values()) {
			if(given.getRow() == pos.getRow() && given.getColumn() == pos.getColumn()) {
				throw new CalcLayoutException("Component already exists at given position:"
						+ " (" + given.getRow() + ", " + given.getColumn() + ").");
			}
		}
		
		components.put(comp, given);
	}

	/**
	 * Returns the alignment along the x axis.
	 * This specifies how the component would like to be aligned relative to other components.
	 * 
	 * @param parent the container in which to do the layout.
	 */
	@Override
	public float getLayoutAlignmentX(Container parent) {
		return 0.5f;
	}

	/**
	 * Returns the alignment along the y axis.
	 * This specifies how the component would like to be aligned relative to other components.
	 * 
	 * @param parent the container in which to do the layout.
	 */
	@Override
	public float getLayoutAlignmentY(Container parent) {
		return 0.5f;
	}

	/**
	 * Invalidates the layout, indicating that if the layout manager has cached information it should be discarded.
	 * Not used in this layout.
	 * 
	 * @param parent the container in which to do the layout.
	 */
	@Override
	public void invalidateLayout(Container parent) {
	}

	/**
	 * Adds the specified component with the specified name to the layout.
	 * Not available in this layout.
	 * 
	 * @param comp the component to be added.
	 * @param name the name of the component.
	 */
	@Override
	public void addLayoutComponent(String name, Component comp) {
	}
	
	/**
	 * Helper method that paints components onto parent container based
	 * on given component dimensions and parent insets.
	 * 
	 * @param prefComponentScaled Component scaled preferred size.
	 * @param insets parent component insets.
	 */
	private void drawElements(Dimension prefComponentScaled, Insets insets) {
		for(Entry<Component, RCPosition> entry : components.entrySet()) {
			Component comp = entry.getKey();
			RCPosition pos = entry.getValue();
			
			if(pos.getColumn() == 1 && pos.getRow() == 1) {
				comp.setBounds(insets.left, insets.top, 5*prefComponentScaled.width + 4*gap, prefComponentScaled.height);
			}else {
				int fromX = insets.left + (pos.getColumn()-1)*(prefComponentScaled.width + gap);
				int fromY = insets.top + (pos.getRow()-1)*(prefComponentScaled.height + gap);
				
				comp.setBounds(fromX, fromY, prefComponentScaled.width, prefComponentScaled.height);
			}
		}
	}
	
	/**
	 * Helper method that parses String input into equivalent RCPositin object,
	 * in format (row,column).
	 * 
	 * @param positionString	String representing RCPositin.
	 * @return	RCPosition created from given String.
	 * @throws CalcLayoutException if given string is not in valid string format.
	 */
	private RCPosition parsePosition(String positionString) {
		String[] pts = positionString.trim().split(",");
		if(pts.length != 2) {
			throw new CalcLayoutException("Invalid RCPoisition provided: " + positionString);
		}
		
		try {
			int row = Integer.parseInt(pts[0].trim());
			int column = Integer.parseInt(pts[1].trim());
			
			return new RCPosition(row, column);
		}catch(Exception ex) {
			throw new CalcLayoutException("Invalid RCPoisition provided: " + positionString);
		}
	}
	
	/**
	 * Helper method that returns components preferred, maximum or minimum dimension,
	 * based on given dimGetter and operator.
	 * Storage is used to save result into.
	 * 
	 * @param storage	Dimension where result is stored.
	 * @param dimGetter	Getter for wanted dimension.
	 * @param operator	Used to compare current dimension and component dimension.
	 */
	private void getComponentDimension(Dimension storage, 
			Function<Component, Dimension> dimGetter, IntBinaryOperator operator) {
		
		for(Entry<Component, RCPosition> entry : components.entrySet()) {
			Component comp = entry.getKey();
			RCPosition pos = entry.getValue();
			
			Dimension size = dimGetter.apply(comp);
			if(size == null)	continue;
			
			storage.height = operator.applyAsInt(size.height, storage.height);
			
			if(pos.getRow() == 1 && pos.getColumn() == 1) {
				storage.width = operator.applyAsInt(storage.width, (size.width - 4*gap)/5);
			}else {
				storage.width = operator.applyAsInt(size.width, storage.width);
			}
		}
	}
	
	/**
	 * Helper method that returns layout preferred, maximum or minimum dimension,
	 * based on given dimGetter and operator.
	 * Storage is used to save result into.
	 * 
	 * @param parent	the container in which to do the layout.	
	 * @param storage	Dimension where result is stored.
	 * @param dimGetter	Getter for wanted dimension.
	 * @param operator	Used to compare current dimension and component dimension.
	 */
	private void getLayoutDimension(Container parent, Dimension storage, 
			Function<Component, Dimension> dimGetter, IntBinaryOperator operator) {
		
		getComponentDimension(storage, dimGetter, operator);
		
		Insets insets = parent.getInsets();
		storage.width = storage.width * COLUMN_COUNT + gap * (COLUMN_COUNT-1) + insets.left + insets.right;
		storage.height = storage.height * ROW_COUNT + gap * (ROW_COUNT-1) + insets.top + insets.bottom;
	}
}
