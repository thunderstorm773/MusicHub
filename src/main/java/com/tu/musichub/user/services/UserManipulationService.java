package com.tu.musichub.user.services;

import com.tu.musichub.user.entities.User;
import com.tu.musichub.user.models.bindingModels.EditUser;
import com.tu.musichub.user.models.bindingModels.RegisterUser;

public interface UserManipulationService {

    User registerUser(RegisterUser registerUser);

    boolean deleteByUsername(String username);

    User edit(EditUser editUser, String username);
}
