package com.lenovo.accountservice.service;

import static com.lenovo.accountservice.common.Constants.LCP_CUSTOMER;
import static com.lenovo.accountservice.common.Constants.LCP_REALM;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.lenovo.accountservice.entity.Account;
import com.lenovo.accountservice.entity.Role;
import com.lenovo.accountservice.entity.model.AccountStatus;
import com.lenovo.accountservice.entity.model.UserIdpRegisteredEvent;
import com.lenovo.accountservice.entity.param.CreateAccountParam;
import com.lenovo.accountservice.repository.AccountRepository;
import com.lenovo.accountservice.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

  private final KeycloakService keycloakService;
  private final AccountRepository accountRepository;
  private final RoleRepository roleRepository;

  public Account createAccount(CreateAccountParam createAccountParam) {
    var roleName = LCP_CUSTOMER;
    var role = roleRepository.findByName(roleName)
        .orElseThrow(() -> new RuntimeException(MessageFormat.format("Role with name [{0}] not found", roleName)));
    var userRepresentation = createExternalUser(createAccountParam, Set.of(role));
    return createInternalAccount(userRepresentation, Set.of(role));
  }

  public Role createRole(String roleName) {
    var role = Role.builder().id(UUID.randomUUID().toString()).name(roleName).build();
    return roleRepository.save(role);
  }

  public List<Account> getAccountsByIds(Set<String> ids) {
    var accounts = accountRepository.findAccountByIdIn(ids);
    log.debug("getAccountsByIds - found: {} accounts", accounts.size());
    return accounts;
  }

  public Account getAccountById(String id) {
    var account = accountRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(MessageFormat.format("Account with ID [{0}] not found", id)));
    log.debug("getAccountById - found account with ID: {}", account.getId());
    return account;
  }

  public boolean isAccountExistsById(String id) {
    var exists = accountRepository.existsById(id);
    log.debug("Account with ID - {} exists - {}", id, exists);
    return exists;
  }

  public void activateAccount(String id, String realmName) {
    var account = getAccountByIdAndRealmName(id, realmName);
    if (AccountStatus.ACTIVE.equals(account.getAccountStatus())) {
      return;
    }
    account.setAccountStatus(AccountStatus.ACTIVE);
    accountRepository.save(account);
  }

  public Account getAccountByIdAndRealmName(String id, String realmName) {
    return accountRepository.findAccountByIdAndRealmName(id, realmName)
        .orElseThrow(() -> new RuntimeException(MessageFormat
            .format("Account with id [{0}] and realm [{1}] not found", id, realmName)));
  }

  public Account createInternalAccount(UserIdpRegisteredEvent event) {
    var account = new Account();
    account.setId(event.getId());
    account.setKeycloakId(event.getId());
    account.setUsername(event.getUsername());
    account.setEmail(event.getEmail());
    account.setFirstName(event.getFirstName());
    account.setLastName(event.getLastName());
    account.setRoles(event.getUserRoles());
    account.setRealmName(event.getRealmName());
    account.setAccountStatus(AccountStatus.ACTIVE);
    return accountRepository.save(account);
  }

  private Account createInternalAccount(UserRepresentation userRepresentation, Set<Role> roles) {
    var account = new Account();
    account.setId(userRepresentation.getId());
    account.setKeycloakId(userRepresentation.getId());
    account.setUsername(userRepresentation.getUsername());
    account.setEmail(userRepresentation.getEmail());
    account.setFirstName(userRepresentation.getFirstName());
    account.setLastName(userRepresentation.getLastName());
    account.setAccountStatus(AccountStatus.INACTIVE);
    account.setRealmName(LCP_REALM);
    account.setRoles(roles.stream().map(Role::getName).collect(Collectors.toSet()));
    return accountRepository.save(account);
  }

  private UserRepresentation createExternalUser(CreateAccountParam createAccountParam, Set<Role> roles) {
    var userRepresentation = new UserRepresentation();
    userRepresentation.setUsername(createAccountParam.getUsername());
    userRepresentation.setFirstName(createAccountParam.getFirstName());
    userRepresentation.setLastName(createAccountParam.getLastName());
    userRepresentation.setEmail(createAccountParam.getEmail());
    userRepresentation.setEnabled(true);
    var user = keycloakService.createUser(LCP_REALM, userRepresentation);
    keycloakService.assignRoles(LCP_REALM, user.getId(), roles);
    keycloakService.resetPassword(LCP_REALM, user.getId(), createAccountParam.getPassword());
    return user;
  }
}
