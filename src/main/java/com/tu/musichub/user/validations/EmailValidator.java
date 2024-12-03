package com.tu.musichub.user.validations;

import com.tu.musichub.user.exceptions.UserNotFoundException;
import com.tu.musichub.user.services.UserExtractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class EmailValidator implements ConstraintValidator<Email, String> {

    private final UserExtractionService userExtractionService;

    @Autowired
    public EmailValidator(UserExtractionService userExtractionService) {
        this.userExtractionService = userExtractionService;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        try {
            this.userExtractionService.findByEmail(email);
            return false;
        } catch (UserNotFoundException e) {
            return true;
        }
    }
}
