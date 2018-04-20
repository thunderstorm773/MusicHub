package com.softuni.musichub.user.services;

import com.softuni.musichub.user.entities.User;
import com.softuni.musichub.user.models.bindingModels.EditUser;
import com.softuni.musichub.user.models.bindingModels.RegisterUser;
import com.softuni.musichub.user.models.viewModels.UserView;

public interface UserManipulationService {

    User registerUser(RegisterUser registerUser);

    boolean deleteByUsername(String username);

    User edit(EditUser editUser, String username);
}
