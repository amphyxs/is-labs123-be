package com.example.prac.repository.imports;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.prac.model.imports.ImportHistoryItem;

public interface ImportHistoryRepository extends JpaRepository<ImportHistoryItem, Long> {
}
