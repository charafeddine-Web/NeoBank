package com.neobank.dto;

import com.neobank.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;

    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    private Role role;

    private boolean active;

    private LocalDateTime createdAt;
}
