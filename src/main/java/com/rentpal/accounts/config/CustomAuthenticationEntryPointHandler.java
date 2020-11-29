package com.rentpal.accounts.config;

import com.rentpal.accounts.common.Utils;
import com.rentpal.accounts.constants.ErrorCodes;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class helps us to send timeout message to client when the session expires.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 8, 2020 9:38:16 PM
 */

@Configuration
public class CustomAuthenticationEntryPointHandler implements  AuthenticationEntryPoint   {

	private final MessageSource messageSource;

	public CustomAuthenticationEntryPointHandler(MessageSource messageSource){
		this.messageSource=messageSource;
	}
	/**
	 * Commence method checks the request and sends the expire message according to the type of request either ajax or normal request.
	 *
	 * @param request the request
	 * @param response the response
	 * @param authException the auth exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public void commence(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException authException) throws IOException {
		if(Utils.isAjaxRequest(request)) {
			String message = messageSource.getMessage("error.session_timeout", null, LocaleContextHolder.getLocale());
	        Utils.sendJSONErrorResponse(response, Utils.getApiException(message, HttpStatus.UNAUTHORIZED, ErrorCodes.SESSION_EXPIRED));
		}else {
		    new DefaultRedirectStrategy().sendRedirect(request, response, "/login");
		}
	}

}
