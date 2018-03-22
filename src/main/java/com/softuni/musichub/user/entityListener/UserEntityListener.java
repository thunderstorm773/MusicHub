package com.softuni.musichub.user.entityListener;

import com.softuni.musichub.user.entity.User;
import com.softuni.musichub.utils.UUIDUtil;
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
