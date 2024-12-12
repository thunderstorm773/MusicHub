package com.tu.musichub.user.utils;

import com.tu.musichub.user.staticData.AccountConstants;
import com.tu.musichub.user.staticData.EmailConstants;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public class EmailUtils {

    public static MimeMessage createResetTokenEmail(HttpServletRequest request,
                                                    String token,
                                                    String userEmail,
                                                    Environment environment,
                                                    JavaMailSender mailSender) throws MessagingException, UnsupportedEncodingException {

        String appUrl = getAppUrl(request);
        String url = appUrl + AccountConstants.RESET_PASSWORD_ROUTE + "?token=" + token;
        String message = String.format(EmailConstants.RESET_PASSWORD_EMAIL_BODY, url);
        return constructEmail(EmailConstants.RESET_PASSWORD_EMAIL_SUBJECT, message,
                userEmail, environment, mailSender);
    }

    private static String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    private static MimeMessage constructEmail(String subject,
                                              String body,
                                              String userMail,
                                              Environment environment,
                                              JavaMailSender mailSender) throws MessagingException, UnsupportedEncodingException {
        String emailFrom = environment.getProperty(EmailConstants.SENDER_EMAIL_PROPERTY);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailFrom, EmailConstants.RESET_PASSWORD_EMAIL_FROM);
        helper.setTo(userMail);
        helper.setSubject(subject);
        helper.setText(body, true);
        return message;
    }
}
