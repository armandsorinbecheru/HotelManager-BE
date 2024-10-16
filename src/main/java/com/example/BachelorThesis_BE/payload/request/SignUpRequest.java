package com.example.BachelorThesis_BE.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    @NotBlank
    @Size(max = 120)
    private String firstName;
    @NotBlank
    @Size(max = 120)
    private String lastName;
    @NotBlank
    @Size(min = 3, max = 20)
    private String email;

//    private UserRole role;
    private String role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
}