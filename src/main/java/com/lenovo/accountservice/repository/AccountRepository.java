package com.lenovo.accountservice.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.lenovo.accountservice.entity.Account;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<Account, String> {

  List<Account> findAccountByIdIn(Collection<String> ids);

  Optional<Account> findAccountByIdAndRealmName(String id, String realmName);
}
