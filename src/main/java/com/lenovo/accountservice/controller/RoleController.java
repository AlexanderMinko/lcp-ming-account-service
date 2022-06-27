package com.lenovo.accountservice.controller;

import com.lenovo.accountservice.entity.Role;
import com.lenovo.accountservice.service.AccountService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("roles")
@RequiredArgsConstructor
public class RoleController {

  private final AccountService accountService;

  @PostMapping
  public ResponseEntity<Role> createRole(@RequestBody String roleName) {
    return new ResponseEntity<>(accountService.createRole(roleName), HttpStatus.CREATED);
  }
}
