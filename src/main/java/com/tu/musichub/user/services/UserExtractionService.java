package com.tu.musichub.user.services;

import com.tu.musichub.user.exceptions.UserNotFoundException;
import com.tu.musichub.user.models.viewModels.ProfileView;
import com.tu.musichub.user.models.viewModels.UserView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserExtractionService extends UserDetailsService {

    UserView findByUsername(String username) throws UserNotFoundException;

    UserView findByEmail(String email) throws UserNotFoundException;

    Page<UserView> findAllByUsernameContains(String username, Pageable pageable);

    Page<UserView> findAll(Pageable pageable);

    boolean isUserHasAnyRole(String username, String... roleNames);

    ProfileView getUserProfileByUsername(String username) throws UserNotFoundException;
}
