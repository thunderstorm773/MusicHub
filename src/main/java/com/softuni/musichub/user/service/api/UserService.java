package com.softuni.musichub.user.service.api;

import com.softuni.musichub.user.exception.UserNotFoundException;
import com.softuni.musichub.user.model.bindingModel.RegisterUser;
import com.softuni.musichub.user.model.viewModel.UserView;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void registerUser(RegisterUser registerUser);

    UserView findByUsername(String username) throws UserNotFoundException;
}
