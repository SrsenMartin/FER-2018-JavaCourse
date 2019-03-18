package hr.fer.zemris.java.raytracer;

import static java.lang.Math.pow;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Program that knows how to display ray tracer viewer.
 * It calculates color for each pixel based on produced ray tracers.
 * After colors for each pixel are calculated calls method acceptResult
 * that knows how to draw scene onto the frame.
 * Program uses maximum number of threads to calculate all needed data.
 * 
 * @author Martin Sršen
 *
 */
public class RayCasterParallel {

	/**
	 * Called when program is started.
	 * 
	 * @param args	Arguments from command prompt.Not used in this example.
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), new Point3D(10, 0, 0), new Point3D(0, 0, 0),
				new Point3D(0, 0, 10), 20, 20);
	}

	/**
	 * Class that implements from RecursiveAction.
	 * Splits job into many little ones until it satisfy given treshold.
	 * After treshold is satisfied, multiple threads does calculation
	 * of colors. Every thread gets part of all frame splitted by y component.
	 * Has method compute that is called each time job is started.
	 */
	public static class Job extends RecursiveAction {

		private static final long serialVersionUID = 1L;
		
		/**
		 * Viewer point as vector.
		 */
		private Point3D eye;
		/**
		 * Screen corner as vector.
		 */
		private Point3D screenCorner;
		/**
		 * Scene x axis. 
		 */
		private Point3D xAxis;
		/**
		 * Scene y axis.
		 */
		private Point3D yAxis;
		/**
		 * Horizontal length.
		 */
		private double horizontal;
		/**
		 * Vertical length.
		 */
		private double vertical;
		/**
		 * Frame width.
		 */
		private int width;
		/**
		 * Frame height.
		 */
		private int height;
		/**
		 * Scene containing all elements.
		 */
		private Scene scene;
		/**
		 * Used to temporarily save colors.
		 */
		private short[] rgb;
		/**
		 * Red color intensity for each pixel.
		 */
		private short[] red;
		/**
		 * Green color intensity for each pixel.
		 */
		private short[] green;
		/**
		 * Blue color intensity for each pixel.
		 */
		private short[] blue;
		/**
		 * y that will this thread start calculation from.
		 */
		private int yMin;
		/**
		 * y that will this thread calculate to.
		 */
		private int yMax;
		
		/**
		 * Treshold used to split job into smaller ones until thread contains 16 or less y.
		 */
		private static final int treshold = 16;
		
		/**
		 * Constructor used to initialize all needed data for calculation.
		 * 
		 * @param eye	Viewer point as vector.
		 * @param screenCorner	Screen corner as vector.
		 * @param xAxis	Scene x axis. 
		 * @param yAxis	Scene y axis. 
		 * @param horizontal	Horizontal length.
		 * @param vertical	Vertical length.
		 * @param width	Frame width.
		 * @param height	Frame height.
		 * @param scene	Scene containing all elements.
		 * @param red	Red color intensity for each pixel.
		 * @param green	Green color intensity for each pixel.
		 * @param blue	Blue color intensity for each pixel.
		 * @param yMin	y that will this thread start calculation from.
		 * @param yMax	y that will this thread calculate to.
		 */
		public Job(Point3D eye, Point3D screenCorner, Point3D xAxis, Point3D yAxis, double horizontal, double vertical, int width,
				int height, Scene scene, short[] red, short[] green, short[] blue, int yMin, int yMax) {
			this.eye = eye;
			this.screenCorner = screenCorner;
			this.xAxis = xAxis;
			this.yAxis = yAxis;
			this.horizontal = horizontal;
			this.vertical = vertical;
			this.width = width;
			this.height = height;
			this.scene = scene;
			this.rgb = new short[3];
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.yMin = yMin;
			this.yMax = yMax;
		}

		/**
		 * Method called every time this class is created.
		 * If class satisfy given treshold, new thread will compute needed data in its range of y.
		 * Else new jobs will be created until it satisfy treshold.
		 */
		@Override
		public void compute() {
			if(yMax-yMin+1 <= treshold) {
				computeDirect();
				return;
			}
			invokeAll(
				new Job(eye, screenCorner, xAxis, yAxis, horizontal, vertical, width, height, scene,
						red, green, blue, yMin, yMin + (yMax - yMin)/2),
				new Job(eye, screenCorner, xAxis, yAxis, horizontal, vertical, width, height, scene,
						red, green, blue, yMin + (yMax - yMin)/2 + 1, yMax)
			);
		}

