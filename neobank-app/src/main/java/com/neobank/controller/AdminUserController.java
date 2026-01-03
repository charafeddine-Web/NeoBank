package com.neobank.controller;

import com.neobank.dto.AuthResponse;
import com.neobank.dto.RegisterRequest;
import com.neobank.dto.UserDto;
import com.neobank.entity.User;
import com.neobank.mapper.AuthMapper;
import com.neobank.mapper.UserMapper;
import com.neobank.service.UserService;
import com.neobank.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;


    @PostMapping
    public ResponseEntity<AuthResponse> createUser(@Valid @RequestBody RegisterRequest dto) {
        AuthResponse resp = userService.createUser(dto);
        User u = userRepository.findByUsername(dto.getUsername()).orElseThrow(() -> new RuntimeException("User not found after create"));
        return ResponseEntity.status(201).body(authMapper.toAuthResponse(u));
    }

    @GetMapping
    public ResponseEntity<List<AuthResponse>> listUsers() {
        List<AuthResponse> users = userRepository.findAll().stream().map(authMapper::toAuthResponse).collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthResponse> getUser(@PathVariable Long id) {
        User u = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(authMapper.toAuthResponse(u));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthResponse> updateUser(@PathVariable Long id, @RequestBody UserDto dto) {
        User u = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (dto.getEmail() != null) u.setEmail(dto.getEmail());
        if (dto.getUsername() != null) u.setUsername(dto.getUsername());
        if (dto.getRole() != null) u.setRole(dto.getRole());
        u.setActive(dto.isActive());
        User saved = userRepository.save(u);
        return ResponseEntity.ok(authMapper.toAuthResponse(saved));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable Long id) {
        userService.activateUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/suspend")
    public ResponseEntity<Void> suspendUser(@PathVariable Long id) {
        userService.suspendUser(id);
        return ResponseEntity.ok().build();
    }


}

