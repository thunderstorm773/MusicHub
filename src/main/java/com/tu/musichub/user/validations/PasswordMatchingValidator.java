package com.tu.musichub.user.validations;

import com.tu.musichub.user.models.bindingModels.RegisterUser;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchingValidator implements ConstraintValidator<PasswordMatching, RegisterUser> {


    @Override
    public boolean isValid(RegisterUser registerUser, ConstraintValidatorContext constraintValidatorContext) {
        String password = registerUser.getPassword();
        String confirmPassword = registerUser.getConfirmPassword();
        if (password == null || confirmPassword == null) {
            return false;
        }

        return password.equals(confirmPassword);
    }
}
