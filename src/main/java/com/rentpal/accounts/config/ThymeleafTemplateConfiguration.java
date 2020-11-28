package com.rentpal.accounts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.nio.charset.StandardCharsets;

/**
 * @author bharath
 * @version 1.0
 * Creation time: Jul 12, 2020 9:52:39 PM
 * This class provides Thymeleaf Template Configuration that generates dynamic HTML from the server side.
 */
@Configuration
public class ThymeleafTemplateConfiguration {
	
	/**
	 * Spring template engine.
	 *
	 * @return the spring template engine
	 */
	@Bean
	public SpringTemplateEngine springTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(htmlTemplateResolver());
        return templateEngine;
    }
    
    /**
     * Html template resolver.
     *
     * @return the spring resource template resolver
     */
    @Bean
    public SpringResourceTemplateResolver htmlTemplateResolver(){
        SpringResourceTemplateResolver htmlTemplateResolver = new SpringResourceTemplateResolver();
        htmlTemplateResolver.setPrefix("classpath:/templates/");
        htmlTemplateResolver.setSuffix(".html");
        htmlTemplateResolver.setTemplateMode(TemplateMode.HTML);
        htmlTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return htmlTemplateResolver;
    }
}
