package com.rentpal.accounts.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * 
 * @author bharath
 * @version 1.0
 * Creation time: Jul 20, 2020 10:24:16 PM
 * 
 * This class contains WebConfiguration
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer  {

	/**
	 * Adds the resource handlers.
	 *
	 * @param registry the registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/static/");
	}
	
	/**
	 * Adds the view controllers.
	 *
	 * @param registry the registry
	 */
	@Override
	public void addViewControllers (ViewControllerRegistry registry) {
		registry.addRedirectViewController("/", "/login");
	}

	/**
	 * Adds the interceptors.
	 *
	 * @param registry the registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("locale");
		localeChangeInterceptor.setIgnoreInvalidLocale(true);
		registry.addInterceptor(localeChangeInterceptor);
	}
}