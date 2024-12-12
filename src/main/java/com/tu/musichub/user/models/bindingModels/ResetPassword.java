package com.tu.musichub.user.models.bindingModels;

import com.tu.musichub.user.validations.PasswordMatching;

@PasswordMatching
public class ResetPassword extends ConfirmPassword {

    private String token;

    public ResetPassword() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
