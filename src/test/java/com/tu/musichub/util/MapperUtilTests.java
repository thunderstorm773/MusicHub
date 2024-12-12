package com.tu.musichub.util;

import com.tu.musichub.user.entities.Role;
import com.tu.musichub.user.models.viewModels.RoleView;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperUtilTests {

    private static final String EXPECTED_ROLE_ID = "1a75e378-7fd3-22c4-12cd-2369b7d7e1ee";
    private static final String EXPECTED_ROLE_NAME = "ROLE_MODERATOR";
    private static final int EXPECTED_ROLES_SIZE = 1;

    @Autowired
    private MapperUtil mapperUtil;

    @MockBean
    private ClientRegistrationRepository clientRegistrationRepository;

    @MockBean
    private JavaMailSender mailSender;

    private List<Role> testRoles;

    private Role createTestRole() {
        Role role = new Role();
        role.setId(EXPECTED_ROLE_ID);
        role.setAuthority(EXPECTED_ROLE_NAME);
        return role;
    }

    @Before
    public void setUp() {
        Role testRole = this.createTestRole();
        this.testRoles = new ArrayList<>();
        this.testRoles.add(testRole);
    }

    @Test
    public void testConvertAll_withSource_returnsExpectedMappedEntitiesSize() {
        List<RoleView> roleViews = this.mapperUtil.convertAll(this.testRoles, RoleView.class);

        Assert.assertEquals(EXPECTED_ROLES_SIZE, roleViews.size());
    }

    @Test
    public void testConvertAll_withSource_returnsCorrectlyMappedSource() {
        List<RoleView> roleViews = this.mapperUtil.convertAll(this.testRoles, RoleView.class);
        RoleView roleView = roleViews.get(0);

        Assert.assertEquals(EXPECTED_ROLE_ID, roleView.getId());
        Assert.assertEquals(EXPECTED_ROLE_NAME, roleView.getAuthority());
    }
}
