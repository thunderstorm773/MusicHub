package com.tu.musichub.user.services;

import com.tu.musichub.user.entities.PasswordResetToken;
import com.tu.musichub.user.models.bindingModels.ForgotPassword;
import com.tu.musichub.user.models.viewModels.PasswordResetTokenView;

import java.util.Date;

public interface PasswordResetTokenService {

    PasswordResetTokenView findByTokenAndExpiryDateAfter(String token, Date date);

    PasswordResetToken createForgotPasswordToken(ForgotPassword forgotPassword);
}
