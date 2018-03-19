package com.softuni.musichub.user.service.api;

import com.softuni.musichub.user.model.viewModel.RoleView;

import java.util.List;

public interface RoleService {

    RoleView findByName(String roleName);

    List<RoleView> findAll();
}
