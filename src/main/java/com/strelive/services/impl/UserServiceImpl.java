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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Stateless
public class UserServiceImpl implements UserService {
    @Inject
    private UserDAO userDAO;
    @Inject
    private RoleService roleService;
    @Inject
    private UserMapper userMapper;
    private static final String UPLOAD_DIR = "uploads/profile_pictures/";

    @Override
    @Transactional
    public LoginResponseDTO register(RegisterRequest registerRequest) {
        User user = userMapper.toEntity(registerRequest);
        String password = user.getPassword();
        String hashedPassword = PasswordHasher.hashPassword(password);
        user.setPassword(hashedPassword);
        Role role = roleService.findByName("USER");
        user.addRole(role);
        user = userDAO.saveOrUpdate(user);
        return new LoginResponseDTO(
                TokenFactory.generateToken(user, TokenType.ACCESS),
                TokenFactory.generateToken(user, TokenType.REFRESH),
                new UserResponseDTO(user.getEmail(), user.getRoles()));
    }

    @Override
    public LoginResponseDTO authenticate(LoginRequestDTO loginRequestDTO) {
        User user = userDAO.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new NotFoundException(UserExceptionMessage.AUTHENTICATION_FAILED));
        checkPasswordValid(loginRequestDTO.getPassword(), user.getPassword());

        return new LoginResponseDTO(
                TokenFactory.generateToken(user, TokenType.ACCESS),
                TokenFactory.generateToken(user, TokenType.REFRESH),
                new UserResponseDTO(user.getEmail(), user.getRoles()));
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
    public Token refreshToken(RefreshRequest request) {
        ExtractToken tokenInfo = extractToken(request.getToken());
        Date expireAt = tokenInfo.getDate();
        String type = tokenInfo.getType();
        if (type.equals(TokenType.ACCESS.name())) {
            throw new InvalidTokenTypeException(UserExceptionMessage.TOKEN_INVALID);
        }

        if (expireAt.after(new Date())) {
            String accessToken = TokenFactory.refreshAccessToken(request.getToken(), TokenType.ACCESS);
            String refreshToken = TokenFactory.refreshAccessToken(request.getToken(), TokenType.REFRESH);
            return new Token(accessToken, refreshToken);
        }
        throw new ExpiredJwtException(null, null, UserExceptionMessage.TOKEN_EXPIRED);
    }

    @Override
    public boolean saveProfilePicture(InputStream fileInputStream, Long userId) {
        if (fileInputStream == null || userId == null) {
            return false;
        }

        Optional<User> userOptional = userDAO.findById(userId);
        if (userOptional.isEmpty()) {
            return false;
        }

        User user = userOptional.get();

        // Generate a unique filename
        String fileName = UUID.randomUUID() + ".jpg";
        String filePath = Paths.get(UPLOAD_DIR, fileName).toString();

        // Save file to local storage
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Update user profile picture in the database
        user.setProfilePicture(fileName);
        userDAO.merge(user);
        return true;
    }
}
