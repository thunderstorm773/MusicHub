package com.tu.musichub.user.services;

import com.tu.musichub.user.entities.User;
import com.tu.musichub.user.models.bindingModels.EditUser;
import com.tu.musichub.user.models.bindingModels.RegisterUser;
import com.tu.musichub.user.models.bindingModels.ResetPassword;
import org.springframework.security.core.Authentication;

public interface UserManipulationService {

    User registerUser(RegisterUser registerUser);

    User loginGoogleUser(Authentication authentication);

    boolean deleteByUsername(String username);

    User edit(EditUser editUser, String username);

    void resetPassword(ResetPassword resetPassword);
}
