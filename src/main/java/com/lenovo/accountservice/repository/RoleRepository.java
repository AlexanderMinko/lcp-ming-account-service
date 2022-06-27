package com.lenovo.accountservice.repository;

import java.util.Optional;

import com.lenovo.accountservice.entity.Role;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {

  Optional<Role> findByName(String name);
}
