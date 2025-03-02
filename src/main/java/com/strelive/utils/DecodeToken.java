package com.strelive.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.strelive.entities.Role;
import com.strelive.entities.User;
import com.strelive.exception.TokenInvalidException;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class DecodeToken {

    private static final String BEARER_PREFIX = "Bearer ";

    private static DecodedJWT decodeToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            return JWT.decode(authorizationHeader.substring(BEARER_PREFIX.length()));
        }
        throw new TokenInvalidException(ApplicationMessage.UNAUTHORIZED);
    }

    public static User getUserFromToken(String authorizationHeader) {
        try {
            DecodedJWT decodedJWT = decodeToken(authorizationHeader);
            User user = new User();
            user.setId(Long.valueOf(decodedJWT.getSubject()));
            user.setUsername(decodedJWT.getClaim("username").asString());
            user.setEmail(decodedJWT.getClaim("email").asString());
            user.setProfilePicture(decodedJWT.getClaim("profilePicture").asString());

            List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
            Set<Role> roleSet = new HashSet<>();
            for (String roleName : roles) {
                roleSet.add(new Role(roleName));
            }
            user.setRoles(roleSet);

            return user;
        } catch (JWTDecodeException ex) {
            throw new TokenInvalidException(ApplicationMessage.UNAUTHORIZED);
        }
    }

    public static String getSubjectToken(String authorizationHeader) {
        try {
            return decodeToken(authorizationHeader).getSubject();
        } catch (JWTDecodeException ex) {
            throw new TokenInvalidException(ApplicationMessage.UNAUTHORIZED);
        }
    }

    public static List<String> getRolesTokenArray(String authorizationHeader) {
        try {
            DecodedJWT decodedJWT = decodeToken(authorizationHeader);
            List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
            return roles.isEmpty() ? List.of() : roles;
        } catch (JWTDecodeException ex) {
            throw new TokenInvalidException(ApplicationMessage.UNAUTHORIZED);
        }
    }

    public static long getIssuedAtToken(String authorizationHeader) {
        try {
            return decodeToken(authorizationHeader).getIssuedAt().getTime();
        } catch (JWTDecodeException ex) {
            throw new TokenInvalidException(ApplicationMessage.UNAUTHORIZED);
        }
    }

    public static long getExpirationToken(String authorizationHeader) {
        try {
            return decodeToken(authorizationHeader).getExpiresAt().getTime();
        } catch (JWTDecodeException ex) {
            throw new TokenInvalidException(ApplicationMessage.UNAUTHORIZED);
        }
    }
}