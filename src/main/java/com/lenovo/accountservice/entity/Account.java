package com.lenovo.accountservice.entity;

import com.lenovo.accountservice.entity.dto.RegistrationRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    private String id;
    private String keycloakId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Set<String> roles = new HashSet<>();
    private String photoUrl;

    public Account(RegistrationRequest request) {
        this.username = request.getUsername();
        this.email = request.getEmail();
        this.firstName = request.getFirstName();
        this.lastName = request.getLastName();
    }

    public Account(UserRepresentation userRepresentation) {
        this.id = userRepresentation.getId();
        this.keycloakId = userRepresentation.getId();
        this.username = userRepresentation.getUsername();
        this.email = userRepresentation.getEmail();
        this.firstName = userRepresentation.getFirstName();
        this.lastName = userRepresentation.getLastName();
    }

}
