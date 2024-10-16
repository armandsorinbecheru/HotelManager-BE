package com.example.BachelorThesis_BE.service.impl;

import com.example.BachelorThesis_BE.dto.UserDTO;
import com.example.BachelorThesis_BE.mail.EmailService;
import com.example.BachelorThesis_BE.model.PasswordResetToken;
import com.example.BachelorThesis_BE.model.User;
import com.example.BachelorThesis_BE.repository.TokenRepository;
import com.example.BachelorThesis_BE.repository.UserRepository;
import com.example.BachelorThesis_BE.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    //    @Value("${reset-password.base-url}")
    private final String resetPasswordBaseURL = "http://localhost:3000";

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserDTO::from)
                .orElse(null);
    }

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserDTO::from);
    }

    @Transactional
    @Override
    public void deleteUserByEmail(String email) {
        userRepository.deleteByEmail(email);
    }

    @Transactional
    @Override
    public void updateUser(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setRole(userDTO.getRole());
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void updatePassword(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(password);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void sendPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate a reset token
        String token = UUID.randomUUID().toString();

        LocalDateTime expiryDate = LocalDateTime.now().plusHours(1);

        // Save the token in the database
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setExpiryDate(expiryDate);
        resetToken.setUser(user);

        tokenRepository.save(resetToken);

        // Send email
        sendResetEmail(user.getEmail(), token);
    }

    @Override
    public void sendResetEmail(String email, String token) {
        String resetURL = resetPasswordBaseURL + "/reset-password?token=" + token;
        String subject = "Password Reset Request";
        String message = "To reset your password, click the link below:\n" + resetURL;

        emailService.sendSimpleMessage(email, subject, message);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token or token expired"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Invalidate the token
        tokenRepository.delete(resetToken);
    }
}