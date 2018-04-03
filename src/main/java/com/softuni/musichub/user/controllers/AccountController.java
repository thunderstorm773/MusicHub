package com.softuni.musichub.user.controllers;

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

@Controller
@RequestMapping("/users")
public class AccountController {

    private final UserManipulationService userManipulationService;

    @Autowired
    public AccountController(UserManipulationService userManipulationService) {
        this.userManipulationService = userManipulationService;
    }

    @GetMapping("/register")
    public ModelAndView getRegisterUserPage(ModelAndView modelAndView,
                                            Model model) {
        if (!model.asMap().containsKey(AccountConstants.REGISTER_USER)) {
            RegisterUser registerUser = new RegisterUser();
            modelAndView.addObject(AccountConstants.REGISTER_USER, registerUser);
        }

        modelAndView.addObject(Constants.TITLE, AccountConstants.USER_REGISTER_TITLE);
        modelAndView.addObject(Constants.VIEW, AccountConstants.USER_REGISTER_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView registerUser(ModelAndView modelAndView,
                                     @Valid @ModelAttribute RegisterUser registerUser,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(AccountConstants.REGISTER_USER, registerUser);
            String bindingResultKey = Constants.BINDING_RESULT_PACKAGE
                    + AccountConstants.REGISTER_USER;
            redirectAttributes.addFlashAttribute(bindingResultKey, bindingResult);
            modelAndView.setViewName("redirect:" + AccountConstants.USERS_REGISTER_ROUTE);
            return modelAndView;
        }

        this.userManipulationService.registerUser(registerUser);
        modelAndView.setViewName("redirect:" + AccountConstants.USERS_LOGIN_ROUTE);
        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView getLoginUserPage(ModelAndView modelAndView,
                                         @RequestParam(value = ErrorConstants.ERROR, required = false) String loginError) {
        if (loginError != null) {
            modelAndView.addObject(ErrorConstants.ERROR,
                    AccountConstants.INVALID_CREDENTIALS_MESSAGE);
        }

        modelAndView.addObject(Constants.TITLE, AccountConstants.USER_LOGIN_TITLE);
        modelAndView.addObject(Constants.VIEW, AccountConstants.USER_LOGIN_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }
}
