package com.neobank.service;

import com.neobank.dto.AuthResponse;
import com.neobank.dto.RegisterRequest;
import com.neobank.entity.User;
import java.util.List;

public interface UserService {

    List<User> listAll();

    AuthResponse createUser(RegisterRequest user);

    User getByUsername(String username);

    User getById(Long id);

    User getByUsernameOrEmail(String identifier);

    Void deleteUser(Long id);

    void activateUser(Long id);

    void suspendUser(Long id);

}
