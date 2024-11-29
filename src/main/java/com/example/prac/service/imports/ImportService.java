package com.example.prac.service.imports;

import java.util.List;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.prac.dto.data.DragonDTO;
import com.example.prac.dto.data.OwnerDTO;
import com.example.prac.dto.imports.ImportHistoryItemDTO;
import com.example.prac.mappers.impl.ImportHistoryItemMapper;
import com.example.prac.model.data.Coordinates;
import com.example.prac.model.data.DragonCave;
import com.example.prac.model.data.DragonHead;
import com.example.prac.model.data.Person;
import com.example.prac.model.imports.ImportHistoryItem;
import com.example.prac.model.imports.ImportStatus;
import com.example.prac.repository.data.CoordinatesRepository;
import com.example.prac.repository.data.DragonCaveRepository;
import com.example.prac.repository.data.DragonHeadRepository;
import com.example.prac.repository.data.PersonRepository;
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
    private final CoordinatesRepository coordinatesRepository;
    private final DragonCaveRepository dragonCaveRepository;
    private final DragonHeadRepository dragonHeadRepository;
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;

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
            dragons.forEach(d -> {
                var coordinates = modelMapper.map(d.getCoordinates(), Coordinates.class);
                coordinatesRepository.save(coordinates);
                d.getCoordinates().setId(coordinates.getId());

                var cave = modelMapper.map(d.getCave(), DragonCave.class);
                dragonCaveRepository.save(cave);
                d.getCave().setId(cave.getId());

                var head = modelMapper.map(d.getHead(), DragonHead.class);
                dragonHeadRepository.save(head);
                d.getHead().setId(head.getId());

                if (d.getKiller() != null) {
                    var person = modelMapper.map(d.getKiller(), Person.class);
                    personRepository.save(person);
                    d.getKiller().setId(person.getId());
                }

                var userDTO = modelMapper.map(authenticationService.getCurrentUser(), OwnerDTO.class);
                d.setOwner(userDTO);

                dragonService.save(d);
            });

            historyItem.setStatus(ImportStatus.COMPLETED);
            historyItem.setNumberOfAddedObjects(dragons.size());
        } catch (Exception e) {
            log.error("Import error", e);
            historyItem.setNumberOfAddedObjects(null);
        }

        return dtoMapper.mapTo(importHistoryRepository.save(historyItem));
    }
}
