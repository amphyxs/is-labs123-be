package com.example.prac.repository.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.prac.model.data.DragonCave;

@Repository
public interface DragonCaveRepository extends JpaRepository<DragonCave, Long> {
}