		/**
		 * Method called once treshold is satisfied.
		 * New thread calculates colors in its range of y from yMin to yMax.
		 */
		private void computeDirect() {
			int offset = yMin*width;
			
			for (int y = yMin; y <= yMax; y++) {
				for (int x = 0; x < width; x++) {
					Point3D screenPoint = screenCorner.add(xAxis.scalarMultiply((x * horizontal / (width - 1))))
							.sub(yAxis.scalarMultiply(y * vertical / (height - 1)));
					Ray ray = Ray.fromPoints(eye, screenPoint);
					
					tracer(scene, ray, rgb);
					
					red[offset] = rgb[0] > 255 ? 255 : rgb[0];
					green[offset] = rgb[1] > 255 ? 255 : rgb[1];
					blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
					
					offset++;
				}
			}
		}
		
		/**
		 * Helper method that finds closest intersection between
		 * viewer(eye) and screenPoint and using that
		 * determines color for surface.
		 * If there is no intersection pixel is painted black.
		 * 
		 * @param scene	Scene containing all elements.
		 * @param ray	Ray between viewer(eye) and screenPoint.
		 * @param rgb	Array used to temporarily store color values.
		 */
		protected void tracer(Scene scene, Ray ray, short[] rgb) {
			rgb[0] = 0;
			rgb[1] = 0;
			rgb[2] = 0;
			
			RayIntersection closest = findClosestIntersection(scene, ray);
			
			if(closest != null) {
				determineColorFor(closest, rgb, scene, ray);
			}
		}
		
		/**
		 * Helper method that calculates color for founded closest intersection
		 * based on viewers ray and all light sources contained onto scene including all objects.
		 * Color is temporarily saved into rgb array.
		 * 
		 * @param closest	Closest ray intersection of viewerRay and screenPoint.
		 * @param rgb	Used to temporarily save color for current pixel.
		 * @param scene	Scene containing all elements.
		 * @param viewerRay	Ray from viewer(eye) onto the scene.
		 */
		private void determineColorFor(RayIntersection closest, short[] rgb, Scene scene, Ray viewerRay) {
			rgb[0] = 15;
			rgb[1] = 15;
			rgb[2] = 15;
			
			for(LightSource light: scene.getLights()) {
				Ray lightRay = Ray.fromPoints(light.getPoint(), closest.getPoint());
				RayIntersection lightClosest = findClosestIntersection(scene, lightRay);
				
				if(lightClosest != null && lightClosest.getDistance() + 1E-2 < (light.getPoint().sub(closest.getPoint()).norm()))	continue;
				else {
					addDifuseComponent(light, lightRay, lightClosest, rgb);
					addMirrorComponent(light, lightRay, lightClosest, rgb, viewerRay);
				}	
			}
		}

		/**
		 * Helper method that calculates mirror component of reflection
		 * and adds it onto currently calculated color.
		 * 
		 * @param light	given LightSource.
		 * @param lightRay	ray from light source onto the screenPoint.
		 * @param lightClosest	closest intersection between light and screenPoint.
		 * @param rgb	used to temporarily save color.
		 * @param viewerRay	Ray from viewer(eye) onto the scene.
		 */
		private void addMirrorComponent(LightSource light, Ray lightRay, RayIntersection lightClosest, short[] rgb, Ray viewerRay) {
			Point3D l = lightRay.direction;
			Point3D norm = lightClosest.getNormal();
			
			Point3D r = norm.scalarMultiply(l.scalarProduct(norm) * 2).modifySub(l).normalize().negate();
			Point3D v = viewerRay.direction.negate();
			double n = lightClosest.getKrn();
			
			double cosAngle = r.scalarProduct(v);
			double cosn = pow(cosAngle, n);
			
			double isb = 0;
			double isr = 0;
			double isg = 0;
			if(cosAngle > 0) {
				isb = light.getB() * lightClosest.getKrb() * cosn;
				isr = light.getR() * lightClosest.getKrr() * cosn;
				isg = light.getG() * lightClosest.getKrg() * cosn;
			}
			
			rgb[0] += isr;
			rgb[1] += isg;
			rgb[2] += isb;
		}

