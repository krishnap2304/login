package com.bootcamp.login.repository;

import com.bootcamp.login.model.Role;
import com.bootcamp.login.model.RoleEnum;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByName(RoleEnum name);
    Boolean existsByName(RoleEnum name);


    void deleteAllById(Iterable<? extends String> iterable);
}
