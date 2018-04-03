package com.softuni.musichub.admin.controllers;

import com.softuni.musichub.admin.staticData.AdminConstants;
import com.softuni.musichub.error.staticData.ErrorConstants;
import com.softuni.musichub.staticData.Constants;
import com.softuni.musichub.user.exceptions.UserNotFoundException;
import com.softuni.musichub.user.models.bindingModels.EditUser;
import com.softuni.musichub.user.models.viewModels.RoleView;
import com.softuni.musichub.user.models.viewModels.UserView;
import com.softuni.musichub.user.services.RoleService;
import com.softuni.musichub.user.services.UserExtractionService;
import com.softuni.musichub.user.services.UserManipulationService;
import com.softuni.musichub.user.staticData.AccountConstants;
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
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class UserController {

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

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException() {
        return "redirect:" + AdminConstants.ALL_USERS_ROUTE;
    }

    @GetMapping("/users/all")
    public ModelAndView getAllUsersPage(ModelAndView modelAndView,
                                        @PageableDefault(AdminConstants.USERS_PER_PAGE) Pageable pageable,
                                        @RequestParam(value = AccountConstants.USERNAME, required = false) String username) {
        Page<UserView> usersViewPage;
        if (username == null) {
            usersViewPage = this.userExtractionService.findAll(pageable);
        } else {
            usersViewPage = this.userExtractionService.findAllByUsernameContains(username, pageable);
        }

        modelAndView.addObject(AdminConstants.TABLE_ACTIONS_STYLE_ENABLED, "");
        modelAndView.addObject(Constants.PAGE, usersViewPage);
        modelAndView.addObject(Constants.TITLE, AdminConstants.ALL_USERS_TITLE);
        modelAndView.addObject(Constants.VIEW, AdminConstants.ALL_USERS_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @GetMapping("/users/delete/{username}")
    public ModelAndView getDeleteUserPage(ModelAndView modelAndView,
                                          @PathVariable String username) {
        UserView userView = this.userExtractionService.findByUsername(username);
        modelAndView.addObject(AdminConstants.DELETE_USER, userView);
        modelAndView.addObject(Constants.TITLE, AdminConstants.DELETE_USER_TITLE);
        modelAndView.addObject(Constants.VIEW, AdminConstants.DELETE_USER_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @PostMapping("/users/delete/{username}")
    public ModelAndView deleteUser(ModelAndView modelAndView,
                                   @PathVariable String username,
                                   @AuthenticationPrincipal UserDetails loggedInUser,
                                   RedirectAttributes redirectAttributes) {
        String loggedInUserUsername = loggedInUser.getUsername();
        if (loggedInUserUsername.equals(username)) {
            redirectAttributes.addFlashAttribute(ErrorConstants.ERROR,
                    AdminConstants.USER_CANNOT_BE_DELETED_MESSAGE);
            modelAndView.setViewName("redirect:" +
                    AdminConstants.DELETE_USER_BASE_ROUTE + username);
            return modelAndView;
        }

        this.userManipulationService.deleteByUsername(username);
        modelAndView.setViewName("redirect:" + AdminConstants.ALL_USERS_ROUTE);
        return modelAndView;
    }

    @GetMapping("/users/edit/{username}")
    public ModelAndView getEditUserPage(ModelAndView modelAndView,
                                        Model model,
                                        @PathVariable String username) {
        UserView userView = this.userExtractionService.findByUsername(username);
        if (model.asMap().containsKey(AdminConstants.EDIT_USER)) {
            EditUser editUser = (EditUser) model.asMap().get(AdminConstants.EDIT_USER);
            Set<String> roleNames = editUser.getRoleNames();
            userView.setRoleNames(roleNames);
        }

        List<RoleView> roleViews = this.roleService.findAll();
        modelAndView.addObject(AccountConstants.ROLES, roleViews);
        modelAndView.addObject(AdminConstants.EDIT_USER, userView);
        modelAndView.addObject(Constants.TITLE, AdminConstants.EDIT_USER_TITLE);
        modelAndView.addObject(Constants.VIEW, AdminConstants.EDIT_USER_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @PostMapping("/users/edit/{username}")
    public ModelAndView editUser(ModelAndView modelAndView,
                                 @PathVariable String username,
                                 @Valid @ModelAttribute EditUser editUser,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(AdminConstants.EDIT_USER, editUser);
            redirectAttributes.addFlashAttribute(ErrorConstants.ERROR,
                    AdminConstants.NOT_SELECTED_ROLES_MESSAGE);
            modelAndView.setViewName("redirect:" +
                    AdminConstants.EDIT_USER_BASE_ROUTE + username);
            return modelAndView;
        }

        this.userManipulationService.edit(editUser, username);
        modelAndView.setViewName("redirect:" + AdminConstants.ALL_USERS_ROUTE);
        return modelAndView;
    }
}
