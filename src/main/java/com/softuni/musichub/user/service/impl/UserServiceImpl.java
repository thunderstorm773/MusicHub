package com.softuni.musichub.user.service.impl;

import com.softuni.musichub.user.entity.Role;
import com.softuni.musichub.user.entity.User;
import com.softuni.musichub.user.exception.UserNotFoundException;
import com.softuni.musichub.user.model.bindingModel.EditUser;
import com.softuni.musichub.user.model.bindingModel.RegisterUser;
import com.softuni.musichub.user.model.viewModel.RoleView;
import com.softuni.musichub.user.model.viewModel.UserView;
import com.softuni.musichub.user.repository.UserRepository;
import com.softuni.musichub.user.service.api.RoleService;
import com.softuni.musichub.user.service.api.UserService;
import com.softuni.musichub.user.staticData.UserConstants;
import com.softuni.musichub.utils.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    private Page<UserView> convertToPage(Page<User> userPage, Pageable pageable) {
        List<User> users = userPage.getContent();
        List<UserView> userViews = this.mapperUtil.convertAll(users, UserView.class);
        Long totalElements = userPage.getTotalElements();
        Page<UserView> userViewPage = new PageImpl<>(userViews, pageable, totalElements);
        return userViewPage;
    }

    private void setRoleViews(UserView userView, Set<Role> roles) {
        Set<String> roleNames = roles.stream().map(Role::getAuthority)
                .collect(Collectors.toSet());
        userView.setRoleNames(roleNames);
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

        Set<Role> roles = user.getAuthorities();
        UserView userView = this.mapperUtil.getModelMapper().map(user, UserView.class);
        this.setRoleViews(userView, roles);
        return userView;
    }

    @Override
    public Page<UserView> findAllByUsernameContains(String username, Pageable pageable) {
        Page<User> userPage = this.userRepository.findAllByUsernameContains(username, pageable);
        Page<UserView> userViewPage = this.convertToPage(userPage, pageable);
        return userViewPage;
    }

    @Override
    public Page<UserView> findAll(Pageable pageable) {
        Page<User> userPage = this.userRepository.findAll(pageable);
        Page<UserView> userViewPage = this.convertToPage(userPage, pageable);
        return userViewPage;
    }

    @Override
    public void deleteByUsername(String username) {
        User user = this.userRepository.findByUsername(username);
        if (user == null) {
            return;
        }

        this.userRepository.delete(user);
    }

    @Override
    public void edit(EditUser editUser, String username) {
        User user = this.userRepository.findByUsername(username);
        if (user == null) {
            return;
        }

        Set<String> roleNames = editUser.getRoleNames();
        Set<RoleView> roleViews = new HashSet<>();
        for (String roleName : roleNames) {
            RoleView roleView = this.roleService.findByName(roleName);
            if (roleView == null) {
                continue;
            }

            roleViews.add(roleView);
        }

        if (roleViews.isEmpty()) {
            return;
        }

        List<Role> roleList = this.mapperUtil.convertAll(roleViews, Role.class);
        Set<Role> newRoles = new HashSet<>(roleList);
        user.setAuthorities(newRoles);
    }

    @Override
    public boolean isUserHasAnyRole(String username, String... roleNames) {
        BigInteger result = (BigInteger) this.userRepository.isUserHasAnyRole(username, roleNames);
        return result.equals(new BigInteger("1"));
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
