package com.rentpal.accounts.validator;

import com.rentpal.accounts.constants.Regex;
import com.rentpal.accounts.model.User;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

public class UserValidator implements Validator {

    private static final Pattern EMAIL_REGEX =
            Pattern.compile(Regex.EMAIL);

    private static final Pattern PASSWORD_REGEX=
            Pattern.compile(Regex.PASSWORD);
    @Override
    public boolean supports(Class userClass) {
        return User.class.equals(userClass);
    }

    @Override
    public void validate(final Object object, final Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "email", "error.user.email.empty");
        ValidationUtils.rejectIfEmpty(errors,"password", "error.user.password.empty");
        User user=(User) object;
        if(user.getEmail()!=null && !EMAIL_REGEX.matcher(user.getEmail()).matches()){
            errors.rejectValue("email","error.user.email.invalid");
        }
        if(user.getPassword()!=null && !PASSWORD_REGEX.matcher(user.getPassword()).matches()){
            errors.rejectValue("password","error.user.password.invalid");
        }
    }
}
