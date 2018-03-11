package com.softuni.musichub.user.entityListener;

import com.softuni.musichub.user.entity.Role;
import com.softuni.musichub.utils.UUIDUtil;
import javax.persistence.PrePersist;

public class RoleEntityListener {

    @PrePersist
    public void setRoleId(Role role) {
        String roleId = UUIDUtil.getRandomUUID();
        role.setId(roleId);
    }
}
