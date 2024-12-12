package com.tu.musichub.user.validations;

import com.tu.musichub.user.models.bindingModels.ConfirmPassword;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchingValidator implements ConstraintValidator<PasswordMatching, ConfirmPassword> {


    @Override
    public boolean isValid(ConfirmPassword baseConfirmPassword, ConstraintValidatorContext constraintValidatorContext) {
        String password = baseConfirmPassword.getPassword();
        String confirmPassword = baseConfirmPassword.getConfirmPassword();
        if (password == null || confirmPassword == null) {
            return false;
        }

        return password.equals(confirmPassword);
    }
}
