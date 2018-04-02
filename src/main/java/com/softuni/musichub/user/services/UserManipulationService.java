package com.softuni.musichub.user.services;

import com.softuni.musichub.user.models.bindingModels.EditUser;
import com.softuni.musichub.user.models.bindingModels.RegisterUser;

public interface UserManipulationService {

    void registerUser(RegisterUser registerUser);

    void deleteByUsername(String username);

    void edit(EditUser editUser, String username);
}
