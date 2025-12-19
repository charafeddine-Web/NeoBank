package com.neobank.service;

import com.neobank.dto.RegisterRequest;
import com.neobank.entity.User;

public interface UserService {

    RegisterRequest createUser(RegisterRequest user);

    User getByUsername(String username);

    User getById(Long id);

}
