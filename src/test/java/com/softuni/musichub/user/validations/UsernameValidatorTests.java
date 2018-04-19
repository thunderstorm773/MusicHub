package com.softuni.musichub.user.validations;

import com.softuni.musichub.user.exceptions.UserNotFoundException;
import com.softuni.musichub.user.models.viewModels.UserView;
import com.softuni.musichub.user.services.UserExtractionService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintValidatorContext;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsernameValidatorTests {

    private static final String TEST_USERNAME = "test";
    private static final String EXISTENCE_USERNAME = "username";
    private static final String NON_EXISTENCE_USERNAME = "nonUsername";

    @MockBean
    private UserExtractionService userExtractionService;

    @Autowired
    private UsernameValidator usernameValidator;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Before
    public void setUp() {
        Mockito.when(this.userExtractionService.findByUsername(NON_EXISTENCE_USERNAME))
                .thenThrow(UserNotFoundException.class);

        Mockito.when(this.userExtractionService.findByUsername(EXISTENCE_USERNAME))
                .thenReturn(new UserView());
    }

    @Test
    public void testIsValid_withUsername_shouldInvokesUserExtractionServiceFindByUsername() {
        this.usernameValidator.isValid(TEST_USERNAME, this.constraintValidatorContext);
        Mockito.verify(this.userExtractionService, Mockito.times(1))
                .findByUsername(TEST_USERNAME);
    }

    @Test
    public void testIsValid_withNonExistenceUsername_returnsTrue() {
        boolean isValid = this.usernameValidator
                .isValid(NON_EXISTENCE_USERNAME, this.constraintValidatorContext);
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValid_withExistenceUsername_returnsFalse() {
        boolean isValid = this.usernameValidator
                .isValid(EXISTENCE_USERNAME, this.constraintValidatorContext);
        Assert.assertFalse(isValid);
    }
}