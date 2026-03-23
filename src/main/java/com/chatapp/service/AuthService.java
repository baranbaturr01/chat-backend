package com.chatapp.service;

import com.chatapp.dto.request.LoginRequest;
import com.chatapp.dto.request.RefreshTokenRequest;
import com.chatapp.dto.request.RegisterRequest;
import com.chatapp.dto.response.AuthResponse;
import com.chatapp.dto.response.UserResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);

    UserResponse getCurrentUser(String email);

    void logout(String email);
}
