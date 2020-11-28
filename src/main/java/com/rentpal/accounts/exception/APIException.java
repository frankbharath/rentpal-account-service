package com.rentpal.accounts.exception;

import org.springframework.http.HttpStatus;

/**
 * @author bharath
 * @version 1.0
 * Creation time: Sep 14, 2020 6:06:52 PM
 * This class error response object on API exception
*/

public class APIException {
	
	/** The timestamp. */
	String timestamp;
	
	/** The message. */
	Object message;
	
	/** The http status. */
	HttpStatus httpStatus;
	
	/** The status. */
	String status;
	
	/** The error code. */
	String errorCode;
	
	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public String getTimestamp() {
		return timestamp;
	}
	
	/**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public Object getMessage() {
		return message;
	}
	
	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(Object message) {
		this.message = message;
	}
	
	/**
	 * Gets the http status.
	 *
	 * @return the http status
	 */
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	
	/**
	 * Sets the http status.
	 *
	 * @param httpStatus the new http status
	 */
	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets the error code.
	 *
	 * @return the error code
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * Sets the error code.
	 *
	 * @param errorCode the new error code
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
}
