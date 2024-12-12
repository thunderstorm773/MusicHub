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
import com.tu.musichub.user.services.PasswordResetTokenService;
import com.tu.musichub.user.services.UserManipulationService;
import com.tu.musichub.user.staticData.AccountConstants;
import com.tu.musichub.user.utils.EmailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/users")
@Slf4j
public class AccountController extends BaseController {

    private final UserManipulationService userManipulationService;

    private final PasswordResetTokenService passwordResetTokenService;

    private final JavaMailSender mailSender;

    private final Environment environment;

    @Autowired
    public AccountController(UserManipulationService userManipulationService,
                             PasswordResetTokenService passwordResetTokenService,
                             JavaMailSender mailSender,
                             Environment environment) {
        this.userManipulationService = userManipulationService;
        this.passwordResetTokenService = passwordResetTokenService;
        this.mailSender = mailSender;
        this.environment = environment;
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
            try {
                MimeMessage mailMessage = EmailUtils.createResetTokenEmail(request, passwordResetToken.getToken(),
                        forgotPassword.getEmail(), this.environment, this.mailSender);
                this.mailSender.send(mailMessage);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
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
