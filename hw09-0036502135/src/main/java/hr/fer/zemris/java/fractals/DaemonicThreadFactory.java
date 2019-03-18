package hr.fer.zemris.java.fractals;

import java.util.concurrent.ThreadFactory;

/**
 * Class that implements ThreadFactory.
 * Tells thread pool generator how to generate threads.
 * This implementation creates daemonic threads
 * that are destroyed once non-daemonic threads ends.
 * 
 * @author Martin Sršen
 *
 */
public class DaemonicThreadFactory implements ThreadFactory {

	/**
	 * Method that generates threads in thread pool generator.
	 * Creates daemonic threads.
	 * 
	 * @param job	Each created tread job.
	 * @return	created daemonic thread that will be used inside thread pool.
	 */
	@Override
	public Thread newThread(Runnable job) {
		Thread worker = new Thread(job);
		worker.setDaemon(true);
		return worker;
	}
}
