package com.lenovo.accountservice.entity.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountParam {
  private String username;
  private String email;
  private String firstName;
  private String lastName;
  private String password;
}
