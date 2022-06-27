package com.lenovo.accountservice.controller;

import java.util.List;
import java.util.Set;

import com.lenovo.accountservice.entity.Account;
import com.lenovo.accountservice.entity.param.CreateAccountParam;
import com.lenovo.accountservice.service.AccountService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("accounts")
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;

  @PostMapping
  public ResponseEntity<List<Account>> getAccounts(@RequestBody Set<String> accountIds) {
    return ResponseEntity.ok(accountService.getAccountsByIds(accountIds));
  }

  @PostMapping("/register")
  public ResponseEntity<Account> createAccount(@RequestBody CreateAccountParam createAccountParam) {
    return ResponseEntity.ok(accountService.createAccount(createAccountParam));
  }
}
