package com.lenovo.accountservice.controller;

import com.lenovo.accountservice.entity.Account;
import com.lenovo.accountservice.entity.Role;
import com.lenovo.accountservice.entity.dto.RegistrationRequest;
import com.lenovo.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<List<Account>> getAccount(@RequestBody Set<String> accountIds) {
        return ResponseEntity.ok(accountService.getAccountsByIds(accountIds));
    }

    @PostMapping("/register")
    public ResponseEntity<Account> createAccount(@RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.ok(accountService.createAccount(registrationRequest));
    }

    @PostMapping("/role/create")
    public ResponseEntity<Role> createRole(@RequestBody String roleName) {
        return new ResponseEntity<>(accountService.createRole(roleName), HttpStatus.CREATED);
    }
}
