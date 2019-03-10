package com.worldline.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.worldline.service.api.PerfectNumberService;

/**
 * A perfect number service that is singleton. Implements two operations check and findAllInRange.
 * @author mohsen
 *
 */
@Service
@Scope("singleton")
public class PerfectNumberServiceImpl implements PerfectNumberService {
	
	/**
	 * Contains all perfect numbers that are less than Long.MAX_VALUE. Is used to cached perfect numbers to prevent calculating perfect numbers 
	 * whenever a request is processed.
	 */
	private final ArrayList<Long> allPerfectNumbers;
	
	/**
	 * Calculates all perfect numbers that are less than Long.MAX_VALUE and save them in allPerfectNumbers.
	 */
	public PerfectNumberServiceImpl() {
		this.allPerfectNumbers = findPerfectNumbers(Long.MAX_VALUE);
	}

	@Override
	public Boolean check(Long number) throws PerfectNumberException {
		if (number<=0) {
			throw new PerfectNumberException("The number must be an integer between 1 and " + Long.MAX_VALUE + ".");
		}
		return this.allPerfectNumbers.contains(number);
	}

	@Override
	public List<Long> findAllInRange(Long lowerBound, Long upperBound) throws PerfectNumberException {
		if (lowerBound<=0 || upperBound<=0) {
			throw new PerfectNumberException("The lower and upper bound must be an integer between 1 and " + Long.MAX_VALUE + ".");
		} else if (lowerBound>upperBound) {
			throw new PerfectNumberException("The lower bound must be lesser than the upper bound.");
		}
		ArrayList<Long> perfectNumbers = new ArrayList<Long>();
		for (Long perfectNumber : this.allPerfectNumbers) {
			if (perfectNumber >= lowerBound && perfectNumber <= upperBound) {
				perfectNumbers.add(perfectNumber);
			}
		}
		return perfectNumbers;
	}
	
	/**
	 * Finds all perfect numbers that are less than a given upper bound. Is based on the Euler theorem to find even perfect numbers and 
	 * another theorem that shows there is no odd perfect number less than Long.MAX_VALUE. 
	 * [Ochem, Pascal, and Michaël Rao. "Odd perfect numbers are greater than 10¹⁵⁰⁰." Mathematics of Computation 81.279 (2012): 1869-1877.]
	 * Euler theorem : All even perfect numbers n are of the form (2^p - 1) * 2^p - 1 , where 2^p - 1 is prime. 
	 * @param upperBound is a non-zero positive long integer that shows the upper bound  
	 * @return the list of all perfect numbers that are less than upperBound
	 */
	private ArrayList<Long> findPerfectNumbers(Long upperBound) {
		ArrayList<Long> perfectNumbers = new ArrayList<Long>();
		// For each integer number greater than 2, checks the number is prime and satisfies the Euler theorem. Since there are a few prime numbers
		// that generates perfect numbers that are small integer and less than Long.MAX_VALUE, no need to ignore odd number to improve performance.
		for (int number = 2;; number++) {
			// Assumes number is prime and creates a possible perfect number created based on Euler theorem. 
			Long possiblePerfectNumber = (long) (Math.pow(2, number-1) * (Math.pow(2, number) - 1));
			if (possiblePerfectNumber < upperBound) {
				// Adds the possible perfect number to perfectNumbers if number and 2^number - 1 are prime.
				if (isPrime(number) && isPrime((int) (Math.pow(2, number) - 1))) {
					perfectNumbers.add(possiblePerfectNumber);
				}
			} else {
				// Breaks the loop if the possible perfect number is equal or greater than the upper bound.  
				break;
			}
		}
		return perfectNumbers;
	}
	
	/**
	 * Checks if a given number is prime based on the well-known theorem. A number n is prime if it is not divisible 
	 * by any natural number greater than 1 and less that square root of n.
	 * @param number is an integer
	 * @return true if number is prime
	 */
	private Boolean isPrime(int number) {
	     Boolean isPrime = true;
	     if (number > 2) {
	    	 for (int m = 2; m <= Math.sqrt(number)+1; m++) {
	    		 if (Math.floorMod(number, m)==0) {
					isPrime = false;
					break;
				}
			 }
	     }
	     return isPrime;
	}

}
