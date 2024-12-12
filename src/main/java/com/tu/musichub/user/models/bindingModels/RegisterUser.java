package com.tu.musichub.user.models.bindingModels;

import com.tu.musichub.user.staticData.AccountConstants;
import com.tu.musichub.user.validations.Email;
import com.tu.musichub.user.validations.PasswordMatching;
import com.tu.musichub.user.validations.Username;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@PasswordMatching
public class RegisterUser extends ConfirmPassword {

    private String username;

    private String email;

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
    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
