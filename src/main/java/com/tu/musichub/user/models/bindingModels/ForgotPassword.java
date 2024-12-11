package com.tu.musichub.user.models.bindingModels;

import com.tu.musichub.user.staticData.AccountConstants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class ForgotPassword {

    private String email;

    @NotBlank(message = AccountConstants.EMAIL_BLANK_MESSAGE)
    @Email(regexp = AccountConstants.EMAIL_PATTERN, message = AccountConstants.INCORRECT_EMAIL_FORMAT_MESSAGE)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
