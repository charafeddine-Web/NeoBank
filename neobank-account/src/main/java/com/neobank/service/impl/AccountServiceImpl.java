package com.neobank.service.impl;

import com.neobank.dto.AccountDto;
import com.neobank.entity.Account;
import com.neobank.entity.User;
import com.neobank.mapper.AccountMapper;
import com.neobank.repository.AccountRepository;
import com.neobank.service.AccountService;
import com.neobank.util.AccountNumberGenerator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        String accountNumber;
        do {
            accountNumber = AccountNumberGenerator.generate();
        } while (accountRepository.existsByAccountNumber(accountNumber));

        entity.setAccountNumber(accountNumber);
        entity.setOperations(new ArrayList<>());
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

        String dtoAccNumber = dto.getAccountNumber();
        if (dtoAccNumber != null && !dtoAccNumber.isBlank()) {
            if (!dtoAccNumber.equals(existing.getAccountNumber()) && accountRepository.existsByAccountNumber(dtoAccNumber)) {
                throw new RuntimeException("Account number already exists");
            }
            existing.setAccountNumber(dtoAccNumber);
        }

        if (dto.getBalance() != null) {
            existing.setBalance(dto.getBalance());
        }

        if (dto.getUserId() != null) {
            User u = new User();
            u.setId(dto.getUserId());
            existing.setUser(u);
        }

        if (dto.getOperationIds() != null) {
            existing.setOperations(accountMapper.idsToOperations(dto.getOperationIds()));
        }

        Account saved = accountRepository.save(existing);
        return accountMapper.toDto(saved);
    }

    @Override
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }
}
