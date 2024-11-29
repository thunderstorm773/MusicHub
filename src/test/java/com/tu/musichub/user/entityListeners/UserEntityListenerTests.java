package com.tu.musichub.user.entityListeners;

import com.tu.musichub.user.entities.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserEntityListenerTests {

    private UserEntityListener userEntityListener;

    private User testUser;

    private User createUser() {
        User user = new User();
        user.setId(null);
        return user;
    }

    @Before
    public void setUp() {
        this.userEntityListener = new UserEntityListener();
        this.testUser = this.createUser();
    }

    @Test
    public void testSetUserId_withUser_shouldSetUUIDOfUser() {
        this.userEntityListener.setUserId(this.testUser);
        Assert.assertNotNull(this.testUser.getId());
    }
}