package com.softuni.musichub.user.controller;

import com.softuni.musichub.staticData.Constants;
import com.softuni.musichub.user.model.bindingModel.RegisterUser;
import com.softuni.musichub.user.service.api.UserService;
import com.softuni.musichub.user.staticData.UserConstants;
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
public class UserController {

    private static final String USER_REGISTER_TITLE = "Register";

    private static final String USER_REGISTER_VIEW = "user/register";

    private static final String USER_LOGIN_TITLE = "Login";

    private static final String USER_LOGIN_VIEW = "user/login";

    private static final String REGISTER_USER = "registerUser";

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public ModelAndView getRegisterUserPage(ModelAndView modelAndView,
                                            Model model) {
        if (!model.asMap().containsKey(REGISTER_USER)) {
            RegisterUser registerUser = new RegisterUser();
            modelAndView.addObject(REGISTER_USER, registerUser);
        }

        modelAndView.addObject(Constants.TITLE, USER_REGISTER_TITLE);
        modelAndView.addObject(Constants.VIEW, USER_REGISTER_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView registerUser(ModelAndView modelAndView,
                                     @Valid @ModelAttribute RegisterUser registerUser,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(REGISTER_USER, registerUser);
            String bindingResultKey = Constants.BINDING_RESULT_PACKAGE + REGISTER_USER;
            redirectAttributes.addFlashAttribute(bindingResultKey, bindingResult);
            modelAndView.setViewName("redirect:/users/register");
            return modelAndView;
        }

        this.userService.registerUser(registerUser);
        modelAndView.setViewName("redirect:/users/login");
        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView getLoginUserPage(ModelAndView modelAndView,
                                         @RequestParam(value = "error", required = false) String loginError) {
        if (loginError != null) {
            modelAndView.addObject(Constants.ERROR,
                    UserConstants.INVALID_CREDENTIALS_MESSAGE);
        }

        modelAndView.addObject(Constants.TITLE, USER_LOGIN_TITLE);
        modelAndView.addObject(Constants.VIEW, USER_LOGIN_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }
}
