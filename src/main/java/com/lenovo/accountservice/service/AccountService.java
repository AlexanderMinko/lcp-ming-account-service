package com.lenovo.accountservice.service;

import com.lenovo.accountservice.entity.Account;
import com.lenovo.accountservice.entity.Role;
import com.lenovo.accountservice.entity.dto.RegistrationRequest;
import com.lenovo.accountservice.repository.AccountRepository;
import com.lenovo.accountservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final KeycloakService keycloakService;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    public Account createAccount(RegistrationRequest registrationRequest) {
        var roleName = "customer";
        var role = roleRepository.findByName(roleName)
            .orElseThrow(() -> new RuntimeException(MessageFormat.format("Role with name [{0}] not found", roleName)));
        var userRepresentation = createExternalUser(registrationRequest, Set.of(role));
        return createInternalAccount(userRepresentation, Set.of(role));
    }

    private Account createInternalAccount(UserRepresentation userRepresentation, Set<Role> roles) {
        var account = new Account(userRepresentation);
        account.setRoles(roles.stream().map(Role::getName).collect(Collectors.toSet()));
        return accountRepository.save(account);
    }

    private UserRepresentation createExternalUser(RegistrationRequest registrationRequest, Set<Role> roles) {
        var userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(registrationRequest.getUsername());
        userRepresentation.setFirstName(registrationRequest.getFirstName());
        userRepresentation.setLastName(registrationRequest.getLastName());
        userRepresentation.setEmail(registrationRequest.getEmail());
        userRepresentation.setEnabled(true);
        var user = keycloakService.createUser("LCPRealm", userRepresentation);
        keycloakService.assignRoles("LCPRealm", user.getId(), roles);
        keycloakService.resetPassword("LCPRealm", user.getId(), registrationRequest.getPassword());
        return user;
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
}
