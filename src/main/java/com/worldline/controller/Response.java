package com.worldline.controller;

/**
 * Returned by perfect number controller including a response code and a message.
 * @author mohsen
 *
 * @param <T> Is either a string, Boolean, or List<Long>
 */
public class Response<T> {
	
	/**
	 * Is a response code to show the processing status of a request
	 */
	private ResponseCode responseCode;
	
	/**
	 *  Is either a string that shows an error message, a Boolean that shows checking result of a number, 
	 *  or List<Long> that shows a list of perfect numbers
	 */
	private T message;

	public ResponseCode getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(ResponseCode responseCode) {
		this.responseCode = responseCode;
	}

	public T getMessage() {
		return message;
	}

	public void setMessage(T message) {
		this.message = message;
	}
	
}
