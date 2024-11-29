package com.tu.musichub.user.services;

import com.tu.musichub.user.models.viewModels.RoleView;

import java.util.List;

public interface RoleService {

    RoleView findByName(String roleName);

    List<RoleView> findAll();
}
