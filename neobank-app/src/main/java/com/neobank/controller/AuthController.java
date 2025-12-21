package com.neobank.controller;


import com.neobank.dto.LoginRequest;
import com.neobank.dto.AuthResponse;
import com.neobank.dto.RegisterRequest;
import com.neobank.entity.User;
import com.neobank.exception.DuplicateResourceException;
import com.neobank.mapper.AuthMapper;
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
    private final AuthMapper authMapper;


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest dto) {
        try {
            AuthResponse reg = userService.createUser(dto);
            String token = jwtUtil.generateToken(reg.getUsername());
            reg.setToken(token);
            return ResponseEntity.ok(reg);
        } catch (DuplicateResourceException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userService.getByUsernameOrEmail(
                request.getUsername()
        );

        String token = jwtUtil.generateToken(user.getUsername());

        AuthResponse response = authMapper.toAuthResponse(user);
        response.setToken(token);

        return ResponseEntity.ok(response);
    }

}
