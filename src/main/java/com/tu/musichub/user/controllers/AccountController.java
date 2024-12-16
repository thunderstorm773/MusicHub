package com.tu.musichub.user.controllers;

import com.tu.musichub.controller.BaseController;
import com.tu.musichub.error.staticData.ErrorConstants;
import com.tu.musichub.staticData.Constants;
import com.tu.musichub.user.entities.PasswordResetToken;
import com.tu.musichub.user.exceptions.PasswordResetTokenNotFoundException;
import com.tu.musichub.user.models.bindingModels.ForgotPassword;
import com.tu.musichub.user.models.bindingModels.RegisterUser;
import com.tu.musichub.user.models.bindingModels.ResetPassword;
import com.tu.musichub.user.models.viewModels.PasswordResetTokenView;
import com.tu.musichub.user.services.PasswordResetTokenServiceImpl;
import com.tu.musichub.user.services.UserManipulationServiceImpl;
import com.tu.musichub.user.staticData.AccountConstants;
import com.tu.musichub.user.utils.EmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/users")
public class AccountController extends BaseController {

    private final UserManipulationServiceImpl userManipulationService;

    private final PasswordResetTokenServiceImpl passwordResetTokenService;

    private final EmailUtils emailUtils;

    @Autowired
    public AccountController(UserManipulationServiceImpl userManipulationService,
                             PasswordResetTokenServiceImpl passwordResetTokenService,
                             EmailUtils emailUtils) {
        this.userManipulationService = userManipulationService;
        this.passwordResetTokenService = passwordResetTokenService;
        this.emailUtils = emailUtils;
    }

    @GetMapping("/register")
    public ModelAndView getRegisterUserPage(@ModelAttribute RegisterUser registerUser) {
        return this.view(AccountConstants.USER_REGISTER_TITLE,
                AccountConstants.USER_REGISTER_VIEW);
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@Valid @ModelAttribute RegisterUser registerUser,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return this.view(AccountConstants.USER_REGISTER_TITLE,
                    AccountConstants.USER_REGISTER_VIEW);
        }

        this.userManipulationService.registerUser(registerUser);
        redirectAttributes.addFlashAttribute(Constants.INFO,
                AccountConstants.REGISTERED_USER_MESSAGE);
        return this.redirect(AccountConstants.USER_LOGIN_ROUTE);
    }

    @GetMapping("/login")
    public ModelAndView getLoginUserPage(@RequestParam(value = ErrorConstants.ERROR, required = false) String loginError) {
        Map<String, Object> objectByKey = new HashMap<>();
        if (loginError != null) {
           objectByKey.put(ErrorConstants.ERROR, AccountConstants.INVALID_CREDENTIALS_MESSAGE);
        }

        objectByKey.put(AccountConstants.LOGIN_STYLE_ENABLED, "");
        return this.view(AccountConstants.USER_LOGIN_TITLE,
                AccountConstants.USER_LOGIN_VIEW, objectByKey);
    }

    @GetMapping("/forgot-password")
    public ModelAndView getForgotPasswordPage(@ModelAttribute ForgotPassword forgotPassword) {
        return this.view(AccountConstants.FORGOT_PASSWORD_TITLE,
                AccountConstants.FORGOT_PASSWORD_VIEW);
    }

    @PostMapping("/forgot-password")
    public ModelAndView createForgotPasswordToken(@Valid @ModelAttribute ForgotPassword forgotPassword,
                                                  BindingResult bindingResult,
                                                  RedirectAttributes redirectAttributes,
                                                  HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return this.view(AccountConstants.FORGOT_PASSWORD_TITLE,
                    AccountConstants.FORGOT_PASSWORD_VIEW);
        }

        PasswordResetToken passwordResetToken = this.passwordResetTokenService.createForgotPasswordToken(forgotPassword);
        if(passwordResetToken != null) {
            this.emailUtils.sendEmail(request, passwordResetToken.getToken(), forgotPassword.getEmail());
        }

        redirectAttributes.addFlashAttribute(Constants.INFO,
                AccountConstants.SENT_FORGOT_PASSWORD_EMAIL_MESSAGE);
        return this.redirect(AccountConstants.USER_LOGIN_ROUTE);
    }


    @GetMapping("/reset-password")
    public ModelAndView getResetPasswordPage(@RequestParam(value = AccountConstants.TOKEN) String token,
                                             @ModelAttribute ResetPassword resetPassword) {
        Date now = new Date();
        PasswordResetTokenView passwordResetToken = this.passwordResetTokenService
                .findByTokenAndExpiryDateAfter(token, now);
        if(passwordResetToken == null) {
            throw new PasswordResetTokenNotFoundException();
        }

        resetPassword.setToken(token);
        return this.view(AccountConstants.RESET_PASSWORD_TITLE,
                AccountConstants.REST_PASSWORD_VIEW);
    }

    @PostMapping("/reset-password")
    public ModelAndView resetPassword(@Valid @ModelAttribute ResetPassword resetPassword,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return this.view(AccountConstants.RESET_PASSWORD_TITLE,
                    AccountConstants.REST_PASSWORD_VIEW);
        }

        this.userManipulationService.resetPassword(resetPassword);
        redirectAttributes.addFlashAttribute(Constants.INFO,
                AccountConstants.RESET_PASSWORD_MESSAGE);
        return this.redirect(AccountConstants.USER_LOGIN_ROUTE);
    }
}
