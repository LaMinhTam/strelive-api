package com.strelive.configurations;

import com.strelive.entities.User;
import com.strelive.utils.DecodeToken;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final String CURRENT_USER_PROPERTY = "currentUser";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authorizationHeader = requestContext.getHeaderString("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return;  // No token, leave currentUser null
        }

        try {
            String subject = DecodeToken.getSubjectToken(authorizationHeader);
            User streamer = new User();
            streamer.setId(Long.valueOf(subject));
            requestContext.setProperty(CURRENT_USER_PROPERTY, streamer);
        } catch (Exception e) {
            // Invalid token, leave currentUser null
        }
    }

    // Utility method to retrieve the user from any controller
    public static User getCurrentUser(ContainerRequestContext requestContext) {
        return (User) requestContext.getProperty(CURRENT_USER_PROPERTY);
    }
}