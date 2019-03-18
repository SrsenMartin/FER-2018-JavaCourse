package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Program that reads complex number roots of polynomial until user write 'done'.
 * User must provide at least 2 roots.
 * Draws and outputs fractals derived from Newton-Raphson iteration based
 * on given roots.
 * Uses 10^3 as convergence and root treshold.
 * Max iterations number is 16^3.
 * 
 * @author Martin Sršen
 *
 */
public class Newton {

	/**
	 * Called when program is started.
	 * 
	 * @param args	Arguments from command prompt.Not used in this example.
	 */
	public static void main(String[] args) {

		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");
		
		List<Complex> roots = new ArrayList<>();
		
		try (Scanner scanner = new Scanner(System.in)) {
			int counter = 0;
			String input = null;

			while (true) {
				System.out.print("Root" + (counter+1) + "> ");
				input = scanner.nextLine().trim().toLowerCase();

				if (input.equals("done")) {
					if (counter < 2) {
						System.out.println("You need to provide at least 2 roots.");
						continue;
					}
					System.out.println("Image of fractal will appear shortly. Thank you.");
					break;
				}

				Complex number = null;
				try {
					ComplexParser parser = new ComplexParser(input);
					number = parser.getComplex();
				} catch(IllegalArgumentException ex) {
					System.out.println(ex.getMessage());
					continue;
				}
				counter++;
				roots.add(number);
			}
			
			FractalViewer.show(new MyProducer(roots));
		}
	}
	
	/**
	 * Implementation of IFractalProducer. Used to generate data required
	 * to draw Newton-Raphson fractal.
	 * Uses all available processors to calculate root indexes that are
	 * closer to complex number than root treshold
	 * that are within convergation treshold.
	 * 
	 * @author Martin Sršen
	 *
	 */
	public static class MyProducer implements IFractalProducer {

		/**
		 * Polynomial generated from given roots.
		 */
		private ComplexRootedPolynomial polynomial;
		/**
		 * ThreadPool used to execute tasks.Contains maxNumber of available processors.
		 */
		private static ExecutorService pool;
		
		/**
		 * Convergence difference treshold.
		 */
		private static final double CONVERGENCE_TRESHOLD = 1E-3;
		/**
		 * Root difference treshold.
		 */
		private static final double ROOT_TRESHOLD = 1E-3;
		/**
		 * Maximal number of iterations.
		 */
		private static final int MAX_ITERATION = 16*16*16;
		/**
		 * Number of available processors.
		 */
		private static final int NO_PROCESSORS = Runtime.getRuntime().availableProcessors();
		/**
		 * Number of jobs.
		 */
		private static final int NO_JOBS = 8 * NO_PROCESSORS;
		
		/**
		 * Constructor that takes List of roots and makes ComplexRootedPolynomial object from it.
		 * Initializes ThreadPool.
		 * 
		 * @param roots	List of given complex number roots.
		 */
		public MyProducer(List<Complex> roots) {
			Complex[] rootsArray = new Complex[roots.size()];
			roots.toArray(rootsArray);
			polynomial = new ComplexRootedPolynomial(rootsArray);
			
			pool = Executors.newFixedThreadPool(NO_PROCESSORS, new DaemonicThreadFactory());
		}

