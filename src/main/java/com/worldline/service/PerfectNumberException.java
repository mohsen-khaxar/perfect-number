package com.worldline.service;

/**
 * Thrown to indicate that the given numbers to check or find perfect numbers are wrongly defined.
 * @author mohsen
 *
 */
public class PerfectNumberException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public PerfectNumberException(String message) {
		super(message);
	}

}
