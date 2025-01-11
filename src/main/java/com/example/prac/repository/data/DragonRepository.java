package com.example.prac.repository.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.prac.model.data.Dragon;

@Repository
public interface DragonRepository extends JpaRepository<Dragon, Long> {
    List<Dragon> findByName(String name);

    @Query("SELECT d FROM Dragon d WHERE d.name LIKE :baseName% ORDER BY " +
            "CASE " +
            "WHEN LENGTH(d.name) = LENGTH(:baseName) THEN 0 " +
            "ELSE CAST(SUBSTRING(d.name, LENGTH(:baseName) + 1) AS INTEGER) END DESC")
    List<Dragon> findByBaseNameWithMaxIndex(String baseName);
}