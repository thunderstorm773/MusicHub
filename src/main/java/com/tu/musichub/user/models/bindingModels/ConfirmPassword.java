package com.tu.musichub.user.models.bindingModels;

import com.tu.musichub.user.staticData.AccountConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public abstract class ConfirmPassword {

    private String password;

    private String confirmPassword;

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
