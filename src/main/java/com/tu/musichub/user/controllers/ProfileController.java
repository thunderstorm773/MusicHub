package com.tu.musichub.user.controllers;

import com.tu.musichub.controller.BaseController;
import com.tu.musichub.staticData.Constants;
import com.tu.musichub.user.models.viewModels.ProfileView;
import com.tu.musichub.user.services.UserExtractionService;
import com.tu.musichub.user.staticData.ProfileConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ProfileController extends BaseController {

    private final UserExtractionService userExtractionService;

    @Autowired
    public ProfileController(UserExtractionService userExtractionService) {
        this.userExtractionService = userExtractionService;
    }

    @GetMapping("/users/profile/{username}")
    public ModelAndView getUserProfilePage(@PathVariable String username) {
        ProfileView userProfile = this.userExtractionService
                .getUserProfileByUsername(username);
        Map<String, Object> objectByKey = new HashMap<>();
        objectByKey.put(ProfileConstants.PROFILE, userProfile);
        objectByKey.put(Constants.TABLE_ACTIONS_STYLE_ENABLED, "");
        return this.view(ProfileConstants.PROFILE_TITLE,
                ProfileConstants.PROFILE_VIEW, objectByKey);
    }
}
