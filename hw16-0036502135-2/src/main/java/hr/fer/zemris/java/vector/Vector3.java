package hr.fer.zemris.java.vector;

import static java.lang.Math.sqrt;

import java.util.Objects;

/**
 * Class that represents 3D Vector. Has various methods 
 * to return new vector created by given calculations on 
 * original vector while original one stays unchanged.
 * 
 * @author Martin Sršen
 *
 */
public class Vector3 {

	/**
	 * Represents vector x axis on coordinate system.
	 */
	private final double x;
	/**
	 * Represents vector y axis on coordinate system.
	 */
	private final double y;
	/**
	 * Represents vector z axis on coordinate system.
	 */
	private final double z;
	
	/**
	 * Constructor that makes new Vector with given x, y and z.
	 * 
	 * @param x	Vector x axis.
	 * @param y	Vector y axis.
	 * @param z Vector z axis.
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Calculates and returns norm for current Vector3 object.
	 * 
	 * @return	norm of current vector.
	 */
	public double norm() {
		return sqrt(x*x + y*y + z*z);
	}
	
	/**
	 * Returns new Vector3 that has same direction as current vector,
	 * but whose norm is 1.
	 * This vector stays unchanged.
	 * 
	 * @return	new normalized Vector3 object.
	 * @throws	IllegalArgumentException	if norm of current vector is 0.
	 */
	public Vector3 normalized() {
		double norm = norm();
		if(norm == 0.) {
			throw new IllegalArgumentException("Vector norm can't be 0.");
		}
		
		return new Vector3(x/norm, y/norm, z/norm);
	}
	
	/**
	 * Returns new Vector3 with values that are calculated by
	 * adding this vector to other vector.
	 * This vector stays unchanged.
	 * 
	 * @param other	Other vector used to calculate add operation on vectors.
	 * @return	new Vector3 with value of this vector added to other vector.
	 * @throws NullPointerException	if given Vector3 is null.
	 */
	public Vector3 add(Vector3 other) {
		Objects.requireNonNull(other, "Vector3 can't be null.");
		
		return new Vector3(x+other.getX(), y+other.getY(), z+other.getZ());
	}
	
	/**
	 * Returns new Vector3 with values that are calculated by
	 * subtracting this vector to other vector.
	 * This vector stays unchanged.
	 * 
	 * @param other	Other vector used to calculate sub operation on vectors.
	 * @return	new Vector3 with value of this vector subtracted to other vector.
	 * @throws NullPointerException	if given Vector3 is null.
	 */
	public Vector3 sub(Vector3 other) {
		Objects.requireNonNull(other, "Vector3 can't be null.");
		
		return new Vector3(x-other.getX(), y-other.getY(), z-other.getZ());
	}
	
	/**
	 * Returns scalar product of this vector and other vector.
	 * 
	 * @param other	given vector used to calculate scalar product.
	 * @return	scalar product of this vector and other vector.
	 * @throws NullPointerException	if given Vector3 is null.
	 */
	public double dot(Vector3 other) {
		Objects.requireNonNull(other, "Vector3 can't be null.");
		
		return x*other.getX() + y*other.getY() + z*other.getZ();
	}
	
	/**
	 * Creates and returns new Vector3 which is
	 * vector product of this vector and other vector.
	 * This vector stays unchanged.
	 * 
	 * @param other	Used to calculate vector product.
	 * @return	new Vector3 object created by vector product of this and other vector.
	 * @throws NullPointerException	if given Vector3 is null.
	 */
	public Vector3 cross(Vector3 other) {
		Objects.requireNonNull(other, "Vector3 can't be null.");
		
		double newX = y*other.getZ() - z*other.getY();
		double newY = z*other.getX() - x*other.getZ();
		double newZ = x*other.getY() - y*other.getX();
		
		return new Vector3(newX, newY, newZ);
	}
	
	/**
	 * Creates and returns new vector made by scaling this vector.
	 * This vector stays unchanged.
	 * 
	 * @param s	factor used to make scaling with this vector.
	 * @return	new Vector made by scaling this vector by scaler.
	 */
	public Vector3 scale(double s) {
		return new Vector3(x*s, y*s, z*s);
	}
	
	/**
	 * Calculates and returns cosines angle between this 
	 * and other vector.
	 * 
	 * @param other	used to create angle with current vector.
	 * @return	cosines angle between this and other vector.
	 * @throws IllegalArgumentException if norm of any vector is 0.
	 * @throws NullPointerException	if given Vector3 is null.
	 */
	public double cosAngle(Vector3 other) {
		Objects.requireNonNull(other, "Vector3 can't be null.");
		
		double normThis = norm();
		double normOther = other.norm();
		
		if(normThis == 0. || normOther == 0.) {
			throw new IllegalArgumentException("Norm can't be 0 in any vector.");
		}
		
		return dot(other)/(normThis * normOther);
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
	 * Getter of vector z axis.
	 * 
	 * @return	Vector z axis.
	 */
	public double getZ() {
		return z;
	}
	
	/**
	 * Returns this vector represented as double array of 3 elements.
	 * 
	 * @return	this vector as double array.
	 */
	public double[] toArray() {
		return new double[] {x, y, z};
	}
	
	/**
	 * String representation of this Vector3 object.
	 * Written using 6 decimal places.
	 * 
	 * @return	String representation of this vector.
	 */
	@Override
	public String toString() {
		return String.format("(%.6f, %.6f, %.6f)", x, y, z);
	}
	
	
}
