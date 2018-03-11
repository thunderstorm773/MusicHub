package com.softuni.musichub.user.model.bindingModel;

import com.softuni.musichub.user.staticData.UserConstants;
import com.softuni.musichub.user.validation.PasswordMatching;
import com.softuni.musichub.user.validation.Username;
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

    @NotBlank(message = UserConstants.USERNAME_BLANK_MESSAGE)
    @Size(min = UserConstants.USERNAME_MIN_LEN,
            max = UserConstants.USERNAME_MAX_LEN,
            message = UserConstants.USERNAME_REQUIRED_LEN_MESSAGE)
    @Username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotBlank(message = UserConstants.EMAIL_BLANK_MESSAGE)
    @Pattern(regexp = UserConstants.EMAIL_PATTERN,
            message = UserConstants.INCORRECT_EMAIL_FORMAT_MESSAGE)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotBlank(message = UserConstants.PASSWORD_BLANK_MESSAGE)
    @Size(min = UserConstants.PASSWORD_MIN_LEN,
            message = UserConstants.PASSWORD_MIN_LEN_MESSAGE)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotBlank(message = UserConstants.CONFIRM_PASSWORD_BLANK_MESSAGE)
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
