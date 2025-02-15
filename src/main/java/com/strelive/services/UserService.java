package com.strelive.services;

import com.strelive.dto.*;

public interface UserService {
    UserResponseDTO register(RegisterRequest registerRequest);

    LoginResponseDTO authenticate(LoginRequestDTO loginRequestDTO);

    ExtractToken extractToken(String token);

    Token refreshToken(RefreshRequest request);
}
