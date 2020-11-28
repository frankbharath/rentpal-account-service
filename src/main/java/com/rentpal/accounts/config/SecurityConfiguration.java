package com.rentpal.accounts.config;

import com.rentpal.accounts.filter.CsrfHeaderFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

@EnableWebSecurity
@Configuration
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final DataSource dataSource;

    private CustomAuthenticationEntryPointHandler authenticationEntryPointHandler;

    private CustomAccessDeniedHandler accessDeniedHandler;

    private MessageSource messageSource;

    private static final Integer REMEMBERMESECONDS=2592000;

    /**
     * Instantiates a new spring security config.
     *
     * @param userDetailsService the user details service
     * @param passwordEncoder the password encoder
     * @param dataSource the data source
     */
    @Autowired
    public SecurityConfiguration(@Qualifier("userDetailService") UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, DataSource dataSource) {
        this.userDetailsService=userDetailsService;
        this.passwordEncoder=passwordEncoder;
        this.dataSource=dataSource;
    }

    @Autowired
    public void setAuthenticationEntryPointHandler(CustomAuthenticationEntryPointHandler authenticationEntryPointHandler) {
        this.authenticationEntryPointHandler = authenticationEntryPointHandler;
    }

    @Autowired
    public void setAccessDeniedHandler(CustomAccessDeniedHandler accessDeniedHandler) {
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * This authentication filter extends UsernamePasswordAuthenticationFilter with custom success handler, failure handler and remember me.
     *
     * @return the ajax authentication filter
     * @throws Exception the exception
     */
    /*@Bean
    public AjaxAuthenticationFilter authenticationFilter() throws Exception {
        AjaxAuthenticationFilter authenticationFilter = new AjaxAuthenticationFilter();
        //authenticationFilter.setAuthenticationFailureHandler(this::loginFailureHandler);
        authenticationFilter.setAuthenticationSuccessHandler(this::loginSuccessHandler);
        authenticationFilter.setAuthenticationManager(authenticationManagerBean());
        authenticationFilter.setRememberMeServices(getTokenBasedRememberMeServices());
        return authenticationFilter;
    }*/

    /**
     * Dao authentication provider performs validation of user credentials.
     *
     * @return the dao authentication provider
     */
    /*@Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        return daoAuthenticationProvider;
    }*/

    /**
     * Configures authentication manager builder with authentication provider.
     *
     * @param auth the auth
     * @throws Exception the exception
     */
    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }*/

    /**
     * Gets the token based remember me services.
     *
     * @return the token based remember me services
     */
    @Bean
    public TokenBasedRememberMeServices getTokenBasedRememberMeServices() {
        TokenBasedRememberMeServices tokenBasedRememberMeServices = new TokenBasedRememberMeServices("rememberme", userDetailsService);
        tokenBasedRememberMeServices.setAlwaysRemember(true);
        tokenBasedRememberMeServices.setTokenValiditySeconds(REMEMBERMESECONDS);
        //tokenBasedRememberMeServices.setUseSecureCookie(true);
        return tokenBasedRememberMeServices;
    }

    /**
     * Spring security configuration to provide csrf config, configs for urls whether it requires authentication or not.
     *
     * @param http the http
     * @throws Exception the exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http//.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPointHandler)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                    .csrf()
                    .csrfTokenRepository(csrfTokenRepository())
                .and()
                .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
                .authorizeRequests()
                /*.anyRequest()
                .permitAll()*/
                .antMatchers(new String[] {"/resources/**","/favicon.ico", "/register", "/reset", "/verify"})
                .permitAll()
                /*.antMatchers(SecurityXMLConfig.getNoAuthUrl())
                .permitAll()
                .antMatchers(SecurityXMLConfig.getAuthReqUrl())*/
                .anyRequest()
                //.authenticated()
                .permitAll()
                .and()
                //.addFilterBefore(
                  //      authenticationFilter(),
                    //    UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .failureUrl("/login?error=true")
                //.successForwardUrl("/home")
                .permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .permitAll()
                .and()
                .rememberMe()
                .rememberMeServices(getTokenBasedRememberMeServices());
    }

    /**
     * Csrf token repository that will set csrf token in the header. AngularJS will read the csrf token and add the csrf token to all the
     * requests
     * @return the csrf token repository
     */
    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

    private void loginSuccessHandler(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        new DefaultRedirectStrategy().sendRedirect(request, response, "/home");
    }

    /**
     * Login failure handler.
     *
     * @param request the request
     * @param response the response
     * @param e the e
     * @throws IOException Signals that an I/O exception has occurred.
     */
    /*private void loginFailureHandler(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        if(e instanceof BadCredentialsException) {
            String message=messageSource.getMessage("error.user.bad_credentials", null, LocaleContextHolder.getLocale());
            Utils.sendJSONErrorResponse(response, Utils.getApiException(message, HttpStatus.UNAUTHORIZED, ErrorCodes.INVALID_CREDENTIALS));
        }else if(e instanceof UsernameNotFoundException){
            Utils.sendJSONErrorResponse(response, Utils.getApiException(e.getMessage(), HttpStatus.UNAUTHORIZED, ErrorCodes.EMAIL_NOT_EXISTS));
        }else if(e instanceof DisabledException) {
            String message=messageSource.getMessage("error.user.email_not_verified", null, LocaleContextHolder.getLocale());
            Utils.sendJSONErrorResponse(response, Utils.getApiException(message, HttpStatus.UNAUTHORIZED, ErrorCodes.EMAIL_NOT_VERIFIED));
        }else {
            String message=messageSource.getMessage("error.unknown.issue", null, LocaleContextHolder.getLocale());
            Utils.sendJSONErrorResponse(response, Utils.getApiException(message, HttpStatus.UNAUTHORIZED, ErrorCodes.UNKNOWN_ISSUE));
        }
    }*/
}
