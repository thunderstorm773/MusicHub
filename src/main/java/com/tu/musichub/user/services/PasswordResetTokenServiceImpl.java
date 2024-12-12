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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final MapperUtil mapperUtil;

    private final UserRepository userRepository;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public PasswordResetTokenServiceImpl(MapperUtil mapperUtil,
                                         UserRepository userRepository,
                                         PasswordResetTokenRepository passwordResetTokenRepository,
                                         BCryptPasswordEncoder passwordEncoder) {
        this.mapperUtil = mapperUtil;
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PasswordResetTokenView findByToken(String token) {
        PasswordResetToken passwordResetToken = this.passwordResetTokenRepository.findByToken(token);
        return this.mapperUtil.getModelMapper().map(passwordResetToken, PasswordResetTokenView.class);
    }

    @Override
    public PasswordResetToken createForgotPasswordToken(ForgotPassword forgotPassword) {
        User user = this.userRepository.findByEmail(forgotPassword.getEmail());
        if (user == null) {
            return null;
        }

        String token = UUIDUtil.getRandomUUID();
        String hashedToken = this.passwordEncoder.encode(token);

        PasswordResetToken passwordResetToken = new PasswordResetToken(token, hashedToken, user);
        passwordResetTokenRepository.save(passwordResetToken);
        return this.mapperUtil.getModelMapper().map(passwordResetToken, PasswordResetToken.class);
    }
}
