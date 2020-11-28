package com.rentpal.accounts.controller;

import com.rentpal.accounts.common.Utils;
import com.rentpal.accounts.constants.Regex;
import com.rentpal.accounts.dto.APIRequestResponse;
import com.rentpal.accounts.model.User;
import com.rentpal.accounts.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import validator.UserValidator;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.Locale;
import java.util.Optional;

@Controller
@Validated
public class UserController {

    /** The user service. */
    private final UserService userService;

    private MessageSource messageSource;

    /** Trims the request parameters **/
    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @Autowired
    public UserController(UserService userService) {
        this.userService=userService;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) { this.messageSource = messageSource; }

    @GetMapping(value = "/login", produces = MediaType.TEXT_HTML_VALUE)
    public String getLogin(Model model, Optional<Boolean> error){
        model.addAttribute("error", error.orElse(false));
        return "login";
    }

    /**
     * Gets register view for the user.
     *
     * @return register.html
     */
    @GetMapping(value = "/register", produces = MediaType.TEXT_HTML_VALUE)
    public String getRegister() {
        return "register";
    }

    /**
     * Adds new user to the database.
     *
     * @param user the user
     * @param confirmPassword the confirm password
     * @return the response entity
     * @throws MessagingException the messaging exception
     */
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> postRegister(@ModelAttribute("user")User user, @RequestParam("confirmPassword") String confirmPassword, BindingResult bindingResult) throws MessagingException, BindException {
        new UserValidator().validate(user, bindingResult);
        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }
        String verificationSent=Utils.getMessage(messageSource, "verification.sent");
        String message=Utils.getMessage(messageSource,"success.user.added_success", new Object[]{verificationSent});
        APIRequestResponse response= Utils.getApiRequestResponse(message, userService.addUser(user, confirmPassword));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Verifies user account.
     *
     * @param token the token
     * @param model the model
     * @return verificationstatus.html
     */
    @GetMapping(value = "/verify", produces = MediaType.TEXT_HTML_VALUE)
    public String verifyUser(@RequestParam("token") String token, Model model) {
        userService.verifyAccountForUser(token);
        String message=Utils.getMessage(messageSource, "verificationstatus.accountverified");
        model.addAttribute("message", message);
        return "success";
    }

    /**
     * Gets reset password view for the user.
     *
     * @param model the model
     * @param token the token
     * @return resetpassword.html
     */
    @GetMapping(value = "/reset", produces = MediaType.TEXT_HTML_VALUE)
    public String getResetPassword(Model model, Optional<String> token) {
        if(!token.isPresent()) {
            model.addAttribute("display", "reset");
        }else {
            String email=userService.getUserEmailForToken(token.get());
            Locale locale=LocaleContextHolder.getLocale();
            String message=messageSource.getMessage("html.resetpassword.resetpassword", new Object[] {email}, locale);
            model.addAttribute("resettext",message);
            model.addAttribute("display", "passwordreset");
            model.addAttribute("token", token.get());
        }
        return "reset";
    }

    /**
     * Sends a reset password link to user
     *
     * @param email the email
     * @return the response entity
     * @throws MessagingException the messaging exception
     */
    @PostMapping(value = "/reset", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> sendResetPasswordLink(@Valid @Pattern(regexp = Regex.EMAIL, message = "error.user.email.invalid") @RequestParam("email") String email) throws MessagingException {
        userService.sendResetPasswordLink(email);
        String message=Utils.getMessage(messageSource, "html.resetpassword.resetsent");
        APIRequestResponse response=Utils.getApiRequestResponse(message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Updates user password
     *
     * @param password the password
     * @param confirmPassword the confirm password
     * @param token the token
     * @return the response entity
     * @throws MessagingException the messaging exception
     */
    @PutMapping(value = "/reset", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> resetPassword(@Valid @Pattern(regexp = Regex.PASSWORD, message = "error.user.password.invalid") @RequestParam("password") String password, @RequestParam("confirmpassword") String confirmPassword, @RequestParam("token") String token, Model model) throws MessagingException {
        userService.resetPassword(token, password, confirmPassword);
        Locale locale=LocaleContextHolder.getLocale();
        String message=messageSource.getMessage("html.resetpassword.success", null, locale);
        APIRequestResponse response=Utils.getApiRequestResponse(message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Changes the user password within the session.
     *
     * @param password the password
     * @param confirmPassword the confirm password
     * @return the response entity
     * @throws MessagingException the messaging exception
     */
    @PutMapping(value = "/api/settings")
    public ResponseEntity<Object> changePassword(@RequestParam("password") String password, @RequestParam("confirmpassword") String confirmPassword) throws MessagingException {
        userService.changePassword(password, confirmPassword);
        Locale locale=LocaleContextHolder.getLocale();
        String message=messageSource.getMessage("success.password.change", null, locale);
        APIRequestResponse response=Utils.getApiRequestResponse(message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Gets the home view.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping(value = "/home", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> home(/*Model model*/) {
        //model.addAttribute("username", Utils.getUserEmail().split("@")[0]);
        return new ResponseEntity<>("response", HttpStatus.OK);
    }

}
