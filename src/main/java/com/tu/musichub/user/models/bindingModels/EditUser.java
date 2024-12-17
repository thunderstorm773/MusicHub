package com.tu.musichub.user.models.bindingModels;

import com.tu.musichub.admin.staticData.AdminConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class EditUser {

    @NotNull
    @Size(min = AdminConstants.MIN_ROLES_COUNT)
    private Set<String> roleNames;
}
