package com.rentpal.accounts.config;

import com.rentpal.accounts.common.DTOModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.sql.DataSource;
import java.util.Locale;
import java.util.Properties;

/**
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jun 15, 2020 10:59:14 PM
 *
 * This class contains spring configuration that are required to run the spring application.
 */

/**
 * This means it is a spring configuration written in java instead of XML.
 */
@Configuration

/**
 * This means enabling spring MVC for the spring application
 */
@EnableWebMvc

/**
 * Performs scan on the packages and creates bean for class annotated with @Component
 */
@ComponentScan("com.rentpal.accounts.model, com.rentpal.accounts.repository, com.rentpal.accounts.controller")

/**
 * Performs scan on the packages and creates bean for class annotated with @Filter
 */
@ServletComponentScan(basePackages = {
        "com.rentpal.accounts.filter"
})

/**
 * Database transaction management to ensure ACID
 */
@EnableTransactionManagement

@EnableConfigurationProperties
@PropertySource("classpath:application.yml")
public class SpringBeanConfiguration {
    /** The Environment variable to read application properties.*/
    @Autowired
    private Environment env;

    /**
     * Gets the datasource properties.
     *
     * @return the datasource properties
     */
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties getDatasourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * This method initializes PostgreSQL data source which will be used further used by JdbcTemplate.
     *
     * @return the data source
     */
    @Bean
    @Primary
    public DataSource dataSource() {
        return getDatasourceProperties().initializeDataSourceBuilder().build();
    }

    /**
     * Used to encode user password.
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Generates a MailSender Object that will be used to send account verification email, password change email etc.
     *
     * @return the java mail sender
     */
    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender= new JavaMailSenderImpl();
        mailSender.setUsername(env.getProperty("spring.mail.username"));
        mailSender.setPassword(env.getProperty("spring.mail.password"));
        mailSender.setHost(env.getProperty("spring.mail.host"));
        mailSender.setPort(Integer.parseInt(env.getProperty("spring.mail.port")));
        Properties properties=new Properties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        mailSender.setJavaMailProperties(properties);
        return mailSender;
    }

    /**
     * Message source.
     *
     * @return the message source
     */
    @Bean("messageSource")
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("i18n/messages","i18n/countries");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /**
     * Locale resolver.
     *
     * @return the locale resolver
     */
    @Bean(name = "defaultResolver")
    public LocaleResolver defaultResolver() {
        CookieLocaleResolver clr = new CookieLocaleResolver();
        clr.setDefaultLocale(Locale.US);
        return clr;
    }

    /**
     * Authentication entry point method to handle session timeout.
     *
     * @return the custom authentication entry point handler
     */
    @Bean
    public CustomAuthenticationEntryPointHandler authenticationEntryPointHandler() {
        return new CustomAuthenticationEntryPointHandler(messageSource());
    }

    /**
     * Access denied method to handle unauthorized access of resource.
     *
     * @return the custom access denied handler
     */
    @Bean
    public CustomAccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler(messageSource());
    }

    /**
     * Generates DTOModelMapper object that converts domain object to Data Transfer Object and vice versa.
     *
     * @return the DTO model mapper
     */
    @Bean
    public DTOModelMapper dtoModelMapper() {
        return new DTOModelMapper();
    }
}
