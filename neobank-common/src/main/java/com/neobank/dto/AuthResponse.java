package com.neobank.dto;

import com.neobank.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";

    private Long userId;
    private String username;
    private String email;
    private Role role;

    public AuthResponse(String token) {
    }
}
