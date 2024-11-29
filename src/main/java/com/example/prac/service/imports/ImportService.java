package com.example.prac.service.imports;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.prac.dto.data.DragonDTO;
import com.example.prac.dto.imports.ImportHistoryItemDTO;
import com.example.prac.mappers.impl.ImportHistoryItemMapper;
import com.example.prac.model.imports.ImportHistoryItem;
import com.example.prac.model.imports.ImportStatus;
import com.example.prac.repository.imports.ImportHistoryRepository;
import com.example.prac.service.auth.AuthenticationService;
import com.example.prac.service.data.DragonService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportService {
    private final ImportHistoryRepository importHistoryRepository;
    private final DragonService dragonService;
    private final ObjectMapper objectMapper;
    private final AuthenticationService authenticationService;
    private final ImportHistoryItemMapper dtoMapper;

    public List<ImportHistoryItemDTO> getAllImportHistoryItems() {
        return StreamSupport.stream(importHistoryRepository.findAll().spliterator(), false)
                .map(dtoMapper::mapTo).toList();
    }

    public ImportHistoryItemDTO importDragonsFromFile(MultipartFile file) {
        ImportHistoryItem historyItem = new ImportHistoryItem();

        historyItem.setStatus(ImportStatus.FAILED);
        historyItem.setOwner(authenticationService.getCurrentUser());

        try {
            List<DragonDTO> dragons = objectMapper.readValue(
                    file.getInputStream(), new TypeReference<>() {
                    });

            dragonService.saveImportedDragonsList(dragons);

            historyItem.setStatus(ImportStatus.COMPLETED);
            historyItem.setNumberOfAddedObjects(dragons.size());
        } catch (Exception e) {
            log.error("Import error", e);
            historyItem.setNumberOfAddedObjects(null);
        }

        return dtoMapper.mapTo(importHistoryRepository.save(historyItem));
    }
}
