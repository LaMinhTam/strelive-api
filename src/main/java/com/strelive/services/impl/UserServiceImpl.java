package com.strelive.services.impl;

import com.strelive.dao.UserDAO;
import com.strelive.dto.*;
import com.strelive.entities.Role;
import com.strelive.entities.User;
import com.strelive.exception.InvalidTokenTypeException;
import com.strelive.exception.UserExceptionMessage;
import com.strelive.mapper.UserMapper;
import com.strelive.services.RoleService;
import com.strelive.services.UserService;
import com.strelive.utils.PasswordHasher;
import com.strelive.utils.TokenFactory;
import com.strelive.utils.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;

import java.util.Date;
import java.util.Set;

@Stateless
public class UserServiceImpl implements UserService {
    @Inject
    private UserDAO userDAO;
    @Inject
    private RoleService roleService;
    @Inject
    private UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDTO register(RegisterRequest registerRequest) {
        User user = userMapper.toEntity(registerRequest);
        String password = user.getPassword();
        String hashedPassword = PasswordHasher.hashPassword(password);
        user.setPassword(hashedPassword);
        Role role = roleService.findByName("USER");
        user.addRole(role);
        return userMapper.toResponseDTO(userDAO.merge(user));
    }

    @Override
    public LoginResponseDTO authenticate(LoginRequestDTO loginRequestDTO) {
        User user = userDAO.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new NotFoundException(UserExceptionMessage.AUTHENTICATION_FAILED));
        checkPasswordValid(loginRequestDTO.getPassword(), user.getPassword());

        String accessToken = generateAccessToken(loginRequestDTO.getEmail(), user.getRoles());
        String refreshToken = generateRefreshToken(loginRequestDTO.getEmail(), user.getRoles());

        UserResponseDTO userResponseDTO = createUserResponseDTO(user);
        return new LoginResponseDTO(accessToken, refreshToken, userResponseDTO);
    }

    private void checkPasswordValid(String inputPassword, String storedPassword) {
        if (!PasswordHasher.hashPassword(inputPassword).equals(storedPassword)) {
            throw new NotAuthorizedException(UserExceptionMessage.AUTHENTICATION_FAILED);
        }
    }

    private String generateAccessToken(String email, Set<Role> roles) {
        return TokenFactory.generateToken(email, roles, TokenType.ACCESS);
    }

    private String generateRefreshToken(String email, Set<Role> roles) {
        return TokenFactory.generateToken(email, roles, TokenType.REFRESH);
    }

    private UserResponseDTO createUserResponseDTO(User user) {
        return new UserResponseDTO(user.getEmail(), user.getRoles());
    }

    @Override
    public ExtractToken extractToken(String token) {
        if (token != null) {
            Claims extractClaim = TokenFactory.decodeToken(token);
            return createExtractToken(extractClaim);
        }

        throw new IllegalArgumentException(UserExceptionMessage.TOKEN_INVALID);
    }

    private ExtractToken createExtractToken(Claims extractClaim) {
        String email = extractClaim.getSubject();
        Set<Role> roles = extractClaim.get("role", Set.class);
        String tokenType = extractClaim.get("type", String.class);
        Date date = extractClaim.getExpiration();

        return new ExtractToken(email, roles, tokenType, date);
    }

    @Override
    public Token refreshToken(RefreshRequest request) {
        ExtractToken tokenInfo = extractToken(request.getToken());
        Date expireAt = tokenInfo.getDate();
        String type = tokenInfo.getType();
        if (type.equals(TokenType.ACCESS.name())) {
            throw new InvalidTokenTypeException(UserExceptionMessage.TOKEN_INVALID);
        }

        if (expireAt.after(new Date())) {
            String accessToken = generateAccessToken(tokenInfo.getEmail(), tokenInfo.getRole());
            String refreshToken = generateRefreshToken(tokenInfo.getEmail(), tokenInfo.getRole());
            return new Token(accessToken, refreshToken);
        }
        throw new ExpiredJwtException(null, null, UserExceptionMessage.TOKEN_EXPIRED);
    }
}
