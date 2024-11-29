package com.tu.musichub.admin.controllers;

import com.tu.musichub.admin.staticData.AdminConstants;
import com.tu.musichub.controller.BaseController;
import com.tu.musichub.error.staticData.ErrorConstants;
import com.tu.musichub.staticData.Constants;
import com.tu.musichub.user.models.bindingModels.EditUser;
import com.tu.musichub.user.models.viewModels.RoleView;
import com.tu.musichub.user.models.viewModels.UserView;
import com.tu.musichub.user.services.RoleService;
import com.tu.musichub.user.services.UserExtractionService;
import com.tu.musichub.user.services.UserManipulationService;
import com.tu.musichub.user.staticData.AccountConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class UserController extends BaseController {

    private final UserExtractionService userExtractionService;

    private final UserManipulationService userManipulationService;

    private final RoleService roleService;

    @Autowired
    public UserController(UserExtractionService userExtractionService,
                          UserManipulationService userManipulationService,
                          RoleService roleService) {
        this.userExtractionService = userExtractionService;
        this.userManipulationService = userManipulationService;
        this.roleService = roleService;
    }

    @GetMapping("/users/all")
    public ModelAndView getAllUsersPage(@PageableDefault(AdminConstants.USERS_PER_PAGE) Pageable pageable,
                                        @RequestParam(value = AccountConstants.USERNAME, required = false) String username) {
        Page<UserView> usersViewPage;
        if (username == null) {
            usersViewPage = this.userExtractionService.findAll(pageable);
        } else {
            usersViewPage = this.userExtractionService.findAllByUsernameContains(username, pageable);
        }

        Map<String, Object> objectByKey = new HashMap<>();
        objectByKey.put(Constants.TABLE_ACTIONS_STYLE_ENABLED, "");
        objectByKey.put(Constants.PAGE, usersViewPage);
        return this.view(AdminConstants.ALL_USERS_TITLE,
                AdminConstants.ALL_USERS_VIEW, objectByKey);
    }

    @GetMapping("/users/delete/{username}")
    public ModelAndView getDeleteUserPage(@PathVariable String username) {
        UserView userView = this.userExtractionService.findByUsername(username);
        Map<String, Object> objectByKey = new HashMap<>();
        objectByKey.put(AdminConstants.DELETE_USER, userView);
        return this.view(AdminConstants.DELETE_USER_TITLE,
                AdminConstants.DELETE_USER_VIEW, objectByKey);
    }

    @PostMapping("/users/delete/{username}")
    public ModelAndView deleteUser(@PathVariable String username,
                                   @AuthenticationPrincipal UserDetails loggedInUser,
                                   RedirectAttributes redirectAttributes) {
        String loggedInUserUsername = loggedInUser.getUsername();
        if (loggedInUserUsername.equals(username)) {
            redirectAttributes.addFlashAttribute(ErrorConstants.ERROR,
                    AdminConstants.USER_CANNOT_BE_DELETED_MESSAGE);
            return this.redirect(AdminConstants.DELETE_USER_BASE_ROUTE + username);
        }

        this.userManipulationService.deleteByUsername(username);
        return this.redirect(AdminConstants.ALL_USERS_ROUTE);
    }

    @GetMapping("/users/edit/{username}")
    public ModelAndView getEditUserPage(Model model, @PathVariable String username) {
        UserView userView = this.userExtractionService.findByUsername(username);
        if (model.asMap().containsKey(AdminConstants.EDIT_USER)) {
            EditUser editUser = (EditUser) model.asMap().get(AdminConstants.EDIT_USER);
            Set<String> roleNames = editUser.getRoleNames();
            userView.setRoleNames(roleNames);
        }

        List<RoleView> roleViews = this.roleService.findAll();
        Map<String, Object> objectByKey = new HashMap<>();
        objectByKey.put(AccountConstants.ROLES, roleViews);
        objectByKey.put(AdminConstants.EDIT_USER, userView);
        return this.view(AdminConstants.EDIT_USER_TITLE,
                AdminConstants.EDIT_USER_VIEW, objectByKey);
    }

    @PostMapping("/users/edit/{username}")
    public ModelAndView editUser(@PathVariable String username,
                                 @Valid @ModelAttribute EditUser editUser,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(AdminConstants.EDIT_USER, editUser);
            redirectAttributes.addFlashAttribute(ErrorConstants.ERROR,
                    AdminConstants.NOT_SELECTED_ROLES_MESSAGE);
           return this.redirect(AdminConstants.EDIT_USER_BASE_ROUTE + username);
        }

        this.userManipulationService.edit(editUser, username);
        redirectAttributes.addFlashAttribute(Constants.INFO, AdminConstants.USER_EDITED_MESSAGE);
        return this.redirect(AdminConstants.EDIT_USER_BASE_ROUTE + username);
    }
}
