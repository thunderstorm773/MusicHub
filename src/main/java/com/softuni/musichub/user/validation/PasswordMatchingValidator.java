package com.softuni.musichub.user.validation;

import com.softuni.musichub.user.model.bindingModel.RegisterUser;
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
