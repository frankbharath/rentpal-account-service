package com.rentpal.accounts.exception;

/**
 * The Class URLException.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 15, 2020 4:21:54 PM
 * This class generates a run time exception. These exceptions are raised while checking the sanity of the request
 */
public class URLException extends RuntimeException{
	
	/**
	 * Instantiates a new URL exception.
	 *
	 * @param message the message
	 */
	public URLException(String message) {
		super(message);
	}
	
	/**
	 * Instantiates a new URL exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public URLException(String message, Throwable cause) {
		super(message, cause);
	}
}
