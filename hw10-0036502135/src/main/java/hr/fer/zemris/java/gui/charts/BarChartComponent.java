package hr.fer.zemris.java.gui.charts;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

/**
 * Custom made component that extends from JComponent.
 * Component that represents bar chart components.
 * Takes BarChart object that contains bar chart informations
 * used to draw bar chart component onto GUI.
 * 
 * @author Martin Sršen
 *
 */
public class BarChartComponent extends JComponent {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * BarChart object containing information for drawing.
	 */
	private final BarChart chart;
	
	/**
	 * Constructor that takes BarChart object used to draw chart.
	 * 
	 * @param chart	object used to get information for drawing chart.
	 */
	public BarChartComponent(BarChart chart) {
		this.chart = chart;
	}
	
	/**
	 * Calls the UI delegate's paint method, if the UI delegate is non-null.
	 * We pass the delegate a copy of the Graphics object to protect the rest
	 * of the paint code from irrevocable changes (for example, Graphics.translate).
	 * 
	 * We override this method so we can create custom component look.
	 * 
	 * @param graphics the Graphics object used to draw.
	 */
	@Override
	protected void paintComponent(Graphics graphics) {
		Graphics2D g2d = (Graphics2D) graphics;
		
		int height = getHeight();
		int width = getWidth();

		drawChart(g2d, width, height);
	}
	
	/**
	 * Helper method that draws whole chart from given dimension and graphics object used to draw.
	 * 
	 * @param g2d	 the Graphics2D object used to draw.
	 * @param width	Widht dimension of BarChart component
	 * @param height	Height dimension of BarChart component.
	 */
	private void drawChart(Graphics2D g2d, int width, int height) {	
		g2d.setColor(Color.GRAY);
		g2d.setFont(new Font("serif", Font.BOLD, 15));
		int maxNumberWidth = Math.max(g2d.getFontMetrics().stringWidth(Integer.toString(chart.getMaxY())),
				g2d.getFontMetrics().stringWidth(Integer.toString(chart.getMinY())));
		
		int x_startx = 50 + maxNumberWidth;
		int x_endx = width - 30;
		int x_startEndy = height-60;
		int y_startEndx = x_startx;
		int y_starty = 30;
		int y_endy = x_startEndy;
		
		int yParts = (chart.getMaxY()-chart.getMinY())/chart.getyGap();
		int yPartHeight = (y_endy - y_starty)/yParts;
		int xParts = chart.getValues().size();
		int xPartWidth = (x_endx - x_startx)/xParts;
		
		drawXYAxis(g2d, x_startx, x_startEndy, x_endx, y_startEndx, y_starty, y_endy);
		drawHorizontalComponents(g2d, y_starty, y_endy, x_startx, x_endx, yParts, yPartHeight);
		drawVerticalComponents(g2d, x_endx, x_startx, y_endy, y_starty, xParts, xPartWidth);
		drawAxisNames(g2d, x_endx, x_startx, height, y_endy, y_starty);
		drawBarChart(g2d, xParts, xPartWidth, yPartHeight, y_endy, x_startx);
		
		g2d.dispose();
	}
	
	/**
	 * Helper method used to draw components rectangle bars on the chart.
	 * 
	 * @param g2d	the Graphics2D object used to draw.
	 * @param xParts	number of x values.
	 * @param xPartWidth	width of each x value.
	 * @param yPartHeight	height of 1 gap part.
	 * @param y_endy	y axis end y.
	 * @param x_startx	x axis start x.
	 */
	private void drawBarChart(Graphics2D g2d, int xParts, int xPartWidth, int yPartHeight, int y_endy, int x_startx) {
		g2d.setPaint(new Color(220, 120, 80));
		
		for(int counter = 0; counter < xParts; counter++) {
			int y = chart.getValues().get(counter).getY();
			int yDifference = y - chart.getMinY();
			int yChartHeight = (int) ((double) yDifference/chart.getyGap() * yPartHeight);
			
			g2d.fill3DRect(x_startx + counter*xPartWidth, y_endy-yChartHeight, xPartWidth, yChartHeight, true);
		} 
	}
	
