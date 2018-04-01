package com.softuni.musichub.user.models.bindingModels;

import com.softuni.musichub.user.staticData.AccountConstants;
import com.softuni.musichub.user.validations.PasswordMatching;
import com.softuni.musichub.user.validations.Username;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@PasswordMatching
public class RegisterUser {

    private String username;

    private String email;

    private String password;

    private String confirmPassword;

    public RegisterUser() {
    }

    @NotBlank(message = AccountConstants.USERNAME_BLANK_MESSAGE)
    @Size(min = AccountConstants.USERNAME_MIN_LEN,
            max = AccountConstants.USERNAME_MAX_LEN,
            message = AccountConstants.USERNAME_REQUIRED_LEN_MESSAGE)
    @Username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotBlank(message = AccountConstants.EMAIL_BLANK_MESSAGE)
    @Pattern(regexp = AccountConstants.EMAIL_PATTERN,
            message = AccountConstants.INCORRECT_EMAIL_FORMAT_MESSAGE)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotBlank(message = AccountConstants.PASSWORD_BLANK_MESSAGE)
    @Size(min = AccountConstants.PASSWORD_MIN_LEN,
            message = AccountConstants.PASSWORD_MIN_LEN_MESSAGE)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotBlank(message = AccountConstants.CONFIRM_PASSWORD_BLANK_MESSAGE)
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
