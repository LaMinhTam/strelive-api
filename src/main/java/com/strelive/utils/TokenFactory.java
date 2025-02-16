package com.strelive.utils;

import com.strelive.entities.Role;
import com.strelive.entities.User;
import com.strelive.exception.UserExceptionMessage;
import io.jsonwebtoken.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenFactory {
    private static final String SECRET_KEY = ConfigUtils.getProperty("secretkey");
    private static final long ACCRESS_TOKEN_EXPIRE = 24 * 60 * 60 * 1000;
    private static final long REFRESH_TOKEN_EXPIRE = 7 * 24 * 60 * 60 * 1000;

    public static String generateToken(User user, TokenType type) {
        long expireTime = (type == TokenType.ACCESS) ? ACCRESS_TOKEN_EXPIRE : REFRESH_TOKEN_EXPIRE;
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("roles", user.getRoles()
                        .stream().map(Role::getName).toArray(String[]::new))
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

            // Extract user ID
            Long userId = Long.parseLong(claims.getSubject());

            // Extract roles
            List<String> roleNames = claims.get("roles", List.class);

            // Convert List<String> to Set<Role>
            Set<Role> roles = roleNames.stream()
                    .map(Role::new)
                    .collect(Collectors.toSet());

            // Generate new access token
            return Jwts.builder()
                    .setSubject(userId.toString())
                    .claim("roles", roles.stream().map(Role::getName).toArray(String[]::new))
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
