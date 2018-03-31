package com.softuni.musichub.user.services;

import com.softuni.musichub.user.exceptions.UserNotFoundException;
import com.softuni.musichub.user.models.bindingModels.EditUser;
import com.softuni.musichub.user.models.bindingModels.RegisterUser;
import com.softuni.musichub.user.models.viewModels.UserView;
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

    boolean isUserHasAnyRole(String username, String... roleNames);
}
