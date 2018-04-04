package com.softuni.musichub.user.services;

import com.softuni.musichub.user.exceptions.UserNotFoundException;
import com.softuni.musichub.user.models.viewModels.ProfileView;
import com.softuni.musichub.user.models.viewModels.UserView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserExtractionService extends UserDetailsService {

    UserView findByUsername(String username) throws UserNotFoundException;

    Page<UserView> findAllByUsernameContains(String username, Pageable pageable);

    Page<UserView> findAll(Pageable pageable);

    boolean isUserHasAnyRole(String username, String... roleNames);

    ProfileView getUserProfileByUsername(String username) throws UserNotFoundException;
}
