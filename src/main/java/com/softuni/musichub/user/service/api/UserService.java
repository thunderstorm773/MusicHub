package com.softuni.musichub.user.service.api;

import com.softuni.musichub.user.exception.UserNotFoundException;
import com.softuni.musichub.user.model.bindingModel.EditUser;
import com.softuni.musichub.user.model.bindingModel.RegisterUser;
import com.softuni.musichub.user.model.viewModel.UserView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void registerUser(RegisterUser registerUser);

    UserView findByUsername(String username) throws UserNotFoundException;

    Page<UserView> findAllByUsernameContains(String username, Pageable pageable);

    Page<UserView> findAll(Pageable pageable);

    void deleteByUsername(String username);

    void edit(EditUser editUser, String username);
}
