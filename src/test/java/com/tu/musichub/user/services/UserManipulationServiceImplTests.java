package com.tu.musichub.user.services;

import com.tu.musichub.user.entities.Role;
import com.tu.musichub.user.entities.User;
import com.tu.musichub.user.models.bindingModels.EditUser;
import com.tu.musichub.user.models.bindingModels.RegisterUser;
import com.tu.musichub.user.models.viewModels.RoleView;
import com.tu.musichub.user.repositories.UserRepository;
import com.tu.musichub.user.staticData.AccountConstants;
import com.tu.musichub.util.MapperUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserManipulationServiceImplTests {

    private static final String TEST_PASSWORD = "password";
    private static final String TEST_CONFIRM_PASSWORD = TEST_PASSWORD;
    private static final String EXPECTED_USERNAME = "username";
    private static final String EXPECTED_EMAIL = "username@abv.bg";
    private static final String EXPECTED_ROLE_USER_ID = "f52018a1-c931-49a0-86af-2faad60af792";
    private static final String EXPECTED_USER_ID = "f54538a1-c931-49a0-23af-2gfrd60af111";
    private static final boolean EXPECTED_IS_ACCOUNT_NON_EXPIRED = true;
    private static final boolean EXPECTED_IS_ACCOUNT_NON_LOCKED = true;
    private static final boolean EXPECTED_IS_CREDENTIALS_NON_EXPIRED = true;
    private static final boolean EXPECTED_IS_ENABLED = true;
    private static final String TEST_USERNAME = "testUsername";
    private static final String NON_EXISTENCE_USERNAME = "nonExistence";
    private static final String EXISTENCE_USERNAME = "existence";

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleServiceImpl roleService;

    @MockBean
    private ClientRegistrationRepository clientRegistrationRepository;

    @MockBean
    private JavaMailSender mailSender;

    @Autowired
    private MapperUtil mapperUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserManipulationServiceImpl userManipulationService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    private RegisterUser testRegisterUser;

    private RoleView testRoleUser;

    private User testUser;

    private EditUser testEditUser;

    private void fillTestRegisterUser() {
        this.testRegisterUser = new RegisterUser();
        this.testRegisterUser.setUsername(EXPECTED_USERNAME);
        this.testRegisterUser.setEmail(EXPECTED_EMAIL);
        this.testRegisterUser.setPassword(TEST_PASSWORD);
        this.testRegisterUser.setConfirmPassword(TEST_CONFIRM_PASSWORD);
    }

    private void fillTestRoleUser() {
        this.testRoleUser = new RoleView();
        this.testRoleUser.setId(EXPECTED_ROLE_USER_ID);
        this.testRoleUser.setAuthority(AccountConstants.ROLE_USER);
    }

    private void fillTestUser() {
        this.testUser = new User();
        this.testUser.setId(EXPECTED_USER_ID);
        String encodedPassword = this.passwordEncoder.encode(TEST_PASSWORD);
        this.testUser.setPassword(encodedPassword);
        this.testUser.setUsername(EXPECTED_USERNAME);
        this.testUser.setEmail(EXPECTED_EMAIL);
        this.testUser.setAccountNonExpired(EXPECTED_IS_ACCOUNT_NON_EXPIRED);
        this.testUser.setAccountNonLocked(EXPECTED_IS_ACCOUNT_NON_LOCKED);
        this.testUser.setEnabled(EXPECTED_IS_ENABLED);
        this.testUser.setCredentialsNonExpired(EXPECTED_IS_CREDENTIALS_NON_EXPIRED);

        Role role = new Role();
        role.setId(EXPECTED_ROLE_USER_ID);
        role.setAuthority(AccountConstants.ROLE_USER);
        this.testUser.getAuthorities().add(role);
    }

    private void fillTestEditUser() {
        this.testEditUser = new EditUser();
        this.testEditUser.setRoleNames(new HashSet<>());
        this.testEditUser.getRoleNames().add(AccountConstants.ROLE_USER);
    }

    @Before
    public void setUp() {
        this.fillTestRegisterUser();
        this.fillTestRoleUser();
        this.fillTestUser();
        this.fillTestEditUser();

        Mockito.when(this.roleService.findByName(AccountConstants.ROLE_USER))
                .thenReturn(this.testRoleUser);

        Mockito.when(this.userRepository.save(this.userArgumentCaptor.capture()))
                .thenReturn(this.testUser);

        Mockito.when(this.userRepository.findByUsername(NON_EXISTENCE_USERNAME))
                .thenReturn(null);

        Mockito.when(this.userRepository.findByUsername(EXISTENCE_USERNAME))
                .thenReturn(this.testUser);
    }

    @Test
    public void testRegisterUser_withRegisterUser_shouldInvokesRoleServiceFindByName() {
        this.userManipulationService.registerUser(this.testRegisterUser);
        Mockito.verify(this.roleService, Mockito.times(1))
                .findByName(AccountConstants.ROLE_USER);
    }

    @Test
    public void testRegisterUser_withRegisterUser_shouldAddRoleToUser() {
        User user = this.userManipulationService.registerUser(this.testRegisterUser);
        Set<Role> roles = user.getAuthorities();

        Assert.assertEquals(1, roles.size());
    }

    @Test
    public void testRegisterUser_withRegisterUser_shouldAddRoleUserToUser() {
        User user = this.userManipulationService.registerUser(this.testRegisterUser);
        Set<Role> roles = user.getAuthorities();
        Role roleUser = roles.iterator().next();

        Assert.assertEquals(EXPECTED_ROLE_USER_ID, roleUser.getId());
        Assert.assertEquals(AccountConstants.ROLE_USER, roleUser.getAuthority());
    }

    @Test
    public void testTestRegisterUser_withRegisterUser_shouldInvokesUserRepositorySave() {
        this.userManipulationService.registerUser(this.testRegisterUser);
        Mockito.verify(this.userRepository, Mockito.times(1))
                .save(this.userArgumentCaptor.capture());
    }

    @Test
    public void testTestRegisterUser_withRegisterUser_shouldCorrectlyHashPassword() {
        User user = this.userManipulationService.registerUser(this.testRegisterUser);
        boolean isPasswordsMatching = this.passwordEncoder.matches(TEST_PASSWORD, user.getPassword());
        Assert.assertTrue(isPasswordsMatching);
    }

    @Test
    public void testTestRegisterUser_withRegisterUser_returnsCorrectlyMappedUser() {
        User user = this.userManipulationService.registerUser(this.testRegisterUser);

        Assert.assertEquals(EXPECTED_USER_ID, user.getId());
        Assert.assertEquals(EXPECTED_USERNAME, user.getUsername());
        Assert.assertEquals(EXPECTED_EMAIL, user.getEmail());
        Assert.assertEquals(EXPECTED_IS_ACCOUNT_NON_EXPIRED, user.isAccountNonExpired());
        Assert.assertEquals(EXPECTED_IS_ACCOUNT_NON_LOCKED, user.isAccountNonLocked());
        Assert.assertEquals(EXPECTED_IS_CREDENTIALS_NON_EXPIRED, user.isCredentialsNonExpired());
        Assert.assertEquals(EXPECTED_IS_ENABLED, user.isEnabled());
    }

    @Test
    public void testDeleteByUsername_withUsername_shouldInvokesUserRepositoryFindByUsername() {
        this.userManipulationService.deleteByUsername(TEST_USERNAME);
        Mockito.verify(this.userRepository, Mockito.times(1))
                .findByUsername(TEST_USERNAME);
    }

    @Test
    public void testDeleteByUsername_withNonExistenceUsername_returnsFalse() {
        boolean isDeleted = this.userManipulationService.deleteByUsername(NON_EXISTENCE_USERNAME);
        Assert.assertFalse(isDeleted);
    }

    @Test
    public void testDeleteByUsername_withExistenceUsername_shouldInvokesUserRepositoryDelete() {
        this.userManipulationService.deleteByUsername(EXISTENCE_USERNAME);
        Mockito.verify(this.userRepository, Mockito.times(1))
                .delete(this.userArgumentCaptor.capture());
    }

    @Test
    public void testDeleteByUsername_withExistenceUsername_returnsTrue() {
        boolean isDeleted = this.userManipulationService.deleteByUsername(EXISTENCE_USERNAME);
        Assert.assertTrue(isDeleted);
    }

    @Test
    public void testEdit_withUsername_shouldInvokesUserRepositoryFindByUsername() {
        this.userManipulationService.edit(new EditUser(), TEST_USERNAME);
        Mockito.verify(this.userRepository, Mockito.times(1))
                .findByUsername(TEST_USERNAME);
    }

    @Test
    public void testEdit_withNonExistenceUsername_returnsNull() {
        User user = this.userManipulationService.edit(this.testEditUser, NON_EXISTENCE_USERNAME);
        Assert.assertNull(user);
    }

    @Test
    public void testEdit_withExistenceUsernameAndNoRoles_returnsNull() {
        this.testEditUser.setRoleNames(new HashSet<>());
        User user = this.userManipulationService.edit(this.testEditUser, EXISTENCE_USERNAME);
        Assert.assertNull(user);
    }

    @Test
    public void testEdit_withExistenceUsernameAndNewRoles_returnsUserWithNewRoles() {
        User user = this.userManipulationService.edit(this.testEditUser, EXISTENCE_USERNAME);
        Set<Role> roles = user.getAuthorities();
        Role roleUser = roles.iterator().next();

        Assert.assertEquals(1, roles.size());
        Assert.assertEquals(AccountConstants.ROLE_USER, roleUser.getAuthority());
    }

    @Test
    public void testEdit_withExistenceUsernameAndNewRoles_returnsCorrectlyMappedUser() {
        User user = this.userManipulationService.edit(this.testEditUser, EXISTENCE_USERNAME);

        Assert.assertEquals(EXPECTED_USER_ID, user.getId());
        Assert.assertEquals(EXPECTED_USERNAME, user.getUsername());
        Assert.assertEquals(EXPECTED_EMAIL, user.getEmail());
        Assert.assertEquals(EXPECTED_IS_ACCOUNT_NON_EXPIRED, user.isAccountNonExpired());
        Assert.assertEquals(EXPECTED_IS_ACCOUNT_NON_LOCKED, user.isAccountNonLocked());
        Assert.assertEquals(EXPECTED_IS_CREDENTIALS_NON_EXPIRED, user.isCredentialsNonExpired());
        Assert.assertEquals(EXPECTED_IS_ENABLED, user.isEnabled());
    }
}