package com.rentpal.accounts.exception;

/**
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 14, 2020 6:19:08 PM
 * This class generates a new run time exception where there is issue in the service layer
 */
public class APIRequestException extends RuntimeException {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2460576406636485507L;

	/**
	 * Instantiates a new API request exception.
	 *
	 * @param message the message
	 */
	public APIRequestException(String message) {
		super(message);
	}
	
	/**
	 * Instantiates a new API request exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public APIRequestException(String message, Throwable cause) {
		super(message, cause);
	}
}