	/**
	 * Helper method used to draw components x and y axis.
	 * 
	 * @param g2d	the Graphics2D object used to draw.
	 * @param x_startx	x axis start x.
	 * @param x_startEndy	x axis start and end y.
	 * @param x_endx	x axis end x.
	 * @param y_startEndx	y axis start and end x.
	 * @param y_starty	y axis start y.
	 * @param y_endy	y axis end y.
	 */
	private void drawXYAxis(Graphics2D g2d, int x_startx, int x_startEndy, int x_endx, int y_startEndx, int y_starty, int y_endy) {
		g2d.drawLine(x_startx, x_startEndy, x_endx+5, x_startEndy);
		g2d.fillPolygon(new int[] {x_endx+5, x_endx+5, x_endx+10}, new int[] {x_startEndy+5, x_startEndy-5, x_startEndy}, 3);
		
		g2d.drawLine(y_startEndx, y_starty-5, y_startEndx, y_endy);
		g2d.fillPolygon(new int[] {y_startEndx-5, y_startEndx+5, y_startEndx}, new int[] {y_starty-5, y_starty-5, y_starty-10}, 3);
	}
	
	/**
	 * Helper method used to draw axis names onto component screen.
	 * 
	 * @param g2d	the Graphics2D object used to draw.
	 * @param x_endx	x axis end x.
	 * @param x_startx	x axis start x.
	 * @param height	height of this component.
	 * @param y_endy	y axis end y.
	 * @param y_starty	y axis start y.
	 */
	private void drawAxisNames(Graphics2D g2d, int x_endx, int x_startx, int height, int y_endy, int y_starty) {
		g2d.drawString(chart.getxAxisDescription(), 
				(x_endx-x_startx)/2 - g2d.getFontMetrics().stringWidth(chart.getxAxisDescription())/2 + 50, height-10);
		
		g2d.rotate(Math.toRadians(-90));
		g2d.drawString(chart.getyAxisDescription(), -(y_endy-y_starty)/2 - g2d.getFontMetrics().stringWidth(chart.getyAxisDescription()), 20);
		g2d.rotate(Math.toRadians(90));
	}

	/**
	 * Helper method used to draw horizontal components of this component.
	 * Draws grid and number values for grid.
	 * 
	 * @param g2d	the Graphics2D object used to draw.
	 * @param y_starty	y axis start y.
	 * @param y_endy	y axis end y.
	 * @param x_startx	x axis start x.
	 * @param x_endx	x axis end x.
	 * @param yParts	number of y gap parts.
	 * @param yPartHeight	height of 1 gap part.
	 */
	private void drawHorizontalComponents(Graphics2D g2d, int y_starty, int y_endy, int x_startx, int x_endx, int yParts, int yPartHeight) {		
		for(int counter = 0; counter <= yParts; counter++) {
			int lineHeight = y_endy - yPartHeight*counter;
			
			g2d.drawLine(x_startx - 5, lineHeight, x_startx, lineHeight);
			
			g2d.setPaint(Color.ORANGE);
			if(counter != 0) {
				g2d.drawLine(x_startx, lineHeight, x_endx+5, lineHeight);
			}
			
			g2d.setColor(Color.BLACK);
			String number = Integer.toString(counter*chart.getyGap() + chart.getMinY());
			g2d.drawString(number, x_startx - 10 - g2d.getFontMetrics().stringWidth(number), lineHeight+5);
			
			g2d.setPaint(Color.GRAY);
		}
	}
	
	/**
	 * Helper method used to draw vertical components of this component.
	 * Draws grid and number values for grid.
	 * 
	 * @param g2d	the Graphics2D object used to draw.
	 * @param x_endx	x axis end x.
	 * @param x_startx	x axis start x.
	 * @param y_endy	y axis end y.
	 * @param y_starty	y axis start y.
	 * @param xParts	number of x values.
	 * @param xPartWidth	width of each x value.
	 */
	private void drawVerticalComponents(Graphics2D g2d, int x_endx, int x_startx, int y_endy, int y_starty, int xParts, int xPartWidth) {
		for(int counter = 0; counter <= xParts; counter++) {
			int lineWidth =x_startx + xPartWidth*counter;
			int lineWidthPrev = x_startx + xPartWidth*(counter-1);
			
			g2d.drawLine(lineWidth, y_endy + 5, lineWidth, y_endy);
			
			g2d.setPaint(Color.ORANGE);
			if(counter != 0) {
				g2d.drawLine(lineWidth, y_starty-5, lineWidth, y_endy);
				g2d.setPaint(Color.BLACK);
				g2d.drawString(Integer.toString(chart.getValues().get(counter-1).getX()), (lineWidth+lineWidthPrev)/2, y_endy + 25);
			}
			
			g2d.setPaint(Color.GRAY);
		}
	}
}
