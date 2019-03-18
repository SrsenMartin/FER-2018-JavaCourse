package hr.fer.zemris.java.hw06.demo2;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static java.lang.Math.sqrt;

/**
 * Class that represents collection of n prime numbers.
 * Class implements Iterable interface, in each next iteration returns next prime number.
 * 
 * @author Martin Sršen
 *
 */
public class PrimesCollection implements Iterable<Integer> {

	/**
	 * Represents how many prime numbers collection contains.
	 */
	private int numberOfPrimes;
	
	/**
	 * Constructor that initializes number of primes that will be calculated.
	 * 
	 * @param numberOfPrimes	Number of primes that will be calculated and returned.
	 * @throws IllegalArgumentException if given number is less that 1.
	 */
	public PrimesCollection(int numberOfPrimes) {
		if(numberOfPrimes < 1) {
			throw new IllegalArgumentException("Broj prostih brojeva mora biti veći od 0");
		}
		
		this.numberOfPrimes = numberOfPrimes;
	}
	
	/**
	 * Returns new iterator of n prime numbers.
	 * 
	 * @return Iterator iterator of prime numbers.
	 */
	@Override
	public Iterator<Integer> iterator() {
		return new IteratorImpl();
	}
	
	/**
	 * Implementation of PrimesCollection iterator.
	 * Each time next is called ,iterator calculates next number and returns it.
	 *
	 */
	private class IteratorImpl implements Iterator<Integer>{

		/**
		 * Currently checked number.
		 */
		private int currentNumber = 1;
		/**
		 * Copy of number of primes to return.
		 */
		private int noPrimesCopy = numberOfPrimes;
		
		/**
		 * Returns true if iterator has next element to return, false otherwise.
		 * 
		 * @return true if iterator has more elements, false otherwise.
		 */
		@Override
		public boolean hasNext() {
			return noPrimesCopy != 0;
		}

		/**
		 * If iterator has next element, it calculates next prime and returns it.
		 * If there are no more elements exception is thrown.
		 * 
		 * @return next prime that was calculated.
		 * @throws NoSuchElementException if there are no more primes to read.
		 */
		@Override
		public Integer next() {
			if(!hasNext()) {
				throw new NoSuchElementException("There are no more primes to calculate.");
			}
			
			while(!isPrime(++currentNumber));
			noPrimesCopy--;
			
			return currentNumber; 
		}
		
		/**
		 * Helper method that checks if given number is prime number or not.
		 * 
		 * @param n Given number to check if it is prime number.
		 * @return true if given number is prime,false otherwise.
		 */
		private boolean isPrime(int n) {
			for(int i = 2; i <= sqrt(n); i++) {
				if(n%i == 0)	return false;
			}
			
			return true;
		}
	}
}
