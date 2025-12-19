package com.neobank.service;

import com.neobank.entity.User;

public interface UserService {

    User createUser(User user);

    User getByUsername(String username);

    User getById(Long id);

}
