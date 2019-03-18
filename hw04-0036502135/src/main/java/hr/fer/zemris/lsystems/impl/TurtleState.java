package hr.fer.zemris.lsystems.impl;

import java.awt.Color;

import hr.fer.zemris.math.Vector2D;

import static java.lang.Math.sqrt;

/**
 * Class that represents turtle state.
 * State is made from position,direction of a turtle,
 * line color and scale of a turtle move.
 * 
 * @author Martin Sršen
 *
 */
public class TurtleState {

	/**
	 * Current turtle position as Vector2D.
	 */
	private Vector2D position;
	/**
	 * Current turtle direction as Vector2D.
	 */
	private Vector2D direction;
	/**
	 * Current turtle line color.
	 */
	private Color color;
	/**
	 * Current turtle move scale.
	 */
	private double scale;
	
	/**
	 * Constructor that initialises turtle state with given arguments.
	 * If given direction is not unit vector,it calls method that makes and returns it.
	 * 
	 * @param position	Given position of turtle.
	 * @param direction	Given direction of turtle.
	 * @param color	Given line color of turtle.
	 * @param scale	Given move scale of turtle.
	 * @throws NullPointerException if null value is given as argument.
	 */
	public TurtleState(Vector2D position, Vector2D direction, Color color, double scale) {
		if(position == null || direction == null || color == null) {
			throw new NullPointerException("TurtleState can't accept null value.");
		}
		
		this.position = position;
		this.color = color;
		this.scale = scale;
		
		//direction, ako nije jedinicni vektor pretvoriti ga.
		this.direction = getUnitVector(direction);
	}

	/**
	 * Creates and returns new TurtleState that is copy of current one.
	 * 
	 * @return	Copy of this TurtleState.
	 */
	public TurtleState copy() {
		return new TurtleState(position.copy(), direction.copy(), color, scale);
	}
	
	/**
	 * Getter method for turtle direction.
	 * 
	 * @return	turtle direction.
	 */
	public Vector2D getDirection() {
		return direction;
	}
	
	/**
	 * Getter method for turtle position.
	 * 
	 * @return	turtle position.
	 */
	public Vector2D getPosition() {
		return position;
	}
	
	/**
	 * Getter method for turtle line color.
	 * 
	 * @return line color.
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Getter method for turtle move scale.
	 * 
	 * @return	turtle move scale.
	 */
	public double getScale() {
		return scale;
	}
	
	/**
	 * Setter method for turtle move scale.
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}
	
	/**
	 * Setter method for turtle line color.
	 * 
	 * @param color	line color.
	 */
	public void setColor(Color color) {
		if(color == null) {
			throw new NullPointerException("TurtleState can't accept null value.");
		}
		
		this.color = color;
	}
	
	/**
	 * Method that takes Vector2D as argument,
	 * alters its magnitude to be unit vector and
	 * returns altered vector.
	 * 
	 * @param vector	Vector to make unit vector from.
	 * @return	altered vector.
	 */
	private Vector2D getUnitVector(Vector2D vector) {
		double x = vector.getX();
		double y = vector.getY();
		double magnitude = sqrt(x*x + y*y);
		
		if(magnitude == 0.) {
			throw new IllegalArgumentException("Direction can't be (0,0).");
		}
		
		vector.scale(1 / magnitude);
		
		return vector;
	}
}
