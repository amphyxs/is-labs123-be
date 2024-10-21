package com.example.prac.repository.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.prac.model.dataEntity.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}