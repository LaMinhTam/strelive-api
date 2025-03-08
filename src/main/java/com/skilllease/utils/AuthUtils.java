package com.skilllease.utils;

import com.skilllease.entities.User;
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
            System.out.println("\n\nsubject = " + subject);
            User streamer = new User();
            streamer.setId(Long.valueOf(subject));
            System.out.println("\n\nstreamer = " + streamer);
            return streamer;
        } catch (Exception e) {
            return null;  // Invalid token or subject
        }
    }

    public static User getUserDetailFromToken(String token) {
        return DecodeToken.getUserFromToken(token);
    }

}