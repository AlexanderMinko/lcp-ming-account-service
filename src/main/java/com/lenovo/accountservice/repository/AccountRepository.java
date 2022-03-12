package com.lenovo.accountservice.repository;

import com.lenovo.accountservice.entity.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;

public interface AccountRepository extends MongoRepository<Account, String> {

    List<Account> findAccountByIdIn(Collection<String> ids);

}
