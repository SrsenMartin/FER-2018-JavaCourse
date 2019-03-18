package hr.fer.zemris.math;

import static java.lang.Math.sin;
import static java.lang.Math.cos;

/**
 * Class that represents 2D Vector. Has various methods to make calculations on
 * current vector, or to return new vector created by given calculations while
 * original one stays unchanged.
 * 
 * @author Martin Sršen
 *
 */
public class Vector2D {

	/**
	 * Represents vector x axis on coordinate system.
	 */
	private double x;
	/**
	 * Represents vector y axis on coordinate system.
	 */
	private double y;

	/**
	 * Constructor that makes new Vector with given x and y.
	 * 
	 * @param x	Vector x axis.
	 * @param y	Vector y axis.
	 */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Getter of vector x axis.
	 * 
	 * @return	Vector x axis.
	 */
	public double getX() {
		return x;
	}

	/**
	 * Getter of vector y axis.
	 * 
	 * @return	Vector y axis.
	 */
	public double getY() {
		return y;
	}

	/**
	 * Translates this vector by given vector.
	 * 
	 * @param offset	Vector used to translate this vector.
	 */
	public void translate(Vector2D offset) {
		x += offset.x;
		y += offset.y;
	}

	/**
	 * Creates and returns new vector made by translation of this vector
	 * by given vector.
	 * This vector stays unchanged.
	 * 
	 * @param offset	Vector used to make translation with this vector.
	 * @return	new Vector with x and y calculated in translation.
	 */
	public Vector2D translated(Vector2D offset) {
		return new Vector2D(x + offset.x, y + offset.y);
	}

	/**
	 * Rotates this vector by given angle.
	 * 
	 * @param angle	Angle used to rotate this vector.
	 */
	public void rotate(double angle) {
		double rotX = rotatedX(angle);
		double rotY = rotatedY(angle);

		x = rotX;
		y = rotY;
	}

	/**
	 * Creates and returns new vector made by rotation of this vector.
	 * This vector stays unchanged.
	 * 
	 * @param angle	angle used to make rotation with this vector.
	 * @return	new Vector with magnitude of this vector and calculated angle.
	 */
	public Vector2D rotated(double angle) {
		return new Vector2D(rotatedX(angle), rotatedY(angle));
	}

	/**
	 * Scales this vector by given factor.
	 * 
	 * @param scaler	Factor used to scale this vector.
	 */
	public void scale(double scaler) {
		x = x * scaler;
		y = y * scaler;
	}

	/**
	 * Creates and returns new vector made by scaling this vector.
	 * This vector stays unchanged.
	 * 
	 * @param scaler	factor used to make scaling with this vector.
	 * @return	new Vector made by scaling this vector by scaler.
	 */
	public Vector2D scaled(double scaler) {
		return new Vector2D(x * scaler, y * scaler);
	}

	/**
	 * Creates and returns new Vector similar to this vector.
	 * 
	 * @return	Copy of this vector.
	 */
	public Vector2D copy() {
		return new Vector2D(x, y);
	}

	/**
	 * Helper method that calculates new x axis of rotated vector
	 * by a given angle.
	 * 
	 * @param angle	Angle to rotate vector by.
	 * @return	x axis of rotated vector.
	 */
	private double rotatedX(double angle) {
		double radians = Math.toRadians(angle);

		return cos(radians) * x - sin(radians) * y;
	}

	/**
	 * Helper method that calculates new y axis of rotated vector
	 * by a given angle.
	 * 
	 * @param angle	Angle to rotate vector by.
	 * @return	y axis of rotated vector.
	 */
	private double rotatedY(double angle) {
		double radians = Math.toRadians(angle);

		return sin(radians) * x + cos(radians) * y;
	}
}
