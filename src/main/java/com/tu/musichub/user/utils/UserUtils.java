package com.tu.musichub.user.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class UserUtils {

    public static String getUsername(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2User) {
            DefaultOAuth2User oAuthUser = (DefaultOAuth2User) principal;
            Map<String, Object> attributes = oAuthUser.getAttributes();

            return (String) attributes.get("name");
        } else if (principal instanceof UserDetails) {
            UserDetails user = (UserDetails) principal;
            return user.getUsername();
        }

        return null;
    }
}
