package com.example.BachelorThesis_BE.service;

import com.example.BachelorThesis_BE.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService userDetailsService();
    UserDTO getUserByEmail(String email);
    Page<UserDTO> getAllUsers(Pageable pageable);
    void deleteUserByEmail(String email);
    void updateUser(UserDTO userDTO);
    void updatePassword(String email, String password);
    void sendPasswordResetToken(String email);
    void sendResetEmail(String email, String token);
    void resetPassword(String email, String password);
}