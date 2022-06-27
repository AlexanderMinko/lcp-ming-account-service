package com.lenovo.accountservice.service;

import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

import com.lenovo.accountservice.entity.Role;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeycloakService {

  private final Keycloak keycloak;

  public UserRepresentation createUser(String realm, UserRepresentation userRepresentation) {
    var users = getUsersResource(keycloak, realm);
    var response = users.create(userRepresentation);
    handleResponseErrorsForUser(response);
    var path = response.getLocation().getPath();
    var userId = path.substring(path.lastIndexOf("/") + 1);
    return users.get(userId).toRepresentation();
  }

  public void assignRoles(String realm, String userId, Set<Role> accountRoles) {
    var users = getUsersResource(keycloak, realm);
    var roles = getRolesResource(keycloak, realm);
    var accountRolesNames = accountRoles.stream().map(Role::getName).collect(Collectors.toList());
    var roleRepresentations = roles.list();
    var requiredRoles = roleRepresentations
        .stream()
        .filter(role -> accountRolesNames.contains(role.getName()))
        .collect(Collectors.toList());
    var userResource = users.get(userId);
    userResource.roles().realmLevel().add(requiredRoles);
  }

  public void resetPassword(String realm, String userId, String password) {
    var users = getUsersResource(keycloak, realm);
    var passwordCredentials = new CredentialRepresentation();
    passwordCredentials.setTemporary(false);
    passwordCredentials.setType(CredentialRepresentation.PASSWORD);
    passwordCredentials.setValue(password);
    users.get(userId).resetPassword(passwordCredentials);
    log.debug("Reset password successfully for user with id: {}", userId);
  }

  private UsersResource getUsersResource(Keycloak keycloak, String realm) {
    return keycloak.realm(realm).users();
  }

  private RolesResource getRolesResource(Keycloak keycloak, String realm) {
    return keycloak.realm(realm).roles();
  }

  private void handleResponseErrorsForUser(final Response response) {
    final int status = response.getStatus();
    if (status == 409) {
      throw new RuntimeException("User already exists");
    } else if (404 == status) {
      throw new RuntimeException("User not found");
    } else {
      if (status >= 400) {
        throw new RuntimeException("HTTP Code " + status);
      }
    }
  }
}
