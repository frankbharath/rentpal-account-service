package com.rentpal.accounts.exception;

import com.rentpal.accounts.common.Utils;
import com.rentpal.accounts.constants.Constants;
import com.rentpal.accounts.constants.ErrorCodes;
import com.rentpal.accounts.model.InvalidField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

	private final MessageSource messageSource;

	/**
	 * Instantiates a new Global exception handler.
	 *
	 * @param messageSource the message source
	 */
	@Autowired
	public GlobalExceptionHandler(MessageSource messageSource){
		this.messageSource=messageSource;
	}

	private APIException getAPIExceptionForInvalidFields(List<InvalidField> invalidFields){
		APIException exception=new APIException();
		exception.setMessage(invalidFields);
		exception.setHttpStatus(HttpStatus.BAD_REQUEST);
		exception.setStatus(Constants.FAILED);
		exception.setErrorCode(ErrorCodes.INVALID_PARAMETER.toString());
		exception.setTimestamp(Utils.getDate(System.currentTimeMillis()));
		return exception;
	}

	/**
	 * Handle bind exception response entity.
	 *
	 * @param ex the ex
	 * @return the response entity
	 */
	@ResponseBody
	@ExceptionHandler(value = {BindException.class})
	public ResponseEntity<Object> handleBindException(BindException ex) {
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();
		List<InvalidField> invalidFields =new ArrayList<>();
		for (org.springframework.validation.FieldError fieldError: fieldErrors) {
			InvalidField invalidField =new InvalidField();
			String message=Utils.getMessage(messageSource, fieldError.getCode());
			invalidField.setMessage(message);
			invalidField.setField(fieldError.getField());
			invalidFields.add(invalidField);
		}
		return new ResponseEntity<>(getAPIExceptionForInvalidFields(invalidFields), HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle missing servlet request parameter exception.
	 *
	 * @param request  the request
	 * @param response the response
	 * @param ex       the ex
	 * @throws IOException      the io exception
	 * @throws ServletException the servlet exception
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public void handleMissingServletRequestParameterException(HttpServletRequest request, HttpServletResponse response, MissingServletRequestParameterException ex) throws IOException, ServletException {
		String name=Utils.getMessage(messageSource, "label."+ex.getParameterName());
		InvalidField invalidField =new InvalidField();
		String message=Utils.getMessage(messageSource, "error.parameter.required", new Object[]{name});
		invalidField.setMessage(message);
		invalidField.setField(ex.getParameterName());
		List<InvalidField> invalidFields= new ArrayList<>();
		invalidFields.add(invalidField);
		if(Utils.isAjaxRequest(request)) {
			sendJSONResponse(response, Utils.getApiException(invalidFields, HttpStatus.BAD_REQUEST));
		}else {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			forwardErrorResponse(request, response, message);
		}
	}

	private void sendJSONResponse(HttpServletResponse response, APIException apiException) throws IOException {
		Utils.sendJSONErrorResponse(response, apiException);
	}

	private void forwardErrorResponse(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
		request.setAttribute("message", message);
		RequestDispatcher rd = request.getRequestDispatcher("/error");
		rd.forward(request, response);
	}


	/**
	 * Handle API request exception.
	 *
	 * @param request  the request
	 * @param response the response
	 * @param e        the e
	 * @throws IOException      Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 */
	@ExceptionHandler(value= {APIRequestException.class})
	public void handleAPIRequestException(HttpServletRequest request, HttpServletResponse response, APIRequestException e) throws IOException, ServletException{
		log.error(e.getMessage(), e);
		String message=Utils.getMessage(messageSource, e.getMessage());
		if(Utils.isAjaxRequest(request)) {
			sendJSONResponse(response, Utils.getApiException(message, HttpStatus.BAD_REQUEST));
		}else {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			forwardErrorResponse(request, response, message);
		}
	}

	/**
	 * Handle constraint violation exception.
	 *
	 * @param request  the request
	 * @param response the response
	 * @param e        the e
	 * @throws IOException      the io exception
	 * @throws ServletException the servlet exception
	 */
	@ExceptionHandler(value= {ConstraintViolationException.class})
	public void handleConstraintViolationException(HttpServletRequest request, HttpServletResponse response, ConstraintViolationException e) throws IOException, ServletException{
		log.error(e.getMessage(), e);
		Set<ConstraintViolation<?>> errors=e.getConstraintViolations();
		List<InvalidField> invalidFields= new ArrayList<>();
		errors.forEach(error->{
			InvalidField invalidField =new InvalidField();
			String message=Utils.getMessage(messageSource, error.getMessage());
			invalidField.setMessage(message);
			invalidFields.add(invalidField);
		});
		if(Utils.isAjaxRequest(request)) {
			sendJSONResponse(response, getAPIExceptionForInvalidFields(invalidFields));
		}else {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			forwardErrorResponse(request, response, invalidFields.get(0).getMessage());
		}
	}

	/**
	 * Handle global exception.
	 *
	 * @param request  the request
	 * @param response the response
	 * @param e        the e
	 * @throws IOException      Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 */
	@ExceptionHandler(value= {Exception.class})
	public void handleGlobalException(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException, ServletException{
		log.error(e.getMessage(), e);
		String message=Utils.getMessage(messageSource,"error.unknown.issue");
		if(Utils.isAjaxRequest(request)) {
			sendJSONResponse(response, Utils.getApiException(message, HttpStatus.BAD_REQUEST));
		}else {
			forwardErrorResponse(request, response, message);
		}
	}

	/**
	 * Handle no handler found exception.
	 *
	 * @param request  the request
	 * @param response the response
	 * @param e        the e
	 * @throws IOException      Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 */
	@ExceptionHandler(NoHandlerFoundException.class)
    public void handleNoHandlerFoundException(HttpServletRequest request, HttpServletResponse response,  NoHandlerFoundException e) throws IOException, ServletException {
		log.error(e.getMessage(), e);
    	if(Utils.isAjaxRequest(request)) {
    		APIException apiException=Utils.getApiException(e.getMessage(), HttpStatus.NOT_FOUND);
    		Utils.sendJSONErrorResponse(response, Utils.getApiException(e.getMessage(), HttpStatus.NOT_FOUND));
    	}else{
    		response.sendRedirect(request.getContextPath() + "/home");
    	}
	}
}
