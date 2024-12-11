package com.tu.musichub.user.utils;

import com.tu.musichub.user.staticData.AccountConstants;
import com.tu.musichub.user.staticData.EmailConstants;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;

import javax.servlet.http.HttpServletRequest;

public class EmailUtils {

    public static SimpleMailMessage createResetTokenEmail(HttpServletRequest request,
                                                          String token,
                                                          String userEmail,
                                                          Environment environment) {

        String appUrl = getAppUrl(request);
        String url = appUrl + AccountConstants.RESET_PASSWORD_ROUTE + "?token=" + token;
        String message = String.format(EmailConstants.RESET_PASSWORD_EMAIL_BODY, url);
        return constructEmail(EmailConstants.RESET_PASSWORD_EMAIL_SUBJECT, message, userEmail, environment);
    }

    private static String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    private static SimpleMailMessage constructEmail(String subject, String body,
                                                    String userMail, Environment environment) {
        String emailFrom = environment.getProperty(EmailConstants.SENDER_EMAIL_PROPERTY);

        final SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(emailFrom);
        email.setSubject(subject);
        email.setText(body);
        email.setTo(userMail);
        return email;
    }
}
