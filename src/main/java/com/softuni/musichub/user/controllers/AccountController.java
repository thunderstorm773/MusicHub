package com.softuni.musichub.user.controllers;

import com.softuni.musichub.controller.BaseController;
import com.softuni.musichub.error.staticData.ErrorConstants;
import com.softuni.musichub.staticData.Constants;
import com.softuni.musichub.user.models.bindingModels.RegisterUser;
import com.softuni.musichub.user.services.UserManipulationService;
import com.softuni.musichub.user.staticData.AccountConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/users")
public class AccountController extends BaseController {

    private final UserManipulationService userManipulationService;

    @Autowired
    public AccountController(UserManipulationService userManipulationService) {
        this.userManipulationService = userManipulationService;
    }

    @GetMapping("/register")
    public ModelAndView getRegisterUserPage(Model model) {
        Map<String, Object> objectByKey = new HashMap<>();
        if (!model.asMap().containsKey(AccountConstants.REGISTER_USER)) {
            RegisterUser registerUser = new RegisterUser();
            objectByKey.put(AccountConstants.REGISTER_USER, registerUser);
        }

        return this.view(AccountConstants.USER_REGISTER_TITLE,
                AccountConstants.USER_REGISTER_VIEW, objectByKey);
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@Valid @ModelAttribute RegisterUser registerUser,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(AccountConstants.REGISTER_USER, registerUser);
            String bindingResultKey = Constants.BINDING_RESULT_PACKAGE
                    + AccountConstants.REGISTER_USER;
            redirectAttributes.addFlashAttribute(bindingResultKey, bindingResult);
            return this.redirect(AccountConstants.USERS_REGISTER_ROUTE);
        }

        this.userManipulationService.registerUser(registerUser);
        return this.redirect(AccountConstants.USERS_LOGIN_ROUTE);
    }

    @GetMapping("/login")
    public ModelAndView getLoginUserPage(@RequestParam(value = ErrorConstants.ERROR, required = false) String loginError) {
        Map<String, Object> objectByKey = new HashMap<>();
        if (loginError != null) {
           objectByKey.put(ErrorConstants.ERROR, AccountConstants.INVALID_CREDENTIALS_MESSAGE);
        }

        return this.view(AccountConstants.USER_LOGIN_TITLE,
                AccountConstants.USER_LOGIN_VIEW, objectByKey);
    }
}