		/**
		 * Helper method that calculates diffuse component of reflection
		 * and adds it onto currently calculated color.
		 * 
		 * @param light	given LightSource.
		 * @param lightRay	ray from light source onto the screenPoint.
		 * @param lightClosest	closest intersection between light and screenPoint.
		 * @param rgb	used to temporarily save color.
		 */
		private void addDifuseComponent(LightSource light, Ray lightRay, RayIntersection lightClosest, short[] rgb) {
			Point3D l = lightRay.direction;
			Point3D norm = lightClosest.getNormal();
			double cosAngle = l.scalarProduct(norm);
			
			double idb = 0;
			double idr = 0;
			double idg = 0;
			if(cosAngle > 0) {
				idb = lightClosest.getKdb() * light.getB() * cosAngle;
				idr = lightClosest.getKdr() * light.getR() * cosAngle;
				idg = lightClosest.getKdg() * light.getG() * cosAngle;
			}
			
			rgb[0] += idr;
			rgb[1] += idg;
			rgb[2] += idb;
		}
		
		/**
		 * Helper method that finds closest intersection of any object on scene
		 * and given ray.
		 * If there is no intersection at all, method returns null.
		 * 
		 * @param scene	Scene containing all elements.
		 * @param ray	Ray between viewer(eye) and screenPoint.
		 * @return	closest intersection between any object and given ray.
		 */
		private RayIntersection findClosestIntersection(Scene scene, Ray ray) {
			List<GraphicalObject> objects = scene.getObjects();
			RayIntersection closest = null;
			
			for(GraphicalObject object: objects) {
				RayIntersection inter = object.findClosestRayIntersection(ray);
				
				if(inter != null && inter.isOuter() && (closest == null || inter.getDistance() < closest.getDistance())) {
					closest = inter;
				}
			}
			
			return closest;
		}
	}
	
	/**
	 * Creates local class that implements IRayTracerProducer and returns it.
	 * Creates class that is capable to create scene snapshots by using ray-tracing technique.
	 * Has method produce that produces needed data and calls method acceptResult to
	 * draw scene snapshot on the GUI.
	 * 
	 * @return	new class that implements IRayTracerProducer.
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {
			
			/**
			 * Method which is called by GUI when a scene snapshot is required.
			 * Creates ForkJoinPool to calculate its data with multiple threads.
			 * 
			 * @param eye position of human observer
			 * @param view position that is observed
			 * @param viewUp specification of view-up vector which is used to determine y-axis for screen
			 * @param horizontal horizontal width of observed space
			 * @param vertical vertical height of observed space
			 * @param width number of pixels per screen row
			 * @param height number of pixel per screen column
			 * @param requestNo used internally and must be passed on to GUI observer with rendered image
			 * @param observer GUI observer that will accept and display image this producer creates
			 */
			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical,
					int width, int height, long requestNo, IRayTracerResultObserver observer) {
				
				System.out.println("Započinjem izračune...");
				short[] red = new short[width * height];
				short[] green = new short[width * height];
				short[] blue = new short[width * height];
				
				Point3D zAxis = view.sub(eye).normalize();
				Point3D yAxis = viewUp.normalize().sub(zAxis.scalarMultiply(zAxis.scalarProduct(viewUp.normalize())))
						.normalize();
				Point3D xAxis = zAxis.vectorProduct(yAxis).normalize();
				
				Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal / 2))
						.add(yAxis.scalarMultiply(vertical / 2));
				
				Scene scene = RayTracerViewer.createPredefinedScene();

				ForkJoinPool pool = new ForkJoinPool();
				pool.invoke(new Job(eye, screenCorner, xAxis, yAxis, horizontal, vertical, width, height, scene,
						red, green, blue, 0, height-1));
				pool.shutdown();
				
				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}
		};
	}
	
}
