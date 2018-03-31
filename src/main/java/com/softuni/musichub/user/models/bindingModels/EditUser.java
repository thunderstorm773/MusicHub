package com.softuni.musichub.user.models.bindingModels;

import com.softuni.musichub.admin.staticData.AdminConstants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public class EditUser {

    private Set<String> roleNames;

    public EditUser() {
    }

    @NotNull
    @Size(min = AdminConstants.MIN_ROLES_COUNT)
    public Set<String> getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(Set<String> roleNames) {
        this.roleNames = roleNames;
    }
}
