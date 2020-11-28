package com.rentpal.accounts.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * The Class CustomErrorController.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 16, 2020 12:39:40 AM
 * This class provides a custom error handling page for run time exceptions
 */
@Controller
public class CustomErrorController implements ErrorController {

	private final MessageSource messageSource;

	public CustomErrorController(MessageSource messageSource){
		this.messageSource=messageSource;
	}
	 /**
 	 * Handle error.
 	 *
 	 * @param request the request
 	 * @param model the model
 	 * @return the string
 	 */
 	@RequestMapping(value="/error", produces = MediaType.TEXT_HTML_VALUE)
	 public String handleError(HttpServletRequest request, Model model) {
		 String errorMessage=messageSource.getMessage("error.unknown.issue", null, LocaleContextHolder.getLocale());
		 if(request.getAttribute("message")!=null) {
			 errorMessage=request.getAttribute("message").toString();
		 }
		 model.addAttribute("message", errorMessage);
		 return "error";
	 }
	 
	 /**
 	 * Gets the error path.
 	 *
 	 * @return the error path
 	 */
 	@Deprecated
	 @Override
	 public String getErrorPath() {
		return null;
	 }

}
