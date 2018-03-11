package com.softuni.musichub.user.service.impl;

import com.softuni.musichub.user.entity.Role;
import com.softuni.musichub.user.entity.User;
import com.softuni.musichub.user.exception.UserNotFoundException;
import com.softuni.musichub.user.model.bindingModel.RegisterUser;
import com.softuni.musichub.user.model.viewModel.RoleView;
import com.softuni.musichub.user.model.viewModel.UserView;
import com.softuni.musichub.user.repository.UserRepository;
import com.softuni.musichub.user.service.api.RoleService;
import com.softuni.musichub.user.service.api.UserService;
import com.softuni.musichub.user.staticData.UserConstants;
import com.softuni.musichub.utils.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final String ROLE_USER = "ROLE_USER";

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final BCryptPasswordEncoder passwordEncoder;

    private final MapperUtil mapperUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleService roleService,
                           BCryptPasswordEncoder passwordEncoder,
                           MapperUtil mapperUtil) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public void registerUser(RegisterUser registerUser) {
        User user = this.mapperUtil.getModelMapper().map(registerUser, User.class);
        String password = user.getPassword();
        String hashedPassword = this.passwordEncoder.encode(password);
        user.setPassword(hashedPassword);

        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        RoleView roleView = this.roleService.findByName(ROLE_USER);
        Role role = this.mapperUtil.getModelMapper().map(roleView, Role.class);
        user.getAuthorities().add(role);
        this.userRepository.save(user);
    }

    @Override
    public UserView findByUsername(String username) throws UserNotFoundException {
        User user = this.userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException();
        }

        UserView userView = this.mapperUtil.getModelMapper()
                .map(user, UserView.class);
        return userView;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(UserConstants.INVALID_CREDENTIALS_MESSAGE);
        }

        return user;
    }
}
