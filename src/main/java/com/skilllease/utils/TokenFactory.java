package com.skilllease.utils;

import com.skilllease.entities.Role;
import com.skilllease.entities.User;
import com.skilllease.exception.UserExceptionMessage;
import io.jsonwebtoken.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenFactory {
    private static final String SECRET_KEY = ConfigUtils.getProperty("secretkey");
    private static final long ACCRESS_TOKEN_EXPIRE = 24 * 60 * 60 * 1000;
    private static final long REFRESH_TOKEN_EXPIRE = 7 * 24 * 60 * 60 * 1000;

    public static String generateToken(User user, TokenType type) {
        long expireTime = (type == TokenType.ACCESS) ? ACCRESS_TOKEN_EXPIRE : REFRESH_TOKEN_EXPIRE;
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("fullName", user.getFullName())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .claim("type", type.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                .compact();
    }

    public static String refreshAccessToken(String refreshToken, TokenType type) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .parseClaimsJws(refreshToken)
                    .getBody();

            // Validate token type
            String tokenType = claims.get("type", String.class);
            if (!TokenType.REFRESH.name().equals(tokenType)) {
                throw new RuntimeException("Invalid token type");
            }

            // Generate new access token
            return Jwts.builder()
                    .setSubject(claims.getSubject())
                    .claim("fullName", claims.get("fullName", Role.class))
                    .claim("email", claims.get("email", Role.class))
                    .claim("role", claims.get("role", Role.class))
                    .claim("type", type.name())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + ACCRESS_TOKEN_EXPIRE))
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                    .compact();

        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid refresh token", e);
        }
    }

    public static Claims decodeToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            throw new ExpiredJwtException(null, null, UserExceptionMessage.TOKEN_EXPIRED);
        } catch (Exception ex) {
            throw new MalformedJwtException(UserExceptionMessage.TOKEN_INVALID);
        }

    }

    public static boolean isExpired(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring("Bearer".length()).trim();;
            }

            Claims claims = decodeToken(token);
            Date expirationDate = claims.getExpiration();
            return expirationDate.after(new Date());
        } catch (ExpiredJwtException ex) {
            return false;
        }
    }

    public static boolean validateToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring("Bearer".length()).trim();;
        }

        Claims claims = decodeToken(token);
        String tokenType = claims.get("type", String.class);
        return tokenType.equalsIgnoreCase("ACCESS");
    }
}
