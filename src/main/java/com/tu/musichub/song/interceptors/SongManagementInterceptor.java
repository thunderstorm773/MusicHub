package com.tu.musichub.song.interceptors;

import com.tu.musichub.home.staticData.HomeConstants;
import com.tu.musichub.song.models.viewModels.SongView;
import com.tu.musichub.song.services.SongExtractionServiceImpl;
import com.tu.musichub.song.staticData.SongConstants;
import com.tu.musichub.user.services.UserExtractionService;
import com.tu.musichub.user.staticData.AccountConstants;
import com.tu.musichub.user.utils.UserUtils;
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

    private final SongExtractionServiceImpl songExtractionService;

    private final UserExtractionService userService;

    private final UserUtils userUtils;

    @Autowired
    public SongManagementInterceptor(SongExtractionServiceImpl songExtractionService,
                                     UserExtractionService userService,
                                     UserUtils userUtils) {
        this.songExtractionService = songExtractionService;
        this.userService = userService;
        this.userUtils = userUtils;
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
        String principalName = this.userUtils.getUsername(authentication);
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
