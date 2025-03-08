package com.skilllease.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.skilllease.entities.Role;
import com.skilllease.entities.User;
import com.skilllease.exception.ErrorCode;
import com.skilllease.exception.TokenInvalidException;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class DecodeToken {

    private static final String BEARER_PREFIX = "Bearer ";

    private static DecodedJWT decodeToken(String authorizationHeader) throws TokenInvalidException {
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            return JWT.decode(authorizationHeader.substring(BEARER_PREFIX.length()));
        }
        throw new TokenInvalidException(ErrorCode.UNAUTHORIZED);
    }

    public static User getUserFromToken(String authorizationHeader) throws TokenInvalidException {
        DecodedJWT decodedJWT = decodeToken(authorizationHeader);
        User user = new User();
        user.setId(Long.valueOf(decodedJWT.getSubject()));
        user.setFullName(decodedJWT.getClaim("fullName").asString());
        user.setEmail(decodedJWT.getClaim("email").asString());
        user.setRole(decodedJWT.getClaim("roles").as(Role.class));

        return user;
    }

    public static String getSubjectToken(String authorizationHeader) throws TokenInvalidException {
        return decodeToken(authorizationHeader).getSubject();
    }

    public static List<String> getRolesTokenArray(String authorizationHeader) throws TokenInvalidException {
        DecodedJWT decodedJWT = decodeToken(authorizationHeader);
        List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
        return roles.isEmpty() ? List.of() : roles;
    }

    public static long getIssuedAtToken(String authorizationHeader) throws TokenInvalidException {
        return decodeToken(authorizationHeader).getIssuedAt().getTime();
    }

    public static long getExpirationToken(String authorizationHeader) throws TokenInvalidException {
        return decodeToken(authorizationHeader).getExpiresAt().getTime();
    }
}