package com.appsec.lab.api.service;

import com.appsec.lab.api.exception.HttpResponseException;
import com.appsec.lab.api.model.account.Account;
import com.appsec.lab.api.model.user.User;
import com.appsec.lab.api.repository.AccountRepository;
import com.appsec.lab.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public Account createAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new HttpResponseException("User not found with id: " + userId, HttpStatus.NOT_FOUND));

        if (accountRepository.findByOwner(user).isPresent()) {
            throw new HttpResponseException("User already has an account", HttpStatus.BAD_REQUEST);
        }

        double initialBalance = user.getRole().equals("ROLE_MANAGER") ? 1.0 : 0.0;

        Account account = Account.builder()
                .accountNumber(UUID.randomUUID().toString())
                .balance(initialBalance)
                .owner(user)
                .build();

        return accountRepository.save(account);
    }

    public Double getBalance(Long accountId) {
        Account account = findAccount(accountId);
        return account.getBalance();
    }

    public Account deposit(Long accountId, Double amount) {
        Account account = findAccount(accountId);
        account.setBalance(account.getBalance() + amount);
        return accountRepository.save(account);
    }

    public Account withdraw(Long accountId, Double amount) {
        Account account = findAccount(accountId);
        if (account.getBalance() < amount) {
            throw new HttpResponseException("Insufficient funds", HttpStatus.BAD_REQUEST);
        }
        account.setBalance(account.getBalance() - amount);
        return accountRepository.save(account);
    }

    public void deactivate(Long accountId) {
        accountRepository.deleteById(accountId);
    }

    private Account findAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new HttpResponseException("Account not found with id: " + id, HttpStatus.NOT_FOUND));
    }
}
