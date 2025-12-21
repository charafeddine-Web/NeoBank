package com.neobank.service;

import com.neobank.dto.AuthResponse;
import com.neobank.dto.RegisterRequest;
import com.neobank.entity.User;

public interface UserService {

    AuthResponse createUser(RegisterRequest user);

    User getByUsername(String username);

    User getById(Long id);

    User getByUsernameOrEmail(String identifier);

}
