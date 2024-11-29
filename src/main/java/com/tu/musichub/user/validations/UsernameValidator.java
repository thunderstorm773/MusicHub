package com.tu.musichub.user.validations;

import com.tu.musichub.user.exceptions.UserNotFoundException;
import com.tu.musichub.user.services.UserExtractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UsernameValidator implements ConstraintValidator<Username, String> {

    private final UserExtractionService userExtractionService;

    @Autowired
    public UsernameValidator(UserExtractionService userExtractionService) {
        this.userExtractionService = userExtractionService;
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        try {
            this.userExtractionService.findByUsername(username);
            return false;
        } catch (UserNotFoundException e) {
            return true;
        }
    }
}
