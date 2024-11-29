package com.tu.musichub.user.entityListeners;

import com.tu.musichub.user.entities.User;
import com.tu.musichub.user.utils.UUIDUtil;

import javax.persistence.PrePersist;

public class UserEntityListener {

    @PrePersist
    public void setUserId(User user) {
        String userId = UUIDUtil.getRandomUUID();
        user.setId(userId);
    }
}
