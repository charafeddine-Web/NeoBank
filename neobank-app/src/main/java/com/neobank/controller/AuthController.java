package com.neobank.controller;


import com.neobank.dto.LoginRequest;
import com.neobank.dto.AuthResponse;
import com.neobank.dto.RegisterRequest;
import com.neobank.mapper.UserMapper;
import com.neobank.security.JWT.JwtUtil;
import com.neobank.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final UserMapper userMapper;


    @PostMapping("/register")
    public ResponseEntity<RegisterRequest> register(@Valid @RequestBody RegisterRequest dto) {
        RegisterRequest reg =userService.createUser(dto);
        return ResponseEntity.ok(reg);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );

        String token = jwtUtil.generateToken(request.getUsernameOrEmail());
        return new AuthResponse(token);
    }
}
