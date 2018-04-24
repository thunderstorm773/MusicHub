package com.softuni.musichub.user.services;

import com.softuni.musichub.staticData.TestConstants;
import com.softuni.musichub.user.entities.Role;
import com.softuni.musichub.user.entities.User;
import com.softuni.musichub.user.exceptions.UserNotFoundException;
import com.softuni.musichub.user.models.viewModels.ProfileView;
import com.softuni.musichub.user.models.viewModels.UserView;
import com.softuni.musichub.user.repositories.UserRepository;
import com.softuni.musichub.util.MapperUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RunWith(SpringRunner.class)
@ActiveProfiles(TestConstants.TEST_PROFILE)
@SpringBootTest
public class UserExtractionServiceImplTests {

    private static final String TEST_USERNAME = "testUsername";
    private static final String NON_EXISTENCE_USERNAME = "nonUsername";
    private static final String EXPECTED_USER_ID = "d1db5fa6-20b4-4493-8886-e11ed62569ba";
    private static final String EXPECTED_USER_USERNAME = "username";
    private static final String EXPECTED_USER_EMAIL = "test@abv.bg";
    private static final String EXPECTED_ROLE_USER = "ROLE_USER";
    private static final String EXPECTED_ROLE_MODERATOR = "ROLE_MODERATOR";
    private static final int EXPECTED_ROLES_SIZE = 2;

    private Set<String> testRoleNames;

    private Page<User> testUserPage;

    @MockBean
    private UserRepository userRepository;

    @Mock
    private Pageable pageable;

    @Autowired
    private MapperUtil mapperUtil;

    @Autowired
    private UserExtractionService userExtractionService;

    private Role createTestRole(String roleName) {
        Role role = new Role();
        role.setId(UUID.randomUUID().toString());
        role.setAuthority(roleName);
        return role;
    }

    private User createTestUser() {
        User user = new User();
        user.setId(EXPECTED_USER_ID);
        user.setUsername(EXPECTED_USER_USERNAME);
        user.setEmail(EXPECTED_USER_EMAIL);
        Role roleUser = this.createTestRole(EXPECTED_ROLE_USER);
        Role roleModerator = this.createTestRole(EXPECTED_ROLE_MODERATOR);
        user.getAuthorities().add(roleUser);
        user.getAuthorities().add(roleModerator);
        user.setSongs(new HashSet<>());
        return user;
    }

    private void fillTestRoleNames() {
        this.testRoleNames = new HashSet<>();
        this.testRoleNames.add(EXPECTED_ROLE_USER);
        this.testRoleNames.add(EXPECTED_ROLE_MODERATOR);
    }

    private void fillTestUserPage() {
        this.testUserPage = new PageImpl<>(new ArrayList<>());
    }

    @Before
    public void setUp()  {
        this.fillTestRoleNames();
        this.fillTestUserPage();
        Mockito.when(this.userRepository.findByUsername(TEST_USERNAME))
                .thenReturn(this.createTestUser());

        Mockito.when(this.userRepository.findByUsername(NON_EXISTENCE_USERNAME))
                .thenThrow(UserNotFoundException.class);

        Mockito.when(this.userRepository.findByUsername(EXPECTED_USER_USERNAME))
                .thenReturn(this.createTestUser());

        Mockito.when(this.userRepository.findAllByUsernameContains(TEST_USERNAME, this.pageable))
                .thenReturn(this.testUserPage);

        Mockito.when(this.userRepository.findAll(this.pageable))
                .thenReturn(this.testUserPage);
    }

    @Test
    public void testFindByUsername_withUsername_shouldInvokesUserRepositoryFindByUsername() {
        this.userExtractionService.findByUsername(TEST_USERNAME);
        Mockito.verify(this.userRepository, Mockito.times(1))
                .findByUsername(TEST_USERNAME);
    }

    @Test(expected = UserNotFoundException.class)
    public void testFindByUsername_withNonExistenceUsername_shouldThrowsUserNotFoundException() {
        this.userExtractionService.findByUsername(NON_EXISTENCE_USERNAME);
    }

