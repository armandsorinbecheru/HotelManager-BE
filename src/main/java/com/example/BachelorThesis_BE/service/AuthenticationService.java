package com.example.BachelorThesis_BE.service;

import com.example.BachelorThesis_BE.payload.request.SignInRequest;
import com.example.BachelorThesis_BE.payload.request.SignUpRequest;
import com.example.BachelorThesis_BE.payload.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SignInRequest request);

    JwtAuthenticationResponse deleteUserById(Long id);

    JwtAuthenticationResponse deleteUserByEmail(String email);
}
