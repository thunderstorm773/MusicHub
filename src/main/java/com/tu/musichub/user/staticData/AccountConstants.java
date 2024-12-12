package com.tu.musichub.user.staticData;

public class AccountConstants {

    public static final int USERNAME_MIN_LEN = 3;

    public static final int USERNAME_MAX_LEN = 10;

    public static final String USERNAME_BLANK_MESSAGE =
            "Username cannot be empty";

    public static final String USERNAME_REQUIRED_LEN_MESSAGE =
            "Username must be between 3 and 10 symbols";

    public static final String USERNAME_EXISTS_MESSAGE =
            "Username already exists";

    public static final String EMAIL_EXISTS_MESSAGE =
            "Email already exists";

    public static final String INCORRECT_EMAIL_FORMAT_MESSAGE =
            "Incorrect email format";

    public static final String EMAIL_BLANK_MESSAGE =
            "Email cannot be empty";

    public static final String EMAIL_PATTERN =
            "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

    public static final String PASSWORD_BLANK_MESSAGE =
            "Password cannot be empty";

    public static final int PASSWORD_MIN_LEN = 4;

    public static final String PASSWORD_MIN_LEN_MESSAGE =
            "Password must be at least 4 symbols";

    public static final String CONFIRM_PASSWORD_BLANK_MESSAGE =
            "Confirm password cannot be empty";

    public static final String PASSWORD_MISMATCHING_MESSAGE =
            "Password and confirm password not matching";

    public static final String INVALID_CREDENTIALS_MESSAGE =
            "Invalid credentials.";

    public static final String ROLES = "roles";

    public static final String USER_REGISTER_TITLE = "Register";

    public static final String USER_REGISTER_VIEW = "user/register";

    public static final String USER_LOGIN_TITLE = "Login";

    public static final String USER_LOGIN_VIEW = "user/login";

    public static final String FORGOT_PASSWORD_TITLE = "Forgot Password";

    public static final String FORGOT_PASSWORD_VIEW = "user/forgot-password";

    public static final String RESET_PASSWORD_TITLE = "Reset Password";

    public static final String REST_PASSWORD_VIEW = "user/reset-password";

    public static final String USERNAME = "username";

    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static final String ROLE_MODERATOR = "ROLE_MODERATOR";

    public static final String ROLE_USER = "ROLE_USER";

    public static final String REGISTERED_USER_MESSAGE =
            "You registered successfully! Please login in.";

    public static final String SENT_FORGOT_PASSWORD_EMAIL_MESSAGE =
            "We will send a reset password link to your email!";

    public static final String USER_LOGIN_ROUTE = "/users/login";

    public static final String RESET_PASSWORD_ROUTE = "/users/reset-password";

    public static final String LOGIN_STYLE_ENABLED = "loginStyleEnabled";

    public static final String TOKEN = "token";

}
