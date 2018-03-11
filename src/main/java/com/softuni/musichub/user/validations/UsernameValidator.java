package com.softuni.musichub.user.validations;

import com.softuni.musichub.user.exception.UserNotFoundException;
import com.softuni.musichub.user.model.viewModel.UserView;
import com.softuni.musichub.user.service.api.UserService;
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
            UserView userView = this.userService.findByUsername(username);
            return false;
        } catch (UserNotFoundException e) {
            return true;
        }
    }
}
