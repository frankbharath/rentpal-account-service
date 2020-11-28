package com.rentpal.accounts.filter;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The Class CsrfHeaderFilter.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 4, 2020 2:09:31 AM
 * This class sets XSRF-TOKEN in the response that will be used AngularJS in front end. For each request, AngularJS will append the 
 * XSRF-TOKEN in the request header.
 */
public class CsrfHeaderFilter extends OncePerRequestFilter {
	
	/**
	 * Do filter internal.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param httpServletResponse the http servlet response
	 * @param filterChain the filter chain
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        CsrfToken csrf = (CsrfToken) httpServletRequest.getAttribute(CsrfToken.class.getName());
        if(csrf != null) {
            Cookie cookie = WebUtils.getCookie(httpServletRequest, "XSRF-TOKEN");
            String token = csrf.getToken();
            if(cookie == null || (token != null && !token.equals(cookie.getValue()))) {
                cookie = new Cookie("XSRF-TOKEN", token);
                cookie.setPath("/");
                httpServletResponse.addCookie(cookie);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
	
}
