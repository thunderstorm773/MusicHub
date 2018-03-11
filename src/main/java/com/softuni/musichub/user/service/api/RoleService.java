package com.softuni.musichub.user.service.api;

import com.softuni.musichub.user.model.viewModel.RoleView;

public interface RoleService {

    RoleView findByName(String roleName);
}
