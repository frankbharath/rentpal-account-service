package com.rentpal.accounts.config;

import com.rentpal.accounts.common.Utils;
import com.rentpal.accounts.constants.ErrorCodes;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class helps us to send message to user, if the user access an unauthorized resource.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 16, 2020 12:11:45 AM
 */
@Configuration
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private final MessageSource messageSource;

	public CustomAccessDeniedHandler(MessageSource messageSource){
		this.messageSource=messageSource;
	}
	/**
	 * Handle method checks whether the request is ajax or not and sends the corresponding error response to the client.
	 *
	 * @param request the request
	 * @param response the response
	 * @param accessDeniedException the access denied exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 */
	@Override
	public void handle(final HttpServletRequest request, final HttpServletResponse response,
			final AccessDeniedException accessDeniedException) throws IOException, ServletException {
		String message=messageSource.getMessage("error.forbidden_access", null, LocaleContextHolder.getLocale());
		if(Utils.isAjaxRequest(request)) {
	        Utils.sendJSONErrorResponse(response, Utils.getApiException(message, HttpStatus.FORBIDDEN, ErrorCodes.FORBIDDEN));
		}else {
			request.setAttribute("message", message);
			RequestDispatcher rd = request.getRequestDispatcher("/error");
			response.setStatus(HttpStatus.FORBIDDEN.value());
			rd.forward(request, response);
		}
	}

}
