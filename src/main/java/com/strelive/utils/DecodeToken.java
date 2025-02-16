package com.strelive.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.strelive.exception.TokenInvalidException;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class DecodeToken {

    private static final String BEARER_PREFIX = "Bearer ";

    private static DecodedJWT decodeToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            return JWT.decode(authorizationHeader.substring(BEARER_PREFIX.length()));
        }
        throw new TokenInvalidException(ApplicationMessage.UNAUTHORIZED);
    }

    public static String getSubjectToken(String authorizationHeader) {
        try {
            return decodeToken(authorizationHeader).getSubject();
        } catch (JWTDecodeException ex) {
            throw new TokenInvalidException(ApplicationMessage.UNAUTHORIZED);
        }
    }

    public static String getRoleToken(String authorizationHeader) {
        try {
            return decodeToken(authorizationHeader).getClaim("role").asString();
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