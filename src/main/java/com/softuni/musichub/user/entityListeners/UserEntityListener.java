package com.softuni.musichub.user.entityListeners;

import com.softuni.musichub.user.entities.User;
import com.softuni.musichub.user.utils.UUIDUtil;
import org.springframework.stereotype.Component;
import javax.persistence.PrePersist;

@Component
public class UserEntityListener {

    @PrePersist
    public void setUserId(User user) {
        String userId = UUIDUtil.getRandomUUID();
        user.setId(userId);
    }
}
