package com.neobank.service.impl;

import com.neobank.dto.AuthResponse;
import com.neobank.dto.RegisterRequest;
import com.neobank.entity.Account;
import com.neobank.entity.User;
import com.neobank.enums.Role;
import com.neobank.exception.DuplicateResourceException;
import com.neobank.exception.UserNotFoundException;
import com.neobank.mapper.AuthMapper;
import com.neobank.mapper.UserMapper;
import com.neobank.repository.AccountRepository;
import com.neobank.repository.UserRepository;
import com.neobank.service.UserService;
import com.neobank.util.AccountNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthMapper authMapper;

    @Override
    @Transactional
    public AuthResponse createUser(RegisterRequest dto) {

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        if (user.getRole() == null) {
            user.setRole(Role.CLIENT);
        }

        User savedUser = userRepository.save(user);

        if (savedUser.getRole() == Role.CLIENT) {
            Account account = new Account();
            account.setUser(savedUser);
            String accountNumber;
            do {
                accountNumber = AccountNumberGenerator.generate();
            } while (accountRepository.existsByAccountNumber(accountNumber) );

            account.setAccountNumber(accountNumber);
            account.setBalance(BigDecimal.ZERO);
            accountRepository.save(account);
        }

        return authMapper.toAuthResponse(savedUser);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found : " + username)
                );
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found id=" + id)
                );
    }

    @Override
    public User getByUsernameOrEmail(String identifier) {
        return userRepository.findByUsername(identifier)
                .or(() -> userRepository.findByEmail(identifier))
                .orElseThrow(() -> new UserNotFoundException("User not found : " + identifier));
    }

    @Override
    public Void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(false);
        userRepository.save(user);

        return null;
    }



    @Override
    public void activateUser(Long id) {
        User user = getById(id);
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void suspendUser(Long id) {
        User user = getById(id);
        user.setActive(false);
        userRepository.save(user);
    }

}
