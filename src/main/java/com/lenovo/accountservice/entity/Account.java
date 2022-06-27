package com.lenovo.accountservice.entity;

import java.util.HashSet;
import java.util.Set;

import com.lenovo.accountservice.entity.model.AccountStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
  private String realmName;
  private AccountStatus accountStatus;
}
