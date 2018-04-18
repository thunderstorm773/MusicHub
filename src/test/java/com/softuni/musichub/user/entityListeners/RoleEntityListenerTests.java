package com.softuni.musichub.user.entityListeners;

import com.softuni.musichub.user.entities.Role;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class RoleEntityListenerTests {

    private RoleEntityListener roleEntityListener;

    private Role testRole;

    private Role createRole() {
        Role role = new Role();
        role.setId(null);
        return role;
    }

    @Before
    public void setUp() {
        this.roleEntityListener = new RoleEntityListener();
        this.testRole = this.createRole();
    }

    @Test
    public void testSetRoleId_withRole_shouldSetUUIDOfRole() {
        this.roleEntityListener.setRoleId(this.testRole);
        Assert.assertNotNull(this.testRole.getId());
    }
}