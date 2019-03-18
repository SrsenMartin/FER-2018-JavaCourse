package hr.fer.zemris.java.raytracer.model;

import static java.lang.Math.sqrt;

/**
 * Implementation of GraphicalObject that represents sphere object
 * that can exist in our scene.
 * Every sphere has its center as vector, radius and various constants used
 * in lightning model for painting.
 * 
 * @author Martin Sršen
 *
 */
public class Sphere extends GraphicalObject {

	/**
	 * Sphere center given as vector.
	 */
	private Point3D center;
	/**
	 * Sphere radius.
	 */
	private double radius;
	/**
	 * Coefficient for diffuse component for red color.
	 */
	private double kdr;
	/**
	 * Coefficient for diffuse component for green color.
	 */
	private double kdg;
	/**
	 * Coefficient for diffuse component for blue color.
	 */
	private double kdb;
	/**
	 * Coefficient for reflective component for red color.
	 */
	private double krr;
	/**
	 * Coefficient for reflective component for green color.
	 */
	private double krg;
	/**
	 * Coefficient for reflective component for blue color.
	 */
	private double krb;
	/**
	 * Coefficient for reflective component.
	 */
	private double krn;
	
	/**
	 * Constructor for sphere to initialize its state.
	 * 
	 * @param center	Sphere center given as vector.
	 * @param radius	Sphere radius.
	 * @param kdr	Coefficient for diffuse component for red color.
	 * @param kdg	Coefficient for diffuse component for green color.
	 * @param kdb	Coefficient for diffuse component for blue color.
	 * @param krr	Coefficient for reflective component for red color.
	 * @param krg	Coefficient for reflective component for green color.
	 * @param krb	Coefficient for reflective component for blue color.
	 * @param krn	Coefficient for reflective component.
	 */
	public Sphere(Point3D center, double radius, double kdr, double kdg,
			double kdb, double krr, double krg, double krb, double krn) {
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}
	
	/**
	 * This method calculates intersection between given ray and sphere.
	 * In case there exists more than one intersection, this method must return
	 * first (closest) intersection encountered by observer that is placed
	 * in point {@linkplain Ray#start} and that looks in direction determined
	 * by {@linkplain Ray#direction}. Intersections that are behind observer
	 * are not considered or returned. If there exists no acceptable intersection
	 * between given ray and this object, the method must return <code>null</code>.
	 * 
	 * @param ray ray for which intersections are searched
	 * @return closest intersection of sphere and given ray that is in front
	 *         of observer of <code>null</code> if such intersection does not exists.
	 */
	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		double b = ray.direction.scalarProduct(ray.start.sub(center));
		double c = ray.start.sub(center).scalarProduct(ray.start.sub(center)) - radius*radius;
		
		double det = b*b - c;
		if(det < -1E-7)	return null;
		
		double detSqrt = sqrt(det);
		double d = 0;
		if(Math.abs(det) < 1E-7) {
			d = -b;
		}else {
			d = (-b + detSqrt) < (-b - detSqrt) ? (-b + detSqrt) : (-b - detSqrt);
		}
		
		return new SphereRayIntersection(ray.start.add(ray.direction.scalarMultiply(d)), d, true);
	}
	
	/**
	 * Implementation of RayIntersection class that represents 
	 * intersection of ray and sphere.
	 * They can have inner and outer intersection.
	 * Inner intersection is intersection where ray leaves sphere,
	 * and outer is where ray enters sphere.
	 * Each intersection is represented by intersection point as
	 * vector, distance between ray and sphere and whether it 
	 * is outer or inner intersection.
	 * Contains getters for normal in intersection point and
	 * getters for coefficients.
	 *
	 */
	private class SphereRayIntersection extends RayIntersection{

		/**
		 * Constructor to create intersection between ray and sphere.
		 * It is defines by intersection point,distance and whether it
		 * is inner or outer intersection.
		 * 
		 * @param point	Point of intersection as vector.
		 * @param distance	Distance between ray start and intersection.
		 * @param outer	Whether it is outer intersection.
		 */
		protected SphereRayIntersection(Point3D point, double distance, boolean outer) {
			super(point, distance, outer);
		}

		/**
		 * Method that calculates and returns normal of intersection point on sphere.
		 * 
		 * @return normal of intersection point on sphere.
		 */
		@Override
		public Point3D getNormal() {
			return center.sub(this.getPoint()).normalize();
		}

		/**
		 * Getter for coefficient for diffuse component for red color.
		 */
		@Override
		public double getKdr() {
			return kdr;
		}

		/**
		 * Getter for coefficient for diffuse component for green color.
		 */
		@Override
		public double getKdg() {
			return kdg;
		}

		/**
		 * Getter for coefficient for diffuse component for blue color.
		 */
		@Override
		public double getKdb() {
			return kdb;
		}

		/**
		 * Getter for coefficient for reflective component for red color.
		 */
		@Override
		public double getKrr() {
			return krr;
		}

		/**
		 * Getter for coefficient for reflective component for green color.
		 */
		@Override
		public double getKrg() {
			return krg;
		}

		/**
		 * Getter for coefficient for reflective component for blue color.
		 */
		@Override
		public double getKrb() {
			return krb;
		}

		/**
		 * Getter for coefficient for reflective component.
		 */
		@Override
		public double getKrn() {
			return krn;
		}
	}
}
