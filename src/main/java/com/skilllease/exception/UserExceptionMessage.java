package com.skilllease.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserExceptionMessage {
    public static final String USER_NOT_FOUND = "User not found";
    public static final String EMAIL_NOT_BLANK = "Email is mandatory";
    public static final String EMAIL_INVALID = "Invalid email format";
    public static final String PASSWORD_NOT_BLANK = "Password is mandatory";
    public static final String ROLE_NOT_BLANK = "Role is mandatory";
    public static final String AUTHENTICATION_FAILED = "Authentication failed";
    public static final String TOKEN_INVALID = "Invalid token";
    public static final String TOKEN_EXPIRED = "Token expired";
}
