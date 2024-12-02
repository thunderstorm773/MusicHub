package com.tu.musichub.user.configs;

import com.tu.musichub.user.services.UserManipulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserManipulationService userManipulationService;

    @Autowired
    public GoogleOAuth2SuccessHandler(UserManipulationService userManipulationService) {
        this.userManipulationService = userManipulationService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        this.userManipulationService.loginGoogleUser(authentication);
        response.sendRedirect("/");
    }
}
