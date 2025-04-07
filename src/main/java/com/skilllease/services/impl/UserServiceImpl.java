package com.skilllease.services.impl;

import com.skilllease.dao.UserRepository;
import com.skilllease.dto.*;
import com.skilllease.entities.Role;
import com.skilllease.entities.User;
import com.skilllease.entities.Wallet;
import com.skilllease.exception.AppException;
import com.skilllease.exception.ErrorCode;
import com.skilllease.exception.InvalidTokenTypeException;
import com.skilllease.exception.UserExceptionMessage;
import com.skilllease.mapper.UserMapper;
import com.skilllease.services.UserService;
import com.skilllease.services.WalletService;
import com.skilllease.utils.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Stateless
public class UserServiceImpl implements UserService {
    @Inject
    private UserRepository userRepository;
    @Inject
    private UserMapper userMapper;
    @Inject
    private WalletService walletService;
    @Inject
    private AuthService authService;

    @Override
    @Transactional
    public LoginResponseDTO register(RegisterRequest registerRequest) {
        User user = userMapper.toEntity(registerRequest);
        String password = user.getPassword();
        String hashedPassword = PasswordHasher.hashPassword(password);
        user.setPassword(hashedPassword);
        user.setCreatedAt(LocalDateTime.now());
        user = userRepository.save(user);
        walletService.save(Wallet.builder().balance(BigDecimal.ZERO).user(user).build());
        return new LoginResponseDTO(
                TokenFactory.generateToken(user, TokenType.ACCESS),
                TokenFactory.generateToken(user, TokenType.REFRESH),
                new UserResponseDTO(user.getId(), user.getFullName(), user.getEmail(), user.getRole(), user.getCvUrl(), user.getProfilePictureUrl()));
    }

    @Override
    public LoginResponseDTO authenticate(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new NotFoundException(UserExceptionMessage.AUTHENTICATION_FAILED));
        checkPasswordValid(loginRequestDTO.getPassword(), user.getPassword());

        return new LoginResponseDTO(
                TokenFactory.generateToken(user, TokenType.ACCESS),
                TokenFactory.generateToken(user, TokenType.REFRESH),
                new UserResponseDTO(user.getId(), user.getFullName(), user.getEmail(), user.getRole(), user.getCvUrl(), user.getProfilePictureUrl()));
    }

    private void checkPasswordValid(String inputPassword, String storedPassword) {
        if (!PasswordHasher.hashPassword(inputPassword).equals(storedPassword)) {
            throw new NotAuthorizedException(UserExceptionMessage.AUTHENTICATION_FAILED);
        }
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
    public Token refreshToken(RefreshRequest request) throws InvalidTokenTypeException {
        ExtractToken tokenInfo = extractToken(request.getToken());
        Date expireAt = tokenInfo.getDate();
        String type = tokenInfo.getType();
        if (type.equals(TokenType.ACCESS.name())) {
            throw new InvalidTokenTypeException(ErrorCode.UNAUTHORIZED);
        }

        if (expireAt.after(new Date())) {
            String accessToken = TokenFactory.refreshAccessToken(request.getToken(), TokenType.ACCESS);
            String refreshToken = TokenFactory.refreshAccessToken(request.getToken(), TokenType.REFRESH);
            return new Token(accessToken, refreshToken);
        }
        throw new ExpiredJwtException(null, null, UserExceptionMessage.TOKEN_EXPIRED);
    }

    @Override
    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User uploadProfilePicture(ProfilePictureForm form) throws IOException, AppException {
        User user = authService.getCurrentUser();
        if (user == null) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        user = userRepository.findById(user.getId()).orElseThrow(() -> new NotFoundException(UserExceptionMessage.USER_NOT_FOUND));
        String profilePictureUrl = CloudinaryUtil.uploadFile(form.getFile(), form.getFileName());
        user.setProfilePictureUrl(profilePictureUrl);
        return userRepository.update(user);
    }

}
