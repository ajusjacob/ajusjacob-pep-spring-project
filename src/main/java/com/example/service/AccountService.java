package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public ResponseEntity<Account> registerAccount(Account account) {
        if (account.getUsername() == null || account.getUsername().trim().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Optional<Account> existingAccount = accountRepository.findByUsername(account.getUsername());
        if (existingAccount.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        Account saveAccount = accountRepository.save(account);
        return ResponseEntity.ok(saveAccount);
    }

    public ResponseEntity<Account> login(Account account) {
        Optional<Account> existingAccount = accountRepository.findByUsername(account.getUsername());
        if (existingAccount.isPresent() && existingAccount.get().getPassword().equals(account.getPassword())) {
            return ResponseEntity.ok(existingAccount.get());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    
    
}
