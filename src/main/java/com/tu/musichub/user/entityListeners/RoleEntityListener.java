package com.tu.musichub.user.entityListeners;

import com.tu.musichub.user.entities.Role;
import com.tu.musichub.user.utils.UUIDUtil;
import javax.persistence.PrePersist;

public class RoleEntityListener {

    @PrePersist
    public void setRoleId(Role role) {
        String roleId = UUIDUtil.getRandomUUID();
        role.setId(roleId);
    }
}
