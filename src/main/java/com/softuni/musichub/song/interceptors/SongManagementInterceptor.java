package com.softuni.musichub.song.interceptors;

import com.softuni.musichub.song.model.viewModel.SongView;
import com.softuni.musichub.song.service.api.SongService;
import com.softuni.musichub.song.staticData.SongConstants;
import com.softuni.musichub.user.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class SongManagementInterceptor extends HandlerInterceptorAdapter {

    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private static final String ROLE_MODERATOR = "ROLE_MODERATOR";

    private final SongService songService;

    private final UserService userService;

    @Autowired
    public SongManagementInterceptor(SongService songService,
                                     UserService userService) {
        this.songService = songService;
        this.userService = userService;
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long songId;
        try {
            String songIdStr = (String) pathVariables.get(SongConstants.ID);
            songId = Long.valueOf(songIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect("/songs/browse");
            return false;
        }

        SongView songView = this.songService.findById(songId);
        String uploaderUsername = songView.getUploaderUsername();
        Authentication authentication = this.getAuthentication();
        String authenticationName = authentication.getName();
        boolean isUserHasAnyRole = this.userService
                .isUserHasAnyRole(authenticationName, ROLE_ADMIN, ROLE_MODERATOR);
        if (!authenticationName.equals(uploaderUsername) && (!isUserHasAnyRole)) {
            response.sendRedirect("/songs/details/" + songId);
            return false;
        }

        return true;
    }
}
