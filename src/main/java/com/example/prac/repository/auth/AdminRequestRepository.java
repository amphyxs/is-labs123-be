package com.example.prac.repository.auth;

import com.example.prac.model.authEntity.AdminRequest;
import com.example.prac.model.authEntity.User;
import com.example.prac.model.dataEntity.Dragon;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRequestRepository extends CrudRepository<AdminRequest, Long>,
        PagingAndSortingRepository<AdminRequest, Long> {
    Optional<AdminRequest> findByRequester(User requester);
}

