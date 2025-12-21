package com.neobank.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Username ou email obligatoire")
    private String username;

    @NotBlank(message = "Mot de passe obligatoire")
    private String password;
}
