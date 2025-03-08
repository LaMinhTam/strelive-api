package com.skilllease.services;

import com.skilllease.dto.*;
import com.skilllease.exception.InvalidTokenTypeException;

public interface UserService {
    LoginResponseDTO register(RegisterRequest registerRequest);

    LoginResponseDTO authenticate(LoginRequestDTO loginRequestDTO);

    ExtractToken extractToken(String token);

    Token refreshToken(RefreshRequest request) throws InvalidTokenTypeException;
}
