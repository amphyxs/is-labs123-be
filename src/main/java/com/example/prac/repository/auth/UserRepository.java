package com.example.prac.repository.auth;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.prac.model.authEntity.Role;
import com.example.prac.model.authEntity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>,
        PagingAndSortingRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    List<User> findByRole(Role role);
}
