package com.rentpal.accounts.common;

import com.rentpal.accounts.constants.Constants;
import com.rentpal.accounts.constants.ErrorCodes;
import com.rentpal.accounts.dto.APIRequestResponse;
import com.rentpal.accounts.exception.APIException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * The Class Utils.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jun 08, 2020 03:00:10 PM
 * This class has basic common utility function that will be used by this application.
 */

public final class Utils {

	/**
	 * Gets Classloader from thread context for static references .
	 *
	 * @return the class loader
	 */
	public static ClassLoader classloader() {
		return Thread.currentThread().getContextClassLoader();
	}
	
	/**
	 * Checks if the String is valid or not. A string is considered invalid if it is null or the length of the string is equal to 0.
	 *
	 * @param value the value
	 * @return true, if it is a valid string
	 */
	public static boolean isValidString(String value) {
		return value!=null && value.trim().length()!=0;
	}
	
	/**
	 * Writes error response to the HttpServletResponse stream.
	 *
	 * @param response the response
	 * @param exception the exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void sendJSONErrorResponse(HttpServletResponse response, APIException exception) throws IOException
	{
		response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=resp.txt;");
        response.setHeader("X-Download-Options", "noopen");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(exception.getHttpStatus().value());
        try (ServletOutputStream out = response.getOutputStream()) {
        	ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(out, exception);
            out.flush();
        }        
	}
	
	/**
	 * Gets the api exception.
	 *
	 * @param message the message
	 * @param status the status
	 * @return the api exception
	 */
	public static APIException getApiException(Object message, HttpStatus status) {
		return getApiException(message, status ,null);
	}
	
	/**
	 * Gets the api exception.
	 *
	 * @param message the message
	 * @param status the status
	 * @param errorCode the error code
	 * @return the api exception
	 */
	public static APIException getApiException(Object message, HttpStatus status, ErrorCodes errorCode) {
		APIException exception=new APIException();
		exception.setTimestamp(Utils.getDate(System.currentTimeMillis()));
		exception.setHttpStatus(status);
		exception.setMessage(message);
		if(errorCode!=null) {
			exception.setErrorCode(errorCode.toString());
		}
		exception.setStatus(Constants.FAILED);
		return exception;
	}
	
	/**
	 * Gets the api request response.
	 *
	 * @param message the message
	 * @return the api request response
	 */
	public static APIRequestResponse getApiRequestResponse(String message) {
		return getApiRequestResponse(message,null);
	}
	
	/**
	 * Gets the api request response.
	 *
	 * @param message the message
	 * @param data the data
	 * @return the api request response
	 */
	public static APIRequestResponse getApiRequestResponse(String message, Object data) {
		APIRequestResponse response=new APIRequestResponse();
		if(data!=null) {
			response.setData(data);
		}
		response.setMessage(message);
		response.setHttpStatus(HttpStatus.OK);
		response.setStatus(Constants.SUCCESS);
		return response;
	}
	
	/**
	 * Generate alpha numeric string.
	 *
	 * @param length the length
	 * @return the string
	 */
	public static String generateAlphaNumericString(int length) {
		return RandomStringUtils.randomAlphanumeric(length);
	}

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public static long getUserId() {
		return 56l;
		//return Long.parseLong(RentPalThreadLocal.get("userId").toString());
	}
	
	/**
	 * Gets the user email.
	 *
	 * @return the user email
	 */
	public static String getUserEmail() {
		return "bharath@gmail.com";
		//return RentPalThreadLocal.get("email").toString();
	}
	
	/**
	 * Checks if it is a ajax request.
	 *
	 * @param request the request
	 * @return true, if is ajax request
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}

	/**
	 * Gets the date.
	 *
	 * @param milliseconds the milliseconds
	 * @return the date
	 */
	public static String getDate(Long milliseconds) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
		return simpleDateFormat.format(new Date(milliseconds));
	}
	
	/**
	 * Parses the date to milliseconds.
	 *
	 * @param date the date
	 * @return the long
	 * @throws ParseException the parse exception
	 */
	public static Long parseDateToMilliseconds(String date) throws ParseException{
		return parseDateToMilliseconds(date, "MMM d, yyyy");
	}
	
	/**
	 * Parses the date to milliseconds.
	 *
	 * @param dateStr the date str
	 * @param format the format
	 * @return the long
	 * @throws ParseException the parse exception
	 */
	public static Long parseDateToMilliseconds(String dateStr, String format) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setLenient(false);
		Date date = sdf.parse(dateStr);
		return date.getTime();
	}
	
	/**
	 * Convert days to milliseconds.
	 *
	 * @param days the days
	 * @return the long
	 */
	public static long convertDaysToMilliseconds(int days) {
		return days*86400000l;
	}

	public static String getMessage(MessageSource messageSource, String key){
		return getMessage(messageSource, key, null);
	}
	public static String getMessage(MessageSource messageSource, String key, Object ...args){
		String message=key;
		try{
			message=messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
		}catch (Exception exception){ }
		return message;
	}
}
