package com.worldline.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.worldline.service.PerfectNumberException;
import com.worldline.service.api.PerfectNumberService;

/**
 * Processes REST requests perfectNumber and perfectNumbersInRange to check if the input number is perfect and 
 * find all perfect numbers in the given range respectively.
 * @author mohsen
 *
 */
@RestController
@RequestMapping("/rest")
public class PerfectNumberController {
	
	@Autowired
	private PerfectNumberService perfectNumberService;
	
    /**
     * Checks if numberString is perfect.
     * @param numberString is an non-zero positive long integer
     * @return true as message if numberString is perfect and OK as reponseCode. Otherwise, REQUSET_ERROR or INTERNAL_ERROR as responseCode and 
     * an error message as message
     */
	@GetMapping("/perfectNumber/{numberString}")
	public Response<?> check(@PathVariable String numberString) {
    	Response<?> response;
    	try {
    		Long number = Long.valueOf(numberString);
    		Response<Boolean> okResponse = new Response<Boolean>();
    		okResponse.setMessage(perfectNumberService.check(number));
    		okResponse.setResponseCode(ResponseCode.OK);
    		response = okResponse;
    	} catch (Exception e) {
			response = handleException(e);
		}
    	return response;
	}
	
	/**
	 * Finds all perfect numbers between lowerBoundString and upperBoundString.
	 * @param lowerBoundString is an non-zero positive long integer and equal or less than upperBoundString
	 * @param upperBoundString is an non-zero positive long integer and equal or greater than lowerBoundString
	 * @return a list of perfect number as message and OK as responseCode. Otherwise, REQUSET_ERROR or INTERNAL_ERROR as responseCode and 
     * an error message as message
	 */
    @GetMapping("/perfectNumbersInRange/{lowerBoundString}/{upperBoundString}")
	public Response<?> findAllInRange(@PathVariable String lowerBoundString, @PathVariable String upperBoundString) {
    	Response<?> response = new Response<List<Long>>();
		try {
			Long lowerBound = Long.valueOf(lowerBoundString);
			Long upperBound = Long.valueOf(upperBoundString);
	    	Response<List<Long>> okResponse = new Response<List<Long>>();
			okResponse.setMessage(perfectNumberService.findAllInRange(lowerBound, upperBound));
	    	okResponse.setResponseCode(ResponseCode.OK);
    		response = okResponse;
		} catch (Exception e) {
			response = handleException(e);
		}
    	return response;
	}

	private Response<?> handleException(Exception e) {
		Response<?> response;
		Response<String> notOkResponse = new Response<String>();
		if (e instanceof PerfectNumberException) {
			notOkResponse.setMessage(e.getMessage());
			notOkResponse.setResponseCode(ResponseCode.REQUEST_ERROR);
		} else if (e instanceof NumberFormatException) {
			// Thrown if the inputs are not in the form of long integer.
			notOkResponse.setMessage("The input parameter(s) must be an integer between 1 and " + Long.MAX_VALUE + ".");
			notOkResponse.setResponseCode(ResponseCode.REQUEST_ERROR);
		} else {
			notOkResponse.setMessage("There is some internal problems. Please try later.");
			notOkResponse.setResponseCode(ResponseCode.INTERNAL_ERROR);
		}
		response = notOkResponse;
		return response;
	}
	
}
