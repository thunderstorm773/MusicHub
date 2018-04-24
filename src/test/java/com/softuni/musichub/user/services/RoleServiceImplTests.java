package com.softuni.musichub.user.services;

import com.softuni.musichub.staticData.TestConstants;
import com.softuni.musichub.user.entities.Role;
import com.softuni.musichub.user.models.viewModels.RoleView;
import com.softuni.musichub.user.repositories.RoleRepository;
import com.softuni.musichub.util.MapperUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@ActiveProfiles(TestConstants.TEST_PROFILE)
@SpringBootTest
public class RoleServiceImplTests {

    private static final String EXPECTED_ROLE_ID = "d6e4ad7c-ed62-44d1-88f4-77895331d53a";
    private static final String EXISTENCE_ROLE_NAME = "ROLE_USER";
    private static final String NON_EXISTENCE_ROLE_NAME = "fefe";
    private static final String TEST_ROLE_NAME = "ROLE_MODERATOR";

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private MapperUtil mapperUtil;

    @Autowired
    private RoleService roleService;

    private Role createTestRole() {
        Role role = new Role();
        role.setId(EXPECTED_ROLE_ID);
        role.setAuthority(EXISTENCE_ROLE_NAME);
        return role;
    }

    @Before
    public void setUp() {
        Mockito.when(this.roleRepository.findByAuthority(TEST_ROLE_NAME))
                .thenReturn(this.createTestRole());

        Mockito.when(this.roleRepository.findByAuthority(NON_EXISTENCE_ROLE_NAME))
                .thenReturn(null);

        Mockito.when(this.roleRepository.findByAuthority(EXISTENCE_ROLE_NAME))
                .thenReturn(this.createTestRole());

        Mockito.when(this.roleRepository.findAll()).thenReturn(new ArrayList<>());
    }

    @Test
    public void testFindByName_withRoleName_shouldInvokesRoleRepositoryFindByAuthority() {
        this.roleService.findByName(TEST_ROLE_NAME);
        Mockito.verify(this.roleRepository, Mockito.times(1))
                .findByAuthority(TEST_ROLE_NAME);
    }

    @Test
    public void testFindByName_withNonExistenceRoleName_returnsNull() {
        RoleView roleView = this.roleService.findByName(NON_EXISTENCE_ROLE_NAME);
        Assert.assertNull(roleView);
    }

    @Test
    public void testFindByName_withExistenceRoleName_returnsCorrectlyMappedRoleView() {
        RoleView roleView = this.roleService.findByName(EXISTENCE_ROLE_NAME);

        Assert.assertEquals(EXPECTED_ROLE_ID, roleView.getId());
        Assert.assertEquals(EXISTENCE_ROLE_NAME, roleView.getAuthority());
    }

    @Test
    public void testFindAll_shouldInvokesRoleRepositoryFindAll() {
        this.roleService.findAll();
        Mockito.verify(this.roleRepository, Mockito.times(1))
                .findAll();
    }
}