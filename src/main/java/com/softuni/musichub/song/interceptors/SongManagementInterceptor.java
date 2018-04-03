package com.softuni.musichub.song.interceptors;

import com.softuni.musichub.home.staticData.HomeConstants;
import com.softuni.musichub.song.models.viewModels.SongView;
import com.softuni.musichub.song.services.SongExtractionService;
import com.softuni.musichub.song.staticData.SongConstants;
import com.softuni.musichub.user.services.UserExtractionService;
import com.softuni.musichub.user.staticData.AccountConstants;
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

    private final SongExtractionService songExtractionService;

    private final UserExtractionService userService;

    @Autowired
    public SongManagementInterceptor(SongExtractionService songExtractionService,
                                     UserExtractionService userService) {
        this.songExtractionService = songExtractionService;
        this.userService = userService;
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private boolean isValidFormatId(String songIdStr) {
        try {
            Long.valueOf(songIdStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isPrincipalUploader(Long songId, String principalName) {
        SongView songView = this.songExtractionService.findById(songId);
        String uploaderUsername = songView.getUploaderUsername();
        return principalName.equals(uploaderUsername);
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String songIdStr = (String) pathVariables.get(SongConstants.ID);
        if (!this.isValidFormatId(songIdStr)) {
            response.sendRedirect(HomeConstants.INDEX_ROUTE);
            return false;
        }

        Long songId = Long.valueOf(songIdStr);
        Authentication authentication = this.getAuthentication();
        String principalName = authentication.getName();
        boolean isPrincipalUploader = this.isPrincipalUploader(songId, principalName);
        boolean isUserHasAnyRole = this.userService.isUserHasAnyRole(principalName,
                AccountConstants.ROLE_ADMIN, AccountConstants.ROLE_MODERATOR);
        if (!isPrincipalUploader && (!isUserHasAnyRole)) {
            response.sendRedirect(SongConstants.SONG_DETAILS_BASE_ROUTE + songId);
            return false;
        }

        return true;
    }
}
