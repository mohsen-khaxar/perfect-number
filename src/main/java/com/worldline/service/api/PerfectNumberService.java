package com.worldline.service.api;

import java.util.List;

import com.worldline.service.PerfectNumberException;

/**
 * Specifies a perfect number service. The service serves two operations : check if a given number is perfect and 
 * find all perfect numbers in a given range.
 * @author mohsen
 *
 */
public interface PerfectNumberService {
	
	/**
	 * Checks if a given number is perfect.
	 * @param number is a long integer that is greater than zero
	 * @return true if the given number is perfect
	 * @throws PerfectNumberException is raised when the given number is equal or less than zero
	 */
	public Boolean check(Long number) throws PerfectNumberException;
	
	/**
	 * Find all perfect numbers in a given range specified by two non-zero positive long integers as lower and upper bounds.
	 * @param lowerBound is a long integer that is greater than zero and equal or less than the upper bound
	 * @param upperBound is a long integer that is greater than zero and equal or greater than the upper bound
	 * @return all perfect numbers in the given range
	 * @throws PerfectNumberException is raised when lowerBound or upperBound are equal or less than zero. 
	 * Or lowerBound is greater than upperBound.
	 */
	public List<Long> findAllInRange(Long lowerBound, Long upperBound) throws PerfectNumberException;

}
