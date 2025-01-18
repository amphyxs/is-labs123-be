package com.example.prac.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.prac.dto.imports.ImportHistoryItemDTO;
import com.example.prac.service.auth.JwtService;
import com.example.prac.service.imports.ImportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class ImportController {
    private final ImportService importService;
    private final JwtService jwtService;

    @GetMapping
    public ResponseEntity<List<ImportHistoryItemDTO>> getAllImportHistoryItems() {
        return ResponseEntity.ok(importService.getAllImportHistoryItems());
    }

    @PostMapping
    public ResponseEntity<ImportHistoryItemDTO> createImport(@RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String authorizationHeader) throws Exception {
        String username = jwtService.extractUsernameFromAuthorizationHeader(authorizationHeader);
        ImportHistoryItemDTO historyItem = importService.importDragonsFromFile(file, username);

        return ResponseEntity.ok(historyItem);
    }
}
