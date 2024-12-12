package com.tu.musichub.user.models.viewModels;

import java.util.Date;

public class PasswordResetTokenView {

    private String id;

    private String token;

    private Date expiryDate;

    public PasswordResetTokenView() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
