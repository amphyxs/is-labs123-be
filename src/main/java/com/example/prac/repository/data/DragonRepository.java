package com.example.prac.repository.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.prac.model.data.Dragon;
import java.util.List;

@Repository
public interface DragonRepository extends JpaRepository<Dragon, Long> {
    List<Dragon> findByName(String name);
}