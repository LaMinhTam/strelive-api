package com.strelive.services;

import com.strelive.dto.*;

import java.io.InputStream;

public interface UserService {
    LoginResponseDTO register(RegisterRequest registerRequest);

    LoginResponseDTO authenticate(LoginRequestDTO loginRequestDTO);

    ExtractToken extractToken(String token);

    Token refreshToken(RefreshRequest request);

    boolean saveProfilePicture(InputStream fileInputStream, String originalFileName, Long userId);
}
