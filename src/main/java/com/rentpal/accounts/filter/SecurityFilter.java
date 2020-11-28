package com.rentpal.accounts.filter;

import com.rentpal.accounts.config.UserSession;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
	* @author bharath
	* @version 1.0
	* Creation time: Jun 10, 2020 11:00:09 AM
	* The class SecurityFilter initiates ThreadLocal to store user session
*/
@WebFilter
public class SecurityFilter implements Filter {

	/** The user session. */
	private final UserSession userSession;
		
	/**
	 * Instantiates a new security filter.
	 *
	 * @param userSession the user session
	 */
	@Autowired
	public SecurityFilter(UserSession userSession) {
		this.userSession=userSession;
	}
	
	/**
	 * @param servlertRequest the servlert request
	 * @param servletResponse the servlet response
	 * @param chain the chain
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest servlertRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)servlertRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String url=request.getRequestURI();
		if(url.startsWith("/login") && userSession.isAuthenticated()) {
			response.sendRedirect(request.getContextPath() + "/home");
			return;
		}
		chain.doFilter(request, response);
	}

}