    @Test
    public void testFindByUsername_withExistenceUsername_returnsCorrectlyMappedUserView() {
        UserView userView = this.userExtractionService.findByUsername(EXPECTED_USER_USERNAME);

        Assert.assertEquals(EXPECTED_USER_ID, userView.getId());
        Assert.assertEquals(EXPECTED_USER_USERNAME, userView.getUsername());
        Assert.assertEquals(EXPECTED_USER_EMAIL, userView.getEmail());
    }

    @Test
    public void testFindByUsername_withExistenceUsername_returnsCorrectlyMappedUserViewWithCorrectRoleNames() {
        UserView userView = this.userExtractionService.findByUsername(EXPECTED_USER_USERNAME);
        Set<String> roleNames = userView.getRoleNames();

        Assert.assertEquals(EXPECTED_ROLES_SIZE, userView.getRoleNames().size());
        for (String testRoleName : this.testRoleNames) {
            Assert.assertTrue(roleNames.contains(testRoleName));
        }
    }

    @Test
    public void testFindAllByUsernameContains_withUsername_shouldInvokesUserRepositoryFindAllByUsernameContains() {
        this.userExtractionService.findAllByUsernameContains(TEST_USERNAME, this.pageable);
        Mockito.verify(this.userRepository, Mockito.times(1))
                .findAllByUsernameContains(TEST_USERNAME, this.pageable);
    }

    @Test
    public void testFindAll_withPageable_shouldInvokesUserRepositoryFindAll() {
        this.userExtractionService.findAll(this.pageable);
        Mockito.verify(this.userRepository, Mockito.times(1))
                .findAll(this.pageable);
    }

    @Test
    public void testIsUserHasAnyRole_withUsername_shouldInvokesUserRepositoryIsUserHasAnyRole() {
        this.userExtractionService.isUserHasAnyRole(TEST_USERNAME, EXPECTED_ROLE_USER);
        Mockito.verify(this.userRepository, Mockito.times(1))
                .isUserHasAnyRole(TEST_USERNAME, EXPECTED_ROLE_USER);
    }

    @Test
    public void testLoadUserByUsername_withUsername_shouldInvokesUserRepositoryFindByUsername() {
        this.userExtractionService.loadUserByUsername(TEST_USERNAME);
        Mockito.verify(this.userRepository, Mockito.times(1))
                .findByUsername(TEST_USERNAME);
    }

    @Test(expected = UserNotFoundException.class)
    public void testLoadUserByUsername_withNonExistenceUsername_shouldThrowsUsernameNotFoundException() {
        this.userExtractionService.loadUserByUsername(NON_EXISTENCE_USERNAME);
    }

    @Test
    public void testLoadUserByUsername_withExistenceUsername_returnsCorrectUser() {
        UserDetails userDetails = this.userExtractionService.loadUserByUsername(EXPECTED_USER_USERNAME);
        Assert.assertEquals(EXPECTED_USER_USERNAME, userDetails.getUsername());
    }

    @Test
    public void testGetUserProfileByUsername_withUsername_shouldInvokesUserRepositoryFindByUsername() {
        this.userExtractionService.getUserProfileByUsername(TEST_USERNAME);
        Mockito.verify(this.userRepository, Mockito.times(1))
                .findByUsername(TEST_USERNAME);
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetUserProfileByUsername_withNonExistenceUsername_shouldThrowsUserNotFoundException() {
        this.userExtractionService.getUserProfileByUsername(NON_EXISTENCE_USERNAME);
    }

    @Test
    public void testGetUserProfileByUsername_withExistenceUsername_returnsCorrectProfile() {
        ProfileView profileView = this.userExtractionService
                .getUserProfileByUsername(EXPECTED_USER_USERNAME);
        Assert.assertEquals(EXPECTED_USER_USERNAME, profileView.getUsername());
    }

    //TODO To tests if songs are correctly sorted descending by published date
}