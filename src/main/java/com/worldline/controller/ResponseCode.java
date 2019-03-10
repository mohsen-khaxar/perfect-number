package com.worldline.controller;

/**
 * Specifies response codes returned by perfect number controller.
 * @author mohsen
 *
 */
public enum ResponseCode {
	// Everything goes well
	OK,
	// Some problems to processe input numbers
	REQUEST_ERROR,
	// Some unknown problems
	INTERNAL_ERROR
}
