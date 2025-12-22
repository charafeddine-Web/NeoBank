package com.neobank.service.impl;

import com.neobank.dto.AccountDto;
import com.neobank.entity.Account;
import com.neobank.mapper.AccountMapper;
import com.neobank.repository.AccountRepository;
import com.neobank.service.AccountService;
import com.neobank.util.AccountNumberGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public AccountDto createAccount(AccountDto dto) {
        Account entity = accountMapper.toEntity(dto);

        if (entity.getAccountNumber() == null || entity.getAccountNumber().isBlank()) {
            String candidate;
            int attempts = 0;
            do {
                candidate = AccountNumberGenerator.generate();
                attempts++;
            } while (accountRepository.existsByAccountNumber(candidate) && attempts < 10);

            if (attempts >= 10 && accountRepository.existsByAccountNumber(candidate)) {
                throw new RuntimeException("Unable to generate unique account number");
            }

            entity.setAccountNumber(candidate);
        } else {
            if (accountRepository.existsByAccountNumber(entity.getAccountNumber())) {
                throw new RuntimeException("Account number already exists");
            }
        }

        Account saved = accountRepository.save(entity);
        return accountMapper.toDto(saved);
    }

    @Override
    public AccountDto getAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        return accountMapper.toDto(account);
    }

    @Override
    public List<AccountDto> listAccounts() {
        return accountRepository.findAll().stream().map(accountMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public AccountDto updateAccount(Long id, AccountDto dto) {
        Account existing = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        Account toUpdate = accountMapper.toEntity(dto);
        toUpdate.setId(existing.getId());
        toUpdate.setVersion(existing.getVersion());
        Account saved = accountRepository.save(toUpdate);
        return accountMapper.toDto(saved);
    }

    @Override
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }
}
