package com.softuni.musichub.user.services;

import com.softuni.musichub.user.entities.Role;
import com.softuni.musichub.user.entities.User;
import com.softuni.musichub.user.exceptions.UserNotFoundException;
import com.softuni.musichub.user.models.viewModels.UserView;
import com.softuni.musichub.user.repositories.UserRepository;
import com.softuni.musichub.user.staticData.AccountConstants;
import com.softuni.musichub.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserExtractionServiceImpl implements UserExtractionService {

    private final UserRepository userRepository;

    private final MapperUtil mapperUtil;

    @Autowired
    public UserExtractionServiceImpl(UserRepository userRepository,
                                     MapperUtil mapperUtil) {
        this.userRepository = userRepository;
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
    public boolean isUserHasAnyRole(String username, String... roleNames) {
        BigInteger result = (BigInteger) this.userRepository.isUserHasAnyRole(username, roleNames);
        return result.equals(new BigInteger("1"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(AccountConstants.INVALID_CREDENTIALS_MESSAGE);
        }

        return user;
    }
}