		/**
		 * Method called each time when user maximize fractal or wants to write fractal.
		 * Uses maximum number of processors for running threads used to calculate data.
		 * Method is required to return data as short array with length of width*height.
		 * 
		 * @param reMin	min complex number real part.
		 * @param reMax max complex number real part.
		 * @param imMin min complex number imaginary part.
		 * @param imMax max complex number imaginary part.
		 * @param width frame width.
		 * @param height frame height.
		 * @param requestNo request identificator.
		 * @param observer used to return generated data.
		 */
		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height,
				long requestNo, IFractalResultObserver observer) {
			short[] data = new short[width * height];
			final int rangeY = height / NO_JOBS;
			
			List<Future<Void>> results = new ArrayList<>();
			
			for (int i = 0; i < NO_JOBS; i++) {
				int yMin = i * rangeY;
				int yMax = (i + 1) * rangeY - 1;
				if (i == NO_JOBS - 1) {
					yMax = height - 1;
				}
				
				Job posao = new Job(reMin, reMax, imMin, imMax, width, height, yMin, yMax, MAX_ITERATION, data,
						polynomial, CONVERGENCE_TRESHOLD, ROOT_TRESHOLD);
				results.add(pool.submit(posao));
			}
			
			for (Future<Void> job : results) {
					try {
						job.get();
					} catch (InterruptedException | ExecutionException e) {}
			}

			observer.acceptResult(data, (short)(polynomial.toComplexPolynom().order() + 1), requestNo);
		}
		
	}
	
	/**
	 * Class that implements from Callable<Void>.
	 * Represents job for each thread that is used to calculate indexes.
	 * Method call is called once thread takes job and starts itself.
	 */
	public static class Job implements Callable<Void> {
		private double reMin;
		private double reMax;
		private double imMin;
		private double imMax;
		private int width;
		private int height;
		private int yMin;
		private int yMax;
		private int m;
		private short[] data;
		private ComplexRootedPolynomial polynomial;
		private double convergenceTreshold;
		private double rootTreshold;

		/**
		 * Construrctor used to initialize all data used to calculate wanted data.
		 * 
		 * @param reMin min complex number real part.
		 * @param reMax max complex number real part.
		 * @param imMin min complex number imaginary part.
		 * @param imMax max complex number imaginary part.
		 * @param width frame width.
		 * @param height frame height.
		 * @param yMin minimum of y used to generate data from.
		 * @param yMax maximum of y used to generate data to.
		 * @param m maximum number of iterations used to generate data.
		 * @param data array where results are stored.
		 * @param polynomial ComplexRootedPolynomial used to generate data.
		 * @param convergenceTreshold allowed convergence difference.
		 * @param rootTreshold allowed root difference.
		 */
		public Job(double reMin, double reMax, double imMin,
				double imMax, int width, int height, int yMin, int yMax, 
				int m, short[] data, ComplexRootedPolynomial polynomial, 
				double convergenceTreshold, double rootTreshold) {
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.m = m;
			this.data = data;
			this.polynomial = polynomial;
			this.convergenceTreshold = convergenceTreshold;
			this.rootTreshold = rootTreshold;
		}
		
		/**
		 * Method that is called each time thread that took job starts.
		 * Calculates indexes of roots and writes them in given short array
		 * at indexes of current pixels in frame.
		 */
		@Override
		public Void call() {
			int offset = yMin * width;
			
			for (int y = yMin; y <= yMax; y++) {
				for (int x = 0; x < width; x++) {
					Complex c = map_to_complex_plain(x, y, 0, width - 1, yMin, height - 1, reMin, reMax, imMin, imMax);
					Complex zn = c;
					int iter = 0;
					double module = 0;
					Complex zn1 = null;

					do {
						Complex numerator = polynomial.apply(zn);
						Complex denominator = polynomial.toComplexPolynom().derive().apply(zn);
						Complex fraction = numerator.divide(denominator);
						zn1 = zn.sub(fraction);
						module = zn1.sub(zn).module();
						zn = zn1;
						iter++;
					} while (Math.abs(module) > convergenceTreshold && iter < m);
					int index = findClosestRootIndex(zn1, rootTreshold);
					if (index == -1) {
						data[offset++] = 0;
					} else {
						data[offset++] = (short) index;
					}
				}
			}
			
			return null;
		}
		
		/**
		 * Method that finds index of closest root in polynomial for given complex number zn1 that is within
		 * given treshold or, if there is no such root, returns -1.
		 * 
		 * @param zn1	Complex number used to find index.
		 * @param rootTreshold	Allowed root difference.
		 * @return	Root index if it is in rootTreshold, -1 otherwise.
		 */
		private int findClosestRootIndex(Complex zn1, double rootTreshold) {
			return polynomial.indexOfClosestRootFor(zn1, rootTreshold);
		}

		/**
		 * Method that generates complex number representation of current pixel in frame
		 * with given width and height.
		 * 
		 * @param x	Current column index in row.
		 * @param y Current row index in frame.
		 * @param xMin	Minimum column index.
		 * @param xMax	Maximum column index.
		 * @param yMin	Minimum row index.
		 * @param yMax	Maximum row index.
		 * @param reMin	min complex number real part.
		 * @param reMax	max complex number real part.
		 * @param imMin min complex number imaginary part.
		 * @param imMax max complex number imaginary part.
		 * @return	Complex number representing pixel in frame with given width and height.
		 */
		private Complex map_to_complex_plain(int x, int y, int xMin, int xMax, int yMin, int yMax, double reMin, double reMax,
				double imMin, double imMax) {
			double cReal = (double)x / xMax * (reMax - reMin) + reMin;
			double cImaginary = (double)(yMax-y) / yMax * (imMax - imMin) + imMin;
			
			return new Complex(cReal, cImaginary);
		}
	}
}
