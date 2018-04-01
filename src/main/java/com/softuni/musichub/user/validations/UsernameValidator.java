package com.softuni.musichub.user.validations;

import com.softuni.musichub.user.exceptions.UserNotFoundException;
import com.softuni.musichub.user.models.viewModels.UserView;
import com.softuni.musichub.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UsernameValidator implements ConstraintValidator<Username, String> {

    private final UserService userService;

    @Autowired
    public UsernameValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        try {
            this.userService.findByUsername(username);
            return false;
        } catch (UserNotFoundException e) {
            return true;
        }
    }
}
