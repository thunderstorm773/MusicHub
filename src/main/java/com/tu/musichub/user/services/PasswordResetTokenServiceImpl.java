package com.tu.musichub.user.services;

import com.tu.musichub.user.entities.PasswordResetToken;
import com.tu.musichub.user.entities.User;
import com.tu.musichub.user.models.bindingModels.ForgotPassword;
import com.tu.musichub.user.models.viewModels.PasswordResetTokenView;
import com.tu.musichub.user.repositories.PasswordResetTokenRepository;
import com.tu.musichub.user.repositories.UserRepository;
import com.tu.musichub.user.utils.UUIDUtil;
import com.tu.musichub.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@Transactional
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final MapperUtil mapperUtil;

    private final UserRepository userRepository;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    public PasswordResetTokenServiceImpl(MapperUtil mapperUtil,
                                         UserRepository userRepository,
                                         PasswordResetTokenRepository passwordResetTokenRepository) {
        this.mapperUtil = mapperUtil;
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Override
    public PasswordResetTokenView findByTokenAndExpiryDateAfter(String token, Date date) {
        PasswordResetToken passwordResetToken = this.passwordResetTokenRepository.findByTokenAndExpiryDateAfter(token, date);
        if(passwordResetToken != null) {
            return this.mapperUtil.getModelMapper().map(passwordResetToken, PasswordResetTokenView.class);
        }

        return null;
    }

    @Override
    public PasswordResetToken createForgotPasswordToken(ForgotPassword forgotPassword) {
        User user = this.userRepository.findByEmail(forgotPassword.getEmail());
        if (user == null) {
            return null;
        }

        String token = UUIDUtil.getRandomUUID();
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(passwordResetToken);
        return this.mapperUtil.getModelMapper().map(passwordResetToken, PasswordResetToken.class);
    }
}
