package com.rentpal.accounts.exception;

/**
 * The Class AjaxNoHandlerException.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 15, 2020 10:48:25 PM
 * This class generates a new run time exception for ajax call if there is no handler for the request.
 */
public class AjaxNoHandlerException extends RuntimeException {
	
	/**
	 * Instantiates a new ajax no handler exception.
	 *
	 * @param message the message
	 */
	public AjaxNoHandlerException(String message) {
		super(message);
	}
	
	/**
	 * Instantiates a new ajax no handler exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public AjaxNoHandlerException(String message, Throwable cause) {
		super(message, cause);
	}
}
