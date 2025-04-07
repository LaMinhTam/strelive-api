package com.skilllease.services;

import com.skilllease.dto.*;
import com.skilllease.entities.User;
import com.skilllease.exception.AppException;
import com.skilllease.exception.InvalidTokenTypeException;

import java.io.IOException;
import java.util.Optional;

public interface UserService {
    LoginResponseDTO register(RegisterRequest registerRequest);

    LoginResponseDTO authenticate(LoginRequestDTO loginRequestDTO);

    ExtractToken extractToken(String token);

    Token refreshToken(RefreshRequest request) throws InvalidTokenTypeException;

    Optional<User> getById(Long id);

    User uploadProfilePicture(ProfilePictureForm form) throws IOException, AppException;
}
