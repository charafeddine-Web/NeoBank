package com.neobank.service;

import com.neobank.dto.AccountDto;

import java.util.List;

public interface AccountService {

    AccountDto createAccount(AccountDto dto);

    AccountDto getAccount(Long id);

    List<AccountDto> listAccounts();

    List<AccountDto> listAccountsForUser(String email);

    AccountDto updateAccount(Long id, AccountDto dto);

    void deleteAccount(Long id);
}
