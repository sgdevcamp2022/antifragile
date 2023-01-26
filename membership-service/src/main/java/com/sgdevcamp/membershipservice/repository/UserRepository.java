package com.sgdevcamp.membershipservice.repository;

import com.sgdevcamp.membershipservice.model.User;
import com.sgdevcamp.membershipservice.model.UserRole;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmailOrUsername(String email, String username);

    Optional<User> findByEmailAndRoleLike(String email, UserRole role);

    Optional<User> findByEmail(String email);
}
