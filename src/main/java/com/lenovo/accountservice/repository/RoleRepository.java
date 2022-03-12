package com.lenovo.accountservice.repository;

import com.lenovo.accountservice.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByName(String name);
}
