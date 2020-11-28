package com.rentpal.accounts.dto;

import org.springframework.http.HttpStatus;

/**
 * The Class APIRequestResponse.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 14, 2020 7:27:39 PM
 * Class Description
 */
public class APIRequestResponse {
	
	/** The message. */
	String message;
	
	/** The http status. */
	HttpStatus httpStatus;
	
	/** The status. */
	String status;
	
	/** The data. */
	Object data;
	
	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
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
	 * Gets the data.
	 *
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * Sets the data.
	 *
	 * @param data the new data
	 */
	public void setData(Object data) {
		this.data = data;
	}

}
