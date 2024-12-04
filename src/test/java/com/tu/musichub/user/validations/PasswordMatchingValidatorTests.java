package com.tu.musichub.user.validations;

import com.tu.musichub.user.models.bindingModels.RegisterUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintValidatorContext;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class PasswordMatchingValidatorTests {

    private static final String TEST_PASSWORD = "password";
    private static final String TEST_CONFIRM_PASSWORD = "confirmPassword";

    private PasswordMatchingValidator passwordMatchingValidator;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    private RegisterUser testRegisterUser;

    private void fillTestRegisterUser() {
        this.testRegisterUser = new RegisterUser();
    }

    @Before
    public void setUp() {
        this.passwordMatchingValidator = new PasswordMatchingValidator();
        this.fillTestRegisterUser();
    }

    @Test
    public void testIsValid_withNullPasswords_returnsFalse() {
        boolean isValid = this.passwordMatchingValidator
                .isValid(this.testRegisterUser, this.constraintValidatorContext);
        Assert.assertFalse(isValid);
    }

    @Test
    public void testIsValid_withNullPassword_returnsFalse() {
        this.testRegisterUser.setConfirmPassword(TEST_CONFIRM_PASSWORD);
        boolean isValid = this.passwordMatchingValidator
                .isValid(this.testRegisterUser, this.constraintValidatorContext);
        Assert.assertFalse(isValid);
    }

    @Test
    public void testIsValid_withNullConfirmPassword_returnsFalse() {
        this.testRegisterUser.setPassword(TEST_PASSWORD);
        boolean isValid = this.passwordMatchingValidator
                .isValid(this.testRegisterUser, this.constraintValidatorContext);
        Assert.assertFalse(isValid);
    }

    @Test
    public void testIsValid_withNonMatchingPasswords_returnsFalse() {
        this.testRegisterUser.setPassword(TEST_PASSWORD);
        this.testRegisterUser.setConfirmPassword(TEST_CONFIRM_PASSWORD);
        boolean isValid = this.passwordMatchingValidator
                .isValid(this.testRegisterUser, this.constraintValidatorContext);
        Assert.assertFalse(isValid);
    }

    @Test
    public void testIsValid_withMatchingPasswords_returnsTrue() {
        this.testRegisterUser.setPassword(TEST_PASSWORD);
        this.testRegisterUser.setConfirmPassword(TEST_PASSWORD);
        boolean isValid = this.passwordMatchingValidator
                .isValid(this.testRegisterUser, this.constraintValidatorContext);
        Assert.assertTrue(isValid);
    }
}