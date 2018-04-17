package com.softuni.musichub.user.services;

import com.softuni.musichub.song.models.viewModels.SongView;
import com.softuni.musichub.user.entities.Role;
import com.softuni.musichub.user.entities.User;
import com.softuni.musichub.user.exceptions.UserNotFoundException;
import com.softuni.musichub.user.models.viewModels.ProfileView;
import com.softuni.musichub.user.models.viewModels.UserView;
import com.softuni.musichub.user.repositories.UserRepository;
import com.softuni.musichub.user.staticData.AccountConstants;
import com.softuni.musichub.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Comparator;
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

    private void setRoleNames(UserView userView, Set<Role> roles) {
        Set<String> roleNames = roles.stream().map(Role::getAuthority)
                .collect(Collectors.toSet());
        userView.setRoleNames(roleNames);
    }

    private void sortProfileSongs(ProfileView profileView) {
        List<SongView> songViews = profileView.getSongs();
        Comparator<SongView> uploadedOnDesc = (s1, s2) ->
                s2.getUploadedOn().compareTo(s1.getUploadedOn());
        songViews = songViews.stream().sorted(uploadedOnDesc)
                .collect(Collectors.toList());
        profileView.setSongs(songViews);
    }

    @Override
    public UserView findByUsername(String username) throws UserNotFoundException {
        User user = this.userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException();
        }

        Set<Role> roles = user.getAuthorities();
        UserView userView = this.mapperUtil.getModelMapper().map(user, UserView.class);
        this.setRoleNames(userView, roles);
        return userView;
    }

    @Override
    public Page<UserView> findAllByUsernameContains(String username, Pageable pageable) {
        Page<User> userPage = this.userRepository.findAllByUsernameContains(username, pageable);
        return this.mapperUtil.convertToPage(pageable, userPage, UserView.class);
    }

    @Override
    public Page<UserView> findAll(Pageable pageable) {
        Page<User> userPage = this.userRepository.findAll(pageable);
        return this.mapperUtil.convertToPage(pageable, userPage, UserView.class);
    }

    @Override
    public boolean isUserHasAnyRole(String username, String... roleNames) {
        return this.userRepository.isUserHasAnyRole(username, roleNames);
    }

    @Override
    public ProfileView getUserProfileByUsername(String username) throws UserNotFoundException{
        User user = this.userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException();
        }

        ProfileView profileView = this.mapperUtil.getModelMapper()
                .map(user, ProfileView.class);
        this.sortProfileSongs(profileView);
        return profileView;
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
