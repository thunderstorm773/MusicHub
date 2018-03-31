package com.softuni.musichub.user.entityListeners;

import com.softuni.musichub.user.entities.Role;
import com.softuni.musichub.util.UUIDUtil;
import javax.persistence.PrePersist;

public class RoleEntityListener {

    @PrePersist
    public void setRoleId(Role role) {
        String roleId = UUIDUtil.getRandomUUID();
        role.setId(roleId);
    }
}
