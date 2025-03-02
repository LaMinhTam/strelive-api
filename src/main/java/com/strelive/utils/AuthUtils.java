package com.strelive.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.strelive.entities.User;
import com.strelive.exception.TokenInvalidException;
import jakarta.servlet.http.HttpServletRequest;

public class AuthUtils {

    private static final String BEARER_PREFIX = "Bearer ";

    public static User getCurrentUser(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            return null;
        }

        try {
            String subject = DecodeToken.getSubjectToken(authorizationHeader);
            User streamer = new User();
            streamer.setId(Long.valueOf(subject));
            return streamer;
        } catch (Exception e) {
            return null;  // Invalid token or subject
        }
    }

}