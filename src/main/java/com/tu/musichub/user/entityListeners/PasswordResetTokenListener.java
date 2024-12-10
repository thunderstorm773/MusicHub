package com.tu.musichub.user.entityListeners;

import com.tu.musichub.user.entities.PasswordResetToken;
import com.tu.musichub.user.utils.UUIDUtil;

import javax.persistence.PrePersist;

public class PasswordResetTokenListener {

    @PrePersist
    public void setPasswordResetTokenId(PasswordResetToken passwordResetToken) {
        String passwordResetTokenId = UUIDUtil.getRandomUUID();
        passwordResetToken.setId(passwordResetTokenId);
    }
}
