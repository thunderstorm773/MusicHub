package com.tu.musichub.user.staticData;

public class EmailConstants {

    public static final String SENDER_EMAIL_PROPERTY = "spring.mail.username";

    public static final String RESET_PASSWORD_EMAIL_SUBJECT = "Reset Password";

    public static final String RESET_PASSWORD_EMAIL_BODY = "<p>Hello,</p>"
            + "<p>You have requested to reset your password.</p>"
            + "<p>Click the link below to change your password:</p>"
            + "<p><a href=\"%s\">Change my password</a></p>"
            + "<br>"
            + " \r\n";
}
