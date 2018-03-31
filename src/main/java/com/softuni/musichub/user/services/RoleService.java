package com.softuni.musichub.user.services;

import com.softuni.musichub.user.models.viewModels.RoleView;

import java.util.List;

public interface RoleService {

    RoleView findByName(String roleName);

    List<RoleView> findAll();
}
