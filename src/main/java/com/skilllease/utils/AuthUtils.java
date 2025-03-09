package com.skilllease.utils;

import com.skilllease.entities.User;
import com.skilllease.exception.TokenInvalidException;
import jakarta.servlet.http.HttpServletRequest;

public class AuthUtils {

    private static final String BEARER_PREFIX = "Bearer ";

    public static User getCurrentUser(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            return null;
        }
        return getUserFromToken(authorizationHeader);
    }

    public static User getUserFromToken(String token) {
        try {
            String subject = DecodeToken.getSubjectToken(token);
            User user = new User();
            user.setId(Long.valueOf(subject));
            return user;
        } catch (Exception e) {
            return null;  // Invalid token or subject
        }
    }

    public static User getUserDetailFromToken(String token) throws TokenInvalidException {
        return DecodeToken.getUserFromToken(token);
    }

}