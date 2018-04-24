package com.softuni.musichub.user.repositories;

import com.softuni.musichub.staticData.TestConstants;
import com.softuni.musichub.user.entities.Role;
import com.softuni.musichub.user.entities.User;
import com.softuni.musichub.user.staticData.AccountConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(TestConstants.TEST_PROFILE)
@DataJpaTest
public class UserRepositoryTests {

    private static final String TEST_USERNAME = "thunder";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager em;

    private User testUser;

    private Role testRoleAdmin;

    private Role testRoleModerator;

    private void addRoleToUser(Role role) {
        this.testUser.getAuthorities().add(role);
    }

    private void fillTestUser() {
        String testUserId = "786a5aeb-1c12-4552-8d98-e576071d02c8";
        this.testUser = this.em.getEntityManager().find(User.class, testUserId);
    }

    private void fillTestRoles() {
        String roleAdminId = "eac5d95c-8876-4b6e-8ca7-7c0824b8ab7d";
        String roleModeratorId = "e7b4da2e-c811-492b-b2b7-50fc276e0ab8";
        this.testRoleAdmin = this.em.getEntityManager().find(Role.class, roleAdminId);
        this.testRoleModerator = this.em.getEntityManager().find(Role.class, roleModeratorId);
    }

    @Before
    public void setUp() {
        this.fillTestUser();
        this.fillTestRoles();
    }

    @Test
    public void testIsUserHasAnyRole_withUserWithoutAnyOfExpectedRoles_returnsFalse() {
        boolean isUserHasAnyRole = this.userRepository.isUserHasAnyRole(TEST_USERNAME,
                AccountConstants.ROLE_ADMIN, AccountConstants.ROLE_MODERATOR);
        Assert.assertFalse(isUserHasAnyRole);
    }

    @Test
    public void testIsUserHasAnyRole_withUserWithAnyOfExpectedRoles_returnsTrue() {
        this.addRoleToUser(this.testRoleAdmin);
        boolean isUserHasAnyRole = this.userRepository.isUserHasAnyRole(TEST_USERNAME,
                AccountConstants.ROLE_ADMIN, AccountConstants.ROLE_MODERATOR);
        Assert.assertTrue(isUserHasAnyRole);
    }

    @Test
    public void testIsUserHasAnyRole_withUserWithAllOfExpectedRoles_returnsTrue() {
        this.addRoleToUser(this.testRoleAdmin);
        this.addRoleToUser(this.testRoleModerator);
        boolean isUserHasAnyRole = this.userRepository.isUserHasAnyRole(TEST_USERNAME,
                AccountConstants.ROLE_ADMIN, AccountConstants.ROLE_MODERATOR);
        Assert.assertTrue(isUserHasAnyRole);
    }
}
