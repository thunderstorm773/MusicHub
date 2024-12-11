package com.tu.musichub.user.services;

import com.tu.musichub.user.entities.PasswordResetToken;
import com.tu.musichub.user.models.bindingModels.ForgotPassword;
import com.tu.musichub.user.models.viewModels.PasswordResetTokenView;

public interface PasswordResetTokenService {

    PasswordResetTokenView findByToken(String token);

    PasswordResetToken createForgotPasswordToken(ForgotPassword forgotPassword);
}
