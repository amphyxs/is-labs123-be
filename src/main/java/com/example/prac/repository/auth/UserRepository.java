package com.example.prac.repository.auth;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.prac.model.auth.Role;
import com.example.prac.model.auth.User;

@Repository
public interface UserRepository extends CrudRepository<User, String>,
        PagingAndSortingRepository<User, String> {
    List<User> findByRole(Role role);
}
