package com.neobank.dto;

import com.neobank.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";

    private String username;
    private String email;
    private Role role;

    private String accountNumber;

}
