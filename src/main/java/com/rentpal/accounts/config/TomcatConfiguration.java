package com.rentpal.accounts.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

/**
 * The Class TomcatConfig.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jul 22, 2020 12:24:16 AM
 * This class allows the Tomcat server to parse the request body. This is useful in SecurityFilter as 
 * each request parameters are validated for sanity.
 */
@Component
public class TomcatConfiguration implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    
    /**
     * Enables Tomcat server to parse request body of POST, PUT and DELETE.
     *
     * @param factory the factory
     */
    @Override
    public void customize(TomcatServletWebServerFactory factory) {
    	factory.addConnectorCustomizers(connector -> {
    		connector.setParseBodyMethods("POST,PUT,DELETE");
    	});
    }
}